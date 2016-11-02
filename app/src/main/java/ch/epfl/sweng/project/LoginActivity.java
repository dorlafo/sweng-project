package ch.epfl.sweng.project;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import ch.epfl.sweng.project.model.Player;
import ch.epfl.sweng.project.notification.JassTokenService;
import ch.epfl.sweng.project.tequila.AuthClient;
import ch.epfl.sweng.project.tequila.AuthServer;
import ch.epfl.sweng.project.tequila.OAuth2Config;
import ch.epfl.sweng.project.tequila.Profile;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private static String clientID = null;
    private static String clientSecret = null;
    private static String redirectUri = null;
    private static String[] scopes = {"Tequila.profile"};
    private static OAuth2Config config;
    private static final int REQUEST_CODE_AUTHENTICATE = 0;

    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        fAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onBackPressed() {
        //DO NOTHING -> it disables the back button
    }

    public void login(View view) {
        // Get credentials from Json file
        try {
            JSONObject jObj = new JSONObject(loadJSONFromAsset());
            clientID = jObj.getString("clientID");
            clientSecret = jObj.getString("clientSecret");
            redirectUri = jObj.getString("redirectURI");
        } catch (JSONException e) {
            finish();
        }
        config = new OAuth2Config(scopes, clientID, clientSecret, redirectUri);
        final String codeRequestUrl = AuthClient.createCodeRequestUrl(config);
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.setData(Uri.parse(codeRequestUrl));
        startActivityForResult(intent, REQUEST_CODE_AUTHENTICATE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_AUTHENTICATE:
                if (resultCode != RESULT_OK || data == null) {
                    // Opens dialog box to alert user of the authentication fail
                    // Allows user to cancel or retry
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle(R.string.auth_failed)
                            .setMessage(R.string.retry_message)
                            .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    final String codeRequestUrl = AuthClient.createCodeRequestUrl(config);
                                    Intent intent = new Intent(LoginActivity.this, WebViewActivity.class);
                                    intent.setData(Uri.parse(codeRequestUrl));
                                    startActivityForResult(intent, REQUEST_CODE_AUTHENTICATE);
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Do nothing, goes back to LoginActivity
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                } else {
                    // Get the token.
                    String code = AuthClient.extractCode(data.getStringExtra("url"));
                    new FetchTokens().execute(code);
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /* Fetched token from Tequila server using AuthServer methods
    * Stores the user profile in Profile object
    * Execute HttpUrlConnection on a separated async thread
    */
    //TODO: Store profile in profile Objet -> Voir avec Dorian
    private class FetchTokens extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            findViewById(R.id.login_button).setVisibility(View.GONE);
            findViewById(R.id.login_text_view).setVisibility(View.GONE);
        }

        @Override
        protected String doInBackground(String... params) {
            Map<String, String> tokens;
            Profile profile;
            try {
                tokens = AuthServer.fetchTokens(config, params[0]);
                profile = AuthServer.fetchProfile(tokens.get("Tequila.profile"));
                authenticateWithFirebase(profile);
                JassTokenService.registerWithServer(profile.sciper, FirebaseInstanceId.getInstance().getToken());
            } catch (IOException e) {
                Log.e("ERR", "IOException, couldn't fetch token");
            }
            return "profile retrieved";
        }

        @Override
        protected void onPostExecute(String result) {
            finish();
        }
    }

    /**
     * Load Tequila credentials from file
     *
     * @return
     */
    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("tequila_credentials.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void authenticateWithFirebase(final Profile profile) {
        // TODO: REPLACE SCIPER WITH HASH
        fAuth.signInWithEmailAndPassword(profile.email, profile.sciper)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete " + task.isSuccessful());
                        if (task.isSuccessful()) {
                            Log.d(TAG, "updating Profile...");
                            fAuth.getCurrentUser().updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(profile.sciper).build());
                        } else {
                            Log.d(TAG, "task.isSuccessful() is false");
                            fAuth.createUserWithEmailAndPassword(profile.email, profile.sciper)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            Log.d(TAG, "Signup was successfull? " + task.isSuccessful());
                                            if (task.isSuccessful()) {
                                                FirebaseDatabase.getInstance().getReference().child("players")
                                                        .child(profile.sciper).setValue(new Player(
                                                        new Player.PlayerID(Long.parseLong(profile.sciper)),
                                                        profile.lastNames,
                                                        profile.firstNames
                                                ));
                                                fAuth.getCurrentUser().updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(profile.sciper).build());
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

}

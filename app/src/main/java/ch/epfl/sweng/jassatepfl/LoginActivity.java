package ch.epfl.sweng.jassatepfl;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import ch.epfl.sweng.jassatepfl.model.Player;
import ch.epfl.sweng.jassatepfl.server.ServerInterface;
import ch.epfl.sweng.jassatepfl.tequila.AuthClient;
import ch.epfl.sweng.jassatepfl.tequila.AuthServer;
import ch.epfl.sweng.jassatepfl.tequila.OAuth2Config;
import ch.epfl.sweng.jassatepfl.tequila.Profile;

public class LoginActivity extends BaseAppCompatActivity {

    private static final String TAG = LoginActivity.class.getSimpleName();

    private static String clientID = null;
    private static String clientSecret = null;
    private static String redirectUri = null;
    private static final String[] scopes = {"Tequila.profile"};
    private static OAuth2Config config;
    private static final int REQUEST_CODE_AUTHENTICATE = 0;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    /*
    This code should not be necessary
    @Override
    public void onStart() {
        super.onStart();

        // Check if user is already logged in
        if (fAuth.getCurrentUser() != null) {
            onAuthSuccess();
        }
    }
    */
    @Override
    public void onBackPressed() {
        //DO NOTHING -> it disables the back button
    }

    /**
     * Launch the login process when a user click on the login button
     * @param view the current view (This is a required parameters for method related to a button)
     */
    public void login(View view) {
        config = getOAuth2Config();
        final String codeRequestUrl = AuthClient.createCodeRequestUrl(config);

        Intent intent = new Intent(this, WebViewActivity.class);
        intent.setData(Uri.parse(codeRequestUrl));
        startActivityForResult(intent, REQUEST_CODE_AUTHENTICATE);
    }

    /**
     * Creates, if it does not exist, a progress Dialog and show it
     */
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    /**
     * Hide the progress dialog if it exist and is showing
     */
    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * Sign in the user with the information from the Profile object. If the sign in does not work,
     * it means that the user does not have an account so it creates one by calling signUp
     * @param profile The user Profile retrieved from Tequila authentication
     */
    //TODO: REPLACE SCIPER WITH HASH
    private void signIn(final Profile profile) {
        Log.d(TAG, "signIn");

        fAuth.signInWithEmailAndPassword(profile.email, profile.sciper)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signIn:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signIn:success");
                            onAuthSuccess();
                        } else {
                            Log.d(TAG, "signIn:failure,goto:signUp");
                            signUp(profile);
                        }
                    }
                });
    }

    /**
     * Sign up the user by creating an account on Firebase and adding it's user profile to the
     * database
     * @param profile The user Profile retrieved from Tequila authentication
     */
    //TODO: REPLACE SCIPER WITH HASH
    private void signUp(final Profile profile) {
        Log.d(TAG, "signUp");

        fAuth.createUserWithEmailAndPassword(profile.email, profile.sciper)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {
                            //Adding the user to the database
                            dbRefWrapped.child("players")
                                    .child(profile.sciper).setValue(new Player(
                                    new Player.PlayerID(Long.parseLong(profile.sciper)),
                                    profile.lastNames,
                                    profile.firstNames
                            ));
                            fAuth.getCurrentUser().updateProfile(new UserProfileChangeRequest.Builder().setDisplayName(profile.sciper).build());
                            onAuthSuccess();
                        } else {
                            Toast.makeText(LoginActivity.this, "Sign Up Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * Handle the result from the log in process
     */
    //TODO: Add a default case to the switch ?
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_AUTHENTICATE:
                if (resultCode != RESULT_OK || data == null) {
                    // Opens dialog box to alert user of the authentication fail
                    // Allows user to cancel or retry
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle(R.string.error_auth_failed)
                            .setMessage(R.string.error_retry_message)
                            .setPositiveButton(R.string.dialog_retry, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    final String codeRequestUrl = AuthClient.createCodeRequestUrl(config);
                                    Intent intent = new Intent(LoginActivity.this, WebViewActivity.class);
                                    intent.setData(Uri.parse(codeRequestUrl));
                                    startActivityForResult(intent, REQUEST_CODE_AUTHENTICATE);
                                }
                            })
                            .setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
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

    /**
     * Fetches token from Tequila server using AuthServer methods
     * Stores the user profile in a Profile object
     * Execute HttpUrlConnection on a separated asynchronous thread
     */
    private class FetchTokens extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            findViewById(R.id.login_button).setVisibility(View.GONE);
            findViewById(R.id.login_text_view).setVisibility(View.GONE);
            showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {
            Map<String, String> tokens;
            Profile profile;
            try {
                tokens = AuthServer.fetchTokens(config, params[0]);
                profile = AuthServer.fetchProfile(tokens.get("Tequila.profile"));
                signIn(profile);
                ServerInterface.getInstance().registerSciperToken(profile.sciper, FirebaseInstanceId.getInstance().getToken());
            } catch (IOException e) {
                //TODO: Handle exception
                Log.e("ERR", "IOException, couldn't fetch token");
            }
            return "profile retrieved";
        }
    }

    /**
     * Hide the login progress dialog and redirect to the MainActivity
     */
    private void onAuthSuccess() {
        // Go to MainActivity
        hideProgressDialog();
        Log.d(TAG, "hideProgressDialog:true");
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    /**
     * Load Tequila credentials from file
     *
     * @return String containing the tequila_credentials
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
            //TODO: Handle the exception
            ex.printStackTrace();
        }
        return json;
    }

    /**
     * Get the OAuth2Config from the JSON file
     * @return the OAuth2Config
     */
    private OAuth2Config getOAuth2Config() {
        try {
            JSONObject jObj = new JSONObject(loadJSONFromAsset());
            clientID = jObj.getString("clientID");
            clientSecret = jObj.getString("clientSecret");
            redirectUri = jObj.getString("redirectURI");
        } catch (JSONException e) {
            //TODO: Handle the exception
            e.printStackTrace();
        }

        return new OAuth2Config(scopes, clientID, clientSecret, redirectUri);
    }
}

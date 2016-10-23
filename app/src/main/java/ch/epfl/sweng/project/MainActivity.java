package ch.epfl.sweng.project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import ch.epfl.sweng.project.tequila.AuthClient;
import ch.epfl.sweng.project.tequila.AuthServer;
import ch.epfl.sweng.project.tequila.OAuth2Config;
import ch.epfl.sweng.project.tequila.Profile;

/**
 * Your app's main activity.
 */
public final class MainActivity extends AppCompatActivity {
    //TODO convention de nommage pour les providers ? "m" pour match puis "Provider". A décider.

    private static JSONObject jObj = null;
    private static String clientID = null;
    private static String clientSecret = null;
    private static String redirectUri = null;
    private static String[] scopes = {"Tequila.profile"};
    private static OAuth2Config config;
    private static final int REQUEST_CODE_AUTHENTICATE = 0;
    private boolean loggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Creates Menu on top left corner
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_menu, menu);
        return true;
    }

    public void viewProfile(View view) {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

    /* Handler for menu items
     * Usage of deprecated method because the new one require
     * a minimum of android 21, we set minimum android 15
     */
    @Override
    @SuppressWarnings("deprecation")
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Start the WebViewActivity to handle the authentication.
            case R.id.menu_login:
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
                return true;
            // Log out.
            case R.id.menu_logout:
                if(android.os.Build.VERSION.SDK_INT < 21) {
                    CookieManager.getInstance().removeAllCookie();
                } else {
                    CookieManager.getInstance().removeAllCookies(null);
                }
                loggedIn = false;
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Makes the menu show login
     * or logout depending on state
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(loggedIn) {
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(true);
        } else {
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_AUTHENTICATE:
                if (resultCode != RESULT_OK || data == null) {
                    // Opens dialog box to alert user of the authentication fail
                    // Allows user to cancel or retry
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle(R.string.auth_failed)
                            .setMessage(R.string.retry_message)
                            .setPositiveButton(R.string.retry, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    final String codeRequestUrl = AuthClient.createCodeRequestUrl(config);
                                    Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                                    intent.setData(Uri.parse(codeRequestUrl));
                                    startActivityForResult(intent, REQUEST_CODE_AUTHENTICATE);
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                   // Do nothing, goes back to MainActivity
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
        protected String doInBackground(String... params) {
            Map<String, String> tokens;
            Profile profile;
            try {
                tokens = AuthServer.fetchTokens(config, params[0]);
                profile = AuthServer.fetchProfile(tokens.get("Tequila.profile"));
                loggedIn = true;
                //TODO: Here store in Profile & send to dataBase
            } catch (java.io.IOException e) {
                Log.e("ERR", "IOException, couldnt fetch token");
            }
            return "profile retrieved";
        }
    }

    public void createMatch(View view) {
        Intent intent = new Intent(this, CreateMatchActivity.class);
        startActivity(intent);
    }

    public String loadJSONFromAsset() {
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


    public void displayMatchesOnMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void displayMatchesInList(View view) {
        Intent intent = new Intent(this, MatchListActivity.class);
        startActivity(intent);
    }
}

package ch.epfl.sweng.project;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;

import java.util.Map;

import ch.epfl.sweng.project.tequila.AuthClient;
import ch.epfl.sweng.project.tequila.AuthServer;
import ch.epfl.sweng.project.tequila.OAuth2Config;
import ch.epfl.sweng.project.tequila.Profile;

/**
 * Your app's main activity.
 */
public final class MainActivity extends AppCompatActivity {

    //TODO: put all this in a file in git-ignore, ask Nicolas/Vinc
    private static String clientID = "5104e0c64dedd33b9bef2b16@epfl.ch";
    private static String clientSecret = "b9ef07b6c1cae5ae18cf7401a34da9b4";
    private static String redirectUri = "jassatepfl://login";
    private static String[] scopes = {"Tequila.profile"};
    private static OAuth2Config config = new OAuth2Config(scopes, clientID, clientSecret, redirectUri);
    private static final int REQUEST_CODE_AUTHENTICATE = 0;
    private boolean logedIn = false;

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
                logedIn = false;
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(logedIn) {
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
                logedIn = true;
                //TODO: Here store in Profile & send to dataBase
            } catch(java.io.IOException e) {
                Log.e("ERR", "IOException, couldnt fetch token");
            }
            return "profile retrieved";
        }
    }

    public void createMatch(View view)
    {
        Intent intent = new Intent(this, CreateMatchActivity.class);
        startActivity(intent);
    }
}
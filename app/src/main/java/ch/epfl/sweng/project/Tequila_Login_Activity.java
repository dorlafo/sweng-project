package ch.epfl.sweng.project;


import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import ch.epfl.sweng.project.tequila.AuthServer;
import ch.epfl.sweng.project.tequila.OAuth2Config;
import ch.epfl.sweng.project.tequila.AuthClient;
import ch.epfl.sweng.project.tequila.Profile;

import java.io.IOException;
import java.util.Map;

/**
 * Tequila Login Activity class.
 *
 * @author Alexis Montavon
 *
 * This code was inspires by the example of Solal Pirelli:
 *   https://github.com/sweng-epfl/tequila-sample/tree/master/src/main/java/ch/epfl/sweng/tequila
 */
public class Tequila_Login_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tequila_login);

        String[] scopes = {"email%20profile"};
        // TODO: once we get credential put clientID here and ask what to do with Secret
        // TODO: Pas bien compris redirectUri ?
        // TODO: Comment recupéré l'uri
        String clientID = "494804924338-u894nvf9e02pgi5l2jknilvtnb8sga3p.apps.googleusercontent.com";
        String clientSecret = "toto";
        String redirectUri = "https://localhost"; // ???

        OAuth2Config config = new OAuth2Config(scopes, clientID, clientSecret, redirectUri);
        String codeRequestUrl = AuthClient.createCodeRequestUrl(config);

        // Open browser activity with the wanted URL
        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
        browserIntent.setData(Uri.parse(codeRequestUrl));
        startActivity(browserIntent);

        while(redirectUri.equals("https://localhost")) {

        }
        redirectUri = browserIntent.getData().toString();

        String code = AuthClient.extractCode(redirectUri);

        Map<String, String> tokens;
        Profile profile;
        try {
            tokens = AuthServer.fetchTokens(config, code);
            profile = AuthServer.fetchProfile(tokens.get("Tequila.profile"));
            System.out.println(profile);
        } catch (IOException TequilaError) {
            System.out.println(TequilaError.toString());
        }
    }
}

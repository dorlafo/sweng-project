package ch.epfl.sweng.project.tequila;

import android.text.TextUtils;

/**
 * Client code for Tequila authentication
 *
 * @author Alexis Montavon
 *
 * This code was taken from the example of Solal Pirelli:
 *   https://github.com/sweng-epfl/tequila-sample/tree/master/src/main/java/ch/epfl/sweng/tequila
 */

public final class AuthClient {

    public static String createCodeRequestUrl(OAuth2Config config) {
        return "https://accounts.google.com/o/oauth2/auth" +
                "?response_type=code" +
                "&client_id=" + HttpUtils.urlEncode(config.clientId) +
                "&redirect_uri=" + HttpUtils.urlEncode(config.redirectUri) +
                "&scope=" + TextUtils.join(",", config.scopes);
    }

    public static String extractCode(String redirectUri) {
        String marker = "code=";
        return redirectUri.substring(redirectUri.indexOf(marker) + marker.length());
    }
}

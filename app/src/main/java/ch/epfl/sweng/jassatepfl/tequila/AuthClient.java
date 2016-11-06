package ch.epfl.sweng.jassatepfl.tequila;


/**
 * Client code for Tequila authentication
 * <p>
 * This code was taken from the example of Solal Pirelli:
 * https://github.com/sweng-epfl/tequila-sample/tree/master/src/main/java/ch/epfl/sweng/tequila
 *
 * @author Alexis Montavon
 */
public final class AuthClient {

    public static String createCodeRequestUrl(OAuth2Config config) {

        String allScopes = "";
        for (String scope : config.scopes) {
            allScopes = scope + ',';
        }
        if (allScopes.endsWith(",")) {
            allScopes = allScopes.substring(0, allScopes.length() - 1);
        }
        return "https://tequila.epfl.ch/cgi-bin/OAuth2IdP/auth" +
                "?response_type=code" +
                "&client_id=" + HttpUtils.urlEncode(config.clientId) +
                "&redirect_uri=" + HttpUtils.urlEncode(config.redirectUri) +
                "&scope=" + allScopes;
    }

    public static String extractCode(String redirectUri) {
        String marker = "code=";
        return redirectUri.substring(redirectUri.indexOf(marker) + marker.length());
    }

}

package ch.epfl.sweng.jassatepfl.tequila;


import org.junit.Test;

import static junit.framework.Assert.assertTrue;

/**
 * Test OAuth2Config class
 *
 * @author Alexis Montavon
 */
public class OAuth2ConfigTest {

    @Test
    public void OAuth2ConfigConstructorTest() {
        String[] scopes = {"one", "two", "three"};
        String clientId = "TITI";
        String clientSecret = "TOTO";
        String redirectUri = "TUTU";
        OAuth2Config config = new OAuth2Config(scopes, clientId, clientSecret, redirectUri);
        assertTrue(config.scopes.equals(scopes));
        assertTrue(config.clientId.equals(clientId));
        assertTrue(config.clientSecret.equals(clientSecret));
        assertTrue(config.redirectUri.equals(redirectUri));
    }

}

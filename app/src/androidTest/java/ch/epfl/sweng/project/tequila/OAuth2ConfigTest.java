package ch.epfl.sweng.project.tequila;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Test OAuth2Config class
 *
 * @author Alexis Montavon
 */
@RunWith(JUnit4.class)
public class OAuth2ConfigTest {

    @Test
    public void OAuth2ConfigConstructorTest() {
        String[] scopes = {"one", "two", "three"};
        String clientId = "TITI";
        String clientSecret = "TOTO";
        String redirectUri = "TUTU";
        OAuth2Config config = new OAuth2Config(scopes, clientId, clientSecret, redirectUri);
        Assert.assertTrue(config.scopes.equals(scopes));
        Assert.assertTrue(config.clientId.equals(clientId));
        Assert.assertTrue(config.clientSecret.equals(clientSecret));
        Assert.assertTrue(config.redirectUri.equals(redirectUri));
    }
}

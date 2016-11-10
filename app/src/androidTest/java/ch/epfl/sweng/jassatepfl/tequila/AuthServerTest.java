package ch.epfl.sweng.jassatepfl.tequila;

import junit.framework.Assert;

import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.fail;


/**
 * Tests for error cases in AuthServer methods
 * Successful cases are tested on the working app
 *
 * @author Alexis Montavon
 */

public class AuthServerTest {

    @Test
    public void fetchTokensError() {
        String[] scopes = {"Tequila.profile"};
        OAuth2Config testConfig = new OAuth2Config(scopes, "TOTO", "TUTU", "TITI");
        String code = "ceciestunfauxcode";
        try{
            AuthServer.fetchTokens(testConfig, code);
            fail("Expected IOException");
        } catch(IOException e) {
            Assert.assertTrue(e.getMessage().contains("Error from Tequila: "));
        }
    }

    @Test
    public void fetchProfileError() {
        String fakeToken = "cecinestpasunvraitoken";
        try {
            AuthServer.fetchProfile(fakeToken);
            fail("Expected IOException");
        } catch(IOException i) {
            Assert.assertTrue(i.getMessage().contains("Error from Tequila: "));
        }
    }

}

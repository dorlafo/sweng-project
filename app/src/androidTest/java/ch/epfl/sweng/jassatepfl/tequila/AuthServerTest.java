package ch.epfl.sweng.jassatepfl.tequila;

import org.junit.Test;

import java.io.IOException;

/**
 * Tests for error cases in AuthServer methods
 * Successful cases are tested on the working app
 *
 * @author Alexis Montavon
 */
public class AuthServerTest {

    @Test(expected = IOException.class)
    public void fetchTokensError() throws IOException {
        String[] scopes = {"Tequila.profile"};
        OAuth2Config testConfig = new OAuth2Config(scopes, "TOTO", "TUTU", "TITI");
        String code = "ceciestunfauxcode";
        AuthServer.fetchTokens(testConfig, code);
    }

    @Test(expected = IOException.class)
    public void fetchProfileError() throws IOException {
        String fakeToken = "cecinestpasunvraitoken";
        AuthServer.fetchProfile(fakeToken);
    }

}

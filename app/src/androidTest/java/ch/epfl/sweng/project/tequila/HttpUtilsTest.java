package ch.epfl.sweng.project.tequila;

import com.google.gson.annotations.SerializedName;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.net.MalformedURLException;

import static org.junit.Assert.fail;

/**
 * Test for HttpUtils methods
 * Only error case for fetch()
 * Successful cases are tested on working app
 *
 * @author Alexis Montavon
 */

@RunWith(JUnit4.class)
public class HttpUtilsTest {

    private static final class JsonProfile {
        @SerializedName("error")
        public String error;

        @SerializedName("Firstname")
        public String firstNames;

        @SerializedName("Name")
        public String lastNames;

        @SerializedName("Email")
        public String email;

        @SerializedName("Sciper")
        public String sciper;

        @SerializedName("Username")
        public String gaspar;
    }

    @Test
    public void encodeNoError() {
        String testString = "blakdkéàçeifo";
        try {
            HttpUtils.urlEncode(testString);
        } catch (AssertionError e) {
            fail("UTF-8 is supported here, Huston we have a problem");
        }
    }

    @Test
    public void fetchError() {
        String fakeUrl = "ceci@%n@@es$ëdoedjft&pasune%vrai&url.com";
        try {
            HttpUtils.fetch(fakeUrl, JsonProfile.class);
            fail("Excepted MalformedURLException");
        } catch (AssertionError e) {
            Assert.assertTrue(e.getMessage().equals(("The URL is malformed!?")));
        } catch (IOException i) {
            fail("Should not throw this");
        }
    }
}

package ch.epfl.sweng.jassatepfl.tequila;

import com.google.gson.annotations.SerializedName;

import org.junit.Test;

import java.io.IOException;

import static junit.framework.Assert.fail;

/**
 * Test for HttpUtils methods
 * Only error case for fetch()
 * Successful cases are tested on working app
 *
 * @author Alexis Montavon
 */
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
            fail("UTF-8 is supported here, Houston we have a problem");
        }
    }

    @Test(expected = AssertionError.class)
    public void fetchError() throws IOException {
        String fakeUrl = "ceci@%n@@es$ëdoedjft&pasune%vrai&url.com";
        HttpUtils.fetch(fakeUrl, JsonProfile.class);
    }

}

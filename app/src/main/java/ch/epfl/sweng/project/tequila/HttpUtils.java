package ch.epfl.sweng.project.tequila;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

/**
 * Utilities for to HTTP requests.
 * <p>
 * This code was taken from the example of Solal Pirelli:
 * https://github.com/sweng-epfl/tequila-sample/tree/master/src/main/java/ch/epfl/sweng/tequila
 *
 * @author Alexis Montavon
 */
public final class HttpUtils {

    public static String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError("UTF-8 is not supported... all hope is lost.");
        }
    }

    public static <T> T fetch(String url, Class<T> classOfT) throws IOException {
        InputStream stream = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            stream = connection.getInputStream();
            String json = new Scanner(stream).useDelimiter("\\A").next();
            return new Gson().fromJson(json, classOfT);
        } catch (MalformedURLException e) {
            throw new AssertionError("The URL is malformed!?");
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }

}
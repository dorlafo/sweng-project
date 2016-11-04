package ch.epfl.sweng.project.notification;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Class implementing registration to notification server
 */
public class JassTokenService extends FirebaseInstanceIdService {
    public static final String SERVER_URL = "http://vps333923.ovh.net";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            registerWithServer(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), refreshedToken);
        }
    }

    /**
     * Registration with notification server
     *
     * @param sciper User sciper to register
     * @param refreshedToken Refresh token to send
     */
    public static void registerWithServer(String sciper, String refreshedToken) {
        JsonObject data = new JsonObject();
        data.addProperty("sciper", sciper);
        data.addProperty("token", refreshedToken);
        HttpURLConnection urlConnection = null;
        try {
            URL srv = new URL(SERVER_URL + "/register");
            urlConnection = (HttpURLConnection) srv.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            out.write(new Gson().toJson(data).getBytes());
            out.flush();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
        } catch (IOException e) {
            Log.e("IDSERVICE", "Could not send packet");
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
    }
}

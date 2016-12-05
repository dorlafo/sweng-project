package ch.epfl.sweng.jassatepfl.server;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
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
 * Singleton class abstracting interactions with the server.
 */
public class ServerInterface {

    public static final String SERVER_URL = "http://vps333923.ovh.net";
    private static final ServerInterface instance = new ServerInterface();
    private static Gson gson = new Gson();

    private ServerInterface() {
        // Defend against reflection attacks
        if (instance != null) {
            throw new IllegalStateException("Already Instantiated");
        }
    }

    public static ServerInterface getInstance() {
        return instance;
    }

    /**
     * Send invite notification to added player
     *
     * @param sciper  Player sciper
     * @param matchId The Match ID
     */
    public void invitePlayer(String sciper, String matchId) {
        JsonObject data = new JsonObject();
        data.addProperty("sciper", sciper);
        data.addProperty("matchId", matchId);
        data.addProperty("by", FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        sendData(data, "/invite");
    }

    /**
     * Registration with notification server
     *
     * @param sciper User sciper to register
     * @param token  Refresh token to send
     */
    public void registerSciperToken(String sciper, String token) {
        JsonObject data = new JsonObject();
        data.addProperty("sciper", sciper);
        data.addProperty("token", token);
        sendData(data, "/register");
    }

    private void sendData(JsonObject data, String uri) {
        HttpURLConnection urlConnection = null;
        try {
            URL srv = new URL(SERVER_URL + uri);
            urlConnection = (HttpURLConnection) srv.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setChunkedStreamingMode(0);
            OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
            out.write(gson.toJson(data).getBytes());
            out.flush();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
        } catch (IOException e) {
            Log.e("ServerInterface", "Could not send packet");
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
    }

}

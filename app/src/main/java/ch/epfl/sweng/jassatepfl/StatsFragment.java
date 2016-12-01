package ch.epfl.sweng.jassatepfl;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import ch.epfl.sweng.jassatepfl.server.ServerInterface;
import ch.epfl.sweng.jassatepfl.stats.DrawableRetriever;

public class StatsFragment extends Fragment {
    protected void retrieveAndDisplay(ImageView view, String graph) {
        String sciper = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String uri = ServerInterface.SERVER_URL + "/plots/" + sciper + "_" + graph + ".png";
        try {
            URL url = new URL(uri);
            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            view.setImageDrawable(
                    new DrawableRetriever().execute(url).get()
            );
        } catch (IOException e) {
            Log.e("BAD RESOURCE", "error");
        } catch (InterruptedException | ExecutionException e) {
            Log.e("CountersFragment", e.getMessage());
        }
    }
}

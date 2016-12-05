package ch.epfl.sweng.jassatepfl.stats;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;

public class DrawableRetriever extends AsyncTask<URL, Void, Drawable> {

    @Override
    protected Drawable doInBackground(URL... params) {
        Drawable drw = null;
        try {
            drw = Drawable.createFromStream(((java.io.InputStream) params[0].getContent()), "src");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  drw;
    }
}

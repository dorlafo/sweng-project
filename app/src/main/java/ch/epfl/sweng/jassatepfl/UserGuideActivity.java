package ch.epfl.sweng.jassatepfl;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.mukesh.MarkdownView;

/**
 * @author Alexis Montavon
 *
 * Activity class for the 'How To' user guide
 */
public class UserGuideActivity extends BaseActivityWithNavDrawer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_user_guide, drawer, false);
        drawer.addView(contentView, 0);
        MarkdownView userGuideView = (MarkdownView) findViewById(R.id.user_guide_text);
        userGuideView.loadMarkdownFromAssets("UserGuide.md");
    }

}

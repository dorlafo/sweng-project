package ch.epfl.sweng.jassatepfl;

import android.os.Bundle;

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
        setContentView(R.layout.activity_user_guide);
        MarkdownView userGuideView = (MarkdownView) findViewById(R.id.user_guide_text);
        userGuideView.loadMarkdownFromAssets("UserGuide.md");
    }

}

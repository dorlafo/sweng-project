package ch.epfl.sweng.project.gui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ch.epfl.sweng.project.R;

/**
 * Your app's main activity.
 */
public final class MainActivity extends AppCompatActivity {
    // TODO replace this code with your app code!

    public static int add(final int a, final int b) {
        return a + b;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
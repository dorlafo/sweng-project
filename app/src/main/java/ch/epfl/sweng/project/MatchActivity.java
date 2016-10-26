package ch.epfl.sweng.project;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MatchActivity extends AppCompatActivity {
    private String matchId;
    private final static String TAG = MatchActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);
    }
}

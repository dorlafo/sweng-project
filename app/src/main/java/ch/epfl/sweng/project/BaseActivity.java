package ch.epfl.sweng.project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;

import javax.inject.Inject;

public abstract class BaseActivity extends AppCompatActivity {
    @Inject
    DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().graph().inject(this);
    }

    protected DatabaseReference getDbRef() {
        return dbRef;
    }

}


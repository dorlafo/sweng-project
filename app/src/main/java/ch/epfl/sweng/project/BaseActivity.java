package ch.epfl.sweng.project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;

import javax.inject.Inject;

import ch.epfl.sweng.project.database.tools.DBReferenceWrapper;

public abstract class BaseActivity extends AppCompatActivity {
    @Inject
    DBReferenceWrapper dbRefWrapped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().graph().inject(this);
    }

    protected DBReferenceWrapper getDbRef() {
        return dbRefWrapped;
    }

}


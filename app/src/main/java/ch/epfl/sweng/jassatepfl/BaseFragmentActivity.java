package ch.epfl.sweng.jassatepfl;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import ch.epfl.sweng.project.database.helpers.DBReferenceWrapper;

public abstract class BaseFragmentActivity extends FragmentActivity {
    @Inject
    public DBReferenceWrapper dbRefWrapped;
    @Inject
    public FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().graph().inject(this);
    }

    protected DBReferenceWrapper getDbRef() {
        return dbRefWrapped;
    }
}

package ch.epfl.sweng.jassatepfl;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import ch.epfl.sweng.jassatepfl.database.helpers.DBReferenceWrapper;

public abstract class BaseAppCompatActivity extends AppCompatActivity {

    @Inject
    public DBReferenceWrapper dbRefWrapped;
    @Inject
    public FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().graph().inject(this);
    }

    /**
     * Getter for the user sciper
     * @return The user' sciper
     */
    public String getUserSciper() {
        String sciper = null;
        try {
            sciper = fAuth.getCurrentUser().getDisplayName();
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.toast_no_connection, Toast.LENGTH_SHORT)
                    .show();
        }
        return sciper;
    }

}

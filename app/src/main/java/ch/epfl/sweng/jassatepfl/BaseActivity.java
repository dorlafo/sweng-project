package ch.epfl.sweng.jassatepfl;
import android.content.Intent;
import android.os.Build;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import ch.epfl.sweng.jassatepfl.database.helpers.DBReferenceWrapper;

/**
 * Base activity extended by almost all other activities. This handle the top menu for log out and
 * redirect any activities to the login activity if not logged in
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Inject
    public DBReferenceWrapper dbRefWrapped;
    @Inject
    public FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getInstance().graph().inject(this);

        fAuth = FirebaseAuth.getInstance();
        showLogin();
    }

    protected DBReferenceWrapper getDbRef() {
        return dbRefWrapped;
    }

    // Creates Menu on top left corner
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_menu, menu);
        return true;
    }

    /* Handler for menu items
     * Handles the logout process by deleting stored cookies and signing out from Firebase
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_logout) {
            if (Build.VERSION.SDK_INT < 21) {
                CookieManager.getInstance().removeAllCookie();
            } else {
                CookieManager.getInstance().removeAllCookies(null);
            }

            fAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Getter for the user sciper
     * @return The user' sciper
     */
    public String getUserSciper() {
        return fAuth.getCurrentUser().getDisplayName();
    }

    /**
     * Launch the LoginActivity if the user is not yet logged in
     */
    private void showLogin() {
        if (fAuth.getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }
}

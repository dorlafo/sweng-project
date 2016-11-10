package ch.epfl.sweng.jassatepfl;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;

import com.google.firebase.auth.FirebaseAuth;

public class BaseActivity extends AppCompatActivity {

    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fAuth = FirebaseAuth.getInstance();
        showLogin();
    }

    // Creates Menu on top left corner
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_menu, menu);
        return true;
    }

    /* Handler for menu items
     * Usage of deprecated method because the new one require
     * a minimum of android 21, we set minimum android 15
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_logout) {
            if (Build.VERSION.SDK_INT < 21) {
                CookieManager.getInstance().removeAllCookie();
            } else {
                CookieManager.getInstance().removeAllCookies(null);
            }
            //Sign out from Firebase
            fAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

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

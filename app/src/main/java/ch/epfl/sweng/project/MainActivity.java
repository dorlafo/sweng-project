package ch.epfl.sweng.project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Build;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;

import com.google.firebase.auth.FirebaseAuth;


/**
 * Your app's main activity.
 */
public final class MainActivity extends AppCompatActivity {
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();
        //Show login screen if not logged in
        showLogin();
    }

    @Override
    public void onBackPressed() {
        //DO NOTHING -> it will disable the back button
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
    @SuppressWarnings("deprecation")
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                if (Build.VERSION.SDK_INT < 21) {
                    CookieManager.getInstance().removeAllCookie();
                } else {
                    CookieManager.getInstance().removeAllCookies(null);
                }
                //Sign out from Firebase
                FirebaseAuth.getInstance().signOut();
                //Show login activity
                showLogin();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Launch the LoginActivity if the user is not yet logged in
     */
    private void showLogin() {
        if(fAuth.getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    public void createMatch(View view) {
        Intent intent = new Intent(this, CreateMatchActivity.class);
        startActivity(intent);
    }

    /**
     * Launch the UserProfileActivity then the Profile button is clicked
     * @param view Required param
     */
    public void viewProfile(View view) {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }

    public void displayMatchesOnMap(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void displayMatchesInList(View view) {
        Intent intent = new Intent(this, MatchListActivity.class);
        startActivity(intent);
    }
}

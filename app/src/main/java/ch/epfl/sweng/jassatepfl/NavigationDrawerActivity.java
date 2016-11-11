package ch.epfl.sweng.jassatepfl;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.CookieManager;

import com.google.firebase.auth.FirebaseAuth;

public abstract class NavigationDrawerActivity extends AppCompatActivity
        implements OnNavigationItemSelectedListener {

    protected DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent = null;

        switch (id) {
            case R.id.nav_main:
                intent = new Intent(this, MainActivity.class);
                break;
            case R.id.nav_create:
                intent = new Intent(this, CreateMatchActivity.class);
                break;
            case R.id.nav_maps:
                intent = new Intent(this, MapsActivity.class);
                break;
            case R.id.nav_list:
                intent = new Intent(this, MatchListActivity.class);
                break;
            case R.id.nav_profile:
                intent = new Intent(this, UserProfileActivity.class);
                break;
            case R.id.nav_logout:
                // Use of deprecated method because the new one requires
                // at least android 21, we set minimum android 19
                if (Build.VERSION.SDK_INT < 21) {
                    CookieManager.getInstance().removeAllCookie();
                } else {
                    CookieManager.getInstance().removeAllCookies(null);
                }
                //Sign out from Firebase
                FirebaseAuth.getInstance().signOut();
                //Show login activity
                intent = new Intent(this, LoginActivity.class);
                break;
            case R.id.nav_exit:
                intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        startActivity(intent);
        return true;
    }

}

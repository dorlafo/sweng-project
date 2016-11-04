package ch.epfl.sweng.project.tools;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.content.ContextCompat;
import android.view.View;

import ch.epfl.sweng.project.R;

public final class PermissionHandler implements OnRequestPermissionsResultCallback {

    public static final int PERMISSION_REQUEST_CODE = 8;

    private final Activity callingActivity;
    private final String permission;

    public PermissionHandler(Activity callingActivity,
                             String permission) {
        this.callingActivity = callingActivity;
        this.permission = permission;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
        }
    }

    public boolean chamallowIsHere() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(callingActivity, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(callingActivity, permission)) {
                Snackbar.make(callingActivity.findViewById(android.R.id.content), permissionRationaleResId(), Snackbar.LENGTH_LONG)
                        .setAction(R.string.ok, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(
                                        callingActivity, new String[]{permission}, PERMISSION_REQUEST_CODE);
                            }
                        })
                        .show();
            } else {
                ActivityCompat.requestPermissions(
                        callingActivity, new String[]{permission}, PERMISSION_REQUEST_CODE);
            }
        }
    }

    public boolean isPermissionGranted() {
        return !chamallowIsHere() ||
                ContextCompat.checkSelfPermission(callingActivity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private int permissionRationaleResId() {
        switch (permission) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                return R.string.request_location_rationale;
            default:
                return R.string.request_unknown_permission;
        }
    }

}

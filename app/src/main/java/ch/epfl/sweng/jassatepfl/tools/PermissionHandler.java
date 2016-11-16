package ch.epfl.sweng.jassatepfl.tools;


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

import ch.epfl.sweng.jassatepfl.R;

/**
 * Utility class used to request a permission and to check the state of
 * that permission.
 */
public final class PermissionHandler implements OnRequestPermissionsResultCallback {

    public static final int PERMISSION_REQUEST_CODE = 33;

    private final Activity callingActivity;
    private final String permission;

    /**
     * Constructs a new PermissionHandler for the given permission.
     *
     * @param callingActivity The activity requesting the permission
     * @param permission      The requested permission
     */
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

    /**
     * Checks the current version of Android.
     *
     * @return true if the current version of Android is Marshmallow or greater,
     * false otherwise
     */
    public boolean chamallowIsHere() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    /**
     * Requests the permission and if it has previously been denied by the user
     * displays a {@link android.support.design.widget.Snackbar Snackbar}
     * explaining why the permission is needed and prompting the user to grant it.
     */
    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(callingActivity, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(callingActivity, permission)) {
                Snackbar.make(callingActivity.findViewById(android.R.id.content), permissionRationaleResId(), Snackbar.LENGTH_LONG)
                        .setAction(R.string.dialog_ok, new View.OnClickListener() {
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

    /**
     * Checks whether the permission has been granted.
     *
     * @return true if the current system version is lower than Marshmallow
     * (no runtime permission needed) or the runtime permission has been granted,
     * false otherwise.
     */
    public boolean permissionIsGranted() {
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

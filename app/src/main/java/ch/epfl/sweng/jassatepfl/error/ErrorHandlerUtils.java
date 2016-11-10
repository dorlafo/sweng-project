package ch.epfl.sweng.jassatepfl.error;

import android.content.Context;
import android.support.v7.app.AlertDialog;

/**
 * Utils function to send error messages to User
 *
 * @author Alexis Montavon
 */

//TODO: Test this method...have to see how
public class ErrorHandlerUtils {

    /**
     * Sends default Error message to User and go back to Activity
     *
     * @param context   Activity's context
     * @param titleId   The title ID in res.value.string
     * @param messageId Error Message
     */
    public static void sendErrorMessage(Context context,
                                        int titleId,
                                        int messageId) {
        new AlertDialog.Builder(context)
                .setTitle(titleId)
                .setMessage(messageId)
                .show();
    }
}

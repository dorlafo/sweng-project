package ch.epfl.sweng.project.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ch.epfl.sweng.project.MainActivity;
import ch.epfl.sweng.project.MatchActivity;
import ch.epfl.sweng.project.R;

import static ch.epfl.sweng.project.notification.NotificationMessages.*;

/**
 * Handler for FireBase Cloud Messaging notifications
 *
 * @author Alexis Montavon
 */
public class FirebaseMessagingHandler extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        String msgBody = remoteMessage.getNotification().getBody();
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + msgBody);
        }

        // Sends notification when app is on foreground.
        final NotificationMessages msgType = getMsgType(msgBody);
        sendNotification(remoteMessage.getNotification().getBody(), msgType);
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     * Redirects User depending on the msgType.
     *
     * @param messageBody FCM message body received.
     * @param msgType NotificationMessage type.
     */
    private void sendNotification(String messageBody, NotificationMessages msgType) {
        Intent intent;
        switch(msgType) {
            case MATCH_FULL:
                intent = new Intent(this, MatchActivity.class);
                break;
            case MATCH_EXPIRED:
                intent = new Intent(this, MainActivity.class);
                break;
            case PLAYER_JOINED:
                intent = new Intent(this, MatchActivity.class);
                break;
            case PLAYER_LEFT:
                intent = new Intent(this, MatchActivity.class);
                break;
            case PLAYER_ACCEPTED_INV:
                intent = new Intent(this, MatchActivity.class);
                break;
            case PLAYER_REJECTED_INV:
                intent = new Intent(this, MatchActivity.class);
                break;
            default:
                intent = new Intent(this, MainActivity.class);
                break;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //TODO: change icon with Jass@EPFL icon!
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle("Jass@EPFL")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    /**
     * Get the notification message's type
     *
     * @param msgBody The body from the remoteMessage recieved
     * @return NotificationMessages, The message type
     */
    private NotificationMessages getMsgType(String msgBody) {
        if(msgBody.equals("Match Full")) {
            return MATCH_FULL;
        } else if(msgBody.equals("Match Expired")) {
            return MATCH_EXPIRED;
        } else if(msgBody.equals("Player Joined")) {
            return PLAYER_JOINED;
        } else if(msgBody.equals("Player Left")) {
            return  PLAYER_LEFT;
        } else if(msgBody.equals("Player Accepted")) {
            return PLAYER_ACCEPTED_INV;
        } else if (msgBody.equals("Player Rejected")) {
            return PLAYER_REJECTED_INV;
        } else {
            // Default case to return player to MainActivity
            // Shouldn't happen if message are well written.
            return DEFAULT_MSG;
        }
    }
}

package ch.epfl.sweng.jassatepfl.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import ch.epfl.sweng.jassatepfl.MainActivity;
import ch.epfl.sweng.jassatepfl.R;
import ch.epfl.sweng.jassatepfl.WaitingPlayersActivity;

import static ch.epfl.sweng.jassatepfl.notification.NotificationMessages.DEFAULT_MSG;
import static ch.epfl.sweng.jassatepfl.notification.NotificationMessages.MATCH_EXPIRED;
import static ch.epfl.sweng.jassatepfl.notification.NotificationMessages.MATCH_FULL;
import static ch.epfl.sweng.jassatepfl.notification.NotificationMessages.PLAYER_INVITED_YOU;
import static ch.epfl.sweng.jassatepfl.notification.NotificationMessages.PLAYER_JOINED;
import static ch.epfl.sweng.jassatepfl.notification.NotificationMessages.PLAYER_LEFT;

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
        //Log.d(TAG, "From: " + remoteMessage.getFrom());
        NotificationMessages msgType;
        Map<String, String> msgData = remoteMessage.getData();

        // Check if message contains a data payload otherwise this isn't one of our notifications.
        if (msgData != null && msgData.size() > 0) {
            //Log.d(TAG, "Message data payload: " + msgData);
            msgType = getMsgType(msgData.get("type"));

            // Sends notification when app is on foreground & background.
            sendNotification(msgType, msgData);
        } else {
            //Log.d(TAG, "Message contains no data payload!");
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     * Redirects User depending on the msgType.
     *
     * @param msgData FCM message data.
     * @param msgType NotificationMessage type.
     */
    private void sendNotification(NotificationMessages msgType, Map<String, String> msgData) {
        Intent intent;
        switch (msgType) {
            case MATCH_FULL:
                intent = new Intent(this, WaitingPlayersActivity.class).putExtra("notif", "matchfull")
                        .putExtra("match_Id", msgData.get("matchId"));
                break;
            case MATCH_EXPIRED:
                intent = new Intent(this, MainActivity.class).putExtra("notif", "matchexpired")
                        .putExtra("match_Id", msgData.get("matchId"));
                break;
            case PLAYER_JOINED:
                intent = new Intent(this, WaitingPlayersActivity.class).putExtra("notif", "playerjoined")
                        .putExtra("match_Id", msgData.get("matchId"))
                        .putExtra("sciper", msgData.get("sciper"));
                break;
            case PLAYER_LEFT:
                intent = new Intent(this, WaitingPlayersActivity.class).putExtra("notif", "playerleft")
                        .putExtra("match_Id", msgData.get("matchId"))
                        .putExtra("sciper", msgData.get("sciper"));
                break;
            case PLAYER_INVITED_YOU:
                intent = new Intent(this, WaitingPlayersActivity.class).putExtra("notif", "invite")
                        .putExtra("match_Id", msgData.get("matchId"))
                        .putExtra("sciper", msgData.get("by"));
                break;
            default:
                intent = new Intent(this, MainActivity.class);
                break;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_notify)
                .setContentTitle(msgData.get("title"))
                .setContentText(msgData.get("body"))
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
     * @param msgType The data Type of the message
     * @return NotificationMessages The message type
     */
    private NotificationMessages getMsgType(String msgType) {
        switch (msgType) {
            case "matchfull":
                return MATCH_FULL;
            case "matchexpired":
                return MATCH_EXPIRED;
            case "playerjoined":
                return PLAYER_JOINED;
            case "playerleft":
                return PLAYER_LEFT;
            case "invite":
                return PLAYER_INVITED_YOU;
            default:
                return DEFAULT_MSG;
        }
    }

}

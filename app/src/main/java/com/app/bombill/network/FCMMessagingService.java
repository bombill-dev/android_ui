package com.app.bombill.network;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.app.bombill.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by amolmhatre on 8/7/20
 */

public class FCMMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCMMessagingService";
    String NOTIFICATION_CHANNEL_ID = "Bombill Notification";

//    @Override
//    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
//        String title = remoteMessage.getNotification().getTitle();
//        String message = remoteMessage.getNotification().getBody();
////        Log.d(TAG, "From: " + remoteMessage.getFrom());
////        Log.d(TAG,"\n"+title);
////        Log.d(TAG,"\n"+message);
//
//        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
//
//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
////                scheduleJob();
//                Log.d(TAG, "For long-running tasks (10 seconds or more)");
//            } else {
//                // Handle message within 10 seconds
////                handleNow();
//                Log.d(TAG, "For short-running tasks (within 10 seconds)");
//            }
//
//        }
//
//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//        }
//
//        Intent intent = new Intent(this, Home_Activity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_ONE_SHOT);
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
//        notificationBuilder.setAutoCancel(true)
//                .setPriority(Notification.PRIORITY_MAX)
////                .setContentTitle(title)
////                .setContentText(message)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setAutoCancel(true)
//                .setContentIntent(pendingIntent)
//                .setContentInfo("Bombill")
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setWhen(System.currentTimeMillis())
//                .setTicker("Amol Mhatre");
//        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(0, notificationBuilder.build());
//    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(
                remoteMessage.getNotification().getTitle(),
                remoteMessage.getNotification().getBody());
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("The token refreshed ",token);
    }

    public void showNotification(String title, String message){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        builder.setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(999, builder.build());
    }
}

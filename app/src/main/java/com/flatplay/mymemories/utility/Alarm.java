package com.flatplay.mymemories.utility;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.flatplay.mymemories.R;
import com.flatplay.mymemories.ui.dashboard.DashboardFragment;
import com.flatplay.mymemories.ui.more.MoreFragment;

import static android.content.Context.NOTIFICATION_SERVICE;

public class Alarm extends BroadcastReceiver {
    public static final String NOTIFICATION_CHANNEL_ID = "channel_id";
    public static final String CHANNEL_NAME = "Notification Channel";
    private int importance = NotificationManager.IMPORTANCE_DEFAULT;
    private NotificationChannel notificationChannel;
    public static final int NOTIFICATION_ID = 101;


    @Override
    public void onReceive(Context context, Intent intent) {

        createNotification(context);
    }

    private void createNotification(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, CHANNEL_NAME, importance);
            //Boolean value to set if lights are enabled for Notifications from this Channel
            notificationChannel.enableLights(true);
            //Boolean value to set if vibration are enabled for Notifications from this Channel
            notificationChannel.enableVibration(true);
            //Sets the color of Notification Light
            notificationChannel.setLightColor(Color.GREEN);
            //Set the vibration pattern for notifications. Pattern is in milliseconds with the format {delay,play,sleep,play,sleep...}
            notificationChannel.setVibrationPattern(new long[]{
                    500,
                    500,
                    500,
                    500,
                    500
            });
            //Sets whether notifications from these Channel should be visible on Lockscreen or not
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

        }

        Intent intent = new Intent(context, DashboardFragment.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(notificationChannel);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);

        builder.setContentTitle("This is heading");
        builder.setContentText("This is description");
        builder.setSmallIcon(R.mipmap.applogo3);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.applogo3));
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setStyle(new NotificationCompat.BigTextStyle()
                .bigText("djksfdnmadjfnskdjhfnkdjhsfbkadgfk" +
                        "fjkghsfhbvjdhbjkzhdfbvjhzbfjvhzjkfhvj" +
                        "kzdhfbvjkzdfjkvhdjkhvfjkdhvjkdvfkvdfk" +
                        "jzfdkfnzkjxnckjdkjdnkjnkvjn" +
                        "jvhkjdhvjdvckjhdvfkdvdhvjd"));

        Notification notification = builder.build();
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(NOTIFICATION_ID, notification);


    }
}

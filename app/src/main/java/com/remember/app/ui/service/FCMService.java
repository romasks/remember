package com.remember.app.ui.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.remember.app.R;
import com.remember.app.ui.cabinet.events.EventFullActivity;
import com.remember.app.ui.cabinet.memory_pages.events.current_event.CurrentEvent;
import com.remember.app.ui.cabinet.memory_pages.show_page.ShowPageActivity;
import com.remember.app.ui.grid.GridActivity;

import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import static com.remember.app.data.Constants.INTENT_EXTRA_EVENT_ID;
import static com.remember.app.data.Constants.INTENT_EXTRA_FROM_NOTIF;
import static com.remember.app.data.Constants.INTENT_EXTRA_ID;
import static com.remember.app.data.Constants.INTENT_EXTRA_IS_LIST;
import static com.remember.app.data.Constants.INTENT_EXTRA_PERSON;

public class FCMService extends FirebaseMessagingService {

    private static final String TAG = FCMService.class.getSimpleName();
    Bitmap bitmap;
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> params = remoteMessage.getData();
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            sendNotification(params);
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

    }

    private void sendNotification(Map<String, String> messageData) {
        Intent intent = new Intent(this, GridActivity.class);
        Log.d("TTTTT", "base");
        if (Objects.equals(messageData.get("type"), "event")) {
            intent = new Intent(this, EventFullActivity.class);
            intent.putExtra(INTENT_EXTRA_EVENT_ID, Integer.parseInt(messageData.get("event_id")));
            intent.putExtra(INTENT_EXTRA_FROM_NOTIF, true);
            Log.d("TTTTT", "event");
        }

        if (Objects.equals(messageData.get("type"), "dead_event")) {
            intent = new Intent(this, CurrentEvent.class);
            intent.putExtra(INTENT_EXTRA_EVENT_ID, Integer.parseInt(messageData.get("event_id")));
            intent.putExtra(INTENT_EXTRA_PERSON, messageData.get("page_name"));
            intent.putExtra(INTENT_EXTRA_FROM_NOTIF, true);
            Log.d("TTTTT", "dead_event");
        }

        if (Objects.equals(messageData.get("type"), "dead") || Objects.equals(messageData.get("type"), "birth")) {
            intent = new Intent(this, ShowPageActivity.class);
            intent.putExtra(INTENT_EXTRA_ID, Integer.parseInt(messageData.get("page_id")));
            intent.putExtra(INTENT_EXTRA_PERSON, messageData.get("page_name"));
            intent.putExtra(INTENT_EXTRA_IS_LIST, true);
            Log.d("TTTTT", "birth");

            intent.setAction("firstLink");
          //  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("firstLink", messageData.get("page_id"));
            //PendingIntent pendingFirst = PendingIntent.getActivity(this, 555, intent, PendingIntent.FLAG_ONE_SHOT);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);


        String title = messageData.get("title");
        String body = messageData.get("body");
        String picture = messageData.get("picture");

        String channelId = getPackageName();
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_remember)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }


//        if (picture != null && !picture.equals("null"))
//            getImageBitmap(BASE_SERVICE_URL + picture);
//        else
        if (notificationManager != null) {
            notificationManager.notify(new Random().nextInt() /* ID of notification */, notificationBuilder.build());
        }
    }

    private Bitmap getImageBitmap(final String shareImageUrl) {
        if (shareImageUrl == null) return null;

        new Thread(() -> {
            try {
                bitmap = BitmapFactory.decodeStream((new URL(shareImageUrl)).openConnection().getInputStream());

                notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap));
                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
            } catch (Exception e) {
                e.printStackTrace();
                bitmap = null;

                notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
            }
        }).start();
        return bitmap;
    }
}
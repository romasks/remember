package com.remember.app.push

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.remember.app.R
import com.remember.app.Remember.Companion.active
import com.remember.app.data.Constants
import com.remember.app.ui.cabinet.events.EventFullActivity
import com.remember.app.ui.cabinet.memory_pages.events.current_event.CurrentEvent
import com.remember.app.ui.cabinet.memory_pages.show_page.ShowPageActivity
import com.remember.app.ui.chat.ChatActivity
import com.remember.app.ui.grid.GridActivity
import java.lang.Integer.parseInt
import java.net.URL
import java.util.*


class FCMService : FirebaseMessagingService() {
    var bitmap: Bitmap? = null
    lateinit var notificationBuilder: NotificationCompat.Builder
    private var notificationManager: NotificationManager? = null
    val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    //var soundUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.sound)


    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.from)
        if (remoteMessage.data.isNotEmpty()) {
            val params = remoteMessage.data
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
            if (!active)
                sendNotification(params)
        }
    }

    override fun onNewToken(token: String) {}

    private fun sendNotification(messageData: Map<String, String>) {
        var intent = Intent(this, GridActivity::class.java)
        val title = messageData["title"]
        val body = messageData["body"]
        val data = messageData["data"]
        val id = messageData["user_id"]
        val picture = messageData["picture"]
        val channelId = packageName

        if (messageData["type"] == "event") {
            intent = Intent(this, EventFullActivity::class.java)
            intent.putExtra(Constants.INTENT_EXTRA_EVENT_ID, messageData["event_id"]!!.toInt())
            intent.putExtra(Constants.INTENT_EXTRA_FROM_NOTIF, true)
        }
        if (messageData["type"] == "dead_event") {
            intent = Intent(this, CurrentEvent::class.java)
            intent.putExtra(Constants.INTENT_EXTRA_EVENT_ID, messageData["event_id"]!!.toInt())
            intent.putExtra(Constants.INTENT_EXTRA_PERSON, messageData["page_name"])
            intent.putExtra(Constants.INTENT_EXTRA_FROM_NOTIF, true)
        }
        if (messageData["type"] == "dead" || messageData["type"] == "birth") {
            intent = Intent(this, ShowPageActivity::class.java)
            intent.putExtra(Constants.INTENT_EXTRA_ID, messageData["page_id"]!!.toInt())
            intent.putExtra(Constants.INTENT_EXTRA_PERSON, messageData["page_name"])
            intent.putExtra(Constants.INTENT_EXTRA_IS_LIST, true)
            intent.action = "firstLink"
            intent.putExtra("firstLink", messageData["page_id"])
        }

        if (title == "Новое сообщение") {
            intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("type", "push")
            intent.putExtra("visaviID", id)
            Log.d("DEBAGPUSH", "push $id FCM")
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT)


        notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_remember)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setPriority(PRIORITY_MAX)
              //  .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + baseContext.packageName + "/" + R.raw.sound))
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setOnlyAlertOnce(true)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                    getString(R.string.app_name),
                    NotificationManager.IMPORTANCE_HIGH)
            val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
            channel.setSound(defaultSoundUri, audioAttributes)
            if (notificationManager != null) {
                notificationManager!!.createNotificationChannel(channel)
            }
        }
//        if (picture != null && !picture.equals("null"))
//            getImageBitmap(BASE_SERVICE_URL + picture);
//        else
        var notificationID = Random().nextInt();
        if (title == "Новое сообщение") {
            notificationID = parseInt(id!!)
        }
        if (notificationManager != null) {
            Log.d("NOTIFICATION", notificationID.toString())
            notificationManager!!.notify(notificationID, notificationBuilder.build())
         //   val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val r = RingtoneManager.getRingtone(applicationContext, defaultSoundUri)
            r.play()
        }
    }

    private fun getImageBitmap(shareImageUrl: String?): Bitmap? {
        if (shareImageUrl == null) return null
        Thread(Runnable {
            try {
                bitmap = BitmapFactory.decodeStream(URL(shareImageUrl).openConnection().getInputStream())
                notificationBuilder!!.setStyle(NotificationCompat.BigPictureStyle().bigPicture(bitmap))
                notificationManager!!.notify(0 /* ID of notification */, notificationBuilder!!.build())
            } catch (e: Exception) {
                e.printStackTrace()
                bitmap = null
                notificationManager!!.notify(0 /* ID of notification */, notificationBuilder!!.build())
            }
        }).start()
        return bitmap
    }

    companion object {
        private val TAG = FCMService::class.java.simpleName
    }
}
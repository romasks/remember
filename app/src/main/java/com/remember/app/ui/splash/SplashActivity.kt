package com.remember.app.ui.splash

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.VideoView
import androidx.annotation.Nullable
import butterknife.BindView
import com.pixplicity.easyprefs.library.Prefs
import com.remember.app.BuildConfig
import com.remember.app.R
import com.remember.app.data.Constants
import com.remember.app.data.Constants.INTENT_EXTRA_PAGE_ID
import com.remember.app.data.Constants.PREFS_KEY_IS_LAUNCH_MODE
import com.remember.app.ui.base.BaseActivity
import com.remember.app.ui.cabinet.events.EventFullActivity
import com.remember.app.ui.cabinet.memory_pages.events.current_event.CurrentEvent
import com.remember.app.ui.cabinet.memory_pages.show_page.ShowPageActivity
import com.remember.app.ui.grid.GridActivity
import com.remember.app.ui.utils.Utils

class SplashActivity : BaseActivity(), SplashView {

    @BindView(R.id.videoView)
    lateinit var videoView: VideoView

    companion object {
        @JvmStatic
        fun clearPrefs() {
            if (!BuildConfig.DEBUG)
                Prefs.clear()
        }
    }

    override fun getContentView() = R.layout.activity_splash

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        Utils.setTheme(this)
        super.onCreate(savedInstanceState)
        checkIntentForNotification()
        Prefs.putBoolean(PREFS_KEY_IS_LAUNCH_MODE, true)
        startActivity(Intent(this, GridActivity::class.java))
        finish()
        startActivity(openActivity(checkIntentForNotification()))

    }


//    2020-07-08 07:26:55.062 25279-25279/com.remember.app D/TTTTTD: page_id ->key; 127 -> value;
//    2020-07-08 07:26:55.063 25279-25279/com.remember.app D/TTTTTD: picture ->key; /uploads/pages/127/1560762372112.png -> value;
//    2020-07-08 07:26:55.064 25279-25279/com.remember.app D/TTTTTD: body ->key; Осталось 4 дня до День памяти у Новодворская Валерия Ильинична -> value;
//    2020-07-08 07:26:55.068 25279-25279/com.remember.app D/TTTTTD: from ->key; /topics/user-1 -> value;
//    2020-07-08 07:26:55.070 25279-25279/com.remember.app D/TTTTTD: type ->key; dead -> value;
//    2020-07-08 07:26:55.072 25279-25279/com.remember.app D/TTTTTD: title ->key; День памяти -> value;
//    2020-07-08 07:26:55.073 25279-25279/com.remember.app D/TTTTTD: event_id ->key; null -> value;
//    2020-07-08 07:26:55.074 25279-25279/com.remember.app D/TTTTTD: google.message_id ->key; 0:1594182412283311%13da1e8d13da1e8d -> value;
//    2020-07-08 07:26:55.075 25279-25279/com.remember.app D/TTTTTD: next_date ->key; Sun Jul 12 2020 00:00:00 GMT+0000 (Coordinated Universal Time) -> value;
//    2020-07-08 07:26:55.076 25279-25279/com.remember.app D/TTTTTD: origin_date ->key; Sat Jul 12 2014 00:00:00 GMT+0000 (Coordinated Universal Time) -> value;


    fun checkIntentForNotification() : NotificationModel {
        val model = NotificationModel(null,"",null, "")
        if (intent.extras != null) {
            for (key in intent.extras!!.keySet()) {
                val value = intent.extras!!.getString(key)
                if (value != null && value != "") {
                    if (key=="type")
                        model.type = value
                    if (key=="page_id")
                        model.pageid = value
                    if (key=="event_id")
                        model.eventId = value
                    if (key=="page_name")
                        model.pageName = value
                    if (value == "") {
                        Log.d("TTTTTD", "value zero")
                    }
                    Log.d("TTTTTD", "$key ->key; $value -> value;")
                    val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.cancel(9379992)
                }
            }
        }
        return model
    }

    fun openActivity(model : NotificationModel?)  : Intent{
        var intent = Intent(this, GridActivity::class.java)
        model?.let {
            if (it.type == "event") {
                intent = Intent(this, EventFullActivity::class.java)
                intent.putExtra(Constants.INTENT_EXTRA_EVENT_ID, it.eventId?.toIntOrNull())
                intent.putExtra(Constants.INTENT_EXTRA_FROM_NOTIF, true)
                Log.d("TTTTTWWWWWW", "event")
                Log.d("TTTTTWWWWWW", "eveввnt")
            }

            if (it.type == "dead_event") {
                intent = Intent(this, CurrentEvent::class.java)
                intent.putExtra(Constants.INTENT_EXTRA_EVENT_ID, it.eventId?.toInt())
                intent.putExtra(Constants.INTENT_EXTRA_PERSON, it.pageName)
                intent.putExtra(Constants.INTENT_EXTRA_FROM_NOTIF, true)
                intent.putExtra(INTENT_EXTRA_PAGE_ID, it.pageid)
                intent.putExtra(Constants.INTENT_EXTRA_OWNER_ID, "0")
                Log.d("TTTTTWWWWW", "dead_event")
                Log.d("TTTTTWWWWW", "${it.eventId}->eventID; ${it.pageName}->pageName; ${it.pageid}->pageId; ${it.type}->type;  " )
            }

            if (it.type == "dead" || it.type == "birth") {
                intent = Intent(this, ShowPageActivity::class.java)
                intent.putExtra(Constants.INTENT_EXTRA_ID, it.pageid?.toIntOrNull())
                intent.putExtra(Constants.INTENT_EXTRA_PERSON, it.pageName)
                intent.putExtra(Constants.INTENT_EXTRA_IS_LIST, true)
                intent.putExtra(Constants.INTENT_EXTRA_FROM_NOTIF, true)
                Log.d("TTTTTWWWWWW", "birth")
            }
        }

        return intent
    }
}

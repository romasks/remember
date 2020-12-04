package com.remember.app.ui.splash

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
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
import com.remember.app.ui.chat.ChatActivity
import com.remember.app.ui.grid.GridActivity
import com.remember.app.utils.Utils


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
        Prefs.putBoolean(PREFS_KEY_IS_LAUNCH_MODE, true)
        startActivity(Intent(this, GridActivity::class.java))
        finish()
        startActivity(openActivity(checkIntentForNotification()))
    }

    private fun checkIntentForNotification(): NotificationModel {
        val model = NotificationModel(null, "", null, "")
        if (intent.extras != null) {
            for (key in intent.extras!!.keySet()) {
                val value = intent.extras!!.getString(key)
                if (value != null && value != "") {
                    if (key == "type")
                        model.type = value
                    if (key == "page_id")
                        model.pageid = value
                    if (key == "event_id")
                        model.eventId = value
                    if (key == "page_name")
                        model.pageName = value
                    if (value == "Новое сообщение") {
                        model.type = "push"
                    }
                    if (key == "user_id") {
                        model.userId = value
                    }
                    val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationManager.cancel(9379992)
                }
            }
        }
        return model
    }

    private fun openActivity(model: NotificationModel?): Intent {
        var intent = Intent(this, GridActivity::class.java)
        intent.flags = FLAG_ACTIVITY_CLEAR_TOP
        model?.let {
            Log.d("DEBAGPUSH", "type->${it.type}; id->${it.userId} ${it.toString()}")
            if (it.type == "event") {
                intent = Intent(this, EventFullActivity::class.java)
                intent.putExtra(Constants.INTENT_EXTRA_EVENT_ID, it.eventId?.toIntOrNull())
                intent.putExtra(Constants.INTENT_EXTRA_FROM_NOTIF, true)
            }

            if (it.type == "dead_event") {
                intent = Intent(this, CurrentEvent::class.java)
                intent.putExtra(Constants.INTENT_EXTRA_EVENT_ID, it.eventId?.toInt())
                intent.putExtra(Constants.INTENT_EXTRA_PERSON, it.pageName)
                intent.putExtra(Constants.INTENT_EXTRA_FROM_NOTIF, true)
                intent.putExtra(INTENT_EXTRA_PAGE_ID, it.pageid)
                intent.putExtra(Constants.INTENT_EXTRA_OWNER_ID, "0")
            }

            if (it.type == "dead" || it.type == "birth") {
                intent = Intent(this, ShowPageActivity::class.java)
                intent.putExtra(Constants.INTENT_EXTRA_ID, it.pageid?.toIntOrNull())
                intent.putExtra(Constants.INTENT_EXTRA_PERSON, it.pageName)
                intent.putExtra(Constants.INTENT_EXTRA_IS_LIST, true)
                intent.putExtra(Constants.INTENT_EXTRA_FROM_NOTIF, true)
            }
            if (it.type == "push") {
                startActivity(intent)
                intent = Intent(this, ChatActivity::class.java)
                intent.putExtra("type", "push")
                intent.putExtra("visaviID", it.userId)
                Log.d("DEBAGPUSH", "${it.userId} ${it.type} Splash")
            }
        }
        return intent
    }
}

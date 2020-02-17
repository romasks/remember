package com.remember.app.ui.splash

import android.content.Intent
import android.os.Bundle
import android.widget.VideoView
import androidx.annotation.Nullable
import butterknife.BindView
import com.pixplicity.easyprefs.library.Prefs
import com.remember.app.BuildConfig
import com.remember.app.R
import com.remember.app.data.Constants.PREFS_KEY_IS_LAUNCH_MODE
import com.remember.app.ui.base.BaseActivity
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

        Prefs.putBoolean(PREFS_KEY_IS_LAUNCH_MODE, true)
        startActivity(Intent(this, GridActivity::class.java))
        finish()
    }
}

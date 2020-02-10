package com.remember.app.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.widget.VideoView;

import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.grid.GridActivity;
import com.remember.app.ui.utils.Utils;

import androidx.annotation.Nullable;
import butterknife.BindView;
import umairayub.madialog.MaDialog;
import umairayub.madialog.MaDialogListener;

import static com.remember.app.data.Constants.INTENT_EXTRA_IS_LAUNCH_MODE;

public class SplashActivity extends BaseActivity implements SplashView {

    @BindView(R.id.videoView)
    VideoView videoView;

    public static void clearPrefs() {
        Prefs.clear();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Utils.setTheme(this);
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, GridActivity.class);
        intent.putExtra(INTENT_EXTRA_IS_LAUNCH_MODE, true);
        startActivity(intent);
        finish();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_splash;
    }
}

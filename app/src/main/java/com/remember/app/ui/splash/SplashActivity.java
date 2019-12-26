package com.remember.app.ui.splash;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import com.remember.app.R;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.grid.GridActivity;
import com.remember.app.ui.utils.Utils;

import androidx.annotation.Nullable;
import butterknife.BindView;

public class SplashActivity extends BaseActivity implements SplashView {

    @BindView(R.id.videoView)
    VideoView videoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Utils.setTheme(this);
        super.onCreate(savedInstanceState);

        startVideo();
    }

    private void startVideo() {
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.pomnyu_logo_animation);
        videoView.setVideoURI(videoUri);
        videoView.setOnCompletionListener(mp -> {
            startActivity(new Intent(this, GridActivity.class));
            finish();
        });
        videoView.start();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_splash;
    }
}

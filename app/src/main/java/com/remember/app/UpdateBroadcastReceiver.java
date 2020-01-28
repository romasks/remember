package com.remember.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.remember.app.ui.splash.SplashActivity;

public class UpdateBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SplashActivity.clearPrefs();
    }
}

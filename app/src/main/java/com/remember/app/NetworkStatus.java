package com.remember.app;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import com.remember.app.Remember;

public class NetworkStatus {
    private static final String TAG = "NetworkStatus";

    public enum Status {
        WIFI,
        MOBILE,
        ETHERNET,
        OFFLINE
    }

    private static Status currentStatus = Status.OFFLINE;

    private static Context appContext = Remember.applicationComponent.context();

    private static boolean isAirplane() {
        return Settings.Global.getInt(appContext.getContentResolver(),
                Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
    }

    public static Status getStatus() {
        ConnectivityManager cm = (ConnectivityManager) appContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                //Log.d(TAG, "Network changed to Wi-Fi");
                currentStatus = Status.WIFI;
            }

            if (activeNetwork.getType() == ConnectivityManager.TYPE_ETHERNET) {
                //Log.d(TAG, "Network changed to Ethernet");
                currentStatus = Status.ETHERNET;
            }

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                //Log.d(TAG, "Network changed to mobile");
                currentStatus = Status.MOBILE;
            }
        } else {
            currentStatus = Status.OFFLINE;
        }

        return currentStatus;
        //Log.d(TAG, "Network changed to none");

    }


    public static boolean isOnline() {
        getStatus();
        return currentStatus.equals(Status.WIFI) || currentStatus.equals(Status.MOBILE) || currentStatus.equals(Status.ETHERNET);
    }

    public static boolean isWifi() {
        return getStatus().equals(Status.WIFI);
    }

    public static boolean isEthernet() {
        return getStatus().equals(Status.ETHERNET);
    }

    public static boolean isMobile() {
        return getStatus().equals(Status.MOBILE);
    }

    public static boolean isOffline() {
        return getStatus().equals(Status.OFFLINE);
    }
}

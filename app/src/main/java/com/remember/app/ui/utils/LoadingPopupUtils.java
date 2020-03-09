package com.remember.app.ui.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingPopupUtils {

    public static ProgressDialog setLoadingDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Идет загрузка...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }
}

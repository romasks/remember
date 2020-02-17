package com.remember.app.ui.base;

import android.os.Bundle
import butterknife.ButterKnife
import butterknife.Unbinder
import com.remember.app.R
import com.remember.app.ui.utils.MvpAppCompatActivity
import com.remember.app.ui.utils.Utils
import umairayub.madialog.MaDialog

abstract class BaseActivity: MvpAppCompatActivity(), BaseView {

    private lateinit var unbinder: Unbinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentView())
        unbinder = ButterKnife.bind(this)
        if (Utils.isThemeDark()) setViewsInDarkTheme()
    }

    protected abstract fun getContentView(): Int

    protected open fun setViewsInDarkTheme() {}

    override fun onDestroy() {
        unbinder.unbind()
        super.onDestroy()
    }

    override fun onErrorOffline() =
        MaDialog.Builder(this@BaseActivity)
            .setTitle("Отсутствует интернет-соединение")
            .setMessage("Пожалуйста, повторите действие позже")
            .setPositiveButtonText("ok")
            .setImage(R.drawable.ic_no_internet)
            .setCancelableOnOutsideTouch(false)
            .setPositiveButtonListener(this::finish)
            .build()
}


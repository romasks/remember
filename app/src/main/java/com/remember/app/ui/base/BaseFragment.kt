package com.remember.app.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import butterknife.ButterKnife
import butterknife.Unbinder
import com.remember.app.utils.ErrorDialog
import com.remember.app.utils.MvpAppCompatFragment

abstract class BaseFragment: MvpAppCompatFragment(), BaseView {

    protected lateinit var unbinder: Unbinder

    @Nullable
    override fun onCreateView(
        @NonNull inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(getContentView(), container, false)
        unbinder = ButterKnife.bind(this, view)
        setUp()
        return view
    }

    protected abstract fun getContentView(): Int

    protected abstract fun setUp()

    override fun onDestroy() {
        super.onDestroy()
        unbinder.unbind()
    }

    override fun onErrorOffline() {
        ErrorDialog(fragmentManager, "Проверьте подключение к сети", "errorOfflineDialog")
    }
}

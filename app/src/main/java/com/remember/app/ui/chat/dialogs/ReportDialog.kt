package com.remember.app.ui.chat.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.remember.app.R
import kotlinx.android.synthetic.main.dialog_report.*

class ReportDialog : DialogFragment() {

    lateinit var listener: ReportListener

    override fun onStart() {
        super.onStart()
        // val width = LinearLayout.LayoutParams.MATCH_PARENT
        val width = setWidthByDensity(this)
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        dialog!!.window?.setLayout(width, height)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //  viewModel = ViewModelProvider(activity!!).get(MainViewModel::class.java)


        return initView(container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        btnReport.setOnClickListener {
            dismiss()
            listener.clickOk()
        }
        btnCancel.setOnClickListener {
            dismiss()
            listener.clickCancel()
        }
    }

    private fun initView(container: ViewGroup?): View {
        val builder = AlertDialog.Builder(activity!!)
        val li = LayoutInflater.from(builder.context)
        val view = li.inflate(R.layout.dialog_report, container)
        val bundle = this.arguments
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent)

        builder.setView(view)
        builder.create()
        return view
    }

    private fun setWidthByDensity(fragment: DialogFragment): Int {
        val metrics = DisplayMetrics()
        fragment.activity!!.windowManager.defaultDisplay.getMetrics(metrics)
        return (metrics.widthPixels) / 100 * 90
    }

    interface ReportListener {
        fun clickOk()
        fun clickCancel()
    }
}
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
import kotlinx.android.synthetic.main.dialog_delete_message.*


class DeleteMessageDialog : DialogFragment() {

    lateinit var listener: DeleteMessageListener
    var type = ""

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
        type = this.arguments?.getString("type")!!
        initUI()

    }

   private fun setTitleAndBodyByType(type : String){
        when(type){
            "forAll"->{ tvBody.text = "Вы действительно хотите удалить все сообщения с данным пользователем? Отменить это действие будет невозможно."
                tvTitle.text = "Удалить все сообщения"}
            else -> {tvBody.text = "Вы действительно хотите удалить сообщение? Отменить это действие будет невозможно."
                tvTitle.text = "Удалить сообщение"}
        }
    }

    private fun initUI() {
        btnForAll.setOnClickListener {
            dismiss()
            listener.deleteForAll()
        }
        btnForMe.setOnClickListener {
            dismiss()
            listener.deleteForMe()
        }
        setTitleAndBodyByType(type)
    }

    private fun initView(container: ViewGroup?): View {
        val builder = AlertDialog.Builder(activity!!)
        val li = LayoutInflater.from(builder.context)
        val view = li.inflate(R.layout.dialog_delete_message, container)
        val bundle = this.arguments
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent)
        //isCancelable = false
        builder.setView(view)
        builder.create()
        return view
    }

    private fun setWidthByDensity(fragment: DialogFragment): Int {
        val metrics = DisplayMetrics()
        fragment.activity!!.windowManager.defaultDisplay.getMetrics(metrics)
        return (metrics.widthPixels) / 100 * 95
    }

    interface DeleteMessageListener {
        fun deleteForAll()
        fun deleteForMe()
    }
}
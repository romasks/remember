package com.remember.app.ui.chat

import android.os.Bundle
import android.view.View
import com.ambitt.utils.replaceFragmentSafely
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.remember.app.R
import com.remember.app.data.Constants
import com.remember.app.data.models.MemoryPageModel
import com.remember.app.ui.base.BaseFragmentMVVM
import kotlinx.android.synthetic.main.fragment_maintainer_page.*

class MaintainerPageFragment : BaseFragmentMVVM() {
    override val layoutId: Int = R.layout.fragment_maintainer_page

    companion object {
        fun newInstance(bundle: Bundle) =
                MaintainerPageFragment().apply {
                    arguments = bundle
                }

        val TAG = "MaintainerPageFragment"
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val model = arguments!!.getParcelable<MemoryPageModel>("model")
        val type = arguments!!.getString("type")
        initUI(model, type!!)
    }

    private fun initUI(model: MemoryPageModel?, type: String) {
        btnBack.setOnClickListener {
            (activity as ChatActivity).onBackPressed()
        }

        model?.let {
            maintainerName.text = it.creatorData?.settingsName
            Glide.with(context!!).load(R.drawable.darth_vader).load(Constants.BASE_URL_FROM_PHOTO + it.creatorData?.picture).transform(CenterInside(), RoundedCorners(1000)).into(imgProfile)
            tvCity.text = "Населенный пункт: ${it.creatorData?.location}"
//            if (type == "maintainer") {
//                btnSendComment.visibility = View.VISIBLE
                btnSendComment.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putParcelable("model", model)
                    replaceFragmentSafely(ChatFragment.newInstance(bundle), "ChatFragment", false, true, R.id.container)
                }
            }
//        else {
//                btnSendComment.visibility = View.GONE
//            }
      //  }

    }
}
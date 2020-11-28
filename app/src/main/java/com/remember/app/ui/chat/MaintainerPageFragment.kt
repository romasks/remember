package com.remember.app.ui.chat

import android.os.Bundle
import android.view.View
import android.view.WindowId
import androidx.lifecycle.Observer
import com.ambitt.utils.replaceFragmentSafely
import com.bumptech.glide.Glide
import com.remember.app.R
import com.remember.app.data.Constants
import com.remember.app.data.models.ChatUser
import com.remember.app.data.models.MemoryPageModel
import com.remember.app.ui.base.BaseFragmentMVVM
import com.remember.app.utils.ImageUtils.setGlideImageWithError
import kotlinx.android.synthetic.main.fragment_maintainer_page.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.lang.Integer.parseInt

class MaintainerPageFragment : BaseFragmentMVVM() {
    override val layoutId: Int = R.layout.fragment_maintainer_page
    val chatViewModel: ChatViewModel by sharedViewModel()

    companion object {
        fun newInstance(bundle: Bundle) =
                MaintainerPageFragment().apply {
                    arguments = bundle
                }

        val TAG = "MaintainerPageFragment"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val type = arguments!!.getString("type")!!
        val id = arguments!!.getString("visaviID", "0")!!
        initLiveData(type, id)
    }

    private fun initLiveData(type: String, id: String) {
        chatViewModel.getChatUser(parseInt(id))
        chatViewModel.chatUser.observe(viewLifecycleOwner, Observer {
            it?.let {
                initUI(type, it)
            }
        })
    }

    private fun initUI(type: String, user: ChatUser?) {
        btnBack.setOnClickListener {
            (activity as ChatActivity).onBackPressed()
        }
        when (type) {
            "maintainer" -> {
                user?.let {
                    toolbarTitle.text = "Профиль создателя"
                    maintainerName.text = "${it.name} ${it.surname}"
                    if (it.picture != null && !it.picture.contains("http"))
                        setGlideImageWithError(context, Constants.BASE_URL_FROM_PHOTO + it.picture, imgProfile)
                    else
                        setGlideImageWithError(context, it.picture, imgProfile)
                    if (!it.location.isNullOrEmpty())
                        tvCity.text = "Населенный пункт: ${it.location}"
                    btnSendComment.setOnClickListener {
                        val bundle = Bundle()
                        bundle.putString("type", "profile")
                        bundle.putString("visaviID", user.id.toString())
                        replaceFragmentSafely(ChatFragment.newInstance(bundle), "ChatFragment", false, true, R.id.container)
                    }
                }
            }
            "user" -> {
                user?.let {
                    toolbarTitle.text = "Профиль пользователя"
                    maintainerName.text = "${it.name} ${it.surname}"
                    if (it.picture != null && !it.picture.contains("http"))
                        setGlideImageWithError(context, Constants.BASE_URL_FROM_PHOTO + it.picture, imgProfile)
                    else
                        setGlideImageWithError(context, it.picture, imgProfile)
                    if (!it.location.isNullOrEmpty())
                        tvCity.text = "Населенный пункт: ${it.location}"
                    btnSendComment.visibility = View.GONE
                }
            }
        }
    }
}
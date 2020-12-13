package com.remember.app.ui.cabinet.memory_pages.show_page

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog
import com.jaychang.sa.utils.StringUtils
import com.pixplicity.easyprefs.library.Prefs
import com.remember.app.R
import com.remember.app.data.Constants
import com.remember.app.data.models.MemoryPageModel
import com.remember.app.data.models.ResponseImagesSlider
import com.remember.app.ui.adapters.PhotoSliderAdapter
import com.remember.app.ui.base.BaseActivity
import com.remember.app.ui.cabinet.biography.BiographyActivity
import com.remember.app.ui.cabinet.epitaphs.EpitaphsActivity
import com.remember.app.ui.cabinet.main.MainActivity
import com.remember.app.ui.cabinet.memory_pages.add_page.NewMemoryPageActivity
import com.remember.app.ui.cabinet.memory_pages.events.EventsActivity
import com.remember.app.ui.chat.ChatActivity
import com.remember.app.utils.*
import com.remember.app.utils.KotlinUtils.getCreatedDaysAgo
import com.theartofdev.edmodo.cropper.CropImage
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKScope
import com.vk.sdk.VKSdk
import com.vk.sdk.api.VKError
import com.vk.sdk.dialogs.VKShareDialog
import com.vk.sdk.dialogs.VKShareDialogBuilder
import kotlinx.android.synthetic.main.activity_page.*
import kotlinx.android.synthetic.main.app_bar_layout.*
import java.io.File


public class ShowPageActivity : BaseActivity(), PopupMap.Callback, ShowPageView, PhotoDialog.Callback, PhotoSliderAdapter.ItemClickListener, SlidePhotoActivity.DeleteCallBack {
    private var FROM_NOTIFICATION = false
    private val TAG = ShowPageActivity::class.java.simpleName

    @JvmField
    @InjectPresenter
    internal var presenter: ShowPagePresenter? = null
    private var photoDialog: PhotoDialog? = null
    private var memoryPageModel: MemoryPageModel? = null
    private var photoSliderAdapter: PhotoSliderAdapter? = null
    private var isList = false
    private var isShow = false
    private var afterSave = false
    private var id = 0
    private var sharing = 0

    var photoList: MutableList<ResponseImagesSlider>? = null
    override fun getContentView(): Int {
        return R.layout.activity_page
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Utils.setTheme(this)
        super.onCreate(savedInstanceState)
        FROM_NOTIFICATION = intent.getBooleanExtra(Constants.INTENT_EXTRA_FROM_NOTIF, false)
        if (Utils.isEmptyPrefsKey(Constants.PREFS_KEY_USER_ID)) {
            addPhotoToSliderBtnLayout!!.visibility = View.GONE
            share_LinLayout!!.visibility = View.GONE
            settings!!.visibility = View.GONE
        }
        tvTitle!!.setText(R.string.memory_page_header_text)
        val i = intent
        isList = i.getBooleanExtra(Constants.INTENT_EXTRA_IS_LIST, false)
        afterSave = i.getBooleanExtra(Constants.INTENT_EXTRA_AFTER_SAVE, false)
        isShow = i.getBooleanExtra(Constants.INTENT_EXTRA_SHOW, false)
        if (isShow) {
            memoryPageModel = i.getParcelableExtra(Constants.INTENT_EXTRA_PERSON) as MemoryPageModel
            presenter!!.getImagesSlider(memoryPageModel!!.id)
            id = memoryPageModel!!.id!!
            settings!!.isClickable = false
            settings!!.visibility = View.GONE
            addPhotoToSliderBtnLayout!!.visibility = View.GONE
            initAll()
        } else {
            id = i.getIntExtra(Constants.INTENT_EXTRA_ID, 0)
            presenter!!.getImagesSlider(id)
            presenter!!.getImageAfterSave(id)
        }
        photoSliderAdapter = PhotoSliderAdapter()
        photoSliderAdapter!!.setClickListener(this)
        recyclerSlider!!.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerSlider!!.adapter = photoSliderAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = CropImage.getActivityResult(data)
        if (resultCode == Activity.RESULT_OK) {
            if (result != null) {
                photoDialog!!.setUri(result.uri)
            } else Log.e(TAG, "RESULT IS NULL!!!")
            Log.i(TAG, "RESULT_OK")
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            assert(result != null)
            Log.e(TAG, result!!.error.message)
            Log.i(TAG, "CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE")
        } else {
            Log.i(TAG, "HZ")
        }
    }

    override fun onBackPressed() {
        if (FROM_NOTIFICATION) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(Constants.INTENT_EXTRA_FROM_NOTIF, true)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
        super.onBackPressed()
    }

    override fun setCoordinates(latitude: Double, longitude: Double) {}
    override fun onReceivedImage(memoryPageModel: MemoryPageModel) {
        this.memoryPageModel = memoryPageModel
        initAll()
        ImageUtils.glideLoadIntoWithError(memoryPageModel.picture, image)
        ImageUtils.glideLoadIntoWithError(memoryPageModel.picture, sharedImage)
    }

    override fun error(throwable: Throwable) {
        if (image == null) Utils.showSnack(image, "Ошибка загрузки изображения")
        Log.d("ADDPHOTO", throwable.message + "")
        share_LinLayout!!.visibility = View.GONE
    }

    override fun onSavedImage(o: Any) {
        photoDialog!!.dismiss()
        Utils.showSnack(image, "Успешно")
        Log.d("ADDPHOTO", o.toString())
        presenter!!.getImagesSlider(memoryPageModel!!.id)
    }

    override fun onImagesSlider(responseImagesSliders: MutableList<ResponseImagesSlider>) {
        photoList = responseImagesSliders
        photoSliderAdapter!!.items = responseImagesSliders
    }

    override fun onDeleteSliderPhoto(o: Any) {}
    override fun onDeleteSliderPhotoError(throwable: Throwable) {}
    override fun showPhoto() {
        ImageUtils.cropImage(this)
    }

    override fun sendPhoto(imageFile: File, string: String) {
        if (memoryPageModel!!.id != null) {
            presenter!!.savePhoto(imageFile, string, memoryPageModel!!.id)
        } else {
            presenter!!.savePhoto(imageFile, string, id)
        }
    }

    override fun onItemClick(view: View, position: Int) {
        startActivity(
                Intent(this@ShowPageActivity, SlidePhotoActivity::class.java)
                        .putExtra(Constants.INTENT_EXTRA_ID, id)
                        .putExtra(Constants.INTENT_EXTRA_POSITION_IN_SLIDER, position)
        )
        SlidePhotoActivity.setCallback(this)
    }

    override fun setViewsInDarkTheme() {
        back_button!!.setImageResource(R.drawable.ic_back_dark_theme)
        settings!!.setImageResource(R.drawable.setting_white)
        panel!!.background = resources.getDrawable(R.drawable.panel_dark)
    }

    private fun pickImage() {
        if (FileUtils.storagePermissionGranted(this) || Build.VERSION.SDK_INT < 23) {
            showPhotoDialog()
        } else {
            FileUtils.verifyStoragePermissions(this)
        }
    }

    private fun openChat(type: String?) {
        val intent = Intent(this, ChatActivity::class.java)
        if (memoryPageModel != null) {
            intent.putExtra("model", memoryPageModel)
        }
        intent.putExtra("type", type)
        startActivity(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            showPhotoDialog()
        } else Toast.makeText(baseContext, "Для загрузки фотографии разрешите доступ к хранилищу.", Toast.LENGTH_LONG).show()
    }

    private fun showPhotoDialog() {
        photoDialog = PhotoDialog()
        photoDialog!!.setCallback(this)
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        photoDialog!!.show(transaction, "photoDialog")
    }

    private fun onBiographyClick() {
        val intent = Intent(this, BiographyActivity::class.java)
        if (memoryPageModel != null && memoryPageModel!!.biography != null) intent.putExtra("biography", memoryPageModel!!.biography)
        startActivity(intent)
    }

    private fun onEpitaphButtonClick() {
        val intent = Intent(this, EpitaphsActivity::class.java)
        intent.putExtra(Constants.INTENT_EXTRA_SHOW, isShow)
        if (memoryPageModel != null && memoryPageModel!!.id != null) intent.putExtra(Constants.INTENT_EXTRA_PAGE_ID, memoryPageModel!!.id)
        startActivity(intent)
    }

    private fun onEventButtonClick() {
        val intent = Intent(this, EventsActivity::class.java)
        intent.putExtra(Constants.INTENT_EXTRA_SHOW, isShow)
        intent.putExtra(Constants.INTENT_EXTRA_NAME, fio!!.text.toString())
        intent.putExtra(Constants.INTENT_EXTRA_PAGE_ID, memoryPageModel!!.id)
        intent.putExtra(Constants.BIRTH_DATE, memoryPageModel!!.datarod)
        intent.putExtra(Constants.INTENT_EXTRA_OWNER_ID, memoryPageModel!!.userId)
        startActivity(intent)
    }

    private fun back() {
        onBackPressed()
    }

    private fun showMap() {
        if (memoryPageModel!!.coords == null || memoryPageModel!!.coords?.isEmpty()!!) {
            Utils.showSnack(image, "Координаты неизвестны")
        } else {
            val popupView = layoutInflater.inflate(R.layout.popup_google_map, null)
            val popupWindow = PopupMap(
                    popupView,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
            popupWindow.setCallback(this)
            popupWindow.setUp(image, supportFragmentManager, memoryPageModel!!.coords)
        }
    }

    private fun editPage() {
        val intent = Intent(this, NewMemoryPageActivity::class.java)
        intent.putExtra("PERSON", memoryPageModel)
        intent.putExtra("LIST", isList)
        intent.putExtra("EDIT", true)
        intent.putExtra(Constants.BURIAL_PLACE_COORDS, memoryPageModel!!.coords)
        intent.putExtra(Constants.BURIAL_PLACE_CITY, memoryPageModel!!.gorod)
        intent.putExtra(Constants.BURIAL_PLACE_CEMETERY, memoryPageModel!!.nazvaklad)
        intent.putExtra(Constants.BURIAL_PLACE_SECTOR, memoryPageModel!!.sector)
        intent.putExtra(Constants.BURIAL_PLACE_LINE, memoryPageModel!!.uchastok)
        intent.putExtra(Constants.BURIAL_PLACE_GRAVE, memoryPageModel!!.nummogil)
        startActivity(intent)
    }

    private fun shareVk() {
        sharing = 1
        val token = VKAccessToken.currentToken()
        if (token == null) {
            VKSdk.login(this, VKScope.FRIENDS, VKScope.WALL, VKScope.PHOTOS)
            Utils.showSnack(image, "Необходимо авторизоваться через ВКонтакте")
            sharePageToVk()
        } else {
            sharePageToVk()
        }
    }

    private fun shareFb() {
        val generatedByIDLink = "https://pomnyu.ru/public/page/" + memoryPageModel!!.id.toString() // Генерация ссылки, для поста (через константу неправильно форматируется ссылка)
        val content = ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(generatedByIDLink))
                .build()
        ShareDialog.show(this, content)
    }

    private fun initClickListener() {
        addPhotoToSliderBtn.setOnClickListener { pickImage() }
        maintainerName!!.setOnClickListener { v: View? -> openChat("profile") }
        tvWriteToMaintainer!!.setOnClickListener { v: View? -> openChat("chat") }
        chatButton!!.setOnClickListener { v: View? -> openChat("allchat") }
        shareFb!!.setOnClickListener { shareFb() }
        shareVk!!.setOnClickListener { shareVk() }
        settings!!.setOnClickListener { editPage() }
        imgBiography!!.setOnClickListener { onBiographyClick() }
        epitButton!!.setOnClickListener { onEpitaphButtonClick() }
        eventsButton!!.setOnClickListener { onEventButtonClick() }
        back_button!!.setOnClickListener { back() }
        mapButton!!.setOnClickListener { showMap() }
    }

    private fun sharePageToVk() {
        if (sharing == 1) {
            val builder = VKShareDialogBuilder()
            builder.setText("ᅠ") //ᅠ
            //builder.setAttachmentImages(new VKUploadImage[]{new VKUploadImage(createBitmapFromView(sharedImage), VKImageParameters.pngImage())});
            val generatedByIDLink = "https://pomnyu.ru/public/page/" + memoryPageModel!!.id.toString() // Генерация ссылки, для поста (через константу неправильно форматируется ссылка)
            builder.setAttachmentLink(getNameTitle(memoryPageModel), generatedByIDLink)
            builder.setShareDialogListener(object : VKShareDialog.VKShareDialogListener {
                override fun onVkShareComplete(postId: Int) {
                    Utils.showSnack(image, "Запись опубликована")
                }

                override fun onVkShareCancel() {
                    Log.i(TAG, "shareVk error2")
                }

                override fun onVkShareError(error: VKError) {
                    Utils.showSnack(image, "Ошибка публикации")
                }
            })
            builder.show(this@ShowPageActivity.supportFragmentManager, "VK_SHARE_DIALOG")
        }
    }

    private fun getNameTitle(memoryPageModel: MemoryPageModel?): String {
        var result = ""
        var textDate = ""
        memoryPageModel?.let {
            result = if (it.thirtname != null && it.thirtname != "null")
                ("Памятная страница. " + StringUtils.capitalize(it.secondname)
                        + " " + StringUtils.capitalize(it.name)
                        + " " + StringUtils.capitalize(it.thirtname))
            else
                (StringUtils.capitalize(it.secondname)
                        + " " + StringUtils.capitalize(it.name))
            textDate = (DateUtils.convertRemoteToLocalFormat(it.datarod)
                    + " - " + DateUtils.convertRemoteToLocalFormat(it.datasmert))
        }

        return "$result. $textDate"
    }

    private fun initAll() {
        initClickListener()
        memoryPageModel?.let {
            with(it) {
                if (!afterSave) {
                    ImageUtils.glideLoadIntoWithError(it.picture, image)
                    ImageUtils.glideLoadIntoWithError(it.picture, sharedImage)
                }
                initTextName(it)
                initDate(it)
                initInfo(it)
                mapButton!!.visibility = if (coords != null && coords.isEmpty()) View.GONE else View.VISIBLE
                imgBiography!!.visibility = if (biography == null) View.GONE else View.VISIBLE
                tvBiography!!.visibility = if (biography == null) View.GONE else View.VISIBLE
                if (status == null || flag == null) share_LinLayout!!.visibility = View.GONE else {
                    if (!(flag == "true" && status == "Одобрено")) {
                        share_LinLayout!!.visibility = View.GONE
                    }
                }

                if (Prefs.getString(Constants.PREFS_KEY_TOKEN, "") == "") {
                    maintainerLayout.visibility = View.GONE
                    chatButton.visibility = View.GONE
                    tvChat.visibility = View.GONE
                } else if (userId == Prefs.getString(Constants.PREFS_KEY_USER_ID, "")) {
                    maintainerLayout.visibility = View.GONE
                } else {
                    maintainerName.text = creatorData?.settingsName
                    tvCountDays.text = "(Создана ${getCreatedDaysAgo(createdAt)} дней назад)"
                }
            }
        }
    }

    private fun initInfo(memoryPageModel: MemoryPageModel) {
        city!!.text = com.remember.app.utils.StringUtils.getStringFromField(memoryPageModel.gorod)
        crypt!!.text = com.remember.app.utils.StringUtils.getStringFromField(memoryPageModel.nazvaklad)
        sector!!.text = com.remember.app.utils.StringUtils.getStringFromField(memoryPageModel.sector)
        line!!.text = com.remember.app.utils.StringUtils.getStringFromField(memoryPageModel.uchastok)
        grave!!.text = com.remember.app.utils.StringUtils.getStringFromField(memoryPageModel.nummogil)
    }

    private fun initDate(memoryPageModel: MemoryPageModel) {
        val textDate = (DateUtils.convertRemoteToLocalFormat(memoryPageModel.datarod)
                + " - " + DateUtils.convertRemoteToLocalFormat(memoryPageModel.datasmert))
        dates!!.text = textDate
    }

    private fun initTextName(memoryPageModel: MemoryPageModel) {
        var result = ""
        result = if (memoryPageModel.thirtname != null && memoryPageModel.thirtname != "null")
            (StringUtils.capitalize(memoryPageModel.secondname)
                    + " " + StringUtils.capitalize(memoryPageModel.name)
                    + " " + StringUtils.capitalize(memoryPageModel.thirtname))
        else
            (StringUtils.capitalize(memoryPageModel.secondname)
                    + " " + StringUtils.capitalize(memoryPageModel.name))
        fio!!.text = result
        if (memoryPageModel.comment != null && memoryPageModel.comment != "") {
            description!!.text = com.remember.app.utils.StringUtils.stripHtml(memoryPageModel.comment)
        } else descriptionTitle!!.visibility = View.GONE
    }

    override fun deletePhoto(position: Int) {
        photoList?.remove(photoSliderAdapter!!.items[position])
        photoSliderAdapter!!.items = photoList
    }
}
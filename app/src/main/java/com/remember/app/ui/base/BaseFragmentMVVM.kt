package com.remember.app.ui.base

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseFragmentMVVM : Fragment() {

    protected abstract val layoutId: Int

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(layoutId, container, false)


    protected fun openChieldFragment(fragment: BaseFragmentMVVM, containerId: Int) {
        val transaction =
            childFragmentManager.beginTransaction()
        transaction.addToBackStack(fragment.tag)
        transaction.replace(containerId, fragment).commit()
    }

//    fun showNextFragment(destination: Int, bundle: Bundle? = Bundle()) {
//        try {
//            findNavController(this).navigate(destination, bundle)
//        } catch (e: IllegalArgumentException) {
//            Log.d("TAG", "Can't open 2 links at once!")
//        } catch (e: IllegalStateException) {
//            Log.d("TAG", "IllegalStateException not associated with a fragment manager")
//        }
//    }

    fun showViewGetImageFromGallery(requestCode: Int) {
        startActivityForResult(
            Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            ), requestCode
        )
    }
}
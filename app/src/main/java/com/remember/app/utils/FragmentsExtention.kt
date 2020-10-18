package com.ambitt.utils


import android.Manifest
import android.content.pm.PackageManager
import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


/**
 * Method to replace the fragment. The [fragment] is added to the container view with id
 * [containerViewId] and a [tag]. The operation is performed by the childFragmentManager.
 * This method checks if fragment is added.
 */
fun androidx.fragment.app.Fragment.replaceFragmentSafely(fragment: androidx.fragment.app.Fragment,
                                                         tag: String,
                                                         allowStateLoss: Boolean = false,
                                                         @IdRes containerViewId: Int,
                                                         @AnimRes enterAnimation: Int = 0,
                                                         @AnimRes exitAnimation: Int = 0,
                                                         @AnimRes popEnterAnimation: Int = 0,
                                                         @AnimRes popExitAnimation: Int = 0) {
    if (isAdded) {
        val ft = activity?.supportFragmentManager?.beginTransaction()
        ft?.setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
        ft?.replace(containerViewId, fragment, tag)
        if (!activity?.supportFragmentManager?.isStateSaved!!) {
            ft?.commit()
        } else if (allowStateLoss) {
            ft?.commitAllowingStateLoss()
        }
    }
}

fun androidx.fragment.app.Fragment.replaceFragmentSafely(fragment: androidx.fragment.app.Fragment,
                                                         tag: String,
                                                         allowStateLoss: Boolean = false,
                                                         addToBackStack: Boolean = false,
                                                         @IdRes containerViewId: Int,
                                                         @StyleRes style: Int = 0,
                                                         @AnimRes enterAnimation: Int = 0,
                                                         @AnimRes exitAnimation: Int = 0,
                                                         @AnimRes popEnterAnimation: Int = 0,
                                                         @AnimRes popExitAnimation: Int = 0) {
    val ft = activity?.supportFragmentManager?.beginTransaction()?.setTransitionStyle(style)?.setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)?.replace(containerViewId, fragment, tag)
    if (addToBackStack) ft?.addToBackStack(tag)
    if (!activity?.supportFragmentManager?.isStateSaved!!) {
        ft?.commit()
    } else if (allowStateLoss) {
        ft?.commitAllowingStateLoss()
    }
}

/**
 * Method to add the fragment. The [fragment] is added to the container view with id
 * [containerViewId] and a [tag]. The operation is performed by the childFragmentManager.
 * This method checks if fragment exists and it is added.
 * @return the fragment added.
 */
fun <T : androidx.fragment.app.Fragment> androidx.fragment.app.Fragment.addFragmentSafely(fragment: T,
                                                                                          tag: String,
                                                                                          allowStateLoss: Boolean = false,
                                                                                          addToBackStack: Boolean = false,
                                                                                          @IdRes containerViewId: Int,
                                                                                          @AnimRes enterAnimation: Int = 0,
                                                                                          @AnimRes exitAnimation: Int = 0,
                                                                                          @AnimRes popEnterAnimation: Int = 0,
                                                                                          @AnimRes popExitAnimation: Int = 0): T {
    if (isAdded && !existsFragmentByTag(tag)) {
        val ft = activity?.supportFragmentManager?.beginTransaction()
        ft?.setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
        ft?.add(containerViewId, fragment, tag)
        if (addToBackStack) {
            ft?.addToBackStack(tag)
        }
        if (!activity?.supportFragmentManager?.isStateSaved!!) {
            ft?.commit()
        } else if (allowStateLoss) {
            ft?.commitAllowingStateLoss()
        }
        return fragment
    }
    return findFragmentByTag(tag) as T
}

/**
 * Method to check if fragment exists. The operation is performed by the childFragmentManager.
 */
fun androidx.fragment.app.Fragment.existsFragmentByTag(tag: String): Boolean {
    return activity?.supportFragmentManager?.findFragmentByTag(tag) != null
}

/**
 * Method to get fragment by tag. The operation is performed by the childFragmentManager.
 */
fun androidx.fragment.app.Fragment.findFragmentByTag(tag: String): androidx.fragment.app.Fragment? {
    return activity?.supportFragmentManager?.findFragmentByTag(tag)
}

fun androidx.fragment.app.Fragment.setTitleWithAnimation(s: String, isFromLeft: Boolean, isToRight: Boolean) {
//    activity.txtSwitcher!!.inAnimation = AnimationUtils.makeInAnimation(context, isFromLeft)
//    activity.txtSwitcher.run {
//        this!!.outAnimation = AnimationUtils.makeOutAnimation(context, isToRight)
//        setText(s.toUpperCase())
//    }
}

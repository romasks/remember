package com.ambitt.utils
import android.app.Activity
import android.content.Context
import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

/**
 * Method to replace the fragment. The [fragment] is added to the container view with id
 * [containerViewId] and a [tag]. The operation is performed by the supportFragmentManager.
 */
fun AppCompatActivity.replaceFragmentSafely(fragment: androidx.fragment.app.Fragment,
                                            tag: String,
                                            allowStateLoss: Boolean = false,
                                            addToBackStack: Boolean = false,
                                            @IdRes containerViewId: Int,
                                            @StyleRes style: Int = 0,
                                            @AnimRes enterAnimation: Int = 0,
                                            @AnimRes exitAnimation: Int = 0,
                                            @AnimRes popEnterAnimation: Int = 0,
                                            @AnimRes popExitAnimation: Int = 0) {
    val ft = supportFragmentManager
            .beginTransaction()
            .setTransitionStyle(style)
            .setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
            .replace(containerViewId, fragment, tag)
    if (addToBackStack) ft.addToBackStack(tag)
    if (!supportFragmentManager.isStateSaved) {
        ft.commit()
    } else if (allowStateLoss) {
        ft.commitAllowingStateLoss()
    }
}

/**
 * Method to add the fragment. The [fragment] is added to the container view with id
 * [containerViewId] and a [tag]. The operation is performed by the supportFragmentManager.
 * This method checks if fragment exists.
 * @return the fragment added.
 */
fun <T : androidx.fragment.app.Fragment> AppCompatActivity.addFragmentSafelfy(fragment: T,
                                                                              tag: String,
                                                                              allowStateLoss: Boolean = false,
                                                                              addToBackStack: Boolean = false,
                                                                              @IdRes containerViewId: Int,
                                                                              @StyleRes style: Int = 0,
                                                                              @AnimRes enterAnimation: Int = 0,
                                                                              @AnimRes exitAnimation: Int = 0,
                                                                              @AnimRes popEnterAnimation: Int = 0,
                                                                              @AnimRes popExitAnimation: Int = 0): T {
    if (!existsFragmentByTag(tag)) {
        val ft = supportFragmentManager
                .beginTransaction()
                .setTransitionStyle(style)
        ft.setCustomAnimations(enterAnimation, exitAnimation, popEnterAnimation, popExitAnimation)
        ft.add(containerViewId, fragment, tag)
        if (addToBackStack) ft.addToBackStack(tag)
        if (!supportFragmentManager.isStateSaved) {
            ft.commit()
        } else if (allowStateLoss) {
            ft.commitAllowingStateLoss()
        }
        return fragment
    }
    return findFragmentByTag(tag) as T
}

fun Context.toast(resourceId: Int) = toast(getString(resourceId))
fun Context.toast(message: CharSequence) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

/**
 * Method to check if fragment exists. The operation is performed by the supportFragmentManager.
 */
fun AppCompatActivity.existsFragmentByTag(tag: String): Boolean =
        supportFragmentManager.findFragmentByTag(tag) != null

/**
 * Method to get fragment by tag. The operation is performed by the supportFragmentManager.
 */
fun AppCompatActivity.findFragmentByTag(tag: String): androidx.fragment.app.Fragment? =
        supportFragmentManager.findFragmentByTag(tag)

fun Activity.setTitleWithAnimation(s: String, b: Boolean, b1: Boolean) {
//    txtSwitcher!!.inAnimation = AnimationUtils.makeInAnimation(this, b)
//    txtSwitcher.run {
//        this!!.outAnimation = AnimationUtils.makeOutAnimation(context, b1)
//        setText(s)
//    }
}


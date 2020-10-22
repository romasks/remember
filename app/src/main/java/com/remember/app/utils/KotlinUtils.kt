package com.remember.app.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.remember.app.R
import java.lang.Integer.parseInt
import java.lang.Long.parseLong
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs


object KotlinUtils {

    private fun makeLinks(
            textView: TextView,
            links: Array<String>,
            clickableSpans: Array<ClickableSpan>,
            context: Context
    ) {
        val spannableString = SpannableString(textView.text)
        links.forEach { i ->
            val clickableSpan = clickableSpans[links.indexOf(i)]
            val startIndexOfLink = textView.text.toString().indexOf(i)
            spannableString.setSpan(
                    clickableSpan, startIndexOfLink, startIndexOfLink + i.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                    StyleSpan(Typeface.BOLD),
                    startIndexOfLink, startIndexOfLink + i.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                    ForegroundColorSpan(context.resources.getColor(R.color.colorWhite)),
                    startIndexOfLink,
                    startIndexOfLink + i.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        with(textView) {
            movementMethod = LinkMovementMethod.getInstance()
            setText(spannableString, TextView.BufferType.SPANNABLE)
        }
    }

    fun setSpannableText(textView: TextView, array: ClickableSpan, context: Context) {
        makeLinks(textView, arrayOf(textView.text.toString()), arrayOf(array), context)
        textView.highlightColor = Color.TRANSPARENT
    }


    fun parsePhone(phone: String): String {

        var maskedPhone = ""
        var digits = phone.split("")
        for (i in digits.indices) {
            when (i) {
                1 -> {
                    maskedPhone += digits[i]
                }
                2 -> {
                    maskedPhone += digits[i]
                }
                3 -> {
                    maskedPhone += " (" + digits[i]
                }
                4 -> {
                    maskedPhone += digits[i]
                }
                5 -> {
                    maskedPhone += digits[i] + ") "
                }
                6 -> {
                    maskedPhone += digits[i]
                }
                7 -> {
                    maskedPhone += digits[i]
                }
                8 -> {
                    maskedPhone += digits[i]
                }
                9 -> {
                    maskedPhone += " " + digits[i]
                }
                10 -> {
                    maskedPhone += digits[i]
                }
                11 -> {
                    maskedPhone += " " + digits[i]
                }
                12 -> {
                    maskedPhone += digits[i]
                }

            }
        }
        return maskedPhone
    }

    fun parseTimeStampToDate(time: Long): String {
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        sdf.timeZone = cal.timeZone
        return sdf.format(Date(time))
    }

    fun getLocaleTime(): String {
        val cal = Calendar.getInstance()
        val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        input.timeZone = cal.timeZone
        return input.format(Date(cal.timeInMillis))
    }

    fun getCreatedDaysAgo(time: String): String {
        val cal = Calendar.getInstance()
        val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val output = SimpleDateFormat("dd")
        var createDate: Long = 0
        var now: Long = 0
        try {
            createDate = input.parse(time).time
            now = cal.timeInMillis
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return TimeUnit.MILLISECONDS.toDays(now.minus(createDate)).toString()
    }

    fun parseTimeStampToDateAndTime(time: Long): String {
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm")
        sdf.timeZone = cal.timeZone
        return sdf.format(Date(time))
    }

    fun parseTimeStampToTime(time: String): String {
        val input = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val output = SimpleDateFormat("HH:mm")
        val tz = TimeZone.getDefault()
        output.timeZone = tz
        var d: Date? = null
        try {
            d = input.parse(time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.HOUR, parseInt(getCurrentTimezoneOffset()?.replace("+", "")?.replace("0", "").toString()))
        var formatted = output.format(d)
        formatted = if (getCurrentTimezoneOffset()?.contains("+")!!) {
            val localeDate = Date(d?.time?.plus(1000 * 60 * 60 * parseLong(getCurrentTimezoneOffset()?.replace("+", "")?.replace("0", "").toString()))!!)
            output.format(localeDate)
        } else {
            val localeDate = Date(d?.time?.minus(1000 * 60 * 60 * parseLong(getCurrentTimezoneOffset()?.replace("+", "")?.replace("0", "").toString()))!!)
            output.format(localeDate)
        }
        return formatted
    }


    fun getCurrentTimezoneOffset(): String? {
        val tz = TimeZone.getDefault()
        val cal = GregorianCalendar.getInstance(tz)
        val offsetInMillis = tz.getOffset(cal.timeInMillis)
        var offset = String.format("%02d%02d", abs(offsetInMillis / 3600000), abs(offsetInMillis / 60000 % 60))
        offset = (if (offsetInMillis >= 0) "+" else "-") + offset
        return offset
    }

    fun <T> MutableLiveData<T>.notifyObserver() {
        this.postValue(this.value)
    }


    fun <T> MutableLiveData<MutableList<T>>.add(item: T) {
        var updatedItems = arrayListOf<T>()
        if (this.value != null)
            updatedItems = this.value as ArrayList
        updatedItems.add(item)
        this.postValue(updatedItems)
    }

    fun <T> MutableLiveData<MutableList<T>>.addToStart(item: T) {
        var updatedItems = arrayListOf<T>()
        if (this.value != null)
            updatedItems = this.value as ArrayList
        updatedItems.add(0, item)
        this.postValue(updatedItems)
    }

    fun <T> MutableLiveData<MutableList<T>>.remove(item: T) {
        val updatedItems = this.value as ArrayList
        updatedItems.remove(item)
        this.postValue(updatedItems)
    }

    fun getPixels(fragment: Fragment): Double {
        val dm = DisplayMetrics()
        fragment.activity?.windowManager?.defaultDisplay?.getMetrics(dm)
        val width = dm.widthPixels
        val height = dm.heightPixels
        val dens = dm.densityDpi
        val wi = width.toDouble() / dens.toDouble()
        val hi = height.toDouble() / dens.toDouble()
        val x = Math.pow(wi, 2.0)
        val y = Math.pow(hi, 2.0)
        return Math.sqrt(x + y)
    }

//    fun getDensity(fragment: BaseFragment, metrics: DisplayMetrics): Int {
//        fragment.activity!!.windowManager.defaultDisplay.getMetrics(metrics)
//        return metrics.densityDpi
//    }

    fun getDensity(activity: AppCompatActivity, metrics: DisplayMetrics): Int {
        activity.windowManager.defaultDisplay.getMetrics(metrics)
        return metrics.densityDpi
    }

    fun getDensity(fragment: Fragment, metrics: DisplayMetrics): Int {
        fragment.activity!!.windowManager.defaultDisplay.getMetrics(metrics)
        return metrics.densityDpi
    }

    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        if (currentFocus == null) View(this) else currentFocus?.let { hideKeyboard(it) }
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun View.setMarginInDP(l: Float, t: Float, r: Float, b: Float) {
        val left = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                l,
                this.resources.displayMetrics
        ).toInt()
        val right = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                r,
                this.resources.displayMetrics
        ).toInt()
        val top = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                t,
                this.resources.displayMetrics
        ).toInt()
        val bottom = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                b,
                this.resources.displayMetrics
        ).toInt()
        val params = layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(left, top, right, bottom)
        layoutParams = params
    }
}


package com.remember.app.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatTextView;

import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;

import static com.remember.app.ui.utils.Utils.pixelsToSp;


public class CustomTextView extends AppCompatTextView {

    public CustomTextView(Context context) {
        super(context);
        float d = this.getTextSize();
        float s = d;
        this.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
                scaleFont());
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        float d = this.getTextSize();
        float s = d;
        float dp = d / getResources().getDisplayMetrics().density + 2;
        float n = dp;

        float a = scaleFont();
        float b = a;
        //this.setTextSize(TypedValue.COMPLEX_UNIT_SP, scaleFont());
        this.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
                scaleFont());
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
            this.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
                    scaleFont());
    }

    private float scaleFont() {
        if (Prefs.getBoolean("standard", true))
           return (this.getTextSize() / getResources().getDisplayMetrics().density - 2);
        else {
            return (this.getTextSize() / getResources().getDisplayMetrics().density + 3);
        }
    }
}
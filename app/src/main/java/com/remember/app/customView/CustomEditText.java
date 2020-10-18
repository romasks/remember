package com.remember.app.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatEditText;

import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;

public class CustomEditText extends AppCompatEditText {

    public CustomEditText(Context context) {
        super(context);
        this.setTextSize(TypedValue.COMPLEX_UNIT_DIP, scaleFont());
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTextSize(TypedValue.COMPLEX_UNIT_DIP, scaleFont());
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, R.attr.TextAreaStyle);
        this.setTextSize(TypedValue.COMPLEX_UNIT_DIP, scaleFont());
    }

    private float scaleFont() {
        if (Prefs.getBoolean("standard", true))
            return (this.getTextSize() / getResources().getDisplayMetrics().density );
        else {
            return (this.getTextSize() / getResources().getDisplayMetrics().density + 3);
        }
    }
}
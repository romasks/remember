package com.remember.app.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import androidx.appcompat.widget.AppCompatTextView;
import com.pixplicity.easyprefs.library.Prefs;

public class CustomTextView extends AppCompatTextView {

    public CustomTextView(Context context) {
        super(context);
        this.setTextSize(TypedValue.COMPLEX_UNIT_DIP, scaleFont());
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTextSize(TypedValue.COMPLEX_UNIT_DIP, scaleFont());
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setTextSize(TypedValue.COMPLEX_UNIT_DIP, scaleFont());
    }

    private float scaleFont() {
        if (Prefs.getBoolean("standard", true))
            return (this.getTextSize() / getResources().getDisplayMetrics().density - 2);
        else {
            return (this.getTextSize() / getResources().getDisplayMetrics().density + 3);
        }
    }
}
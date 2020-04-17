package com.remember.app.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.Switch;

import com.pixplicity.easyprefs.library.Prefs;

public class CustomSwitch extends Switch {
    public CustomSwitch(Context context) {
        super(context);
        this.setTextSize(TypedValue.COMPLEX_UNIT_DIP, scaleFont());
    }

    public CustomSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTextSize(TypedValue.COMPLEX_UNIT_DIP, scaleFont());
    }

    public CustomSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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

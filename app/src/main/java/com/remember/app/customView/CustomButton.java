package com.remember.app.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatButton;

import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;

import static com.remember.app.ui.utils.Utils.pixelsToSp;

public class CustomButton extends AppCompatButton {
    public CustomButton(Context context) {
        super(context);
        this.setTextSize(TypedValue.COMPLEX_UNIT_DIP, scaleFont());
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTextSize(TypedValue.COMPLEX_UNIT_DIP, scaleFont());
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, R.attr.CustomAppButtonStyle);
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

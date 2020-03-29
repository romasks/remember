package com.remember.app.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.AppCompatEditText;

import com.pixplicity.easyprefs.library.Prefs;

import static com.remember.app.ui.utils.Utils.pixelsToSp;

public class CustomAutoCompleteEditText extends AppCompatEditText {


    public CustomAutoCompleteEditText(Context context) {
        super(context);
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, scaleFont());
    }

    public CustomAutoCompleteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, scaleFont());
    }

    public CustomAutoCompleteEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setTextSize(scaleFont());
    }

    private float scaleFont() {
        if (Prefs.getBoolean("standard", true))
            return pixelsToSp(getContext(), this.getTextSize() - 2);
        else {
            return pixelsToSp(getContext(), this.getTextSize() + 3);
        }
    }
}

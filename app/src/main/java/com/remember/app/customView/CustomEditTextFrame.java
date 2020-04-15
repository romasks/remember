package com.remember.app.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.widget.AppCompatEditText;

import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;

import butterknife.BindView;

public class CustomEditTextFrame extends FrameLayout {

    AppCompatEditText inputText;

    public CustomEditTextFrame(Context context) {
        super(context);
        initView(context,null);
        inputText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, scaleFont());
    }

    public CustomEditTextFrame(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
        inputText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, scaleFont());
    }

    public CustomEditTextFrame(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs);
        inputText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, scaleFont());
    }

    private float scaleFont() {
        if (Prefs.getBoolean("standard", true))
            return (inputText.getTextSize() / getResources().getDisplayMetrics().density);
        else {
            return (inputText.getTextSize() / getResources().getDisplayMetrics().density + 3);
        }
    }

    private void initView(Context context, AttributeSet attrs) {
        if (attrs == null) return;
       // inflate(getContext(), R.layout.custom_edit_text, this);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.custom_edit_text, this, true);
        inputText = v.findViewById(R.id.inputText);
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomEditTextFrame);
        this.inputText.setHint(a.getString(R.styleable.CustomEditTextFrame_hint));
        this.inputText.setTextColor(a.getColor(R.styleable.CustomEditTextFrame_textColor, getResources().getColor(R.color.colorBlackDark)));
        this.inputText.setHintTextColor(a.getColor(R.styleable.CustomEditTextFrame_textColorHint, getResources().getColor(R.color.colorBlackDark)));
        this.inputText.setHighlightColor(a.getColor(R.styleable.CustomEditTextFrame_textColorHighlight, getResources().getColor(R.color.abc_dark)));
        this.inputText.setLinkTextColor(a.getColor(R.styleable.CustomEditTextFrame_textColorHighlight, getResources().getColor(R.color.colorPrimary)));
        // Important: always recycle the TypedArray
        a.recycle();
    }

    public AppCompatEditText getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText.setText(inputText);
    }
}
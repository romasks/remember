package com.remember.app.ui.menu.settings;

import android.widget.RadioButton;
import android.widget.TextView;

import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.customView.CustomRadioButton;
import com.remember.app.customView.CustomTextView;

import butterknife.BindView;

import static com.remember.app.ui.utils.Utils.pixelsToSp;

public class FontSettingsFragment extends SettingsBaseFragment {

    @BindView(R.id.rbStandard)
    CustomRadioButton rbStandard;
    @BindView(R.id.rbBig)
    CustomRadioButton rbBig;
    @BindView(R.id.tvStandard)
    CustomTextView tvStandard;
    @BindView(R.id.tvBig)
    CustomTextView tvBig;

    public FontSettingsFragment() {
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_font_settings;
    }

    @Override
    protected void setUp() {
        initUI();
    }

    @Override
    void onSaveClick() {
        saveFontSize();
        getActivity().recreate();
    }

    private void initUI() {
        if (Prefs.getBoolean("standard", true)){
            rbStandard.setChecked(true);
            rbBig.setChecked(false);
        }else {
            rbStandard.setChecked(false);
            rbBig.setChecked(true);
        }
        rbStandard.setOnCheckedChangeListener((buttonView, isChecked) ->{
            rbBig.setChecked(!isChecked);
            tvStandard.setTextSize(18f);
            tvBig.setTextSize(18f);
        });
        rbBig.setOnCheckedChangeListener((buttonView, isChecked) ->{
            rbStandard.setChecked(!isChecked);
            tvStandard.setTextSize(23f);
            tvBig.setTextSize(23f);
        });
    }

    private void saveFontSize() {
        if (rbStandard.isChecked())
            Prefs.putBoolean("standard", true);
        else
            Prefs.putBoolean("standard", false);
    }
}

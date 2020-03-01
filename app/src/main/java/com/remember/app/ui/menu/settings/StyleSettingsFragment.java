package com.remember.app.ui.menu.settings;

import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;

import butterknife.OnCheckedChanged;

import static com.remember.app.data.Constants.PREFS_KEY_THEME;
import static com.remember.app.data.Constants.PREFS_KEY_THEME_CHANGED;
import static com.remember.app.data.Constants.THEME_LIGHT;

public class StyleSettingsFragment extends SettingsBaseFragment {

    @Override
    protected int getContentView() {
        return R.layout.fragment_style_settings;
    }

    @Override
    protected void setUp() {

    }

    @Override
    void onSaveClick() {

    }

    @OnCheckedChanged(R.id.change_theme)
    void onChangeTheme() {
        Prefs.putBoolean(PREFS_KEY_THEME_CHANGED, true);
        Prefs.putBoolean(PREFS_KEY_THEME, !Prefs.getBoolean(PREFS_KEY_THEME, THEME_LIGHT));
        ((SettingActivity) getActivity()).recreateSettings();
    }
}

package com.remember.app.ui.menu.settings;

import android.os.Handler;
import android.widget.Switch;

import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;

import butterknife.BindView;
import butterknife.OnClick;

import static com.remember.app.data.Constants.PREFS_KEY_THEME;
import static com.remember.app.data.Constants.THEME_LIGHT;

public class StyleSettingsFragment extends SettingsBaseFragment {

    @BindView(R.id.change_theme)
    Switch changeThemeSwitcher;

    public StyleSettingsFragment() {
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_style_settings;
    }

    @Override
    protected void setUp() {
        changeThemeSwitcher.setChecked(Prefs.getBoolean(PREFS_KEY_THEME, false));
    }

    @Override
    void onSaveClick() {

    }

    @OnClick(R.id.change_theme)
    void onChangeTheme() {
        Prefs.putBoolean(PREFS_KEY_THEME, !Prefs.getBoolean(PREFS_KEY_THEME, THEME_LIGHT));
        new Handler().postDelayed(this::recreateSettings, 100);
    }

    private void recreateSettings() {
        if (getActivity() != null) {
            ((SettingActivity) getActivity()).recreateSettings();
        }
    }
}

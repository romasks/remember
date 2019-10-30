package com.remember.app.ui.cabinet.events;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.data.models.ResponseEvents;
import com.remember.app.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class EventFullActivity extends BaseActivity {

    @BindView(R.id.avatar)
    ImageView avatarImage;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.body)
    TextView body;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.back)
    ImageView backImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Prefs.getInt("IS_THEME",0)==2) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            setTheme(R.style.AppTheme_Dark);
        }else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);

        if (Prefs.getInt("IS_THEME",0)==2){
            backImg.setImageResource(R.drawable.ic_back_dark_theme);
            title.setTextColor(getResources().getColor(R.color.colorWhiteDark));

        }
        if (getIntent().getExtras() != null) {
            ResponseEvents responseEvents = new Gson().fromJson(String.valueOf(getIntent().getExtras().get("EVENTS")), ResponseEvents.class);
            System.out.println();
            Drawable mDefaultBackground = this.getResources().getDrawable(R.drawable.darth_vader);
            try {
                if (!responseEvents.getPicture().contains("upload")) {
                    Glide.with(this)
                            .load("http://помню.рус/uploads/" + responseEvents.getPicture())
                            .error(mDefaultBackground)
                            .into(avatarImage);
                } else {
                    Glide.with(this)
                            .load(mDefaultBackground)
                            .error(mDefaultBackground)
                            .into(avatarImage);
                }
            } catch (Exception e) {
                Glide.with(this)
                        .load(mDefaultBackground)
                        .error(mDefaultBackground)
                        .into(avatarImage);
            }
            title.setText(responseEvents.getName());
            date.setText(responseEvents.getPutdate());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                body.setText(Html.fromHtml(responseEvents.getBody(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                body.setText(Html.fromHtml(responseEvents.getBody()));
            }
        }
        setBlackWhite(avatarImage);
    }
    public void setBlackWhite(ImageView imageView) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        imageView.setColorFilter(filter);
    }

    @OnClick(R.id.back)
    public void back() {
        onBackPressed();
        finish();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_full_event;
    }
}

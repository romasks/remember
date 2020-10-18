package com.remember.app.ui.cabinet.biography;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

import com.remember.app.R;
import com.remember.app.customView.CustomTextView;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.utils.StringUtils;
import com.remember.app.utils.Utils;

import butterknife.BindView;

public class BiographyActivity extends BaseActivity {

    @BindView(R.id.tvBiography)
    CustomTextView tvBiography;
    @BindView(R.id.settings)
    ImageView settings;
    @BindView(R.id.back_button)
    AppCompatImageView back;
    @BindView(R.id.title)
    CustomTextView title;

    @Override
    protected int getContentView() {
        return R.layout.activity_biography;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setTheme(this);
        super.onCreate(savedInstanceState);
        String biography = getIntent().getStringExtra("biography");
        if (biography != null)
            tvBiography.setText(StringUtils.stripHtml(biography));
        title.setText("Биография");
        settings.setVisibility(View.GONE);
        back.setOnClickListener(v -> onBackPressed());
    }
}

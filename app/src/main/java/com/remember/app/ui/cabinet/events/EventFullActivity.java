package com.remember.app.ui.cabinet.events;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() != null) {
            ResponseEvents responseEvents = new Gson().fromJson(String.valueOf(getIntent().getExtras().get("EVENT")), ResponseEvents.class);
            System.out.println();
            Drawable mDefaultBackground = this.getResources().getDrawable(R.drawable.darth_vader);
            try {
                if (!responseEvents.getPicture().contains("upload")) {
                    Glide.with(this)
                            .load("http://86.57.172.88:8082/uploads/" + responseEvents.getPicture())
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

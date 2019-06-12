package com.remember.app.ui.question;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.remember.app.R;
import com.remember.app.ui.base.BaseActivity;

import butterknife.BindView;

public class QuestionActivity extends BaseActivity {

    @BindView(R.id.spinner)
    MaterialSpinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        spinner.setItems("Вопросы", "Предложения");
        spinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item)
                -> Snackbar.make(view, "Clicked " + item, Snackbar.LENGTH_LONG).show());
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_question;
    }
}

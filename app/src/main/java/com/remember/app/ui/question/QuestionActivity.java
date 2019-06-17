package com.remember.app.ui.question;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.snackbar.Snackbar;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.data.models.RequestQuestion;
import com.remember.app.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class QuestionActivity extends BaseActivity implements QuestionView {

    @InjectPresenter
    QuestionPresenter presenter;

    @BindView(R.id.spinner)
    MaterialSpinner spinner;
    @BindView(R.id.body)
    EditText body;

    private boolean isQuestion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        spinner.setItems("Вопросы", "Предложения");
        spinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item)
                -> isQuestion = item.equals("Вопросы"));
    }

    @OnClick(R.id.pick)
    public void send() {
        RequestQuestion requestQuestion = new RequestQuestion();
        if (isQuestion) {
            requestQuestion.setType("question");
        } else {
            requestQuestion.setType("predlogenie");
        }
        requestQuestion.setBody(body.getText().toString());
        requestQuestion.setName(Prefs.getString("EMAIL", ""));
        requestQuestion.setUserId(Integer.parseInt(Prefs.getString("USER_ID", "")));
        presenter.send(requestQuestion);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_question;
    }

    @Override
    public void onReceived(Object o) {
        body.setText("");
        Toast.makeText(this, "Ваша заявка успешно отправлена", Toast.LENGTH_LONG).show();
    }

    @Override
    public void error(Throwable throwable) {
        Toast.makeText(this, "Ошибка отправки", Toast.LENGTH_LONG).show();
    }
}

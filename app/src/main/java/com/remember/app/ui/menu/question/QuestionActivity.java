package com.remember.app.ui.menu.question;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.customView.CustomEditTextFrame;
import com.remember.app.customView.CustomTextView;
import com.remember.app.data.models.RequestQuestion;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.utils.QuestionSendDialog;
import com.remember.app.utils.Utils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.remember.app.data.Constants.PREFS_KEY_EMAIL;
import static com.remember.app.data.Constants.PREFS_KEY_USER_ID;

public class QuestionActivity extends BaseActivity implements QuestionView {

    @InjectPresenter
    QuestionPresenter presenter;

    @BindView(R.id.spinner)
    MaterialSpinner spinner;
    @BindView(R.id.body)
    CustomEditTextFrame body;
    @BindView(R.id.back_button)
    ImageView back;
    @BindView(R.id.title)
    CustomTextView title;
    @BindView(R.id.settings)
    ImageView settings;

    private boolean isQuestion = false;
    private final String QUESTION = "Вопрос";
    private final String PROPOSAL = "Предложение";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.setTheme(this);

        super.onCreate(savedInstanceState);
        title.setText(R.string.questions_header_text);
        settings.setVisibility(View.GONE);

        if (Utils.isThemeDark()) {
            back.setImageResource(R.drawable.ic_back_dark_theme);
            body.setBackground(getResources().getDrawable(R.drawable.edit_text_with_border_dark));
            spinner.setBackgroundColor(getResources().getColor(R.color.colorBlackDark));

        }

        spinner.setItems(QUESTION, PROPOSAL);
        spinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener<String>) (view, position, id, item)
                -> isQuestion = item.equals(QUESTION));
    }

    @OnClick(R.id.back_button)
    public void back() {
        onBackPressed();
        finish();
    }

    @OnClick(R.id.pick)
    public void send() {
        RequestQuestion requestQuestion = new RequestQuestion();
        requestQuestion.setType("");
        requestQuestion.setBody(body.getInputText().getText().toString());
        requestQuestion.setName(Prefs.getString(PREFS_KEY_EMAIL, ""));
        requestQuestion.setUserId(Integer.parseInt(Prefs.getString(PREFS_KEY_USER_ID, "")));
        presenter.send(requestQuestion);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_question;
    }

    @Override
    public void onReceived(Object o) {
        body.setInputText("");
//        Toast.makeText(this, "Ваша заявка успешно отправлена", Toast.LENGTH_LONG).show();
        successDialog();
    }

    @Override
    public void error(Throwable throwable) {
        Toast.makeText(this, "Ошибка отправки", Toast.LENGTH_LONG).show();
    }

    private void successDialog() {
        QuestionSendDialog dialog = new QuestionSendDialog();
        dialog.show(getSupportFragmentManager().beginTransaction(), "successDialog");
    }
}

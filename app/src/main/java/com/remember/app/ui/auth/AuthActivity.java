package com.remember.app.ui.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.data.models.ResponseAuth;
import com.remember.app.data.models.ResponseRestorePassword;
import com.remember.app.data.models.ResponseSocialAuth;
import com.remember.app.data.models.ResponseVk;
import com.remember.app.ui.cabinet.main.MainActivity;
import com.remember.app.ui.utils.LoadingPopupUtils;
import com.remember.app.ui.utils.MvpAppCompatActivity;
import com.remember.app.ui.utils.RepairPasswordDialog;
import com.remember.app.ui.utils.Utils;
import com.remember.app.ui.utils.WrongEmailDialog;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;

import java.util.Collections;
import java.util.List;

import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ru.mail.auth.sdk.MailRuAuthSdk;
import ru.ok.android.sdk.Odnoklassniki;
import ru.ok.android.sdk.util.OkAuthType;
import ru.ok.android.sdk.util.OkScope;

import static com.jaychang.sa.facebook.SimpleAuth.connectFacebook;
import static com.remember.app.data.Constants.PREFS_KEY_AVATAR;
import static com.remember.app.data.Constants.PREFS_KEY_EMAIL;
import static com.remember.app.data.Constants.PREFS_KEY_NAME_USER;
import static com.remember.app.data.Constants.PREFS_KEY_TOKEN;
import static com.remember.app.data.Constants.PREFS_KEY_USER_ID;

public class AuthActivity extends MvpAppCompatActivity implements AuthView, RepairPasswordDialog.Callback {

    private static final String APP_ID = "512000155578";
    private static final String APP_KEY = "CLLQFHJGDIHBABABA";
    private static final String REDIRECT_URL = "okauth://ok512000155578";

    private static final int MAIL_RU_REQUEST_CODE = 1015;

    @InjectPresenter
    AuthPresenter presenter;

    @BindView(R.id.login_value)
    AutoCompleteTextView login;
    @BindView(R.id.password_value)
    AutoCompleteTextView password;

    private Unbinder unbinder;
    private Odnoklassniki odnoklassniki;
    private MailRuAuthSdk mailru;
    private ProgressDialog popupDialog;
    private AuthHelper authHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        unbinder = ButterKnife.bind(this);

        TextView register = findViewById(R.id.register);
        TextView wrongPas = findViewById(R.id.wrong_password);
        register.setPaintFlags(register.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        wrongPas.setPaintFlags(wrongPas.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        mailru = MailRuAuthSdk.getInstance();
        mailru.setRequestCodeOffest(MAIL_RU_REQUEST_CODE);

        odnoklassniki = Odnoklassniki.createInstance(this, APP_ID, APP_KEY);

        authHelper = new AuthHelper(presenter, mailru, odnoklassniki);
    }

    @OnClick(R.id.vk)
    public void signInVk() {
        VKSdk.login(this, VKScope.EMAIL, VKScope.FRIENDS, VKScope.WALL, VKScope.PHOTOS);
    }

    @OnClick(R.id.mailru)
    public void signInMailRu() {
        mailru.startLogin(this);
    }

    @OnClick(R.id.ok)
    public void signInOk() {
        odnoklassniki.requestAuthorization(this, REDIRECT_URL, OkAuthType.ANY, OkScope.VALUABLE_ACCESS, OkScope.LONG_ACCESS_TOKEN);
    }

    @OnClick(R.id.fb)
    public void signInFacebook() {
        List<String> scopes = Collections.singletonList("");
        connectFacebook(scopes, authHelper.fbAuthCallback());
    }

    @OnClick(R.id.sign_in_btn)
    public void signIn() {
        if (login.getText().toString().equals("")) {
            Utils.showSnack(login, "Введите e-mail");
        } else if (password.getText().toString().equals("")) {
            Utils.showSnack(login, "Введите пароль");
        } else {
            try {
                presenter.singInAuth(login.getText().toString(), password.getText().toString());
            } catch (Exception e) {
                Utils.showSnack(login, "Произошла ошибка, проверьте введенные данные");
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (authHelper.isVkActivityResult(requestCode, resultCode, data)) return;
        if (authHelper.isOkActivityResult(requestCode, resultCode, data)) return;
        if (authHelper.isMailRuActivityResult(requestCode, resultCode, data)) return;
        super.onActivityResult(requestCode, resultCode, data);
    }


    @OnClick(R.id.register)
    public void moveRegisterPage() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    @OnClick(R.id.wrong_password)
    public void repairPassword() {
        try {
            RepairPasswordDialog repairPasswordDialog = new RepairPasswordDialog();
            repairPasswordDialog.setCallback(this);
            repairPasswordDialog.show(getSupportFragmentManager().beginTransaction(), "repairPasswordDialog");
        } catch (Exception e) {
        }

    }

    @OnClick(R.id.back)
    public void back() {
        onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onLogged(ResponseAuth responseAuth) {
        if (responseAuth != null) {
            Prefs.putString(PREFS_KEY_USER_ID, responseAuth.getId().toString());
            Prefs.putString(PREFS_KEY_TOKEN, responseAuth.getToken());
            Prefs.putString(PREFS_KEY_NAME_USER, responseAuth.getName());
            Prefs.putString(PREFS_KEY_EMAIL, responseAuth.getEmail());
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onError(Throwable throwable) {
        Utils.showSnack(login, "Неправильный логин или пароль");
        /*try {
            errorDialog("Неправильный логин или пароль");
        } catch (Exception e) {
            errorDialog("Неправильный логин или пароль");
        }*/
    }

    @Override
    public void onReceivedInfo(ResponseVk response) {
        presenter.signInVk();
        Prefs.putString(PREFS_KEY_NAME_USER, response.getFirstName());
    }

    @Override
    public void onLoggedSocial(ResponseSocialAuth responseSocialAuth) {
        if (responseSocialAuth != null) {
            Prefs.putString(PREFS_KEY_USER_ID, responseSocialAuth.getId().toString());
            Prefs.putString(PREFS_KEY_TOKEN, responseSocialAuth.getToken());
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        Prefs.putString(PREFS_KEY_AVATAR, "");
    }

    @Override
    public void onRestored(ResponseRestorePassword responseRestorePassword) {
        popupDialog.dismiss();
        if (responseRestorePassword.getResponse()) {
            errorDialog("Новый пароль успешно отправлен на E-mail");
        } else {
            Utils.showSnack(login, "Ошибка отправки");
        }
    }

    @Override
    public void errorRestored(Throwable throwable) {
        popupDialog.dismiss();
        Utils.showSnack(login, "Ошибка отправки");
    }

    public void errorDialog(String text) {
        WrongEmailDialog wrongEmailDialog = new WrongEmailDialog();
        wrongEmailDialog.setDescription(text);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        wrongEmailDialog.show(transaction, "wrongEmailDialog");
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void sendEmail(String email) {
        popupDialog = LoadingPopupUtils.showLoadingDialog(this);
        presenter.restorePassword(email);
    }
}

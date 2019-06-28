package com.remember.app.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jaychang.sa.AuthCallback;
import com.jaychang.sa.SocialUser;
import com.jaychang.sa.twitter.SimpleAuth;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.data.models.ResponseAuth;
import com.remember.app.data.models.ResponseSettings;
import com.remember.app.data.models.ResponseVk;
import com.remember.app.ui.cabinet.main.MainActivity;
import com.remember.app.ui.utils.DeleteAlertDialog;
import com.remember.app.ui.utils.MvpAppCompatActivity;
import com.remember.app.ui.utils.WrongEmailDialog;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ru.ok.android.sdk.Odnoklassniki;
import ru.ok.android.sdk.util.OkAuthType;
import ru.ok.android.sdk.util.OkScope;

public class AuthActivity extends MvpAppCompatActivity implements AuthView {

    @InjectPresenter
    AuthPresenter presenter;

    @BindView(R.id.login_value)
    AutoCompleteTextView login;
    @BindView(R.id.password_value)
    AutoCompleteTextView password;
    @BindView(R.id.vk)
    ImageButton vk;

    private static final String APP_ID = "CBAGJGDNEBABABABA";
    private static final String APP_KEY = "A488208737DA4B970D6E3EB1";
    private static final String REDIRECT_URL = "okauth://ok1278579968";

    private Unbinder unbinder;
    private TwitterAuthClient client;
    private Odnoklassniki odnoklassniki;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        unbinder = ButterKnife.bind(this);
        client = new TwitterAuthClient();

    }

    @OnClick(R.id.vk)
    public void signInVk() {
        VKSdk.login(this, "email");
    }

    @OnClick(R.id.ok)
    public void signInOk() {
        odnoklassniki = Odnoklassniki.createInstance(this, APP_ID, APP_KEY);
        odnoklassniki.requestAuthorization(this, REDIRECT_URL, OkAuthType.ANY, OkScope.LONG_ACCESS_TOKEN);
    }

    @OnClick(R.id.fb)
    public void signInFacebook() {
        List<String> scopes = Arrays.asList("");

        com.jaychang.sa.facebook.SimpleAuth.connectFacebook(scopes, new AuthCallback() {
            @Override
            public void onSuccess(SocialUser socialUser) {
                Prefs.putString("EMAIL", socialUser.email);
                String[] str = socialUser.fullName.split(" ");
                Prefs.putString("NAME_USER", str[0]);
                Prefs.putString("AVATAR", socialUser.profilePictureUrl);
                presenter.signInFacebook();
            }

            @Override
            public void onError(Throwable error) {
                errorDialog("Ошибка авторизации");
                Log.d("FACEBOOK", error.getMessage());
            }

            @Override
            public void onCancel() {
                Log.d("FACEBOOK", "Canceled");
            }
        });
    }

    @OnClick(R.id.twitter)
    public void signInTwitter() {
        SimpleAuth.connectTwitter(new AuthCallback() {
            @Override
            public void onSuccess(SocialUser socialUser) {
                Prefs.putString("EMAIL", socialUser.email);
                String[] str = socialUser.fullName.split(" ");
                Prefs.putString("NAME_USER", str[0]);
                Prefs.putString("AVATAR", socialUser.profilePictureUrl);
                presenter.signInTwitter();
            }

            @Override
            public void onError(Throwable error) {
                errorDialog("Ошибка авторизации");
                Log.e("TWITTER", error.getMessage());
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @OnClick(R.id.sign_in_btn)
    public void signIn() {
        if (login.getText().toString().equals("")) {
            Toast.makeText(this, "Введите e-mail", Toast.LENGTH_LONG).show();
        } else if (password.getText().toString().equals("")) {
            Toast.makeText(this, "Введите пароль", Toast.LENGTH_LONG).show();
        } else {
            presenter.singInAuth(login.getText().toString(), password.getText().toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                Prefs.putString("EMAIL", res.email);
                VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "photo_200"));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        VKApiUser user = ((VKList<VKApiUser>) response.parsedModel).get(0);
                        Prefs.putString("AVATAR", user.photo_200);
                    }

                    @Override
                    public void onError(VKError error) {
                        errorDialog("Ошибка авторизации");
                    }

                    @Override
                    public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                    }
                });
                presenter.getInfoUser();
            }

            @Override
            public void onError(VKError error) {

            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @OnClick(R.id.register)
    public void moveRegisterPage() {
        startActivity(new Intent(this, RegisterActivity.class));
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
            Prefs.putString("USER_ID", responseAuth.getId().toString());
            Prefs.putString("NAME_USER", responseAuth.getName());
            Prefs.putString("EMAIL", responseAuth.getEmail());
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void error(Throwable throwable) {
        errorDialog("Неправильный логин или пароль");
    }

    @Override
    public void onRecievedInfo(ResponseVk response) {
        presenter.signInVk();
        Prefs.putString("NAME_USER", response.getFirstName());
    }

    @Override
    public void onLogged(List<ResponseSettings> responseSettings) {
        Prefs.putString("USER_ID", responseSettings.get(0).getId().toString());
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void errorDialog(String text){
        WrongEmailDialog wrongEmailDialog = new WrongEmailDialog();
        FragmentManager manager = getSupportFragmentManager();
        wrongEmailDialog.setDescription(text);
        FragmentTransaction transaction = manager.beginTransaction();
        wrongEmailDialog.show(transaction, "wrongEmailDialog");
    }
}

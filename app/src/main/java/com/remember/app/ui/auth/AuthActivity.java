package com.remember.app.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.snackbar.Snackbar;
import com.jaychang.sa.AuthCallback;
import com.jaychang.sa.SocialUser;
import com.jaychang.sa.twitter.SimpleAuth;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.data.models.ResponseAuth;
import com.remember.app.data.models.ResponseSettings;
import com.remember.app.data.models.ResponseVk;
import com.remember.app.ui.cabinet.main.MainActivity;
import com.remember.app.ui.utils.MvpAppCompatActivity;
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

public class AuthActivity extends MvpAppCompatActivity implements AuthView {

    @InjectPresenter
    AuthPresenter presenter;

    @BindView(R.id.login_value)
    AutoCompleteTextView login;
    @BindView(R.id.password_value)
    AutoCompleteTextView password;
    @BindView(R.id.vk)
    ImageButton vk;

    private Unbinder unbinder;
    private TwitterAuthClient client;

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
                Snackbar.make(password, "Ошибка авторизации", Snackbar.LENGTH_LONG).show();
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
            Snackbar.make(password, "Введите e-mail", Snackbar.LENGTH_LONG).show();
        } else if (password.getText().toString().equals("")) {
            Snackbar.make(password, "Введите пароль", Snackbar.LENGTH_LONG).show();
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
        Snackbar.make(password, "Неправильный логин или пароль", Snackbar.LENGTH_LONG).show();
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
}

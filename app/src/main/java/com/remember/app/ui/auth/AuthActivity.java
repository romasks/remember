package com.remember.app.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.snackbar.Snackbar;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.data.models.ResponseAuth;
import com.remember.app.data.models.ResponseSettings;
import com.remember.app.data.models.ResponseVk;
import com.remember.app.ui.cabinet.main.MainActivity;
import com.remember.app.ui.utils.MvpAppCompatActivity;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

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

    private Unbinder unbinder;
    private static final String APP_ID = "125497344";
    private static final String APP_KEY = "CBABPLHIABABABABA";
    private static final String REDIRECT_URL = "okauth://ok125497344";
    private Odnoklassniki odnoklassniki;
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

    @OnClick(R.id.twitter)
    public void signInTwitter() {
        if (getTwitterSession() == null) {

            //if user is not authenticated start authenticating
            client.authorize(this, new Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {

                    TwitterSession twitterSession = result.data;
                    Snackbar.make(login, "success", Snackbar.LENGTH_SHORT).show();
                    fetchTwitterEmail(twitterSession);
                }

                @Override
                public void failure(TwitterException e) {
                    Snackbar.make(login, "Failed to authenticate. Please try again.", Snackbar.LENGTH_SHORT).show();

                }
            });
        } else {
            Snackbar.make(login, "User already authenticated", Snackbar.LENGTH_SHORT).show();
            fetchTwitterEmail(getTwitterSession());
        }
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

    public void fetchTwitterEmail(final TwitterSession twitterSession) {
        client.requestEmail(twitterSession, new Callback<String>() {
            @Override
            public void success(Result<String> result) {
                Snackbar.make(password,  twitterSession.getUserName(), Snackbar.LENGTH_LONG).show();

            }

            @Override
            public void failure(TwitterException exception) {
                Snackbar.make(password, "Failed to authenticate. Please try again.", Snackbar.LENGTH_LONG).show();

            }
        });
    }

    private TwitterSession getTwitterSession() {
        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();

//        TwitterAuthToken authToken = session.getAuthToken();
//        String token = authToken.token;
//        String secret = authToken.secret;

        return session;
    }
}

package com.remember.app.ui.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.jaychang.sa.AuthCallback;
import com.jaychang.sa.SocialUser;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.BuildConfig;
import com.remember.app.R;
import com.remember.app.data.models.ResponseAuth;
import com.remember.app.data.models.ResponseRestorePassword;
import com.remember.app.data.models.ResponseSocialAuth;
import com.remember.app.data.models.ResponseVk;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.cabinet.main.MainActivity;
import com.remember.app.ui.utils.ErrorDialog;
import com.remember.app.ui.utils.LoadingPopupUtils;
import com.remember.app.ui.utils.RepairPasswordDialog;
import com.remember.app.ui.utils.Utils;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import ru.mail.auth.sdk.MailRuAuthSdk;
import ru.mail.auth.sdk.MailRuCallback;
import ru.mail.auth.sdk.api.OAuthRequestErrorCodes;
import ru.mail.auth.sdk.api.token.OAuthTokensResult;

import ru.ok.android.sdk.Odnoklassniki;
import ru.ok.android.sdk.OkListener;
import ru.ok.android.sdk.SharedKt;
import ru.ok.android.sdk.util.OkAuthType;
import ru.ok.android.sdk.util.OkScope;

import static com.remember.app.data.Constants.PREFS_KEY_ACCESS_TOKEN;
import static com.remember.app.data.Constants.PREFS_KEY_AVATAR;
import static com.remember.app.data.Constants.PREFS_KEY_EMAIL;
import static com.remember.app.data.Constants.PREFS_KEY_NAME_USER;
import static com.remember.app.data.Constants.PREFS_KEY_TOKEN;
import static com.remember.app.data.Constants.PREFS_KEY_USER_ID;

public class AuthActivity extends BaseActivity implements AuthView, RepairPasswordDialog.Callback {

    private static final String APP_ID = "512000155578";
    private static final String APP_KEY = "CLLQFHJGDIHBABABA";
    private static final String REDIRECT_URL = "okauth://ok512000155578";

    @InjectPresenter
    AuthPresenter presenter;

    @BindView(R.id.login_value)
    AutoCompleteTextView login;
    @BindView(R.id.password_value)
    AutoCompleteTextView password;
    @BindView(R.id.vk)
    ImageButton vk;

    private Odnoklassniki odnoklassniki;
    private ProgressDialog popupDialog;
    private boolean isSuccessRestored = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView register = findViewById(R.id.register);
        TextView wrongPas = findViewById(R.id.wrong_password);
        register.setPaintFlags(register.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        wrongPas.setPaintFlags(wrongPas.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        if (BuildConfig.DEBUG) {
           //login.setText("admin@ya.ru");
         //  password.setText("11111111");
        }

        /*PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.remember.app", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }*/



        /*String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
        for (String fingerprint : fingerprints) {
            Log.e("fingerprint", fingerprint);
        }*/
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_auth;
    }

    @OnClick(R.id.vk)
    public void signInVk() {
        if (presenter.isOffline()) return;
        VKSdk.login(this, VKScope.EMAIL, VKScope.FRIENDS, VKScope.WALL, VKScope.PHOTOS);
    }

    @OnClick(R.id.ok)
    public void signInOk() {
        if (presenter.isOffline()) return;
        odnoklassniki = Odnoklassniki.createInstance(this, APP_ID, APP_KEY);
        odnoklassniki.requestAuthorization(this, REDIRECT_URL, OkAuthType.ANY, OkScope.VALUABLE_ACCESS, OkScope.LONG_ACCESS_TOKEN);
    }

    @OnClick(R.id.fb)
    public void signInFacebook() {
        List<String> scopes = Collections.singletonList("");

        if (presenter.isOffline()) return;
        com.jaychang.sa.facebook.SimpleAuth.connectFacebook(scopes, new AuthCallback() {
            @Override
            public void onSuccess(SocialUser socialUser) {
                Prefs.putString(PREFS_KEY_EMAIL, socialUser.email);
                Prefs.putString(PREFS_KEY_ACCESS_TOKEN, socialUser.accessToken);
                String[] str = socialUser.fullName.split(" ");
                Prefs.putString(PREFS_KEY_NAME_USER, str[0]);
                Prefs.putString(PREFS_KEY_AVATAR, socialUser.profilePictureUrl);
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

    // Mail Ru
    @OnClick(R.id.mailru)
    public void signInMailRu() {
        MailRuAuthSdk.getInstance().startLogin(this);
    }


    @OnClick(R.id.sign_in_btn)
    public void signIn() {

        if (login.getText().toString().equals("")) {
            Utils.showSnack(login, getResources().getString(R.string.auth_enter_email));

        } else if (password.getText().toString().equals("")) {
            Utils.showSnack(login, getResources().getString(R.string.auth_enter_password));

        } else {
            try {
                presenter.singInAuth(login.getText().toString(), password.getText().toString());

            } catch (Exception e) {
                Utils.showSnack(login, getResources().getString(R.string.auth_error_data_enter));
            }

    }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!MailRuAuthSdk.getInstance().handleAuthResult(requestCode, resultCode, data, new MailRuCallback<OAuthTokensResult, Integer>() {
                    @Override
                    public void onResult(OAuthTokensResult oAuthTokensResult) {
                        Log.d("Mail RU","Successfully signed in");
                        Log.d("Mail RU","Access token: " + oAuthTokensResult.getAccessToken());
                        Log.d("Mail RU","Refresh token: " + oAuthTokensResult.getRefreshToken());

                        Prefs.putString(PREFS_KEY_ACCESS_TOKEN, oAuthTokensResult.getAccessToken());
                        presenter.signInMailRu();
                    }

                    @Override
                    public void onError(Integer integer) {
                        Log.d("Mail RU", OAuthRequestErrorCodes.toReadableString(integer));
                    }
                }
        ))
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                Prefs.putString(PREFS_KEY_EMAIL, res.email);
                Prefs.putString(PREFS_KEY_ACCESS_TOKEN, res.accessToken);
                /*VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "photo_200"));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(VKResponse response) {
                        VKApiUser user = ((VKList<VKApiUser>) response.parsedModel).get(0);
                        Prefs.putString(PREFS_KEY_AVATAR, user.photo_200);
                        presenter.getInfoUser();
                    }

                    @Override
                    public void onError(VKError error) {
                        errorDialog("Ошибка авторизации");
                    }

                    @Override
                    public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                    }
                });*/
                presenter.getInfoUser();
            }

            @Override
            public void onError(VKError error) {
                Log.d("VK ActivityResult Error", error.errorMessage);
            }})) {
            if (!odnoklassniki.onAuthActivityResult(requestCode, resultCode, data, new OkListener() {
                @Override
                public void onSuccess(@NotNull JSONObject jsonObject) {
                    try {
                        Prefs.putString(PREFS_KEY_ACCESS_TOKEN, String.valueOf(jsonObject.get(SharedKt.PARAM_ACCESS_TOKEN)));
                        presenter.signInOk();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(@Nullable String s) {
                    Log.d("OK ActivityResult Error", s);
                }
            })) {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
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
        Utils.showSnack(login, getResources().getString(R.string.auth_wrong_login_or_pwd));
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
        isSuccessRestored = true;
        if (responseRestorePassword.getResponse()) {
            errorDialog(getResources().getString(R.string.restore_pwd_successfully_send));
        } else {
            Utils.showSnack(login, getResources().getString(R.string.restore_pwd_error_send));
        }
    }

    @Override
    public void errorRestored(Throwable throwable) {
        if (!isSuccessRestored) {
            popupDialog.dismiss();
            Utils.showSnack(login, getResources().getString(R.string.restore_pwd_error_send));
        }
    }

    public void errorDialog(String text) {
        new ErrorDialog(getSupportFragmentManager(), text);
    }

    @Override
    public void sendEmail(String email) {
        popupDialog = LoadingPopupUtils.showLoadingDialog(this);
        presenter.restorePassword(email);
    }
}

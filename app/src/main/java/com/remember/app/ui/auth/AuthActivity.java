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
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import java.util.Collections;
import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
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

public class AuthActivity extends MvpAppCompatActivity implements AuthView, RepairPasswordDialog.Callback {

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

    private Unbinder unbinder;
    private TwitterAuthClient client;
    private Odnoklassniki odnoklassniki;
    private ProgressDialog popupDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        unbinder = ButterKnife.bind(this);
        client = new TwitterAuthClient();
        TextView register = findViewById(R.id.register);
        TextView wrongPas = findViewById(R.id.wrong_password);
        register.setPaintFlags(register.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        wrongPas.setPaintFlags(wrongPas.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


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

    @OnClick(R.id.vk)
    public void signInVk() {
        VKSdk.login(this, VKScope.EMAIL, VKScope.FRIENDS, VKScope.WALL, VKScope.PHOTOS);
    }

    @OnClick(R.id.ok)
    public void signInOk() {
        odnoklassniki = Odnoklassniki.createInstance(this, APP_ID, APP_KEY);
        odnoklassniki.requestAuthorization(this, REDIRECT_URL, OkAuthType.ANY, OkScope.VALUABLE_ACCESS, OkScope.LONG_ACCESS_TOKEN);
    }

    @OnClick(R.id.fb)
    public void signInFacebook() {
        List<String> scopes = Collections.singletonList("");

        com.jaychang.sa.facebook.SimpleAuth.connectFacebook(scopes, new AuthCallback() {
            @Override
            public void onSuccess(SocialUser socialUser) {
                Prefs.putString(PREFS_KEY_EMAIL, socialUser.email);
                Prefs.putString(PREFS_KEY_ACCESS_TOKEN, socialUser.accessToken);
                String[] str = socialUser.fullName.split(" ");
                Prefs.putString(PREFS_KEY_NAME_USER, str[0]);
                //Prefs.putString(PREFS_KEY_AVATAR, socialUser.profilePictureUrl);
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
//
//    @OnClick(R.id.twitter)
//    public void signInTwitter() {
//        SimpleAuth.connectTwitter(new AuthCallback() {
//            @Override
//            public void onSuccess(SocialUser socialUser) {
//                Prefs.putString(PREFS_KEY_EMAIL, socialUser.email);
//                String[] str = socialUser.fullName.split(" ");
//                Prefs.putString(PREFS_KEY_NAME_USER, str[0]);
//                Prefs.putString(PREFS_KEY_AVATAR, socialUser.profilePictureUrl);
//                presenter.signInTwitter();
//            }
//
//            @Override
//            public void onError(Throwable onError) {
//                errorDialog("Ошибка авторизации");
//                Log.e("TWITTER", onError.getMessage());
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//        });
//    }

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
            }
        })) {
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
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            repairPasswordDialog.show(transaction, "repairPasswordDialog");
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
        FragmentManager manager = getSupportFragmentManager();
        wrongEmailDialog.setDescription(text);
        FragmentTransaction transaction = manager.beginTransaction();
        wrongEmailDialog.show(transaction, "wrongEmailDialog");
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void sendEmail(String email) {
        popupDialog = LoadingPopupUtils.showLoadingDialog(this);
        presenter.restorePassword(email);
    }
}

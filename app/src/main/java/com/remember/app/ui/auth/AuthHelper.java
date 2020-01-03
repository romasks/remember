package com.remember.app.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import com.jaychang.sa.AuthCallback;
import com.jaychang.sa.SocialUser;
import com.pixplicity.easyprefs.library.Prefs;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.util.VKUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import ru.mail.auth.sdk.AuthError;
import ru.mail.auth.sdk.AuthResult;
import ru.mail.auth.sdk.MailRuAuthSdk;
import ru.mail.auth.sdk.MailRuCallback;
import ru.ok.android.sdk.Odnoklassniki;
import ru.ok.android.sdk.OkListener;
import ru.ok.android.sdk.SharedKt;

import static com.remember.app.data.Constants.PREFS_KEY_ACCESS_TOKEN;
import static com.remember.app.data.Constants.PREFS_KEY_EMAIL;
import static com.remember.app.data.Constants.PREFS_KEY_NAME_USER;

class AuthHelper {

    private AuthPresenter presenter;
    private Odnoklassniki odnoklassniki;
    private MailRuAuthSdk mailru;

    AuthHelper(AuthPresenter presenter, MailRuAuthSdk mailru, Odnoklassniki odnoklassniki) {
        this.presenter = presenter;
        this.mailru = mailru;
        this.odnoklassniki = odnoklassniki;
    }

    void printFbHashKey(Context context) {
        PackageInfo info;
        try {
            info = context.getPackageManager().getPackageInfo("com.remember.app", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    void printVkFingerprint(Context context) {
        String[] fingerprints = VKUtil.getCertificateFingerprint(context, context.getPackageName());
        for (String fingerprint : fingerprints) {
            Log.e("fingerprint", fingerprint);
        }
    }

    AuthCallback fbAuthCallback() {
        return new AuthCallback() {
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
        };
    }

    boolean isVkActivityResult(int requestCode, int resultCode, Intent data) {
        return VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
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
                Log.e("VK ActivityResult", error.errorMessage);
            }
        });
    }

    boolean isMailRuActivityResult(int requestCode, int resultCode, Intent data) {
        return mailru.handleActivityResult(requestCode, resultCode, data, new MailRuCallback<AuthResult, AuthError>() {
            @Override
            public void onResult(AuthResult authResult) {
                // mailru.requestUserInfo(result, UserInfoCallback())
            }

            @Override
            public void onError(AuthError error) {
                Log.e("MailRu ActivityResult", error.getErrorReason());
            }
        });
    }

    boolean isOkActivityResult(int requestCode, int resultCode, Intent data) {
        return odnoklassniki.onAuthActivityResult(requestCode, resultCode, data, new OkListener() {
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
            public void onError(@Nullable String error) {
                Log.e("OK ActivityResult", error != null ? error : "Error OK login");
            }
        });
    }

}

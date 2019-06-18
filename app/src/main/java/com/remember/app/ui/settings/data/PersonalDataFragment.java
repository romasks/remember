package com.remember.app.ui.settings.data;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.remember.app.R;
import com.remember.app.data.models.ResponseSettings;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class PersonalDataFragment extends Fragment implements PersonalDataFragmentView {

    @InjectPresenter
    PersonalDataFragmentPresenter presenter;

    @BindView(R.id.avatar)
    ImageView avatar;
    @BindView(R.id.login)
    AutoCompleteTextView login;
    @BindView(R.id.secName)
    AutoCompleteTextView secondName;
    @BindView(R.id.name)
    AutoCompleteTextView name;
    @BindView(R.id.surname)
    AutoCompleteTextView middleName;
    @BindView(R.id.livePlace)
    AutoCompleteTextView city;
    @BindView(R.id.email)
    AutoCompleteTextView email;
    @BindView(R.id.phone)
    AutoCompleteTextView phone;
    @BindView(R.id.religion)
    AutoCompleteTextView religion;

    private Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.getInfo();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_setings_lk, container, false);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                if (getContext() != null){
                    Glide.with(getContext())
                            .load(resultUri)
                            .apply(RequestOptions.circleCropTransform())
                            .into(avatar);
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    @OnClick(R.id.add_new_photo)
    public void addNewPhoto() {
        CropImage.activity()
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(getContext(), this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onReceivedInfo(ResponseSettings responseSettings) {
        login.setText(responseSettings.getEmail());
        secondName.setText(responseSettings.getSetting().getSurname());
        name.setText(responseSettings.getSetting().getSurname());
        middleName.setText(responseSettings.getSetting().getSurname());
        email.setText(responseSettings.getEmail());
        phone.setText(responseSettings.getSetting().getPhone());
        religion.setText(responseSettings.getSetting().getRelId());
    }

    @Override
    public void error(Throwable throwable) {
        Snackbar.make(avatar,"Ошибка загрузки данных", Snackbar.LENGTH_LONG).show();
    }
}

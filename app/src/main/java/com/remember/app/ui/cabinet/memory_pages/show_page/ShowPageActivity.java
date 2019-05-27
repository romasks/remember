package com.remember.app.ui.cabinet.memory_pages.show_page;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.remember.app.R;
import com.remember.app.data.models.AddPageModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ShowPageActivity extends MvpAppCompatActivity {

    @BindView(R.id.fio)
    TextView name;
    @BindView(R.id.dates)
    TextView date;
    @BindView(R.id.city)
    TextView city;
    @BindView(R.id.crypt)
    TextView crypt;
    @BindView(R.id.sector)
    TextView sector;
    @BindView(R.id.grave)
    TextView grave;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        unbinder = ButterKnife.bind(this);
        Intent i = getIntent();
        AddPageModel person = i.getParcelableExtra("PERSON");

        if (person != null){
            initTextName(person);
            initDate(person);
            initInfo(person);
        }
    }

    private void initInfo(AddPageModel person) {
        city.setText(person.getCity());
        crypt.setText(person.getCemeteryName());
        sector.setText(person.getSpotId());
        grave.setText(person.getGraveId());
    }

    private void initDate(AddPageModel person) {
        String textDate = person.getBirthDate() + " - " + person.getDeathDate();
        date.setText(textDate);
    }

    private void initTextName(AddPageModel person) {
        String textName = person.getSecondName() + " " + person.getName() + " " + person.getThirdName();
        name.setText(textName);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}

package com.remember.app.ui.cabinet.memory_pages.show_page;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.remember.app.R;
import com.remember.app.data.models.AddPageModel;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.ui.cabinet.main.MainActivity;
import com.remember.app.ui.cabinet.epitaphs.EpitaphsActivity;
import com.remember.app.ui.cabinet.memory_pages.events.EventsActivity;

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
    @BindView(R.id.events)
    ImageButton events;
    @BindView(R.id.epitButton)
    ImageButton epitaphyButton;

    private Unbinder unbinder;
    private boolean isList = false;
    private AddPageModel person;
    private MemoryPageModel memoryPageModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        unbinder = ButterKnife.bind(this);
        Intent i = getIntent();
        isList = i.getBooleanExtra("IS_LIST", false);
        if (!isList) {
            person = i.getParcelableExtra("PERSON");
            if (person != null) {
                initTextName(person);
                initDate(person);
                initInfo(person);
            }
        } else {
            memoryPageModel = i.getParcelableExtra("PERSON");
            if (memoryPageModel != null) {
                initTextName(memoryPageModel);
                initDate(memoryPageModel);
                initInfo(memoryPageModel);
            }
        }
        epitaphyButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, EpitaphsActivity.class);
            if (isList) {
                intent.putExtra("ID_PAGE", memoryPageModel.getId());
            } else {
                intent.putExtra("ID_PAGE", person.getId());
            }
            startActivity(intent);
        });
        events.setOnClickListener(v -> {
            Intent intent = new Intent(this, EventsActivity.class);
            intent.putExtra("NAME", name.getText().toString());
            if (isList) {
                intent.putExtra("ID_PAGE", memoryPageModel.getId());
            } else {
                intent.putExtra("ID_PAGE", person.getId());
            }
            startActivity(intent);
        });
    }

    private void initInfo(MemoryPageModel memoryPageModel) {
        city.setText(memoryPageModel.getGorod());
        crypt.setText(memoryPageModel.getNazvaklad());
        sector.setText(memoryPageModel.getUchastok());
        grave.setText(memoryPageModel.getNummogil());
    }

    private void initDate(MemoryPageModel memoryPageModel) {
        String textDate = memoryPageModel.getDatarod() + " - " + memoryPageModel.getDatasmert();
        date.setText(textDate);
    }

    private void initTextName(MemoryPageModel memoryPageModel) {
        String textName = memoryPageModel.getSecondname() + " " + memoryPageModel.getName() + " " + memoryPageModel.getThirtname();
        name.setText(textName);
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
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}

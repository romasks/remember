package com.remember.app.ui.grid;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.presenter.InjectPresenter;
import com.google.android.material.navigation.NavigationView;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.R;
import com.remember.app.data.models.MemoryPageModel;
import com.remember.app.data.models.RequestSearchPage;
import com.remember.app.data.models.ResponsePages;
import com.remember.app.data.models.ResponseUserInfo;
import com.remember.app.ui.adapters.ImageAdapter;
import com.remember.app.ui.auth.AuthActivity;
import com.remember.app.ui.base.BaseActivity;
import com.remember.app.ui.cabinet.main.MainActivity;
import com.remember.app.ui.cabinet.memory_pages.show_page.ShowPageActivity;
import com.remember.app.ui.menu.events.EventsActivityMenu;
import com.remember.app.ui.menu.notifications.NotificationsActivity;
import com.remember.app.ui.menu.page.PageActivityMenu;
import com.remember.app.ui.menu.question.QuestionActivity;
import com.remember.app.ui.menu.settings.SettingActivity;
import com.remember.app.ui.utils.PopupPageScreen;
import com.remember.app.ui.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

import static com.remember.app.data.Constants.INTENT_EXTRA_IS_LIST;
import static com.remember.app.data.Constants.INTENT_EXTRA_PERSON;
import static com.remember.app.data.Constants.INTENT_EXTRA_SHOW;
import static com.remember.app.data.Constants.PREFS_KEY_AVATAR;
import static com.remember.app.data.Constants.PREFS_KEY_EMAIL;
import static com.remember.app.data.Constants.PREFS_KEY_NAME_USER;
import static com.remember.app.data.Constants.PREFS_KEY_SETTINGS_SHOW_NOTIFICATIONS;
import static com.remember.app.data.Constants.PREFS_KEY_USER_ID;
import static com.remember.app.ui.utils.ImageUtils.setGlideImage;

public class GridActivity extends BaseActivity implements GridView, ImageAdapter.Callback, PopupPageScreen.Callback, NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = GridActivity.class.getSimpleName();

    @InjectPresenter
    GridPresenter presenter;

    @BindView(R.id.image_rv)
    RecyclerView recyclerView;
    @BindView(R.id.search)
    ImageView search;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.show_all)
    Button showAll;
    @BindView(R.id.progress)
    ProgressBar progressBar;
    @BindView(R.id.avatar_small_toolbar)
    ImageView avatar_user;
    @BindView(R.id.menu_icon)
    ImageView button_menu;
    @BindView(R.id.grid_sign_in)
    Button signInButton;

    private ImageView imageViewBigAvatar;
    private ImageAdapter imageAdapter;
    private TextView navUserName;
    private DrawerLayout drawer;

    private int pageNumber = 1;
    private int theme_setting = 0;
    private int countSum = 0;

    @Override
    protected int getContentView() {
        return R.layout.activity_grid;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Utils.setTheme(this);

        super.onCreate(savedInstanceState);

        search.setImageResource(Utils.isThemeDark() ? R.drawable.ic_search_dark_theme : R.drawable.ic_search);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setHasFixedSize(true);

        imageAdapter = new ImageAdapter();
        imageAdapter.setCallback(this);
        imageAdapter.setContext(this);
        recyclerView.setAdapter(imageAdapter);

        //setUpLoadMoreListener();
        presenter.getImages(pageNumber);

        showAll.setOnClickListener(v -> {
            showAll.setVisibility(View.GONE);
            pageNumber = 1;
            presenter.getImages(pageNumber);
        });

        drawer = findViewById(R.id.drawer_layout_2);
        button_menu.setOnClickListener(k -> {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (theme_setting == 1) {
            this.recreate();
        }
        drawer.setDrawerLockMode(
                Utils.isEmptyPrefsKey(PREFS_KEY_USER_ID)
                        ? DrawerLayout.LOCK_MODE_LOCKED_CLOSED
                        : DrawerLayout.LOCK_MODE_UNLOCKED
        );
        /*if (!Utils.isEmptyPrefsKey(PREFS_KEY_USER_ID) && !Utils.isEmptyPrefsKey(PREFS_KEY_TOKEN)) {
            presenter.getInfo();
        }*/
        getInfoUser();
    }

    private void getInfoUser() {
        if (!Utils.isEmptyPrefsKey(PREFS_KEY_USER_ID)) {
            avatar_user.setVisibility(View.VISIBLE);
            button_menu.setVisibility(View.VISIBLE);
            signInButton.setText("Перейти в кабинет");

            setSupportActionBar(findViewById(R.id.toolbar));

            NavigationView navigationView = findViewById(R.id.nav_view_grid);
            navigationView.setNavigationItemSelectedListener(this);

            View headerView = navigationView.getHeaderView(0);

            navUserName = headerView.findViewById(R.id.user_name);
            navUserName.setText(Prefs.getString(PREFS_KEY_NAME_USER, ""));

            TextView navEmail = headerView.findViewById(R.id.user_email);
            navEmail.setText(Prefs.getString(PREFS_KEY_EMAIL, ""));

            imageViewBigAvatar = headerView.findViewById(R.id.logo);

            if (Utils.isEmptyPrefsKey(PREFS_KEY_AVATAR)) {
                setGlideImage(this, R.drawable.ic_unknown, avatar_user);
                setGlideImage(this, R.drawable.ic_unknown, imageViewBigAvatar);
            } else {
                setGlideImage(this, Prefs.getString(PREFS_KEY_AVATAR, ""), avatar_user);
                setGlideImage(this, Prefs.getString(PREFS_KEY_AVATAR, ""), imageViewBigAvatar);
            }

            imageViewBigAvatar.setOnClickListener(view -> {
                startActivity(new Intent(this, SettingActivity.class));
            });
        } else {
            avatar_user.setVisibility(View.GONE);
            button_menu.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.grid_sign_in)
    public void entry() {
        if (!Utils.isEmptyPrefsKey(PREFS_KEY_USER_ID)) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            startActivity(new Intent(this, AuthActivity.class));
        }
    }

    @OnClick(R.id.search)
    public void doSearch() {
        showEventScreen();
    }

    @Override
    public void onReceivedImages(ResponsePages responsePages) {
        imageAdapter.setItems(responsePages.getResult());
        progressBar.setVisibility(View.GONE);
        countSum = responsePages.getPages();

        if (pageNumber < countSum) {
            progressBar.setVisibility(View.VISIBLE);
            pageNumber++;
            presenter.getImages(pageNumber);
        }
    }

    @Override
    public void onSearchedPages(List<MemoryPageModel> memoryPageModels) {
        if (memoryPageModels.isEmpty()) {
            Utils.showSnack(recyclerView, "Записи не найдены");
        }
        showAll.setVisibility(memoryPageModels.isEmpty() ? View.VISIBLE : View.GONE);
        imageAdapter.setItemsSearch(memoryPageModels);
        progressBar.setVisibility(View.GONE);
    }

    private void setUpLoadMoreListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView,
                                   int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (pageNumber < countSum) {
                    progressBar.setVisibility(View.VISIBLE);
                    pageNumber++;
                    presenter.getImages(pageNumber);
                }
            }
        });
    }

    private void showEventScreen() {
        View popupView = getLayoutInflater().inflate(R.layout.popup_page_screen, null);

        ConstraintLayout layout = popupView.findViewById(R.id.cont);
        Toolbar toolbar = popupView.findViewById(R.id.toolbar);
        ImageView backImg = popupView.findViewById(R.id.back);
        TextView textView = popupView.findViewById(R.id.textView2);
        AutoCompleteTextView lastName = popupView.findViewById(R.id.last_name_value);
        AutoCompleteTextView name = popupView.findViewById(R.id.first_name_value);
        AutoCompleteTextView middleName = popupView.findViewById(R.id.father_name_value);
        AutoCompleteTextView place = popupView.findViewById(R.id.live_place_value);
        AutoCompleteTextView dateBegin = popupView.findViewById(R.id.date_begin_value);
        AutoCompleteTextView dateEnd = popupView.findViewById(R.id.date_end_value);

        if (Utils.isThemeDark()) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryBlack));
            layout.setBackgroundColor(getResources().getColor(R.color.colorBlackDark));
            backImg.setImageResource(R.drawable.ic_back_dark_theme);
            int textColorDark = getResources().getColor(R.color.colorWhiteDark);
            textView.setTextColor(textColorDark);
            name.setTextColor(textColorDark);
            lastName.setTextColor(textColorDark);
            middleName.setTextColor(textColorDark);
            dateBegin.setTextColor(textColorDark);
            dateEnd.setTextColor(textColorDark);
            place.setTextColor(textColorDark);
        }

        PopupPageScreen popupWindowPage = new PopupPageScreen(
                popupView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindowPage.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        popupWindowPage.setFocusable(true);
        popupWindowPage.setCallback(this);
        popupWindowPage.setUp(title);
    }

    @Override
    public void openPage(MemoryPageModel memoryPageModel) {
        Intent intent = new Intent(this, ShowPageActivity.class);
        intent.putExtra(INTENT_EXTRA_PERSON, memoryPageModel);
        intent.putExtra(INTENT_EXTRA_IS_LIST, true);
        intent.putExtra(INTENT_EXTRA_SHOW, true);
        startActivity(intent);
    }

    @Override
    public void search(RequestSearchPage requestSearchPage) {
        presenter.search(requestSearchPage);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.settings) {
            startActivity(new Intent(this, SettingActivity.class));
            theme_setting = 1;
            return true;
        }
        if (id == R.id.event_calendar) {
            startActivity(new Intent(this, EventsActivityMenu.class));
            return true;
        }
        if (id == R.id.memory_pages) {
            startActivity(new Intent(this, PageActivityMenu.class));
            return true;
        }
        if (id == R.id.questions) {
            startActivity(new Intent(this, QuestionActivity.class));
            return true;
        }
        if (id == R.id.exit) {
            Prefs.clear();
            startActivity(new Intent(this, GridActivity.class));
            finish();
            return true;
        }
        if (id == R.id.notifications) {
            startActivity(new Intent(this, NotificationsActivity.class));
            return true;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        TextView navUserName = drawer.findViewById(R.id.user_name);
        navUserName.setText(Prefs.getString(PREFS_KEY_NAME_USER, ""));

        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public void onReceivedInfo(ResponseUserInfo responseSettings) {
        if (!responseSettings.getSettings().getPicture().isEmpty()) {
            Prefs.putString(PREFS_KEY_AVATAR, responseSettings.getSettings().getPicture());

            setGlideImage(this, responseSettings.getSettings().getPicture(), avatar_user);
            setGlideImage(this, responseSettings.getSettings().getPicture(), imageViewBigAvatar);
        } else {
            setGlideImage(this, R.drawable.ic_unknown, avatar_user);
            setGlideImage(this, R.drawable.ic_unknown, imageViewBigAvatar);
        }

        Prefs.putString(PREFS_KEY_NAME_USER, responseSettings.getName() + " " + responseSettings.getSettings().getSurname());
        navUserName.setText(Prefs.getString(PREFS_KEY_NAME_USER, ""));

        Prefs.putBoolean(PREFS_KEY_SETTINGS_SHOW_NOTIFICATIONS, responseSettings.getSettings().getNotificationsEnabled() == 1);
    }

    @Override
    public void onError(Throwable throwable) {
        Utils.showSnack(recyclerView, "Ошибка получения плиток");
    }
}

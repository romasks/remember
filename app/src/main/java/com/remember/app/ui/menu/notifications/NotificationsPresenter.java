package com.remember.app.ui.menu.notifications;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.arellomobile.mvp.InjectViewState;
import com.remember.app.Remember;
import com.remember.app.data.models.EpitNotificationModel;
import com.remember.app.data.models.EventNotificationModel;
import com.remember.app.ui.base.BasePresenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class NotificationsPresenter extends BasePresenter<NotificationsView> {

    enum NotificationFilterType {ALL, RELIGIOUS_EVENTS, DEAD_EVENTS}

    NotificationsPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    void getEventNotification(NotificationFilterType filterType) {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.getEventNotifications(filterType.toString())
            .subscribeOn(Schedulers.io())
            .map(this::prepareEventNotifications)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getViewState()::onNotificationsLoaded, getViewState()::onError);
        unsubscribeOnDestroy(subscription);
    }

    void getEpitNotifications() {
        if (isOffline()) return;
        Disposable subscription = serviceNetwork.getEpitNotifications()
            .subscribeOn(Schedulers.io())
            .map(this::prepareEpitNotifications)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(getViewState()::onNotificationsLoaded, getViewState()::onError);
        unsubscribeOnDestroy(subscription);
    }

    private List<EventNotificationModel> prepareEventNotifications(List<EventNotificationModel> notifications) throws ParseException {
        SimpleDateFormat formatGMT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
        formatGMT.setTimeZone(TimeZone.getTimeZone("GMT"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

        Date date;

        for (EventNotificationModel notification : notifications) {
            date = formatGMT.parse(notification.getNextDate().replaceAll("Z$", "+0000"));
            notification.setDisplayedDate(dateFormat.format(date));

            notification.setDisplayedText(getEventTitle(notification));
        }

        return notifications;
    }

    private List<EpitNotificationModel> prepareEpitNotifications(List<EpitNotificationModel> notifications) throws ParseException {
        SimpleDateFormat formatGMT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
        formatGMT.setTimeZone(TimeZone.getTimeZone("GMT"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

        Date date;

        for (EpitNotificationModel notification : notifications) {
            date = formatGMT.parse(notification.getCreatedAt().replaceAll("Z$", "+0000"));
            notification.setDisplayedDate(dateFormat.format(date));

            notification.setDisplayedText(getEpitTitle(notification));
        }

        return notifications;
    }

    private SpannableString getEventTitle(EventNotificationModel event) {
        int color = Color.parseColor("#917b5a");

        SpannableString title;
        String tmpTitle;

        int tmpPos = 0;

        String daysStr;
        if (event.getRemainDays() == 0) daysStr = "";
        else if (event.getRemainDays() > 10 && event.getRemainDays() < 20) daysStr = "дней";
        else if (event.getRemainDays() % 10 == 1) daysStr = "день";
        else if (event.getRemainDays() % 10 >= 2 && event.getRemainDays() % 10 <= 4)
            daysStr = "дня";
        else daysStr = "дней";

        switch (event.getType()) {
            case "dead_event":
                if (event.getRemainDays() == 0) {
                    tmpTitle = "Сегодня " + event.getEventName() + " у " + event.getPageName();

                    title = new SpannableString(tmpTitle);
                    addSpan(title, tmpTitle.indexOf(event.getEventName()), tmpTitle.length(), color, true);
                } else {
                    tmpTitle = "Осталось " + event.getRemainDays() + " " + daysStr + " до " + event.getEventName() +
                        " у " + event.getPageName();

                    title = new SpannableString(tmpTitle);

                    tmpPos = tmpTitle.indexOf(String.valueOf(event.getRemainDays()));
                    addSpan(title, tmpPos, tmpPos + String.valueOf(event.getRemainDays()).length(), color, true);

                    tmpPos = tmpTitle.indexOf(event.getEventName());
                    addSpan(title, tmpPos, tmpPos + event.getEventName().length(), color, true);

                    tmpPos = tmpTitle.indexOf(event.getPageName());
                    addSpan(title, tmpPos, tmpTitle.length(), color, true);
                }
                break;

            case "birth":
                if (event.getRemainDays() == 0) {
                    tmpTitle = "Сегодня День рождения у " + event.getPageName();

                    title = new SpannableString(tmpTitle);

                    tmpPos = tmpTitle.indexOf("День рождения");
                    addSpan(title, tmpPos, tmpPos + "День рождения".length(), color, true);

                    tmpPos = tmpTitle.indexOf(event.getPageName());
                    addSpan(title, tmpPos, tmpTitle.length(), color, true);
                } else {
                    tmpTitle =
                        "Осталось " + event.getRemainDays() + " " + daysStr + " до Дня рождения у " + event.getPageName();

                    title = new SpannableString(tmpTitle);

                    tmpPos = tmpTitle.indexOf(String.valueOf(event.getRemainDays()));
                    addSpan(title, tmpPos, tmpPos + String.valueOf(event.getRemainDays()).length(), color, true);

                    tmpPos = tmpTitle.indexOf("Дня рождения");
                    addSpan(title, tmpPos, tmpPos + "Дня рождения".length(), color, true);

                    tmpPos = tmpTitle.indexOf(event.getPageName());
                    addSpan(title, tmpPos, tmpTitle.length(), color, true);
                }
                break;

            case "dead":
                if (event.getRemainDays() == 0) {
                    tmpTitle = "Сегодня День смерти у " + event.getPageName();

                    title = new SpannableString(tmpTitle);

                    tmpPos = tmpTitle.indexOf("День смерти");
                    addSpan(title, tmpPos, tmpPos + "День смерти".length(), color, true);

                    tmpPos = tmpTitle.indexOf(event.getPageName());
                    addSpan(title, tmpPos, tmpTitle.length(), color, true);
                } else {
                    tmpTitle =
                        "Осталось " + event.getRemainDays() + " " + daysStr + " до Дня смерти у " + event.getPageName();

                    title = new SpannableString(tmpTitle);

                    tmpPos = tmpTitle.indexOf(String.valueOf(event.getRemainDays()));
                    addSpan(title, tmpPos, tmpPos + String.valueOf(event.getRemainDays()).length(), color, true);

                    tmpPos = tmpTitle.indexOf("Дня смерти");
                    addSpan(title, tmpPos, tmpPos + "Дня смерти".length(), color, true);

                    tmpPos = tmpTitle.indexOf(event.getPageName());
                    addSpan(title, tmpPos, tmpTitle.length(), color, true);
                }
                break;

            case "event":
                if (event.getRemainDays() == 0) {
                    tmpTitle = "Сегодня " + event.getEventName();

                    title = new SpannableString(tmpTitle);

                    addSpan(title, tmpTitle.indexOf(event.getEventName()), tmpTitle.length(), color, true);
                } else {
                    tmpTitle = "Осталось " + event.getRemainDays() + " " + daysStr + " до " + event.getEventName();

                    title = new SpannableString(tmpTitle);

                    tmpPos = tmpTitle.indexOf(String.valueOf(event.getRemainDays()));
                    addSpan(title, tmpPos, tmpPos + String.valueOf(event.getRemainDays()).length(), color, true);

                    tmpPos = tmpTitle.indexOf(event.getEventName());
                    addSpan(title, tmpPos, tmpTitle.length(), color, true);
                }
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + event.getType());
        }

        return title;
    }

    private SpannableString getEpitTitle(EpitNotificationModel epit) {
        int color = Color.parseColor("#917b5a");

        String tmpTitle =
            epit.getUserName() + " оставил(а) эпитафию на странице " + epit.getPageName() + ": " + epit.getText();

        SpannableString title = new SpannableString(tmpTitle);

        addSpan(title, 0, epit.getUserName().length(), color, true);

        int tmpPos = tmpTitle.indexOf(epit.getPageName());
        addSpan(title, tmpPos, tmpPos + epit.getPageName().length(), color, true);

        tmpPos = tmpTitle.indexOf(epit.getText());
        addSpan(title, tmpPos, tmpPos + epit.getText().length(), Color.BLACK, false);

        return title;
    }

    private void addSpan(Spannable text, int start, int end, int color, boolean isBold) {
        text.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        if (isBold)
            text.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    }

}

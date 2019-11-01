package com.remember.app.ui.menu.notifications;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.pixplicity.easyprefs.library.Prefs;
import com.remember.app.Remember;
import com.remember.app.data.models.EpitNotificationModel;
import com.remember.app.data.models.EventNotificationModel;
import com.remember.app.data.network.ServiceNetwork;
import com.remember.app.ui.base.BasePresenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@InjectViewState
public class NotificationsPresenter extends BasePresenter<NotificationsView> {

    enum NotificationFilterType {ALL, RELIGIOUS_EVENTS, DEAD_EVENTS}

    @Inject
    ServiceNetwork serviceNetwork;

    NotificationsPresenter() {
        Remember.getApplicationComponent().inject(this);
    }

    void getEventNotification(NotificationFilterType filterType) {

        Disposable subscription = serviceNetwork.getEventNotifications(filterType.toString())
                .subscribeOn(Schedulers.io())
                .map(this::prepareEventNotifications)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onNotificationsLoaded, Throwable::printStackTrace);
        unsubscribeOnDestroy(subscription);
    }

    void getEpitNotifications(){
        Disposable subscription = serviceNetwork.getEpitNotifications()
                .subscribeOn(Schedulers.io())
                .map(this::prepareEpitNotifications)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getViewState()::onNotificationsLoaded, Throwable::printStackTrace);
        unsubscribeOnDestroy(subscription);
    }

    private List<EventNotificationModel> prepareEventNotifications(List<EventNotificationModel> notifications) throws ParseException {
        SimpleDateFormat formatGMT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", Locale.getDefault());
        formatGMT.setTimeZone(TimeZone.getTimeZone("GMT"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

        Date date;

        for (EventNotificationModel notification : notifications){
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

        for (EpitNotificationModel notification : notifications){
            date = formatGMT.parse(notification.getCreatedAt().replaceAll("Z$", "+0000"));
            notification.setDisplayedDate(dateFormat.format(date));

            notification.setDisplayedText(getEpitTitle(notification));
        }

        return notifications;
    }

    private SpannableString getEventTitle(EventNotificationModel event){
        int color = Color.parseColor("#917b5a");

        SpannableString title;
        String tmpTitle;

        int tmpPos = 0;

        switch (event.getType()){
            case "dead_event":
                if (event.getRemainDays() == 0) {
                    tmpTitle = "Сегодня " + event.getEventName();

                    title = new SpannableString(tmpTitle);
                    addSpan(title, tmpTitle.indexOf(event.getEventName()), tmpTitle.length(), color, true);
                }
                else {
                    tmpTitle = "Осталось " + event.getRemainDays() + " дней до " + event.getEventName() + " у " + event.getPageName();

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

                    addSpan(title, tmpTitle.indexOf(event.getPageName()), tmpTitle.length(), color, true);
                }
                else {
                    tmpTitle = "Осталось " + event.getRemainDays() + " дней до Дня рождения у " + event.getPageName();

                    title = new SpannableString(tmpTitle);

                    tmpPos = tmpTitle.indexOf(String.valueOf(event.getRemainDays()));
                    addSpan(title, tmpPos, tmpPos + String.valueOf(event.getRemainDays()).length(), color, true);

                    tmpPos = tmpTitle.indexOf(event.getPageName());
                    addSpan(title, tmpPos, tmpTitle.length(), color, true);
                }
                break;

            case "dead":
                if (event.getRemainDays() == 0) {
                    tmpTitle = "Сегодня Годовщина смерти у " + event.getPageName();

                    title = new SpannableString(tmpTitle);

                    addSpan(title, tmpTitle.indexOf(event.getPageName()), tmpTitle.length(), color, true);
                }
                else {
                    tmpTitle = "Осталось " + event.getRemainDays() + " дней до Годовщины смерти у " + event.getPageName();

                    title = new SpannableString(tmpTitle);

                    tmpPos = tmpTitle.indexOf(String.valueOf(event.getRemainDays()));
                    addSpan(title, tmpPos, tmpPos + String.valueOf(event.getRemainDays()).length(), color, true);

                    tmpPos = tmpTitle.indexOf(event.getPageName());
                    addSpan(title, tmpPos, tmpTitle.length(), color, true);
                }
                break;

            case "event":
                if (event.getRemainDays() == 0) {
                    tmpTitle = "Сегодня " + event.getEventName();

                    title = new SpannableString(tmpTitle);

                    addSpan(title, tmpTitle.indexOf(event.getEventName()), tmpTitle.length(), color, true);
                }
                else {
                    tmpTitle = "Осталось " + event.getRemainDays() + " дней до " + event.getEventName();

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

    private SpannableString getEpitTitle(EpitNotificationModel epit){
        int color = Color.parseColor("#917b5a");

        String tmpTitle = epit.getUserName() + " оставил эпитафию на странице " + epit.getPageName() + ": " + epit.getText();

        SpannableString title = new SpannableString(tmpTitle);

        addSpan(title, 0, epit.getUserName().length(), color, true);

        int tmpPos = tmpTitle.indexOf(epit.getPageName());
        addSpan(title, tmpPos, tmpPos + epit.getPageName().length(), color, true);

        tmpPos = tmpTitle.indexOf(epit.getText());
        addSpan(title, tmpPos, tmpPos + epit.getText().length(), Color.BLACK, false);

        return title;
    }

    private void addSpan(Spannable text, int start, int end, int color, boolean isBold){
        text.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        if (isBold)
            text.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    }

}

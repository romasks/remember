package com.remember.app.data;

import com.theartofdev.edmodo.cropper.CropImageView;

public class Constants {

    public static final String BASE_SERVICE_URL = "http://помню.рус";
//    public static final String BASE_SERVICE_URL = "http://pomnyu.ru";
    public static final String PLAY_MARKET_LINK = "https://play.google.com/store/apps/details?id=com.remember.app";
   // public static final String SIMPLE_PAGE_LINK = "http://pomnyu.ru/page/"; //Для определения адресса конкретной анкеты, для репоста
    public static final String FACEBOOK_APP_ID = "661338814675848";

    public static final String PREFS_KEY_SETTINGS_SHOW_NOTIFICATIONS = "SETTINGS_SHOW_NOTIFICATIONS";
    public static final String PREFS_KEY_IS_LAUNCH_MODE = "IS_LAUNCH_MODE";
    public static final String PREFS_KEY_ACCESS_TOKEN = "ACCESS_TOKEN";
    public static final String PREFS_KEY_NAME_USER = "NAME_USER";
    public static final String PREFS_KEY_USER_ID = "USER_ID";
    public static final String PREFS_KEY_AVATAR = "AVATAR";
    public static final String PREFS_KEY_EMAIL = "EMAIL";
    public static final String PREFS_KEY_TOKEN = "TOKEN";
    public static final String PREFS_KEY_THEME = "THEME";

    public static final boolean THEME_LIGHT = false;
    public static final boolean THEME_DARK = true;

    public static final String INTENT_EXTRA_IS_LAUNCH_MODE = "IS_LAUNCH_MODE";

    public static final String INTENT_EXTRA_EVENT_DESCRIPTION = "EVENT_DESCRIPTION";
    public static final String INTENT_EXTRA_EVENT_IMAGE_URL = "EVENT_IMAGE_URL";
    public static final String INTENT_EXTRA_EVENT_PERSON = "EVENT_PERSON";
    public static final String INTENT_EXTRA_EVENT_NAME = "EVENT_NAME";
    public static final String INTENT_EXTRA_EVENT_DATE = "EVENT_DATE";
    public static final String INTENT_EXTRA_EVENT_ID = "EVENT_ID";
    public static final String INTENT_EXTRA_EVENT_IS_FOR_ONE = "IS_FOR_ONE";
    public static final String INTENT_EXTRA_EVENT_ACCESS = "ACCESS";

    public static final String INTENT_EXTRA_IS_EVENT_EDITING = "IS_EVENT_EDITING";

    public static final String INTENT_EXTRA_PERSON_NAME = "PERSON_NAME";
    public static final String INTENT_EXTRA_AFTER_SAVE = "AFTER_SAVE";
    public static final String INTENT_EXTRA_FROM_NOTIF = "FROM_NOTIF";
    public static final String INTENT_EXTRA_IS_LIST = "IS_LIST";
    public static final String INTENT_EXTRA_PAGE_ID = "PAGE_ID";
    public static final String BIRTH_DATE = "BIRTH_DATE";
    public static final String INTENT_EXTRA_PERSON = "PERSON";
    public static final String INTENT_EXTRA_SHOW = "SHOW";
    public static final String INTENT_EXTRA_NAME = "NAME";
    public static final String INTENT_EXTRA_ID = "ID";

    public static final String INTENT_EXTRA_POSITION_IN_SLIDER = "POSITION_IN_SLIDER";
    public static final String INTENT_EXTRA_SETTINGS_FRAGMENT_PAGER_STATE = "SETTINGS_FRAGMENT_PAGER_STATE";

    public static final String NOTIF_EVENT_TYPE_EVENT = "event";
    public static final String NOTIF_EVENT_TYPE_BIRTH = "birth";
    public static final String NOTIF_EVENT_TYPE_DEAD = "dead";
    public static final String NOTIF_EVENT_TYPE_DEAD_EVENT = "dead_event";

    public static final String BURIAL_PLACE_COORDS = "BURIAL_PLACE_COORDS";
    public static final String BURIAL_PLACE_CITY = "BURIAL_PLACE_CITY";
    public static final String BURIAL_PLACE_CEMETERY = "BURIAL_PLACE_CEMETERY";
    public static final String BURIAL_PLACE_SECTOR = "BURIAL_PLACE_SECTOR";
    public static final String BURIAL_PLACE_LINE = "BURIAL_PLACE_LINE";
    public static final String BURIAL_PLACE_GRAVE = "BURIAL_PLACE_GRAVE";

    public static final String IMAGES_STATUS_APPROVED = "Одобрено";

    public static final String SEARCH_ON_GRID = "grid";
    public static final String SEARCH_ON_MAIN = "main";

    public static final CropImageView.CropShape CROP_IMAGE_OVAL = CropImageView.CropShape.OVAL;
    public static final CropImageView.CropShape CROP_IMAGE_RECT = CropImageView.CropShape.RECTANGLE;
}

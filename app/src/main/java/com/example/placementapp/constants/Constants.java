package com.example.placementapp.constants;

public abstract class Constants {

    public class UserTypes {
        public static final String ADMIN = "0";
        public static final String STUDENT = "1";
    }

    public class FirebaseConstants {
        public static final String PATH_LOGIN = "Login";
        public static final String PATH_NOTIFICATIONS = "Notification";
        public static final String PATH_APPLICATIONS = "Applications/";
        public static final String PATH_APPILED_COMPANIES = "AppliedCompanies/";
        public static final String FIREBASE_SERVER_KEY = "key=AAAAcbbeSbc:APA91bEO3kDXcK8khLywCXAMuV7516Fttdp5re70s80NzaZrQ-F6QUIxUemg87qZCrAc3nX7f9hitS8uA69dMvW6MfC7AhyQDLnaxC9tUGJBg61aCeaHuyjAvbIy5N2Kg0LRMx87VPOp";
        public static final String FCM_API = "https://fcm.googleapis.com/fcm/send";
    }

    public class SharedPrefConstants {
        public static final String SHARED_PREF = "MyPreferences";
        public static final String KEY_PASSWORD = "password";
        public static final String KEY_NAME = "name";
        public static final String KEY_BRANCH = "branch";
        public static final String KEY_MAIL = "mail";
        public static final String KEY_PRN = "prn";
        public static final String KEY_TYPE = "type";
    }

    public enum StatusEnum {
        SUCCESS,
        INCORRECT,
        INVALID,
        FAILURE
    }

    public enum NavigationView {
        STUDENT,
        ADMIN
    }

    public class HttpConstants {
        public static final String USER_REGISTRATION_URL = "https://placement-server.herokuapp.com/rest/user/register";
        public static final String USER_LOGIN_URL = "https://placement-server.herokuapp.com/rest/user/login";
        public static final String SAVE_NOTIFICATION_URL = "https://placement-server.herokuapp.com/rest/notification/save-notification";
        public static final String GET_NOTIFICATIONS_URL = "https://placement-server.herokuapp.com/rest/notification/get-notifications?branch=";
        public static final String GET_NOTIFICATION_DETAILS_URL = "https://placement-server.herokuapp.com/rest/notification/get-notification?id=";
        public static final String GET_USERS_URL = "https://placement-server.herokuapp.com/rest/user/get-users?branch=";
        public static final String GET_SPECIFIC_USER_URL = "https://placement-server.herokuapp.com/rest/user/get-user?prn=";
        public static final String UPDATE_USER_PROFILE_URL = "https://placement-server.herokuapp.com/rest/user/update-profile";

        public static final String KEY_STATUS_CODE = "statusCode";
        public static final String USER_REGISTERED = "201";
        public static final String EXISTING_USER = "301";
        public static final String SUCCESS = "200";
        public static final String FAILURE = "305";
    }
}

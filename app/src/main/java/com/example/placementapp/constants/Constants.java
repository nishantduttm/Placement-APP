package com.example.placementapp.constants;

public abstract class Constants {

    public class UserTypes{
        public static final String ADMIN = "0";
        public static final String STUDENT = "1";
    }

    public class FirebaseConstants{
        public static final String PATH_LOGIN = "Login";
        public static final String PATH_NOTIFICATIONS = "Notification";
        public static final String PATH_APPLICATIONS = "Applications/";
        public static final String PATH_APPILED_COMPANIES = "AppliedCompanies/";
    }

    public class SharedPrefConstants{
        public static final String SHARED_PREF = "MyPreferences";
        public static final String KEY_PASSWORD = "password";
        public static final String KEY_NAME = "name";
        public static final String KEY_BRANCH = "branch";
        public static final String KEY_MAIL = "mail";
        public static final String KEY_PRN = "prn";
        public static final String KEY_TYPE = "type";
    }
}

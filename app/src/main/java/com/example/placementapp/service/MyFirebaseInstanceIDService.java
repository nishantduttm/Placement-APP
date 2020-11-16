package com.example.placementapp.service;

import android.util.Log;

import com.example.placementapp.constants.Constants;
import com.example.placementapp.helper.SharedPrefHelper;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import androidx.annotation.NonNull;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService {

    private String branch;
    private static final String TAG = "mFirebaseIIDService";
//    private static final String SUBSCRIBE_TO = "Students";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        branch = SharedPrefHelper.getEntryfromSharedPreferences(getApplicationContext(),Constants.SharedPrefConstants.KEY_BRANCH);
        subscribeStudentToTopic(branch);
        Log.i(TAG, "onTokenRefresh completed with token: " + token);
    }

    private void subscribeStudentToTopic(String branch) {
        switch(branch)
        {
            case "Comp":
            {
                FirebaseMessaging.getInstance().subscribeToTopic("Comp");
                break;
            }

            case "Mech":
            {
                FirebaseMessaging.getInstance().subscribeToTopic("Mech");
                break;
            }

            case "Civil":
            {
                FirebaseMessaging.getInstance().subscribeToTopic("Civil");
                break;
            }

            case "MechSandwich":
            {
                FirebaseMessaging.getInstance().subscribeToTopic("MechSandwich");
                break;
            }
        }
    }
}


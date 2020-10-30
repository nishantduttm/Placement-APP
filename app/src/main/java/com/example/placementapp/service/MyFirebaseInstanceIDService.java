//package com.example.placementapp.service;
//
//import android.util.Log;
//
//import com.google.firebase.messaging.FirebaseMessaging;
//import com.google.firebase.messaging.FirebaseMessagingService;
//
//import androidx.annotation.NonNull;
//
//public class MyFirebaseInstanceIDService extends FirebaseMessagingService {
//
//    private static final String TAG = "mFirebaseIIDService";
//    private static final String SUBSCRIBE_TO = "Students";
//
//    @Override
//    public void onNewToken(@NonNull String token) {
//        super.onNewToken(token);
//        FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_TO);
//        Log.i(TAG, "onTokenRefresh completed with token: " + token);
//    }
//}

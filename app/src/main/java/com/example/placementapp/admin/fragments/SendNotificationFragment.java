package com.example.placementapp.admin.fragments;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.placementapp.Animation.MyBounceInterpolator;
import com.example.placementapp.R;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.placementapp.admin.helper.MySingleton;
import com.example.placementapp.constants.Constants;
import com.example.placementapp.helper.FirebaseHelper;
import com.example.placementapp.pojo.Notification;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SendNotificationFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {

    private EditText companyName;
    private RadioButton radioButton;
    private RadioGroup radioGroup;
    private EditText message;
    private Button sendNotificationButton;
    private DatabaseReference databaseReference;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAcbbeSbc:APA91bEO3kDXcK8khLywCXAMuV7516Fttdp5re70s80NzaZrQ-F6QUIxUemg87qZCrAc3nX7f9hitS8uA69dMvW6MfC7AhyQDLnaxC9tUGJBg61aCeaHuyjAvbIy5N2Kg0LRMx87VPOp";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;

    public SendNotificationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_send_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        companyName = view.findViewById(R.id.companyName);
        message = view.findViewById(R.id.message);

        if (message != null) {
            message.setVerticalScrollBarEnabled(true);
            message.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
            message.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
            message.setMovementMethod(ScrollingMovementMethod.getInstance());

            message.setOnTouchListener(this);
        }

        radioGroup = view.findViewById(R.id.radioGroup);
        sendNotificationButton = view.findViewById(R.id.sendNotificationButton);
        sendNotificationButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Animation myAnim = AnimationUtils.loadAnimation(this.getContext(), R.anim.bounce_animation);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.05, 5);
        myAnim.setInterpolator(interpolator);
        v.startAnimation(myAnim);

        String companyNamevalue = companyName.getText().toString();
        String messagevalue = message.getText().toString();
        if (companyNamevalue.length() == 0)
            companyName.setError("This field cannot be empty");
        if (messagevalue.length() == 0)
            message.setError("This field cannot be empty");
        if(radioGroup.getCheckedRadioButtonId() == -1)
            Toast.makeText(this.getContext(), "Please SELECT A BRANCH!!", Toast.LENGTH_SHORT).show();

        if (companyNamevalue.length() != 0 && messagevalue.length() != 0 && radioGroup.getCheckedRadioButtonId() != -1) {

            Long time;
            String timestamp;

            int selectedId = radioGroup.getCheckedRadioButtonId();
            radioButton = this.getView().findViewById(selectedId);
            TOPIC = "/topics/" + radioButton.getText();

            time = System.currentTimeMillis();

            Notification notif = new Notification(companyNamevalue,messagevalue,radioButton.getText().toString());
            databaseReference = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_NOTIFICATIONS + "/" + time);
            databaseReference.setValue(notif);

            NOTIFICATION_TITLE = companyNamevalue;
            NOTIFICATION_MESSAGE = messagevalue;

            JSONObject notification = new JSONObject();
            JSONObject notifcationBody = new JSONObject();
            try {
                notifcationBody.put("title", NOTIFICATION_TITLE);
                notifcationBody.put("message", NOTIFICATION_MESSAGE);

                notification.put("to", TOPIC);
                notification.put("data", notifcationBody);
            } catch (JSONException e) {
                Log.e(TAG, "onCreate: " + e.getMessage());
            }
            sendNotification(notification);
        }
    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getContext(), "Notification Sent Successfully", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onResponse: " + response.toString());
                        companyName.setText("");
                        message.setText("");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Request error", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };

        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        v.getParent().requestDisallowInterceptTouchEvent(true);
        if ((event.getAction() & MotionEvent.ACTION_UP) != 0 && (event.getActionMasked() & MotionEvent.ACTION_UP) != 0) {
            v.getParent().requestDisallowInterceptTouchEvent(false);
        }
        return false;
    }
}







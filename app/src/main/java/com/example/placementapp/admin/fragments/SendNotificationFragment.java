package com.example.placementapp.admin.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.placementapp.Animation.MyBounceInterpolator;
import com.example.placementapp.R;
import com.example.placementapp.admin.helper.MySingleton;
import com.example.placementapp.constants.Constants;
import com.example.placementapp.helper.FirebaseHelper;
import com.example.placementapp.pojo.Notification;
import com.example.placementapp.pojo.NotificationDto;
import com.example.placementapp.pojo.UserDto;
import com.example.placementapp.utils.HttpUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SendNotificationFragment extends Fragment implements View.OnClickListener, View.OnTouchListener {

    private EditText companyName;
    private RadioGroup radioGroup;
    private EditText salary;
    private EditText venue;
    private EditText companyDescription;

    private ProgressDialog loadingbar;

    private NotificationDto notificationDto;

    private TextView date;

    private boolean isNotificationDataSaved = false;
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
        salary = view.findViewById(R.id.packageEditText);
        venue = view.findViewById(R.id.venue);
        companyDescription = view.findViewById(R.id.companyDescription);
        date = view.findViewById(R.id.dateView);

        //Intializing Date TextView
        date.setOnClickListener(this::dateDialogInitializer);

        if (venue != null) {
            venue.setVerticalScrollBarEnabled(true);
            venue.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
            venue.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
            venue.setMovementMethod(ScrollingMovementMethod.getInstance());

            venue.setOnTouchListener(this);
        }

        if (companyDescription != null) {
            companyDescription.setVerticalScrollBarEnabled(true);
            companyDescription.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
            companyDescription.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
            companyDescription.setMovementMethod(ScrollingMovementMethod.getInstance());

            companyDescription.setOnTouchListener(this);
        }

        radioGroup = view.findViewById(R.id.radioGroup);
        Button sendNotificationButton = view.findViewById(R.id.sendNotificationButton);
        sendNotificationButton.setOnClickListener(this);
    }

    //Date Setter
    private void dateDialogInitializer(View view) {
        Calendar calendar = Calendar.getInstance();

        int presentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int presentMonth = calendar.get(Calendar.MONTH);
        int presentYear = calendar.get(Calendar.YEAR);


        DatePickerDialog datePicker = new DatePickerDialog(view.getContext(), (view1, year, monthOfYear, dayOfMonth) -> {
            date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
        }, presentYear, presentMonth, presentDay);
        datePicker.show();
    }


    @Override
    public void onClick(View v) {
        Animation myAnim = AnimationUtils.loadAnimation(this.getContext(), R.anim.bounce_animation);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.05, 5);
        myAnim.setInterpolator(interpolator);
        v.startAnimation(myAnim);

        loadingbar = new ProgressDialog(getContext());
        loadingbar.setTitle("Sending Notification");
        loadingbar.setMessage("Please wait while we send your notification");
        loadingbar.setCanceledOnTouchOutside(true);
        loadingbar.show();

        String companyNamevalue = companyName.getText().toString();
        String venueValue = venue.getText().toString();
        String salaryValue = salary.getText().toString();
        String dateValue = date.getText().toString();
        String companyDescriptionValue = companyDescription.getText().toString();


        if (isInputValid(companyNamevalue, venueValue, salaryValue, dateValue, companyDescriptionValue)) {

            RadioButton radioButton = this.getView().findViewById(radioGroup.getCheckedRadioButtonId());
            TOPIC = "/topics/" + radioButton.getText();

            notificationDto = new NotificationDto(companyNamevalue, radioButton.getText().toString(), dateValue, salaryValue, venueValue, companyDescriptionValue);

            persistInDatabase(notificationDto);
        }
    }

    private boolean isInputValid(String companyNameValue, String venueValue, String salaryValue, String dateValue, String companyDescriptionValue) {
        boolean isValid = true;
        if (companyNameValue.isEmpty()) {
            companyName.setError("This field cannot be empty");
            isValid = false;
        }
        if (venueValue.isEmpty()) {
            venue.setError("This field cannot be empty");
            isValid = false;
        }
        if (salaryValue.isEmpty()) {
            salary.setError("This field cannot be empty");
            isValid = false;
        }
        if (dateValue.isEmpty()) {
            date.setError("This field cannot be empty");
            isValid = false;
        }
        if (companyDescriptionValue.isEmpty()) {
            companyDescription.setError("This field cannot be empty");
            isValid = false;
        }
        if (radioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this.getContext(), "Please SELECT A BRANCH!!", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        return isValid;
    }


    private void persistInDatabase(NotificationDto notificationBean) {
        try {
//            String jsonString = new Gson().toJson(notificationBean, NotificationDto.class);
            JSONObject requestObject = new JSONObject();
            requestObject.put("companyName",notificationBean.getCompanyName());
            requestObject.put("companyBranch",notificationBean.getCompanyBranch());
            requestObject.put("date",notificationBean.getDate());
            requestObject.put("companyPackage",notificationBean.getCompanyPackage());
            requestObject.put("companyVenue",notificationBean.getCompanyVenue());
            requestObject.put("companyDescription",notificationBean.getCompanyDescription());

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    Constants.HttpConstants.SAVE_NOTIFICATION_URL,
                    requestObject,
                    this::parseResponse,
                    error -> {
                        Log.e("Error", "Http Call Error");
                    }
            );
            HttpUtils.addRequestToHttpQueue(request,getContext());
        } catch (JSONException e) {
            Log.e("Error", ""+e);
        }
    }


    private void parseResponse(JSONObject resp) {
        if (resp == null) {
            return;
        }
        try {

            if(resp.getString("statusCode").equals(Constants.HttpConstants.SUCCESS)) {

                NOTIFICATION_TITLE = notificationDto.getCompanyName();
                NOTIFICATION_MESSAGE = "Placement Drive Scheduled";

                JSONObject notification = new JSONObject();
                JSONObject notificationBody = new JSONObject();
                try {
                    notificationBody.put("title", NOTIFICATION_TITLE);
                    notificationBody.put("message", NOTIFICATION_MESSAGE);

                    notification.put("to", TOPIC);
                    notification.put("data", notificationBody);
                } catch (JSONException e) {
                    Log.e(TAG, "onCreate: " + e.getMessage());
                }
                sendNotification(notification);
                loadingbar.dismiss();
            }
            else {
                Toast.makeText(getContext(), "Notification Could not be saved", Toast.LENGTH_SHORT).show();
                loadingbar.dismiss();
            }

        } catch (Exception e) {
            Log.e("Notification Status", "Notification Could not be saved");
            loadingbar.dismiss();
        }
    }

    private void sendNotification(JSONObject notification) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constants.FirebaseConstants.FCM_API, notification,
                response -> {
                    Toast.makeText(getContext(), "Notification Sent Successfully", Toast.LENGTH_SHORT).show();
                    companyName.setText("");
                    venue.setText("");
                    companyDescription.setText("");
                    salary.setText("");
                    date.setText("");
                },
                error -> Toast.makeText(getContext(), "Request error", Toast.LENGTH_LONG).show()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", Constants.FirebaseConstants.FIREBASE_SERVER_KEY);
                params.put("Content-Type", contentType);
                return params;
            }
        };

        HttpUtils.addRequestToHttpQueue(jsonObjectRequest,getContext());
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

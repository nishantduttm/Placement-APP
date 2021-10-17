package com.example.placementapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.placementapp.R;
import com.example.placementapp.admin.fragments.ViewStudentsList;
import com.example.placementapp.constants.Constants;
import com.example.placementapp.helper.SharedPrefHelper;
import com.example.placementapp.pojo.NotificationDto;
import com.example.placementapp.student.StudentApplicationStatusActivity;

public class CompanyPopUpActivity extends Activity implements View.OnClickListener {

    private NotificationDto notification;
    private TextView companyName;
    private TextView venue;
    private TextView eligibility;
    private TextView date;
    private TextView salary;
    private Button applicationButton;

    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_popup);

        notification = (NotificationDto) getIntent().getSerializableExtra("details");

        userType = SharedPrefHelper.getEntryfromSharedPreferences(this.getBaseContext(), Constants.SharedPrefConstants.KEY_TYPE);

        initializeValues(); //Initialize Views

        if(userType.equals(Constants.UserTypes.ADMIN))
            applicationButton.setText("Check Applications");
        else
            applicationButton.setText("Fill Application");

        setValues(); //Set Values

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.82));

        applicationButton.setOnClickListener(this);
    }

    private void setValues() {
        companyName.setText(notification.getCompanyName());
        venue.setText(notification.getCompanyVenue());
        eligibility.setText(notification.getCompanyDescription());
        date.setText(notification.getDate());
        salary.setText(notification.getCompanyPackage());
    }

    private void initializeValues() {
        companyName = findViewById(R.id.companyNameHeading);
        venue = findViewById(R.id.companyVenueView);
        eligibility = findViewById(R.id.companyEligibilityView);
        date = findViewById(R.id.companyDateView);
        salary = findViewById(R.id.salaryView);
        applicationButton = findViewById(R.id.ApplicationsButton);
    }

    @Override
    public void onClick(View view) {

        if(userType.equals(Constants.UserTypes.ADMIN))
        {
            Intent intent = new Intent(view.getContext(), ViewStudentsList.class);
            intent.putExtra("companyID", notification.getNotificationId());
            view.getContext().startActivity(intent);
        }
        else
        {
            Intent intent = new Intent(view.getContext(), StudentApplicationStatusActivity.class);
            intent.putExtra("companyName", companyName.getText().toString());
            intent.putExtra("companyID", notification.getNotificationId());
            view.getContext().startActivity(intent);
        }
    }
}
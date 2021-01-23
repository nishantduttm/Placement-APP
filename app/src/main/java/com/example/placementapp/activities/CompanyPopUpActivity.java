package com.example.placementapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import com.example.placementapp.R;
import com.example.placementapp.pojo.Notification;

public class CompanyPopUpActivity extends Activity {

    private Notification notification;
    private TextView companyName;
    private TextView venue;
    private TextView eligibility;
    private TextView date;
    private TextView salary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_popup);

        notification = (Notification) getIntent().getSerializableExtra("details");

        initializeValues(); //Initialize Views
        setValues(); //Set Values

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.82));
    }

    private void setValues() {
        companyName.setText(notification.getCompanyName());
        venue.setText(notification.getVenue());
        eligibility.setText(notification.getEligibility());
        date.setText(notification.getDate());
        salary.setText(notification.getSalary());
    }

    private void initializeValues() {
        companyName = findViewById(R.id.companyNameHeading);
        venue = findViewById(R.id.companyVenueView);
        eligibility = findViewById(R.id.companyEligibilityView);
        date = findViewById(R.id.companyDateView);
        salary = findViewById(R.id.salaryView);
    }

}
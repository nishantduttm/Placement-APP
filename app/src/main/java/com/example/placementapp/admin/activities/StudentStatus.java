package com.example.placementapp.admin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.placementapp.Adapters.RecyclerViewAdapterProcessRound;
import com.example.placementapp.R;
import com.example.placementapp.constants.Constants;
import com.example.placementapp.helper.SharedPrefHelper;
import com.example.placementapp.pojo.ApplicationForm;
import com.example.placementapp.pojo.FormStatus;

import java.util.List;

public class StudentStatus extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewAdapterProcessRound recyclerViewAdapterProcessRound;
    private ApplicationForm a;
    private String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_status);
        userType = SharedPrefHelper.getEntryfromSharedPreferences(this.getBaseContext(), Constants.SharedPrefConstants.KEY_TYPE);
        a = (ApplicationForm)getIntent().getExtras().getParcelable("details");
        initializeviews();
    }

    void initializeviews() {
        if(a != null) {
            TextView emailAddress = findViewById(R.id.student_email_address);
            TextView prn = findViewById(R.id.student_prn);
            TextView studentName = findViewById(R.id.student_name);
            TextView studentBranch = findViewById(R.id.student_branch);
            TextView companyName = findViewById(R.id.comapany_name);
            Button appplicationStatusButton = findViewById(R.id.ApplicationStausButton);
            recyclerView = findViewById(R.id.recycler_view);
            emailAddress.setText(a.getStudentMailID());
            prn.setText(a.getStudentPRN());
            studentName.setText(a.getStudentName());
            studentBranch.setText(a.getStudentBranch());
            companyName.setText(a.getCompanyName());
            recyclerView.setVisibility(View.GONE);
            Log.i("a",a.getFormStatusList().get(0).processDate);
            recyclerViewAdapterProcessRound = new RecyclerViewAdapterProcessRound(a.getFormStatusList(),this);
            RecyclerView.LayoutManager manager = new GridLayoutManager(this, 1);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(recyclerViewAdapterProcessRound);
            if(userType.equals(Constants.UserTypes.ADMIN))
                appplicationStatusButton.setText("Ok");
            else
                appplicationStatusButton.setText("Edit Application");
        }
    }
}
package com.example.placementapp.admin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.placementapp.Adapters.RecyclerViewAdapterProcessRound;
import com.example.placementapp.R;
import com.example.placementapp.activities.CompanyPopUpActivity;
import com.example.placementapp.admin.fragments.ViewStudentsList;
import com.example.placementapp.constants.Constants;
import com.example.placementapp.helper.FirebaseHelper;
import com.example.placementapp.helper.SharedPrefHelper;
import com.example.placementapp.pojo.ApplicationForm;
import com.example.placementapp.pojo.FormStatus;
import com.example.placementapp.pojo.Statistics;
import com.example.placementapp.pojo.StudentApplicationDto;
import com.example.placementapp.student.StudentApplicationStatusActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StudentStatus extends AppCompatActivity implements ValueEventListener {
    private RecyclerView recyclerView;
    private RecyclerViewAdapterProcessRound recyclerViewAdapterProcessRound;
    private StudentApplicationDto studentStatusForm;
    private String userType;

    private ProgressDialog loadingbar;

    List<FormStatus> formStatusList = new ArrayList<>();

    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_status);

        loadingbar = new ProgressDialog(StudentStatus.this);
        loadingbar.setTitle("Fetching questions");
        loadingbar.setMessage("Please wait while we update your feed");
        loadingbar.setCanceledOnTouchOutside(true);
        loadingbar.show();

        userType = SharedPrefHelper.getEntryfromSharedPreferences(this.getBaseContext(), Constants.SharedPrefConstants.KEY_TYPE);
        studentStatusForm = (StudentApplicationDto)getIntent().getExtras().getSerializable("details");

        ref = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_APPLICATIONS + "/" + studentStatusForm.getApplicationId());
        ref.addListenerForSingleValueEvent(this);

        initializeviews();
    }

    void initializeviews() {
        if(studentStatusForm != null) {
            TextView emailAddress = findViewById(R.id.student_email_address);
            TextView prn = findViewById(R.id.student_prn);
            TextView studentName = findViewById(R.id.student_name);
            TextView studentBranch = findViewById(R.id.student_branch);
            TextView companyName = findViewById(R.id.student_status_company_name);
            Button appplicationStatusButton = findViewById(R.id.ApplicationStatusButton);

            recyclerView = findViewById(R.id.recycler_view);
            emailAddress.setText(studentStatusForm.getEmail());
            prn.setText(studentStatusForm.getPrn());
            studentName.setText(studentStatusForm.getName());
            studentBranch.setText(studentStatusForm.getBranch());
            companyName.setText(studentStatusForm.getCompanyName());

            recyclerViewAdapterProcessRound = new RecyclerViewAdapterProcessRound(formStatusList);
            RecyclerView.LayoutManager manager = new GridLayoutManager(this, 1);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(recyclerViewAdapterProcessRound);

            if(userType.equals(Constants.UserTypes.ADMIN)) {
                appplicationStatusButton.setText("Ok");
                appplicationStatusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), ViewStudentsList.class);
                        intent.putExtra("companyID", studentStatusForm.getNotificationId());
                        v.getContext().startActivity(intent);
                    }
                });
            }
            else {
                appplicationStatusButton.setText("Edit Application");
                appplicationStatusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), StudentApplicationStatusActivity.class);
                        intent.putExtra("companyName",studentStatusForm.getCompanyName());
                        intent.putExtra("companyID", studentStatusForm.getNotificationId());
                        v.getContext().startActivity(intent);
                    }
                });
            }
        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        formStatusList.clear();

        if(snapshot!=null)
        {
            for(DataSnapshot childSnapshot : snapshot.getChildren())
            {
                if(childSnapshot!=null)
                    formStatusList.add(childSnapshot.getValue(FormStatus.class));
            }
            recyclerViewAdapterProcessRound.notifyDataSetChanged();
        }
        loadingbar.dismiss();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Toast.makeText(StudentStatus.this, "Sorry. Couldn't fetch form status", Toast.LENGTH_SHORT).show();
        loadingbar.dismiss();
    }
}
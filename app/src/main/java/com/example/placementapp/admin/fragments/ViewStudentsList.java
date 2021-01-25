package com.example.placementapp.admin.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.placementapp.Adapters.RecyclerViewAdapterViewNotifcation;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.placementapp.Adapters.RecyclerViewAdapterViewStudents;
import com.example.placementapp.R;
import com.example.placementapp.constants.Constants;
import com.example.placementapp.helper.FirebaseHelper;
import com.example.placementapp.helper.SharedPrefHelper;
import com.example.placementapp.pojo.ApplicationForm;
import com.example.placementapp.pojo.Notification;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewStudentsList extends Activity implements ValueEventListener, AdapterView.OnItemSelectedListener {

    private DatabaseReference ref;
    private RecyclerView recyclerView;
    private RecyclerViewAdapterViewStudents studentsAdapter;
    private List<ApplicationForm> studentList;
    private ProgressBar progressBar;

    private TextView selectStatusView;
    private String status;

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public ViewStudentsList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        studentList = new ArrayList<>();

        setContentView(R.layout.activity_student_list);

        selectStatusView = findViewById(R.id.selectStatusView);

        String companyID = getIntent().getStringExtra("companyID");

        ref = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_APPLICATIONS + companyID);
        ref.addListenerForSingleValueEvent(this);

        selectStatusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                LayoutInflater inflater = getLayoutInflater();
                View v = (inflater.inflate(R.layout.spinner_dialog, null));
                builder.setTitle("Select Student Placement Status");
                Spinner spinner = v.findViewById(R.id.spinner);

                List<String> list = new ArrayList<>();
                list.add("Choose Status");
                list.add("Placed");
                list.add("Not Placed");
                list.add("In Process");
                list.add("Hold");

                ArrayAdapter<String> adapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (spinner.getSelectedItemId() != 0) {
                            selectStatusView.setText(spinner.getSelectedItem().toString());
                            status = spinner.getSelectedItem().toString();
                        }
                        dialogInterface.dismiss();
                        ref.addListenerForSingleValueEvent(ViewStudentsList.this);
                    }
                });

                builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.setView(v);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        recyclerView = findViewById(R.id.studentListRecyclerView);
        recyclerView.setVisibility(View.GONE);
        progressBar = findViewById(R.id.studentListProgressBar);
        progressBar.setVisibility(View.VISIBLE);
        studentsAdapter =new RecyclerViewAdapterViewStudents(studentList,this);
        RecyclerView.LayoutManager manager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(studentsAdapter);
    }


    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        studentList.clear();

        GenericTypeIndicator<Map<String, ApplicationForm>> t = new GenericTypeIndicator<Map<String, ApplicationForm>>() {
        };
        Map<String, ApplicationForm> students = snapshot.getValue(t);

        if (students == null)
            Toast.makeText(this, "No Applicants Yet! Stay Tuned!", Toast.LENGTH_SHORT).show();
        else {

            for (Map.Entry<String, ApplicationForm> entry : students.entrySet()) {
                ApplicationForm applicationForm = entry.getValue();

                if (applicationForm != null) {

                    if (status != null) {
                        if (applicationForm.getOverallStatus().equals(status))
                            studentList.add(new ApplicationForm(applicationForm.getStudentMailID(),applicationForm.getStudentPRN(),applicationForm.getStudentName(),applicationForm.getStudentBranch(),applicationForm.getCompanyName(),applicationForm.getCompanyId(),applicationForm.getFormStatusList(),applicationForm.getOverallStatus()));
                    } else
                        studentList.add(new ApplicationForm(applicationForm.getStudentMailID(),applicationForm.getStudentPRN(),applicationForm.getStudentName(),applicationForm.getStudentBranch(),applicationForm.getCompanyName(),applicationForm.getCompanyId(),applicationForm.getFormStatusList(),applicationForm.getOverallStatus()));
                }
            }
        }

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        if (recyclerView != null && studentsAdapter != null) {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(studentsAdapter);
            studentsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        status = adapterView.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
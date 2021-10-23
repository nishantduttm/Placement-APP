package com.example.placementapp.admin.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import com.example.placementapp.admin.activities.StatisticsActivity;
import com.example.placementapp.constants.Constants;
import com.example.placementapp.helper.SharedPrefHelper;
import com.example.placementapp.pojo.ApplicationForm;
import com.example.placementapp.pojo.Statistics;
import com.example.placementapp.pojo.StudentApplicationDto;
import com.example.placementapp.pojo.StudentApplicationList;
import com.example.placementapp.utils.HttpUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewStudentsList extends Activity implements AdapterView.OnItemSelectedListener {

    private DatabaseReference ref;
    private RecyclerView recyclerView;
    private RecyclerViewAdapterViewStudents studentsAdapter;
    private List<StudentApplicationDto> studentList;
    private ProgressDialog loadingbar;
    private FloatingActionButton floatingActionButton;



    private Statistics statistics;

    private boolean visited = false;
    private int placedCount = 0;
    private int notPlacedCount = 0;
    private int inProcessCount = 0;
    private int onHoldCount = 0;

    private int companyID;

    private TextView selectStatusView;
    private String status = "All Students";

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

        loadingbar = new ProgressDialog(ViewStudentsList.this);

        setContentView(R.layout.activity_student_list);

        selectStatusView = findViewById(R.id.selectStatusView);

        companyID = getIntent().getIntExtra("companyID", -1);


//        ref = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_APPLICATIONS + companyID);
//        ref.addListenerForSingleValueEvent(this);

        getApplicationsFromDb();

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
                list.add("All Students");
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
                        if (recyclerView != null) {
                            studentsAdapter = new RecyclerViewAdapterViewStudents(studentList,ViewStudentsList.this,status);
                            recyclerView.setVisibility(View.VISIBLE);
                            recyclerView.setAdapter(studentsAdapter);
                            studentsAdapter.notifyDataSetChanged();
                        }
                        dialogInterface.dismiss();
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
        floatingActionButton = findViewById(R.id.floatingActionButton);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && floatingActionButton.getVisibility() == View.VISIBLE) {
                    floatingActionButton.hide();
                } else if (dy < 0 && floatingActionButton.getVisibility() != View.VISIBLE) {
                    floatingActionButton.show();
                }
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateStatistics(studentList);
                Intent intent = new Intent(view.getContext(), StatisticsActivity.class);
                view.getContext().startActivity(intent);
            }
        });
        recyclerView.setVisibility(View.GONE);

        studentsAdapter = new RecyclerViewAdapterViewStudents(studentList, this, status);
        RecyclerView.LayoutManager manager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(studentsAdapter);
    }

    private void getApplicationsFromDb() {

        loadingbar.setTitle("Fetching Students");
        loadingbar.setMessage("Please wait while we update your list");
        loadingbar.setCanceledOnTouchOutside(true);
        loadingbar.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                Constants.HttpConstants.GET_APPLICATIONS_URL + companyID,
                null,
                this::parseResponse,
                error -> {
                    Log.i("Error", "JSON OBJECT REQUEST ERROR");
                    loadingbar.dismiss();
                }
        );

        HttpUtils.addRequestToHttpQueue(jsonObjectRequest, ViewStudentsList.this);

    }

    private void parseResponse(JSONObject jsonObject) {
        studentList.clear();

        StudentApplicationList studentApplicationList = new Gson().fromJson(jsonObject.toString(), StudentApplicationList.class);
        studentList.addAll(studentApplicationList.getStudentApplicationsDto());

        Log.i("StudentList", studentList.toString());

        if (studentList.isEmpty()) {
            Toast.makeText(this, "No Applicants Yet! Stay Tuned!", Toast.LENGTH_SHORT).show();
        }

        if (loadingbar != null) {
            loadingbar.dismiss();
        }

        if (recyclerView != null && studentsAdapter != null) {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(studentsAdapter);
            studentsAdapter.notifyDataSetChanged();
        }
    }


    public void generateStatistics(List<StudentApplicationDto> studentApplicationDtos) {

        for (StudentApplicationDto student : studentApplicationDtos) {

            if (student != null) {

                switch (student.getOverallStatus()) {
                    case "In Process":
                        inProcessCount += 1;
                        break;
                    case "Placed":
                        placedCount += 1;
                        break;
                    case "Not Placed":
                        notPlacedCount += 1;
                        break;
                    case "Hold":
                        onHoldCount += 1;
                        break;
                }
            }
        }
        statistics = new Statistics(placedCount, notPlacedCount, inProcessCount, onHoldCount);
        SharedPrefHelper.saveObjectEntryinSharedPreferences(this.getApplicationContext(), "stat", statistics);
//                Intent intent = new Intent("stat_intent").putExtra("statistics",statistics);
//                LocalBroadcastManager.getInstance(this.getParent().getBaseContext()).sendBroadcast(intent);
        Log.i("placedCount", String.valueOf(statistics.getPlacedCount()));
        Log.i("notPlacedCount", String.valueOf(statistics.getNotPlacedCount()));
        Log.i("inProcessCount", String.valueOf(statistics.getInProcessCount()));
        Log.i("onHoldCount", String.valueOf(statistics.getOnHoldCount()));
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        status = adapterView.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
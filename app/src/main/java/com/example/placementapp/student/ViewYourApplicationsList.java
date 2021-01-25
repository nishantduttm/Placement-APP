package com.example.placementapp.student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.placementapp.Adapters.RecyclerViewAdapterViewNotifcation;
import com.example.placementapp.Adapters.RecyclerViewAdapterViewYourApplications;
import com.example.placementapp.R;
import com.example.placementapp.constants.Constants;
import com.example.placementapp.helper.FirebaseHelper;
import com.example.placementapp.helper.SharedPrefHelper;
import com.example.placementapp.pojo.ApplicationForm;
import com.example.placementapp.pojo.Notification;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ViewYourApplicationsList extends Fragment implements ValueEventListener {

    private DatabaseReference ref;
    private RecyclerView recyclerView;
    private RecyclerViewAdapterViewYourApplications applicationsAdapter;
    private List<ApplicationForm> applicationsList;
    private ProgressBar progressBar;

    private String userPRN;


    public ViewYourApplicationsList() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationsList = new ArrayList<>();
        userPRN = SharedPrefHelper.getEntryfromSharedPreferences(this.getContext(), Constants.SharedPrefConstants.KEY_PRN);
        ref = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_APPILED_COMPANIES + userPRN);
        ref.addListenerForSingleValueEvent(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_applied_companies_list,container,false);

        recyclerView = v.findViewById(R.id.your_applications_recycler_view);
        recyclerView.setVisibility(View.GONE);
        progressBar = v.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        applicationsAdapter = new RecyclerViewAdapterViewYourApplications(applicationsList,this);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(applicationsAdapter);
        return v;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {

        for(DataSnapshot childsnapshot : snapshot.getChildren())
        {
            ApplicationForm applicationForm = childsnapshot.getValue(ApplicationForm.class);
            if(applicationForm!=null) {
                    applicationsList.add(new ApplicationForm(applicationForm.getStudentMailID(),applicationForm.getStudentPRN(),applicationForm.getStudentName(),applicationForm.getStudentBranch(),applicationForm.getCompanyName(),applicationForm.getCompanyId(),applicationForm.getFormStatusList(),applicationForm.getOverallStatus()));
                }
            else
                Toast.makeText(this.getContext(), "No Applied Applications!", Toast.LENGTH_SHORT).show();
        }

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        if (recyclerView != null && applicationsAdapter != null) {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(applicationsAdapter);
            applicationsAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}

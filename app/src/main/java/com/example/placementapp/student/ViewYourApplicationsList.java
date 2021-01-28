package com.example.placementapp.student;

import android.os.AsyncTask;
import android.os.Build;
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
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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

        SwipeRefreshLayout applicationRefresh = v.findViewById(R.id.applicationsRefresh);
        applicationRefresh.setColorSchemeResources(R.color.colorAccent);


        recyclerView = v.findViewById(R.id.your_applications_recycler_view);
        recyclerView.setVisibility(View.GONE);
        progressBar = v.findViewById(R.id.progress_bar);
        applicationsAdapter = new RecyclerViewAdapterViewYourApplications(applicationsList,this);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(applicationsAdapter);

        applicationRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ref = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_APPILED_COMPANIES + userPRN);
                ref.addListenerForSingleValueEvent(ViewYourApplicationsList.this);
                applicationRefresh.setRefreshing(false);
            }
        });
        return v;
    }

    public class ApplicationThreadFetcher extends AsyncTask<DataSnapshot,String,Integer>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected Integer doInBackground(DataSnapshot... snapshots) {
            DataSnapshot snapshot = snapshots[0];
            applicationsList.clear();
            Map<String,ApplicationForm> applications = snapshot.getValue(new GenericTypeIndicator<Map<String, ApplicationForm>>() {
            });

            if(applications == null)
                showToast("No Applications Yet!");
            else
            {
                for(Map.Entry<String,ApplicationForm> entry : applications.entrySet())
                {
                    ApplicationForm applicationForm = entry.getValue();
                    if(applicationForm!=null) {
                        applicationsList.add(new ApplicationForm(applicationForm.getStudentMailID(),applicationForm.getStudentPRN(),applicationForm.getStudentName(),applicationForm.getStudentBranch(),applicationForm.getCompanyName(),applicationForm.getCompanyId(),applicationForm.getFormStatusList(),applicationForm.getOverallStatus()));
                    }
                    else
                        showToast("No Applied Applications!");
                }
                applicationsList.sort((a, b) -> {
                    return (int) (Long.parseLong(b.getCompanyId()) - Long.parseLong(a.getCompanyId()));
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            progressBar.setVisibility(View.GONE);
            if (recyclerView != null && applicationsAdapter != null) {
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(applicationsAdapter);
                applicationsAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        new ApplicationThreadFetcher().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,snapshot);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    private void showToast(String toastMsg) {
        if (this.getActivity() != null) {
            this.getActivity().runOnUiThread(() -> Toast.makeText(this.getContext(), toastMsg, Toast.LENGTH_SHORT).show());
        }
    }
}

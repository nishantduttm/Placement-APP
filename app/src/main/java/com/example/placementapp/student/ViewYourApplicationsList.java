package com.example.placementapp.student;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.placementapp.Adapters.RecyclerViewAdapterViewNotifcation;
import com.example.placementapp.Adapters.RecyclerViewAdapterViewYourApplications;
import com.example.placementapp.R;
import com.example.placementapp.admin.fragments.ViewStudentsList;
import com.example.placementapp.constants.Constants;
import com.example.placementapp.helper.FirebaseHelper;
import com.example.placementapp.helper.SharedPrefHelper;
import com.example.placementapp.pojo.ApplicationForm;
import com.example.placementapp.pojo.Notification;
import com.example.placementapp.pojo.StudentApplicationDto;
import com.example.placementapp.pojo.StudentApplicationList;
import com.example.placementapp.utils.HttpUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Comparator;
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

import org.json.JSONObject;

public class ViewYourApplicationsList extends Fragment{

    private DatabaseReference ref;
    private RecyclerView recyclerView;
    private RecyclerViewAdapterViewYourApplications applicationsAdapter;
    private List<StudentApplicationDto> applicationsList;

    private ProgressDialog loadingbar;

    private String userPRN;


    public ViewYourApplicationsList() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationsList = new ArrayList<>();
        userPRN = SharedPrefHelper.getEntryfromSharedPreferences(this.getContext(), Constants.SharedPrefConstants.KEY_PRN);

        loadingbar = new ProgressDialog(getContext());
    }

    private void getApplicationsFromDb() {

        loadingbar.setTitle("Fetching your Applications");
        loadingbar.setMessage("Please wait while we update your list");
        loadingbar.setCanceledOnTouchOutside(true);
        loadingbar.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                Constants.HttpConstants.GET_MY_APPLICATIONS_URL + userPRN,
                null,
                this::parseResponse,
                error -> {
                    Log.i("Error", "JSON OBJECT REQUEST ERROR");
                    loadingbar.dismiss();
                }
        );

        HttpUtils.addRequestToHttpQueue(jsonObjectRequest, getContext());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void parseResponse(JSONObject jsonObject) {
        applicationsList.clear();

        StudentApplicationList studentApplicationList = new Gson().fromJson(jsonObject.toString(), StudentApplicationList.class);
        applicationsList.addAll(studentApplicationList.getStudentApplicationsDto());

        if(!applicationsList.isEmpty()) {
            applicationsList.sort(Comparator.comparing(StudentApplicationDto::getCompanyName));
        }

        Log.i("StudentList", applicationsList.toString());

        if (applicationsList.isEmpty()) {
            Toast.makeText(getContext(), "No Applications. Please fill Application first", Toast.LENGTH_SHORT).show();
        }

        if (loadingbar != null) {
            loadingbar.dismiss();
        }

        if (recyclerView != null && applicationsAdapter != null) {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(applicationsAdapter);
            applicationsAdapter.notifyDataSetChanged();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_applied_companies_list,container,false);

        SwipeRefreshLayout applicationRefresh = v.findViewById(R.id.applicationsRefresh);
        applicationRefresh.setColorSchemeResources(R.color.colorAccent);

        getApplicationsFromDb();


        recyclerView = v.findViewById(R.id.your_applications_recycler_view);
        recyclerView.setVisibility(View.GONE);
        applicationsAdapter = new RecyclerViewAdapterViewYourApplications(applicationsList,ViewYourApplicationsList.this);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(applicationsAdapter);

        applicationRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getApplicationsFromDb();
                applicationRefresh.setRefreshing(false);
            }
        });
        return v;
    }

}

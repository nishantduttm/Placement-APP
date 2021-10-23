package com.example.placementapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
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
import com.example.placementapp.activities.DashboardActivity;
import com.example.placementapp.admin.fragments.ViewStudentsList;
import com.example.placementapp.constants.Constants;
import com.example.placementapp.helper.FirebaseHelper;
import com.example.placementapp.helper.SharedPrefHelper;
import com.example.placementapp.pojo.ApplicationCountDto;
import com.example.placementapp.pojo.ApplicationForm;
import com.example.placementapp.pojo.Notification;
import com.example.placementapp.utils.HttpUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONObject;

public class PlacementDashboardFragment extends Fragment{

    private TextView appliedCompaniesView;
    private TextView inProcessCompaniesView;
    private TextView placedCompaniesView;
    private TextView notPlacedCompaniesView;
    private TextView onHoldCompaniesView;
    private ProgressBar dashboardProgressBar;
    private TextView nameTextView;

    String userPrn = null;
    
    private ProgressDialog loadingbar;

    public PlacementDashboardFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = getLayoutInflater().inflate(R.layout.fragment_placement_dashboard, container, false);
        nameTextView = v.findViewById(R.id.dashboardName);
        String name = SharedPrefHelper.getEntryfromSharedPreferences(this.getContext(), Constants.SharedPrefConstants.KEY_NAME);
        nameTextView.setText("Hi, "+name.toUpperCase().charAt(0) + name.substring(1).toLowerCase());
        appliedCompaniesView = v.findViewById(R.id.appliedCompaniesView);
        inProcessCompaniesView = v.findViewById(R.id.inProcessCompaniesView);
        placedCompaniesView = v.findViewById(R.id.placedCompaniesView);
        notPlacedCompaniesView = v.findViewById(R.id.notPlacedCompaniesView);
        onHoldCompaniesView = v.findViewById(R.id.onHoldCompaniesView);
        
        loadingbar = new ProgressDialog(getContext());

        userPrn = SharedPrefHelper.getEntryfromSharedPreferences(v.getContext(),Constants.SharedPrefConstants.KEY_PRN);

        getApplicationStatusCount();
        
        return v;
    }

    private void getApplicationStatusCount() {

        loadingbar.setTitle("Loading...");
        loadingbar.setMessage("Please wait while we update your dashboard");
        loadingbar.setCanceledOnTouchOutside(true);
        loadingbar.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                Constants.HttpConstants.GET_APPLICATION_COUNT_URL + userPrn,
                null,
                this::parseResponse,
                error -> {
                    Log.i("Error","JSON Error");
                    loadingbar.dismiss();
                }
        );

        HttpUtils.addRequestToHttpQueue(jsonObjectRequest,getContext());
    }

    private void parseResponse(JSONObject jsonObject) {
        ApplicationCountDto applicationCountDto = new Gson().fromJson(jsonObject.toString(),ApplicationCountDto.class);
        if(applicationCountDto != null) {

            int placedCount = applicationCountDto.getPlacedCount();
            int notPlacedCount = applicationCountDto.getNotPlacedCount();
            int inProcessCount = applicationCountDto.getInProcessCount();
            int onHoldCount = applicationCountDto.getOnHoldCount();

            int appliedCount = placedCount + notPlacedCount + inProcessCount + onHoldCount;

            if(appliedCount == 0) {
                Toast.makeText(getContext(), "No Applications Filled yet", Toast.LENGTH_SHORT).show();
            }

            appliedCompaniesView.setText(String.valueOf(appliedCount));
            inProcessCompaniesView.setText(String.valueOf(inProcessCount));
            placedCompaniesView.setText(String.valueOf(placedCount));
            notPlacedCompaniesView.setText(String.valueOf(notPlacedCount));
            onHoldCompaniesView.setText(String.valueOf(onHoldCount));

        }
        loadingbar.dismiss();
    }


}

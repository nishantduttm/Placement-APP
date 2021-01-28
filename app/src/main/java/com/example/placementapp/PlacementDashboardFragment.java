package com.example.placementapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.placementapp.activities.DashboardActivity;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PlacementDashboardFragment extends Fragment implements ValueEventListener {

    private DatabaseReference statusRef;

    private TextView appliedCompaniesView;
    private TextView inProcessCompaniesView;
    private TextView placedCompaniesView;
    private TextView notPlacedCompaniesView;
    private TextView onHoldCompaniesView;
    private ProgressBar dashboardProgressBar;
    private TextView nameTextView;
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
        dashboardProgressBar = v.findViewById(R.id.dashboardProgressBar);

        String userID = SharedPrefHelper.getEntryfromSharedPreferences(v.getContext(),Constants.SharedPrefConstants.KEY_PRN);

        statusRef = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_APPILED_COMPANIES + userID);
        statusRef.addValueEventListener(this);
        return v;
    }

    public class StatusDetailsThreadFetcher extends AsyncTask<DataSnapshot,String,List<Integer>>
    {
        @Override
        protected void onPreExecute() {
            dashboardProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Integer> doInBackground(DataSnapshot... snapshots) {
            DataSnapshot snapshot = snapshots[0];
            List<Integer> list = new ArrayList<>(Arrays.asList(0,0,0,0,0));
            int appliedCount = 0;
            int placedCount = 0;
            int notPlacedCount = 0;
            int inProcessCount = 0;
            int onHoldCount = 0;

            Map<String, ApplicationForm> forms = snapshot.getValue(new GenericTypeIndicator<Map<String, ApplicationForm>>() {
            });
            if(forms == null)
                showToast("No Applications Yet!");
            else
            {
                for (Map.Entry<String,ApplicationForm> entry : forms.entrySet())
                {
                    ApplicationForm form = entry.getValue();
                    if(form != null)
                    {
                        switch (form.getOverallStatus()) {
                            case "In Process":
                                inProcessCount +=1;
                                list.set(1, inProcessCount);
                                break;
                            case "Placed":
                                placedCount +=1;
                                list.set(2, placedCount);
                                break;
                            case "Not Placed":
                                notPlacedCount +=1;
                                list.set(3, notPlacedCount);
                                break;
                            case "Hold":
                                onHoldCount +=1;
                                list.set(4, onHoldCount);
                                break;
                        }
                        appliedCount +=1;
                        list.set(0, appliedCount);
                    }
                }
            }
            return list;
        }

        @Override
        protected void onPostExecute(List<Integer> resultList) {
            dashboardProgressBar.setVisibility(View.GONE);
            appliedCompaniesView.setText(String.valueOf(resultList.get(0)));
            inProcessCompaniesView.setText(String.valueOf(resultList.get(1)));
            placedCompaniesView.setText(String.valueOf(resultList.get(2)));
            notPlacedCompaniesView.setText(String.valueOf(resultList.get(3)));
            onHoldCompaniesView.setText(String.valueOf(resultList.get(4)));
        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        new StatusDetailsThreadFetcher().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,snapshot);
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

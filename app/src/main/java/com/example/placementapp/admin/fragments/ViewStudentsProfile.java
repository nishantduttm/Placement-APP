package com.example.placementapp.admin.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.placementapp.Adapters.RecyclerViewAdapterViewNotifcation;
import com.example.placementapp.Adapters.RecyclerViewAdapterViewStudents;
import com.example.placementapp.Adapters.RecyclerViewAdapterViewStudentsProfile;
import com.example.placementapp.R;
import com.example.placementapp.constants.Constants;
import com.example.placementapp.helper.FirebaseHelper;
import com.example.placementapp.helper.SharedPrefHelper;
import com.example.placementapp.pojo.ApplicationForm;
import com.example.placementapp.pojo.Notification;
import com.example.placementapp.pojo.NotificationsList;
import com.example.placementapp.pojo.StudentApplicationList;
import com.example.placementapp.pojo.StudentUser;
import com.example.placementapp.pojo.UserDto;
import com.example.placementapp.pojo.UserDtoList;
import com.example.placementapp.utils.HttpUtils;
import com.example.placementapp.utils.StringUtils;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.json.JSONObject;

public class ViewStudentsProfile extends Fragment implements AdapterView.OnItemSelectedListener {

    private static final List<String> list;
    private ProgressDialog loadingbar;

    static {
        list = new ArrayList<>();
        list.add("Choose a Stream");
        list.add("All");
        list.add("Comp");
        list.add("Mech");
        list.add("Civil");
        list.add("MechSandwich");
    }

    private static List<UserDto> studentsProfileList;

    private RecyclerView recyclerView;
    private RecyclerViewAdapterViewStudentsProfile profileAdapter;
    private SwipeRefreshLayout notificationRefresh;


    private TextView branchText;
    private String branch = "All";

    String userType;
    String userPRN;

    public ViewStudentsProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_view_students_profile, container, false);

        loadingbar = new ProgressDialog(v.getContext());

        studentsProfileList = new ArrayList<>();

        userType = SharedPrefHelper.getEntryfromSharedPreferences(v.getContext(), Constants.SharedPrefConstants.KEY_TYPE);

        recyclerView = v.findViewById(R.id.view_students_profile_recycler_view);
        recyclerView.setVisibility(View.GONE);

        if (userType.equals(Constants.UserTypes.ADMIN)) {
            initializeViewForAdmin(inflater, container, v);
        }

        return v;
    }

    private void initializeViewForAdmin(LayoutInflater inflater, ViewGroup container, View v) {
        branchText = v.findViewById(R.id.studentBranchText);
        TextView selectBranchText = v.findViewById(R.id.studentBranchTextView);
        selectBranchText.setVisibility(View.VISIBLE);
        branchText.setVisibility(View.VISIBLE);

        getStudentsFromDb();

        branchText.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            View v1 = inflater.inflate(R.layout.spinner_dialog, container, false);
            builder.setTitle("Select Branch to Filter");
            Spinner spinner = v1.findViewById(R.id.spinner);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(v1.getContext(), android.R.layout.simple_spinner_item, list);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            builder.setPositiveButton("Ok", (dialogInterface, i) -> {
                if (spinner.getSelectedItemId() != 0) {
                    branchText.setText(spinner.getSelectedItem().toString());
                    branch = spinner.getSelectedItem().toString();
                }
                if (recyclerView != null) {
                    profileAdapter = new RecyclerViewAdapterViewStudentsProfile(studentsProfileList,ViewStudentsProfile.this,branch);
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(profileAdapter);
                    profileAdapter.notifyDataSetChanged();
                }
                dialogInterface.dismiss();
            });

            builder.setNegativeButton("Dismiss", (dialogInterface, i) -> dialogInterface.dismiss());

            builder.setView(v1);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        recyclerView.setVisibility(View.GONE);

        profileAdapter = new RecyclerViewAdapterViewStudentsProfile(studentsProfileList,ViewStudentsProfile.this,branch);
        RecyclerView.LayoutManager manager = new GridLayoutManager(v.getContext(), 1);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(profileAdapter);
    }

    private void getStudentsFromDb() {
        loadingbar.setTitle("Fetching Students");
        loadingbar.setMessage("Please wait while we update your list");
        loadingbar.setCanceledOnTouchOutside(true);
        loadingbar.show();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                Constants.HttpConstants.GET_ALL_USERS_URL,
                null,
                this::parseResponse,
                error -> {
                    Log.i("Error", "JSON OBJECT REQUEST ERROR");
                    loadingbar.dismiss();
                }
        );

        HttpUtils.addRequestToHttpQueue(jsonObjectRequest, getContext());
    }

    private void parseResponse(JSONObject jsonObject) {
        studentsProfileList.clear();

        UserDtoList userDtoList = new Gson().fromJson(jsonObject.toString(), UserDtoList.class);
        studentsProfileList.addAll(userDtoList.getUsers());

        Log.i("ProfileList", studentsProfileList.toString());

        if (studentsProfileList.isEmpty()) {
            Toast.makeText(getContext(), "No Students Yet! Stay Tuned!", Toast.LENGTH_SHORT).show();
        }

        if (loadingbar != null) {
            loadingbar.dismiss();
        }

        if (recyclerView != null && profileAdapter != null) {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(profileAdapter);
            profileAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i == 0)
            branch = list.get(1);
        else
            branch = adapterView.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}
package com.example.placementapp.admin.fragments;

import android.app.AlertDialog;
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
import com.example.placementapp.Adapters.RecyclerViewAdapterViewStudentsProfile;
import com.example.placementapp.R;
import com.example.placementapp.constants.Constants;
import com.example.placementapp.helper.FirebaseHelper;
import com.example.placementapp.helper.SharedPrefHelper;
import com.example.placementapp.pojo.ApplicationForm;
import com.example.placementapp.pojo.Notification;
import com.example.placementapp.pojo.NotificationsList;
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
    private ProgressBar progressBar;
    private SwipeRefreshLayout notificationRefresh;

    private TextView branchText;
    private String branch = "ALL";

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

        studentsProfileList = new ArrayList<>();

        userType = SharedPrefHelper.getEntryfromSharedPreferences(v.getContext(), Constants.SharedPrefConstants.KEY_TYPE);

        if (userType.equals(Constants.UserTypes.ADMIN)) {
            initializeViewForAdmin(inflater, container, v);
        }

        recyclerView = v.findViewById(R.id.view_students_profile_recycler_view);
        recyclerView.setVisibility(View.GONE);
        progressBar = v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        profileAdapter = new RecyclerViewAdapterViewStudentsProfile(studentsProfileList, this, userType);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(profileAdapter);

        //getDataFromCache(ref);

        return v;
    }

    private void initializeViewForAdmin(LayoutInflater inflater, ViewGroup container, View v) {
        branchText = v.findViewById(R.id.studentBranchText);
        TextView selectBranchText = v.findViewById(R.id.studentBranchTextView);
        selectBranchText.setVisibility(View.VISIBLE);
        branchText.setVisibility(View.VISIBLE);
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
                dialogInterface.dismiss();
                //getDataFromCache(ref);
                new ViewStudentsProfile.ViewStudentsProfileTaskRunner().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Constants.HttpConstants.GET_USERS_URL, branch);
            });

            builder.setNegativeButton("Dismiss", (dialogInterface, i) -> dialogInterface.dismiss());

            builder.setView(v1);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
        new ViewStudentsProfile.ViewStudentsProfileTaskRunner().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Constants.HttpConstants.GET_USERS_URL, branch);
    }


    public class ViewStudentsProfileTaskRunner extends AsyncTask<String, String, List<UserDto>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected List<UserDto> doInBackground(String... urls) {
            String url = urls[0];
            String branch = urls[1];
            studentsProfileList.clear();
            // TODO make call to Web server
            if (StringUtils.isBlank(url) || StringUtils.isBlank(branch)) {
                Log.e("Error", "No Url/branch to make Http Call");
                showToast("No Students Yet! Stay Tuned!");
                return null;
            }
            HttpUtils.addRequestToHttpQueue(constructHttpRequest(url, branch), getContext());
            return null;
        }

        private Request<?> constructHttpRequest(String url, String branch) {
            url = url + branch;
            return new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    this::populateStudentsList,
                    error -> {
                        Log.e("Error", "HTTP Call failed: ");
                    }
            );
        }

        private void populateStudentsList(JSONObject jsonObject) {
            if (jsonObject == null)
                return;
            Log.i("Object",jsonObject.toString());
            studentsProfileList.addAll(new Gson().fromJson(jsonObject.toString(), UserDtoList.class).getUsers());
            if (studentsProfileList.isEmpty()) {
                Log.i("Data",studentsProfileList.toString());
                showToast("No Students Yet! Stay Tuned!");
            }
            else {
                displayList();
            }
        }

        @Override
        protected void onPostExecute(List<UserDto> users) {
           /* if (!notificationList.isEmpty()) {
                notificationCache.cleanUp();
                notificationCache.put(NOTIFICATIONS_CACHE_KEY + branch, notificationList);
            }*/
            displayList();
        }

    }

    private void displayList() {
        progressBar.setVisibility(View.GONE);
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

    private void showToast(String toastMsg) {
        if (this.getActivity() != null) {
            this.getActivity().runOnUiThread(() -> Toast.makeText(this.getContext(), toastMsg, Toast.LENGTH_SHORT).show());
        }
    }
}
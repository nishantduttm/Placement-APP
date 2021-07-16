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

import com.example.placementapp.Adapters.RecyclerViewAdapterViewNotifcation;
import com.example.placementapp.Adapters.RecyclerViewAdapterViewStudentsProfile;
import com.example.placementapp.R;
import com.example.placementapp.constants.Constants;
import com.example.placementapp.helper.FirebaseHelper;
import com.example.placementapp.helper.SharedPrefHelper;
import com.example.placementapp.pojo.ApplicationForm;
import com.example.placementapp.pojo.Notification;
import com.example.placementapp.pojo.StudentUser;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

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

public class ViewStudentsProfile extends Fragment implements ValueEventListener, AdapterView.OnItemSelectedListener {

    private static final List<String> list;
    private static final Cache<String, List<StudentUser>> studentProfileCache;
    private static final String NOTIFICATIONS_CACHE_KEY = "CACHE_NOTIFICATIONS_";
    private DatabaseReference myApplicationRef;
    static {
        list = new ArrayList<>();
        list.add("Choose a Stream");
        list.add("Comp");
        list.add("Mech");
        list.add("Civil");
        list.add("MechSandwich");
        studentProfileCache = CacheBuilder.newBuilder().build();
    }

    private static List<StudentUser> studentsProfileList;

    private DatabaseReference ref;
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
        this.ref = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_LOGIN);

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
                this.ref.addListenerForSingleValueEvent(this);
                //getDataFromCache(ref);
            });

            builder.setNegativeButton("Dismiss", (dialogInterface, i) -> dialogInterface.dismiss());

            builder.setView(v1);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
        this.ref.addListenerForSingleValueEvent(this);
    }

    private List<StudentUser> getDataFromCache(DatabaseReference ref) {
        List<StudentUser> users = null;
        //List<Notification> notifications = notificationCache.getIfPresent(NOTIFICATIONS_CACHE_KEY + branch);
        //notifications == null || notifications.isEmpty()
        if (true) {
            this.ref = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_LOGIN);
            this.ref.addListenerForSingleValueEvent(this);
        } else {
            studentsProfileList.clear();
            for (StudentUser user : users) {
                if (!branch.equals("ALL")) {
                    if (user.getBranch().equals(branch))
                        studentsProfileList.add(user);
                } else {
                    studentsProfileList.add(user);
                }
            }
            Log.i("STATS AFTER: ", String.valueOf(users.size()));
            displayList();
        }
        return studentsProfileList;
    }


    public class ViewStudentsProfileTaskRunner extends AsyncTask<DataSnapshot, String, List<StudentUser>> {

        @RequiresApi(api = Build.VERSION_CODES.N)
        void getNotification(Map<String, StudentUser> users) {
            for (Map.Entry<String, StudentUser> entry : users.entrySet()) {
                StudentUser user = entry.getValue();
                Log.i("Branch",branch);
                if (!branch.equals("ALL")) {
                    if (user.getType().equals(Constants.UserTypes.STUDENT) && user.getBranch().equals(branch)) {
                        studentsProfileList.add(user);
                    }
                } else {
                    if(user.getType().equals(Constants.UserTypes.STUDENT))
                        studentsProfileList.add(user);
                }
            }
            if(studentsProfileList.isEmpty())
                showToast("No Students Yet..! Stay Tuned..!");
            studentsProfileList.sort((a, b) -> a.getName().compareTo(b.getName()));
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected List<StudentUser> doInBackground(DataSnapshot... snapshots) {
            DataSnapshot snapshot = snapshots[0];
            studentsProfileList.clear();
            Map<String, StudentUser> users = snapshot.getValue(new GenericTypeIndicator<Map<String, StudentUser>>() {
            });

            if (users == null)
                showToast("No Students Yet! Stay Tuned!");
            else {
                getNotification(users);
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<StudentUser> users) {
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
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        new ViewStudentsProfileTaskRunner().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, snapshot);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        showToast("System Error..Please try again later");
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
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
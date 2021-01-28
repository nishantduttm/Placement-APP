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
import com.example.placementapp.R;
import com.example.placementapp.constants.Constants;
import com.example.placementapp.helper.FirebaseHelper;
import com.example.placementapp.helper.SharedPrefHelper;
import com.example.placementapp.pojo.ApplicationForm;
import com.example.placementapp.pojo.Notification;
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

public class ViewNotificationList extends Fragment implements ValueEventListener, AdapterView.OnItemSelectedListener {

    private static final List<String> list;
    private static final Cache<String, List<Notification>> notificationCache;
    private static final String NOTIFICATIONS_CACHE_KEY = "CACHE_NOTIFICATIONS_";
    private DatabaseReference myApplicationRef;
    Set<Long> myApplication;
    static {
        list = new ArrayList<>();
        list.add("Choose a Stream");
        list.add("Comp");
        list.add("Mech");
        list.add("Civil");
        list.add("MechSandwich");
        notificationCache = CacheBuilder.newBuilder().build();
    }

    private static List<Notification> notificationList;

    private DatabaseReference ref;
    private RecyclerView recyclerView;
    private RecyclerViewAdapterViewNotifcation notificationAdapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout notificationRefresh;

    private TextView branchText;
    private String branch = "ALL";
    private long count;
    String userType;
    String userPRN;

    public ViewNotificationList() {
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
        View v = inflater.inflate(R.layout.fragment_notification_list, container, false);

        notificationList = new ArrayList<>();
        myApplication = new HashSet<>();
        userType = SharedPrefHelper.getEntryfromSharedPreferences(v.getContext(), Constants.SharedPrefConstants.KEY_TYPE);
        this.ref = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_NOTIFICATIONS);
        if (userType.equals(Constants.UserTypes.ADMIN)) {
            initializeViewForAdmin(inflater, container, v);
        } else if (userType.equals(Constants.UserTypes.STUDENT)) {
            userPRN = SharedPrefHelper.getEntryfromSharedPreferences(v.getContext(), Constants.SharedPrefConstants.KEY_PRN);
            initializeViewForStudent(v);
        }
        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setVisibility(View.GONE);
        progressBar = v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        notificationAdapter = new RecyclerViewAdapterViewNotifcation(notificationList, this, userType);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(notificationAdapter);

        //getDataFromCache(ref);

        return v;
    }

    private void initializeViewForAdmin(LayoutInflater inflater, ViewGroup container, View v) {
        branchText = v.findViewById(R.id.BranchText);
        TextView selectBranchText = v.findViewById(R.id.selectbranchtext);
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

    private List<Notification> getDataFromCache(DatabaseReference ref) {
        List<Notification> notifications = null;
        //List<Notification> notifications = notificationCache.getIfPresent(NOTIFICATIONS_CACHE_KEY + branch);
        //notifications == null || notifications.isEmpty()
        if (true) {
            this.ref = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_NOTIFICATIONS);
            this.ref.addListenerForSingleValueEvent(this);
        } else {
            notificationList.clear();
            for (Notification notify : notifications) {
                if (!branch.equals("ALL")) {
                    if (notify.getBranch().equals(branch))
                        notificationList.add(notify);
                } else {
                    notificationList.add(notify);
                }
            }
            Log.i("STATS AFTER: ", String.valueOf(notifications.size()));
            displayList();
        }
        return notificationList;
    }

    private void initializeViewForStudent(View v) {
        TextView notificationText = v.findViewById(R.id.notificationtext);
        notificationText.setVisibility(View.VISIBLE);
        branch = SharedPrefHelper.getEntryfromSharedPreferences(v.getContext(), Constants.SharedPrefConstants.KEY_BRANCH);

        ref.addListenerForSingleValueEvent(ViewNotificationList.this);
    }

    public class ViewNotificationTaskRunner extends AsyncTask<DataSnapshot, String, List<Notification>> {

        @RequiresApi(api = Build.VERSION_CODES.N)
        void getNotification(Map<String, Notification> notifications) {
            for (Map.Entry<String, Notification> entry : notifications.entrySet()) {
                Notification notification = entry.getValue();
                if (!branch.equals("ALL")) {
                    if (notification.getBranch().equals(branch)) {
                        if (myApplication.size() != 0) {
                            if (!myApplication.contains(Long.parseLong(notification.getTime())))
                                notificationList.add(notification);
                        } else {
                            notificationList.add(notification);
                        }
                    }
                } else {
                    notificationList.add(notification);
                }
                DatabaseReference refToGetCount = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_APPLICATIONS + entry.getKey());
                refToGetCount.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int count = (int) snapshot.getChildrenCount();
                        notification.setCount(String.valueOf(count));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        showToast("System Error..Please try again later");
                    }
                });
            }
            notificationList.sort((a, b) -> {
                return (int) (Long.parseLong(b.time) - Long.parseLong(a.time));
            });
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected List<Notification> doInBackground(DataSnapshot... snapshots) {
            DataSnapshot snapshot = snapshots[0];
            notificationList.clear();
            Map<String, Notification> notifications = snapshot.getValue(new GenericTypeIndicator<Map<String, Notification>>() {
            });

            if (notifications == null)
                showToast("No Notifications Yet! Stay Tuned!");
            else {
                if (userType.equals(Constants.UserTypes.STUDENT)) {
                    myApplicationRef = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_APPILED_COMPANIES + userPRN);
                    myApplicationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot1) {
                            Map<String, ApplicationForm> applications = snapshot1.getValue(new GenericTypeIndicator<Map<String, ApplicationForm>>() {
                            });

                            if (applications != null) {
                                for (Map.Entry<String, ApplicationForm> entry : applications.entrySet()) {
                                    ApplicationForm applicationForm = entry.getValue();
                                    myApplication.add(Long.parseLong(applicationForm.getCompanyId()));
                                }
                            }
                            Log.i("myApplications", myApplication.toString());
                            getNotification(notifications);
                            displayList();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                } else {
                    getNotification(notifications);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Notification> notifications) {
           /* if (!notificationList.isEmpty()) {
                notificationCache.cleanUp();
                notificationCache.put(NOTIFICATIONS_CACHE_KEY + branch, notificationList);
            }*/
            displayList();
        }

    }

    private void displayList() {
        progressBar.setVisibility(View.GONE);
        if (recyclerView != null && notificationAdapter != null) {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(notificationAdapter);
            notificationAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        new ViewNotificationTaskRunner().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, snapshot);
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
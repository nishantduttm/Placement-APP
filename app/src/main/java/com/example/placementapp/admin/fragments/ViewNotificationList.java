package com.example.placementapp.admin.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.placementapp.Adapters.RecyclerViewAdapterViewNotifcation;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.placementapp.R;
import com.example.placementapp.constants.Constants;
import com.example.placementapp.helper.FirebaseHelper;
import com.example.placementapp.helper.SharedPrefHelper;
import com.example.placementapp.pojo.Notification;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewNotificationList extends Fragment implements ValueEventListener, AdapterView.OnItemSelectedListener {

    private DatabaseReference ref;
    private DatabaseReference ref2;
    private RecyclerView recyclerView;
    private RecyclerViewAdapterViewNotifcation notificationAdapter;
    private List<Notification> notificationList;
    private ProgressBar progressBar;

    private TextView branchText, selectbranchtext, notificationtext;
    private String branch = null;
    private String userType;
    private long count;

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public ViewNotificationList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationList = new ArrayList<>();
        ref = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_NOTIFICATIONS);
        ref.addListenerForSingleValueEvent(this);
}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_notification_list, container, false);

        userType = SharedPrefHelper.getEntryfromSharedPreferences(v.getContext(),Constants.SharedPrefConstants.KEY_TYPE);

        if(userType.equals(Constants.UserTypes.ADMIN))
        {
            branchText = v.findViewById(R.id.BranchText);
            selectbranchtext = v.findViewById(R.id.selectbranchtext);
            selectbranchtext.setVisibility(View.VISIBLE);
            branchText.setVisibility(View.VISIBLE);
            branchText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    View v = inflater.inflate(R.layout.spinner_dialog,container,false);
                    builder.setTitle("Select Branch to Filter");
                    Spinner spinner = v.findViewById(R.id.spinner);

                    List<String> list = new ArrayList<>();
                    list.add("Choose a Stream");
                    list.add("Comp");
                    list.add("Mech");
                    list.add("Civil");
                    list.add("MechSandwich");

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(v.getContext(),android.R.layout.simple_spinner_item,list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if(spinner.getSelectedItemId()!=0)
                            {
                                branchText.setText(spinner.getSelectedItem().toString());
                                branch = spinner.getSelectedItem().toString();
                            }
                            dialogInterface.dismiss();
                            ref.addListenerForSingleValueEvent(ViewNotificationList.this);
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
        }

        if(userType.equals(Constants.UserTypes.STUDENT))
        {
            notificationtext = v.findViewById(R.id.notificationtext);
            notificationtext.setVisibility(View.VISIBLE);
            branch = SharedPrefHelper.getEntryfromSharedPreferences(v.getContext(),Constants.SharedPrefConstants.KEY_BRANCH);
            ref.addListenerForSingleValueEvent(ViewNotificationList.this);
        }


        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setVisibility(View.GONE);
        progressBar = v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        notificationAdapter = new RecyclerViewAdapterViewNotifcation(notificationList,this, userType);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(notificationAdapter);
        return v;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        notificationList.clear();
        GenericTypeIndicator<Map<String, Notification>> t = new GenericTypeIndicator<Map<String, Notification>>() {};
        Map<String, Notification> notifications = snapshot.getValue(t);

        if(notifications == null)
            Toast.makeText(this.getContext(), "No Notifications Yet! Stay Tuned!", Toast.LENGTH_SHORT).show();
        else {

            for (Map.Entry<String, Notification> entry : notifications.entrySet()) {
                Notification notification = entry.getValue();
                ref2 = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_APPLICATIONS + entry.getKey());
                ref2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        count = snapshot.getChildrenCount();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                notification.setCount(String.valueOf(count));
                Toast.makeText(this.getContext(), notification.getCount(), Toast.LENGTH_SHORT).show();

                if (notification != null) {

                    if (branch != null) {
                        if (notification.getBranch().equals(branch))
                            notificationList.add(new Notification(notification.getTime(), notification.getCompanyName(), notification.getVenue(), notification.getBranch(), notification.getSalary(), notification.getEligibility(), notification.getDate(), notification.getCount()));
                    } else
                        notificationList.add(new Notification(notification.getTime(), notification.getCompanyName(), notification.getVenue(), notification.getBranch(), notification.getSalary(), notification.getEligibility(), notification.getDate(), notification.getCount()));
                }
            }
        }

        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        if (recyclerView != null && notificationAdapter != null) {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(notificationAdapter);
            notificationAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        branch = adapterView.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
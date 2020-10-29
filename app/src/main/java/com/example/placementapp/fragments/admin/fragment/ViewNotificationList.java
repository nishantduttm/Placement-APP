package com.example.placementapp.fragments.admin.fragment;

import android.os.Bundle;

import Adapters.NotificationAdapter;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.placementapp.R;
import com.example.placementapp.constants.Constants;
import com.example.placementapp.helper.FirebaseHelper;
import com.example.placementapp.pojo.Notification;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewNotificationList extends Fragment implements ValueEventListener {

    private DatabaseReference ref;
    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notificationList;

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
        recyclerView = v.findViewById(R.id.recycler_view);
        notificationAdapter = new NotificationAdapter(notificationList,this);
        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(notificationAdapter);
        return v;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        for(DataSnapshot childsnapshot : snapshot.getChildren())
        {
            Notification notification = childsnapshot.getValue(Notification.class);
            if(notification!=null)
                notificationList.add(new Notification(notification.getCompanyName(),notification.getMessage(),notification.getTimestamp()));
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
}
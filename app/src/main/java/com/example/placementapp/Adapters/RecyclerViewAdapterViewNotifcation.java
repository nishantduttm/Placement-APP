package com.example.placementapp.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.placementapp.R;

import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.placementapp.admin.fragments.ViewNotificationList;
import com.example.placementapp.pojo.Notification;

public class RecyclerViewAdapterViewNotifcation extends RecyclerView.Adapter<RecyclerViewAdapterViewNotifcation.MyViewHolder> {

    private Context context;
    private List<Notification> notificationList;
    private ViewNotificationList fragment;

    public RecyclerViewAdapterViewNotifcation(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    public RecyclerViewAdapterViewNotifcation(List<Notification> notificationList, ViewNotificationList fragment) {
        this.notificationList = notificationList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(fragment.getContext())
                .inflate(R.layout.notification_format, viewGroup, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Notification notification = (Notification) notificationList.get(position);
        holder.parent.setAnimation(AnimationUtils.loadAnimation(fragment.getContext(), R.anim.fade_scale_animation));
        holder.companyName.setText(notification.companyName);
        holder.comapanyDescription.setText(notification.message);
        holder.timestamp.setText("Posted On: " + notification.timestamp);

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView companyName, comapanyDescription, timestamp;
        CardView parent;

        public MyViewHolder(View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            companyName = itemView.findViewById(R.id.company_name);
            comapanyDescription = itemView.findViewById(R.id.company_description);
            timestamp = itemView.findViewById(R.id.time);
        }
    }
}


package com.example.placementapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.transition.AutoTransition;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.placementapp.Animation.MyBounceInterpolator;
import com.example.placementapp.R;
import com.example.placementapp.activities.CompanyPopUpActivity;
import com.example.placementapp.admin.fragments.ViewNotificationList;
import com.example.placementapp.pojo.Notification;
import com.example.placementapp.student.StudentApplicationStatusActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterViewNotifcation extends RecyclerView.Adapter<RecyclerViewAdapterViewNotifcation.MyViewHolder> implements View.OnClickListener {

    private Context context;
    private List<Notification> notificationList;
    private ViewNotificationList fragment;
    private String userType;

    public RecyclerViewAdapterViewNotifcation(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    public RecyclerViewAdapterViewNotifcation(List<Notification> notificationList, ViewNotificationList fragment, String userType) {
        this.notificationList = notificationList;
        this.fragment = fragment;
        this.userType = userType;
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
        holder.cardView.setAnimation(AnimationUtils.loadAnimation(fragment.getContext(), R.anim.fade_scale_animation));
        holder.companyName.setText(notification.getCompanyName());
        holder.applicantsCountView.setText(notification.getCount());

        holder.itemView.setTag(position);

        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {

        int pos = (int) view.getTag();
        Intent intent = new Intent(view.getContext(), CompanyPopUpActivity.class);
        intent.putExtra("details", notificationList.get(pos));
        view.getContext().startActivity(intent);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView companyName,applicantsCountView,applicantsCountTextView;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.base_cardview);
            applicantsCountTextView = itemView.findViewById(R.id.applicantsCountTextView);
            applicantsCountView = itemView.findViewById(R.id.applicantsCountView);
            companyName = itemView.findViewById(R.id.companyNameView);

            applicantsCountTextView.setVisibility(View.VISIBLE);
            applicantsCountView.setVisibility(View.VISIBLE);
        }
    }
}


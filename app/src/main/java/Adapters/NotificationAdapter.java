package Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.placementapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.placementapp.fragments.admin.fragment.ViewNotificationList;
import com.example.placementapp.pojo.Notification;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private List notificationList;
    private ViewNotificationList fragment;
    private Context context;

    public NotificationAdapter(List<Notification> notificationList, ViewNotificationList fragment) {
        this.notificationList = notificationList;
        this.fragment = fragment;
    }

    public NotificationAdapter(Context context, List<Notification> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.notification_format, viewGroup, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Notification notification = (Notification) notificationList.get(position);
        Random rnd = new Random();
        int currentColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.parent.setBackgroundColor(currentColor);
        holder.companyName.setText(notification.companyName);
        holder.comapanyDescription.setText(notification.message);
        holder.timestamp.setText(notification.timestamp);

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
            companyName = itemView.findViewById(R.id.companyName);
            comapanyDescription = itemView.findViewById(R.id.companyDescription);
        }
    }
}


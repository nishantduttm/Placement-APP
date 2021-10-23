package com.example.placementapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.transition.AutoTransition;
import android.transition.Fade;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.placementapp.Animation.MyBounceInterpolator;
import com.example.placementapp.R;
import com.example.placementapp.activities.CompanyPopUpActivity;
import com.example.placementapp.admin.fragments.ViewNotificationList;
import com.example.placementapp.constants.Constants;
import com.example.placementapp.pojo.Notification;
import com.example.placementapp.pojo.NotificationDto;
import com.example.placementapp.pojo.StudentApplicationDto;
import com.example.placementapp.student.StudentApplicationStatusActivity;
import com.example.placementapp.utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

public class RecyclerViewAdapterViewNotifcation extends RecyclerView.Adapter<RecyclerViewAdapterViewNotifcation.MyViewHolder> implements View.OnClickListener {

    private Context context;
    private List<NotificationDto> notificationList;
    private ViewNotificationList fragment;
    private RelativeLayout hiddenView;
    private List<MyViewHolder> myViewHolders = new ArrayList<>();

    private String url = Constants.HttpConstants.GET_NOTIFICATION_DETAILS_URL;

    public RecyclerViewAdapterViewNotifcation(Context context, List<NotificationDto> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    public RecyclerViewAdapterViewNotifcation(List<NotificationDto> notificationList, ViewNotificationList fragment, String branch) {
        this.fragment = fragment;

        List<NotificationDto> temp = new ArrayList<>();
        if(!branch.equals("ALL")) {
            for (NotificationDto notificationDto : notificationList) {
                if (notificationDto.getCompanyBranch().equals(branch))
                    temp.add(notificationDto);
            }
        }
        else
        {
            temp = notificationList;
        }

        this.notificationList = temp;

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
        NotificationDto notification = notificationList.get(position);
        holder.cardView.setAnimation(AnimationUtils.loadAnimation(fragment.getContext(), R.anim.fade_scale_animation));
        holder.companyName.setText(notification.getCompanyName());

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

        HttpUtils.addRequestToHttpQueue(constructHttpRequest(url, notificationList.get(pos)), fragment.getContext());

    }

    private JsonObjectRequest constructHttpRequest(String url, NotificationDto notificationDto) {
        try {
            url = url + notificationDto.notificationId;

            return new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    this::getResponse,
                    null
            );
        } catch (RuntimeException e) {
            Log.i("Error", "Http Error");
        }
        return null;
    }

    private void getResponse(JSONObject jsonObject) {
        if (jsonObject != null) {
            NotificationDto notification = new Gson().fromJson(jsonObject.toString(), NotificationDto.class);
            Intent intent = new Intent(fragment.getContext(), CompanyPopUpActivity.class);
            intent.putExtra("details", notification);
            fragment.getContext().startActivity(intent);
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView companyName, applicantCountView, applicantCountTextView;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.base_cardview);
            companyName = itemView.findViewById(R.id.companyNameView);

        }
    }
}


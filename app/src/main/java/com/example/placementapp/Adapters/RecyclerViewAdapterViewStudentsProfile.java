package com.example.placementapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.placementapp.R;
import com.example.placementapp.activities.CompanyPopUpActivity;
import com.example.placementapp.admin.fragments.ViewNotificationList;
import com.example.placementapp.admin.fragments.ViewStudentsProfile;
import com.example.placementapp.constants.Constants;
import com.example.placementapp.pojo.Notification;
import com.example.placementapp.pojo.NotificationDto;
import com.example.placementapp.pojo.StudentUser;
import com.example.placementapp.pojo.UserDto;
import com.example.placementapp.student.UpdateProfile;
import com.example.placementapp.utils.HttpUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterViewStudentsProfile extends RecyclerView.Adapter<RecyclerViewAdapterViewStudentsProfile.MyViewHolder> implements View.OnClickListener {

    private Context context;
    private List<UserDto> studentsProfileList;
    private ViewStudentsProfile fragment;
    private RelativeLayout hiddenView;
    private List<MyViewHolder> myViewHolders = new ArrayList<>();
    private ImageButton imgButton;
    private String userType;

    private String url = Constants.HttpConstants.GET_SPECIFIC_USER_URL;

    public RecyclerViewAdapterViewStudentsProfile(Context context, List<UserDto> studentsProfileList) {
        this.context = context;
        this.studentsProfileList = studentsProfileList;
    }

    public RecyclerViewAdapterViewStudentsProfile(List<UserDto> studentsProfileList, ViewStudentsProfile fragment, String userType) {
        this.studentsProfileList = studentsProfileList;
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
        UserDto user = (UserDto) studentsProfileList.get(position);
        holder.cardView.setAnimation(AnimationUtils.loadAnimation(fragment.getContext(), R.anim.fade_scale_animation));
        holder.companyName.setText(user.getName());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return studentsProfileList.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {

        int pos = (int) view.getTag();

        HttpUtils.addRequestToHttpQueue(constructHttpRequest(url, studentsProfileList.get(pos)), fragment.getContext());

//        holder.applicationsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Animation myAnim = AnimationUtils.loadAnimation(fragment.getContext(), R.anim.bounce_animation);
//                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 10);
//                myAnim.setInterpolator(interpolator);
//                view.startAnimation(myAnim);
//
//                if (userType.equals("1")) {
//                    new Handler().postDelayed(() -> {
//                        Intent i = new Intent(view.getContext(), StudentApplicationStatusActivity.class);
//                        i.putExtra("companyName", notificationList.get(pos).getCompanyName());
//                        i.putExtra("companyID", notificationList.get(pos).getTime());
//                        view.getContext().startActivity(i);
//                    }, 1000);
//                }
//            }
//        });
    }

    private JsonObjectRequest constructHttpRequest(String url, UserDto userDto) {
        try {
            url = url + userDto.getPrn();

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
            UserDto userDto = new Gson().fromJson(jsonObject.toString(), UserDto.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("profileDetails",userDto);

            Fragment frag = new UpdateProfile();
            frag.setArguments(bundle);


            FragmentManager manager = fragment.getActivity().getSupportFragmentManager();
            FragmentTransaction trans = manager.beginTransaction();

            trans.replace(R.id.activity_main_frame_layout,frag);
            trans.setCustomAnimations(R.anim.fade_in,R.anim.fade_out);
            trans.commit();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView companyName;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.base_cardview);
            companyName = itemView.findViewById(R.id.companyNameView);
        }
    }
}


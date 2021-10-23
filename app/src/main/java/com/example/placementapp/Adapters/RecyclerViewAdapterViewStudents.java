package com.example.placementapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Parcelable;
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
import android.widget.Toast;

import com.example.placementapp.Animation.MyBounceInterpolator;
import com.example.placementapp.R;
import com.example.placementapp.activities.CompanyPopUpActivity;
import com.example.placementapp.admin.activities.StudentStatus;
import com.example.placementapp.admin.fragments.ViewNotificationList;
import com.example.placementapp.admin.fragments.ViewStudentsList;
import com.example.placementapp.pojo.ApplicationForm;
import com.example.placementapp.pojo.Notification;
import com.example.placementapp.pojo.Statistics;
import com.example.placementapp.pojo.StudentApplicationDto;
import com.example.placementapp.student.StudentApplicationStatusActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapterViewStudents extends RecyclerView.Adapter<RecyclerViewAdapterViewStudents.MyViewHolder> implements View.OnClickListener {

    private Context context;
    private List<StudentApplicationDto> studentList;

    public RecyclerViewAdapterViewStudents(List<StudentApplicationDto> studentList, Context context, String status) {
        List<StudentApplicationDto> temp = new ArrayList<>();
        if(!status.equals("All Students")) {
            for (StudentApplicationDto student : studentList) {
                if (student.getOverallStatus().equals(status))
                    temp.add(student);
            }
        }
        else
        {
            temp = studentList;
        }

        this.studentList = temp;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.notification_format, viewGroup, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        StudentApplicationDto studentApplicationDto = (StudentApplicationDto) studentList.get(position);

        holder.cardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation));
        holder.studentName.setText(studentApplicationDto.getName());
        holder.statusView.setText(studentApplicationDto.getOverallStatus());

        holder.itemView.setTag(position);

        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {

        int pos = (int) view.getTag();
        Intent intent = new Intent(view.getContext(), StudentStatus.class);
        intent.putExtra("details", studentList.get(pos));
        view.getContext().startActivity(intent);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView studentName, statusTextView, statusView;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.base_cardview);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            statusView = itemView.findViewById(R.id.statusView);
            studentName = itemView.findViewById(R.id.companyNameView);

            statusTextView.setVisibility(View.VISIBLE);
            statusView.setVisibility(View.VISIBLE);
        }
    }
}


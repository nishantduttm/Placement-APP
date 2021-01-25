package com.example.placementapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.placementapp.R;
import com.example.placementapp.admin.activities.StudentStatus;
import com.example.placementapp.pojo.FormStatus;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterProcessRound extends RecyclerView.Adapter<RecyclerViewAdapterProcessRound.MyViewHolder> implements View.OnClickListener{
    private Context context;
    private List<FormStatus> formStatusList;
    private StudentStatus  studentStatus;
    private List<RecyclerViewAdapterProcessRound.MyViewHolder> myViewHolders = new ArrayList<>();
    public RecyclerViewAdapterProcessRound(Context context, List<FormStatus> formStatusList) {
        this.context = context;
        this.formStatusList = formStatusList;
    }
    public RecyclerViewAdapterProcessRound(List<FormStatus> formStatusList, StudentStatus activity) {
        this.formStatusList = formStatusList;
        this.studentStatus = activity;
    }


    @Override
    public void onClick(View v) {

    }

    @NonNull
    @Override
    public RecyclerViewAdapterProcessRound.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.process_round_format,parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        FormStatus formStatus = (FormStatus) formStatusList.get(position);
        holder.cardView.setAnimation(AnimationUtils.loadAnimation(this.context, R.anim.fade_scale_animation));
        holder.processRound.setText(formStatus.processRound);
        holder.processDate.setText(formStatus.processDate);
        holder.itemView.setTag(position);


        myViewHolders.add(position, holder);
    }

    @Override
    public int getItemCount() {
        return formStatusList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView processRound, processDate;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.base_cardview_proceesRound);
            processRound = itemView.findViewById(R.id.process_round);
            processDate = itemView.findViewById(R.id.process_date);
        }
    }
}

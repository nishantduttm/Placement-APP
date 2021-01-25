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

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterProcessRound extends RecyclerView.Adapter<RecyclerViewAdapterProcessRound.MyViewHolder> implements View.OnClickListener{
    private Context context;
    private List<FormStatus> formStatusList;
    private StudentStatus  studentStatus;

    public RecyclerViewAdapterProcessRound(List<FormStatus> formStatusList) {
        this.formStatusList = formStatusList;
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
        holder.processRound.setText(formStatus.processRound);
        holder.processDate.setText(formStatus.processDate);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return formStatusList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView processRound, processDate;
        CardView card_view;
        public MyViewHolder(View itemView) {
            super(itemView);
            card_view = itemView.findViewById(R.id.card_view);
            processRound = itemView.findViewById(R.id.process_round_status);
            processDate = itemView.findViewById(R.id.process_date_status);
        }
    }
}

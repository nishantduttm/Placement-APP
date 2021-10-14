package com.example.placementapp.Adapters;


import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.placementapp.R;
import com.example.placementapp.student.WebActivity;

import java.util.ArrayList;

public class PrepMaterialAdapter extends RecyclerView.Adapter<PrepMaterialAdapter.MyViewHolder>{

    ArrayList name;
    ArrayList preplink;
    ArrayList companyIcon;
    Context context;

    public PrepMaterialAdapter(Context context, ArrayList name, ArrayList preplink, ArrayList companyIcon) {
        this.context = context;
        this.name = name;
        this.preplink = preplink;
        this.companyIcon = companyIcon;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
// infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.prep_material_row_design, parent, false);
// set the view's size, margins, paddings and layout parameters
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.nameTF.setText((CharSequence) name.get(position));
        holder.preplinkTF.setText((CharSequence) preplink.get(position));
        holder.companyIF.setImageResource((Integer) companyIcon.get(position));

        holder.preplinkTF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String website = (String) preplink.get(position);
                //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
                Intent browserIntent = new Intent(context, WebActivity.class);
                browserIntent.putExtra("url",website);
                context.startActivity(browserIntent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return name.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView nameTF;
        ImageButton arrow;
        TextView preplinkTF;
        ImageView companyIF;
        RelativeLayout hiddenView;
        CardView cardView;
        public MyViewHolder(View itemView) {
            super(itemView);

// get the reference of item view's
            nameTF = (TextView) itemView.findViewById(R.id.nameTV);
            preplinkTF = (TextView) itemView.findViewById(R.id.preplinkTV);
            companyIF = (ImageView) itemView.findViewById(R.id.companyIV);
            cardView = (CardView) itemView.findViewById(R.id.base_cardview);
            hiddenView = (RelativeLayout) itemView.findViewById(R.id.hidden_view);
            arrow = (ImageButton) itemView.findViewById(R.id.arrow_button);

            arrow.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View view) {

                    if (hiddenView.getVisibility() == View.VISIBLE) {

                        // The transition of the hiddenView is carried out
                        //  by the TransitionManager class.
                        // Here we use an object of the AutoTransition
                        // Class to create a default transition.
                        TransitionManager.beginDelayedTransition(cardView,
                                new AutoTransition());
                        hiddenView.setVisibility(View.GONE);
                        arrow.setImageResource(R.drawable.ic_baseline_expand_more_24);
                    }

                    // If the CardView is not expanded, set its visibility
                    // to visible and change the expand more icon to expand less.
                    else {

                        TransitionManager.beginDelayedTransition(cardView,
                                new AutoTransition());
                        hiddenView.setVisibility(View.VISIBLE);
                        arrow.setImageResource(R.drawable.ic_baseline_expand_less_24);
                    }

                }
            });

        }
    }
}

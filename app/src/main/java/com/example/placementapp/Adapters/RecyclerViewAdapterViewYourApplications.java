//package com.example.placementapp.Adapters;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.os.Handler;
//import android.transition.AutoTransition;
//import android.transition.Fade;
//import android.transition.TransitionManager;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.widget.Button;
//import android.widget.ImageButton;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.example.placementapp.Animation.MyBounceInterpolator;
//import com.example.placementapp.R;
//import com.example.placementapp.admin.fragments.ViewNotificationList;
//import com.example.placementapp.pojo.Notification;
//import com.example.placementapp.student.StudentApplicationStatusActivity;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.RequiresApi;
//import androidx.cardview.widget.CardView;
//import androidx.recyclerview.widget.RecyclerView;
//
//public class RecyclerViewAdapterViewYourApplications {
//
//    private Context context;
//    private List<Notification> notificationList;
//    private ViewNotificationList fragment;
//    private RelativeLayout hiddenView;
//    private List<RecyclerViewAdapterViewNotifcation.MyViewHolder> myViewHolders = new ArrayList<>();
//    private ImageButton imgButton;
//    private String userType;
//
//    public RecyclerViewAdapterViewYourApplications(Context context, List<Notification> notificationList) {
//        this.context = context;
//        this.notificationList = notificationList;
//    }
//
//    public RecyclerViewAdapterViewYourApplications(List<Notification> notificationList, ViewNotificationList fragment, String userType ) {
//        this.notificationList = notificationList;
//        this.fragment = fragment;
//        this.userType = userType;
//    }
//
//    @NonNull
//    @Override
//    public RecyclerViewAdapterViewNotifcation.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View itemView = LayoutInflater.from(fragment.getContext())
//                .inflate(R.layout.notification_format, viewGroup, false);
////        return new RecyclerViewAdapterViewNotifcation.MyViewHolder(itemView);
//    }
//
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerViewAdapterViewNotifcation.MyViewHolder holder, int position) {
//        Notification notification = (Notification) notificationList.get(position);
//        holder.cardView.setAnimation(AnimationUtils.loadAnimation(fragment.getContext(), R.anim.fade_scale_animation));
//        holder.companyName.setText(notification.getCompanyName());
//        holder.venue.setText(notification.getVenue());
//        holder.eligibility.setText(notification.getEligibility());
//        holder.salary.setText(notification.getSalary());
//        holder.date.setText(notification.getDate());
//        holder.itemView.setTag(position);
//
//
//        myViewHolders.add(position, holder);
//
//        holder.itemView.setOnClickListener(this);
//    }
//
////    @Override
////    public int getItemCount() {
////        return notificationList.size();
////    }
//
////    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
////    @Override
//    public void onClick(View view) {
//
//        int pos = (int) view.getTag();
//        RecyclerViewAdapterViewNotifcation.MyViewHolder holder = myViewHolders.get(pos);
//        if (holder.hiddenView.getVisibility() == View.VISIBLE) {
//            TransitionManager.beginDelayedTransition(holder.cardView,
//                    new Fade(Fade.MODE_IN));
//            holder.hiddenView.setVisibility(View.GONE);
//            holder.imageButton.setImageResource(R.drawable.ic_baseline_arrow_drop_down_circle_24);
//        } else {
//            TransitionManager.beginDelayedTransition(holder.cardView,
//                    new AutoTransition());
//            holder.hiddenView.setVisibility(View.VISIBLE);
//            holder.imageButton.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
//        }
//
//        holder.applicationsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Animation myAnim = AnimationUtils.loadAnimation(fragment.getContext(), R.anim.bounce_animation);
//                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.1, 10);
//                myAnim.setInterpolator(interpolator);
//                view.startAnimation(myAnim);
//
//                if(userType.equals("1"))
//                {
//                    new Handler().postDelayed(() -> {
//                        Intent i = new Intent(view.getContext(), StudentApplicationStatusActivity.class);
//                        i.putExtra("companyName", notificationList.get(pos).getCompanyName());
//                        i.putExtra("companyID",notificationList.get(pos).getTime());
//                        view.getContext().startActivity(i);
//                    }, 1000);
//                }
//            }
//        });
//    }
//
//    public class MyViewHolder extends RecyclerView.ViewHolder {
//        TextView companyName, venue, date, salary, eligibility;
//        CardView cardView;
//        RelativeLayout hiddenView;
//        ImageButton imageButton;
//        Button applicationsButton;
//
//        public MyViewHolder(View itemView) {
//            super(itemView);
//            cardView = itemView.findViewById(R.id.base_cardview);
//            hiddenView = itemView.findViewById(R.id.hiddenlayout);
//            companyName = itemView.findViewById(R.id.companyNameView);
//            venue = itemView.findViewById(R.id.venueView);
//            date = itemView.findViewById(R.id.date);
//            salary = itemView.findViewById(R.id.salaryView);
//            eligibility = itemView.findViewById(R.id.eligibilityView);
//            imageButton = itemView.findViewById(R.id.imageButton);
//            applicationsButton = itemView.findViewById(R.id.ApplicationsButton);
//            if(userType.equals("0"))
//                applicationsButton.setText("Check Application");
//            else
//                applicationsButton.setText("Fill Application");
//        }
//    }
//
//}

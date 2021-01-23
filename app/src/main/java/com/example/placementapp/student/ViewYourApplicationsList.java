//package com.example.placementapp.student;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.example.placementapp.Adapters.RecyclerViewAdapterViewNotifcation;
//import com.example.placementapp.Adapters.RecyclerViewAdapterViewYourApplications;
//import com.example.placementapp.R;
//import com.example.placementapp.constants.Constants;
//import com.example.placementapp.helper.FirebaseHelper;
//import com.example.placementapp.helper.SharedPrefHelper;
//import com.example.placementapp.pojo.Notification;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.GridLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//public class ViewYourApplicationsList extends Fragment implements ValueEventListener {
//
//    private DatabaseReference ref;
//    private RecyclerView recyclerView;
//    private RecyclerViewAdapterViewYourApplications applicationsAdapter;
//    private List<Notification> applicationsList;
//    private ProgressBar progressBar;
//
//    private TextView branchText, selectbranchtext, notificationtext;
//    private String branch = null;
//    private String userPRN;
//
//    public ViewYourApplicationsList() {
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        applicationsList = new ArrayList<>();
//        ref = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_APPILED_COMPANIES);
//        ref.addListenerForSingleValueEvent(this);
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View v = inflater.inflate(R.layout.fragment_applied_companies_list,container,false);
//
//        userPRN = SharedPrefHelper.getEntryfromSharedPreferences(v.getContext(), Constants.SharedPrefConstants.KEY_PRN);
//
//        recyclerView = v.findViewById(R.id.recycler_view);
//        recyclerView.setVisibility(View.GONE);
//        progressBar = v.findViewById(R.id.progress_bar);
//        progressBar.setVisibility(View.VISIBLE);
//        applicationsAdapter = new RecyclerViewAdapterViewYourApplications(notificationList,this, userType);
//        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), 1);
//        recyclerView.setLayoutManager(manager);
//        recyclerView.setAdapter(applicationsAdapter);
//        return v;
//    }
//
//    @Override
//    public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//    }
//
//    @Override
//    public void onCancelled(@NonNull DatabaseError error) {
//
//    }
//}

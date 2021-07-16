package com.example.placementapp.student;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.placementapp.Animation.MyBounceInterpolator;
import com.example.placementapp.R;
import com.example.placementapp.activities.RegisterActivity;
import com.example.placementapp.constants.Constants;
import com.example.placementapp.helper.FirebaseHelper;
import com.example.placementapp.helper.SharedPrefHelper;
import com.example.placementapp.pojo.StudentUser;
import com.example.placementapp.pojo.User;
import com.example.placementapp.utils.StringUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class UpdateProfile extends Fragment implements View.OnClickListener {

    private String user_name;
    private String branch;
    private String mailId;
    private String prn;
    private String year;
    private String div;
    private String mobile;

    //Widgets
    private TextView nameHeadingView;
    private TextView nameView;
    private TextView branchView;
    private TextView prnView;
    private TextView mailView;
    private EditText yearView;
    private EditText divView;
    private EditText mobileView;
    private EditText overall;
    private EditText sem1;
    private EditText sem2;
    private EditText sem3;
    private EditText sem4;
    private EditText sem5;
    private EditText sem6;
    private EditText sem7;
    private EditText sem8;


    private Button save;

    //Firebase
    private DatabaseReference readData;
    private DatabaseReference writeData;
    private StudentUser user;

    //Sem_Results
    private Map<String,EditText> semResults = new HashMap<>();

    public UpdateProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user_name = SharedPrefHelper.getEntryfromSharedPreferences(this.getContext(),Constants.SharedPrefConstants.KEY_NAME);
        branch = SharedPrefHelper.getEntryfromSharedPreferences(this.getContext(),Constants.SharedPrefConstants.KEY_BRANCH);
        mailId = SharedPrefHelper.getEntryfromSharedPreferences(this.getContext(),Constants.SharedPrefConstants.KEY_MAIL);
        prn = SharedPrefHelper.getEntryfromSharedPreferences(this.getContext(),Constants.SharedPrefConstants.KEY_PRN);

        readData = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_LOGIN + "/" + prn);
        readData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                user = snapshot.getValue(StudentUser.class);

                if(user.getYear() != null)
                    yearView.setText(user.getYear());
                if(user.getDiv() != null)
                    divView.setText(user.getDiv());
                if(user.getMobile() != null)
                    mobileView.setText(user.getMobile());
                if(user.getSemResults() != null)
                {
                    Map<String,String> map = user.getSemResults();

                    for(Map.Entry<String,String> entry : map.entrySet())
                        semResults.get(entry.getKey()).setText(entry.getValue());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

        writeData = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_LOGIN + "/" + prn);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_update_profile, container, false);

        nameHeadingView = v.findViewById(R.id.userName);
        nameView = v.findViewById(R.id.FullNameValue);
        branchView = v.findViewById(R.id.BranchNameValue);
        prnView = v.findViewById(R.id.prnValue);
        mailView = v.findViewById(R.id.EmailValue);
        yearView = v.findViewById(R.id.ClassNameValue);
        divView = v.findViewById(R.id.DivNameValue);
        mobileView = v.findViewById(R.id.mobileNoValue);
        save = v.findViewById(R.id.saveUserButton);
        overall = v.findViewById(R.id.overallSgpaValue);
        sem1 = v.findViewById(R.id.SgpaSem1Value);
        sem2 = v.findViewById(R.id.SgpaSem2Value);
        sem3 = v.findViewById(R.id.SgpaSem3Value);
        sem4 = v.findViewById(R.id.SgpaSem4Value);
        sem5 = v.findViewById(R.id.SgpaSem5Value);
        sem6 = v.findViewById(R.id.SgpaSem6Value);
        sem7 = v.findViewById(R.id.SgpaSem7Value);
        sem8 = v.findViewById(R.id.SgpaSem8Value);
        storeSemInList(v);

        if(user_name != null && branch != null && mailId != null && prn != null)
        {
            nameHeadingView.setText(user_name);
            nameView.setText(user_name);
            branchView.setText(branch);
            prnView.setText(prn);
            mailView.setText(mailId);
        }
        else
        {
            Toast.makeText(v.getContext(), "Error Fetching Data..! Retry Again..!", Toast.LENGTH_SHORT).show();
        }

        save.setOnClickListener(this);

        return v;
    }

    public void storeSemInList(View v) {

        semResults.put("overall",overall);
        semResults.put("sem1",sem1);
        semResults.put("sem2",sem2);
        semResults.put("sem3",sem3);
        semResults.put("sem4",sem4);
        semResults.put("sem5",sem5);
        semResults.put("sem6",sem6);
        semResults.put("sem7",sem7);
        semResults.put("sem8",sem8);
    }

    @Override
    public void onClick(View view) {
        Animation myAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.bounce_animation);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.05, 5);
        myAnim.setInterpolator(interpolator);
        view.startAnimation(myAnim);

        year = yearView.getText().toString();
        div = divView.getText().toString();
        mobile = mobileView.getText().toString();

        boolean flag = true;

        if (StringUtils.isNotBlank(year) && StringUtils.isNotBlank(div) && StringUtils.isNotBlank(mobile)) {
                if (!year.matches("[A-Z]{2}")) {
                    yearView.setError("Capital Letters Only(FE,SE,TE,BE)");
                    Toast.makeText(view.getContext(), "Invalid Year Format", Toast.LENGTH_SHORT).show();
                    flag = false;
                }
                if (!div.matches("[0-5]{1}")) {
                    divView.setError("Only Numbers Allowed(1-5)");
                    Toast.makeText(view.getContext(), "Invalid Div Format", Toast.LENGTH_SHORT).show();
                    flag = false;
                }
                if (!mobile.matches("[0-9]{10}")) {
                    mobileView.setError("Invalid Number Format");
                    Toast.makeText(view.getContext(), "Invalid Number Format", Toast.LENGTH_SHORT).show();
                    flag = false;
                }
                if(flag)
                {
                    Map<String,String> map = new HashMap<>();

                    for(Map.Entry<String,EditText> entry : semResults.entrySet())
                        map.put(entry.getKey(),entry.getValue().getText().toString());

                    StudentUser u = new StudentUser(mailId,
                            user.getPassword(),
                            user_name,
                            user.getType(),
                            user.getBranch(),
                            user.getPrn(),
                            year,
                            div,
                            mobile,
                            map);

                    writeData.setValue(u);
                    Toast.makeText(view.getContext(), "Profile Updated Successfully..!", Toast.LENGTH_SHORT).show();
                }
        }
        else {
            if (!StringUtils.isNotBlank(yearView.getText().toString())) {
                yearView.setError("Cannot Be Blank");
            }
            if(!StringUtils.isNotBlank(divView.getText().toString())){
                divView.setError("Cannot Be Blank");
            }
            if(!StringUtils.isNotBlank(mobileView.getText().toString())){
                mobileView.setError("Cannot Be Blank");
            }
            Toast.makeText(view.getContext(), "These field cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }
}
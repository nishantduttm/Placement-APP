package com.example.placementapp.student;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.placementapp.Animation.MyBounceInterpolator;
import com.example.placementapp.R;
import com.example.placementapp.activities.LoginActivity;
import com.example.placementapp.activities.RegisterActivity;
import com.example.placementapp.admin.fragments.ViewStudentsProfile;
import com.example.placementapp.constants.Constants;
import com.example.placementapp.helper.FirebaseHelper;
import com.example.placementapp.helper.SharedPrefHelper;
import com.example.placementapp.pojo.StudentUser;
import com.example.placementapp.pojo.User;
import com.example.placementapp.pojo.UserDto;
import com.example.placementapp.utils.HttpUtils;
import com.example.placementapp.utils.StringUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

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
    private EditText cgpa;
    private EditText percentage;
    private EditText sem1;
    private EditText sem2;
    private EditText sem3;
    private EditText sem4;
    private EditText sem5;
    private EditText sem6;
    private EditText sem7;
    private EditText sem8;

    private boolean check;

    private Button save;
    private Button back;

    private UserDto user;

    private String url = Constants.HttpConstants.GET_SPECIFIC_USER_URL;

    //Sem_Results
    private Map<String, EditText> semResults = new HashMap<>();

    public UpdateProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setValuesInProfile() {
        if (user.getYear() != null)
            yearView.setText(user.getYear());
        if (user.getDivision() != 0)
            divView.setText(String.valueOf(user.getDivision()));
        if (user.getMobile() != null)
            mobileView.setText(user.getMobile());
        if (user.getSemResults() != null) {

            Map<String,Double> map = user.getSemResults();

            for(Map.Entry<String,Double> entry : map.entrySet())
            {
                semResults.get(entry.getKey()).setText(String.valueOf(entry.getValue()));
                if (check) {
                    EditText text = semResults.get(entry.getKey());
                    text.setFocusable(false);
                    text.setFocusableInTouchMode(false);
                    text.setClickable(false);
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_update_profile, container, false);

        nameHeadingView = v.findViewById(R.id.userName);
        nameView = v.findViewById(R.id.FullNameValue);
        branchView = v.findViewById(R.id.BranchNameValue);
        prnView = v.findViewById(R.id.prnValue);
        mailView = v.findViewById(R.id.EmailValue);
        yearView = v.findViewById(R.id.ClassNameValue);
        divView = v.findViewById(R.id.DivNameValue);
        mobileView = v.findViewById(R.id.mobileNoValue);
        save = v.findViewById(R.id.saveUserButton);
        back = v.findViewById(R.id.BackButton);
        cgpa = v.findViewById(R.id.overallSgpaValue);
        percentage = v.findViewById(R.id.overallPercentageValue);
        sem1 = v.findViewById(R.id.SgpaSem1Value);
        sem2 = v.findViewById(R.id.SgpaSem2Value);
        sem3 = v.findViewById(R.id.SgpaSem3Value);
        sem4 = v.findViewById(R.id.SgpaSem4Value);
        sem5 = v.findViewById(R.id.SgpaSem5Value);
        sem6 = v.findViewById(R.id.SgpaSem6Value);
        sem7 = v.findViewById(R.id.SgpaSem7Value);
        sem8 = v.findViewById(R.id.SgpaSem8Value);

        cgpa.setFocusable(false);
        cgpa.setFocusableInTouchMode(false);
        cgpa.setClickable(false);

        percentage.setFocusable(false);
        percentage.setFocusableInTouchMode(false);
        percentage.setClickable(false);

        check = SharedPrefHelper.getEntryfromSharedPreferences(v.getContext(), Constants.SharedPrefConstants.KEY_TYPE)
                .equals(Constants.UserTypes.ADMIN);

        storeSemInList(v);

        if (SharedPrefHelper.getEntryfromSharedPreferences(this.getContext(), Constants.SharedPrefConstants.KEY_TYPE)
                .equals(Constants.UserTypes.ADMIN)) {
            Bundle bundle = getArguments();
            user = (UserDto) bundle.getSerializable("profileDetails");

            user_name = user.getName();
            branch = user.getBranch();
            mailId = user.getEmail();
            prn = user.getPrn();

            setValuesInProfile();

            save.setVisibility(View.GONE);
            back.setVisibility(View.VISIBLE);

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment frag = new ViewStudentsProfile();

                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction trans = manager.beginTransaction();

                    trans.replace(R.id.activity_main_frame_layout, frag);
                    trans.addToBackStack(null);
                    trans.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
                    trans.commit();
                }
            });

            divView.setFocusable(false);
            divView.setFocusableInTouchMode(false);
            divView.setClickable(false);

            yearView.setFocusable(false);
            yearView.setFocusableInTouchMode(false);
            yearView.setClickable(false);

            mobileView.setFocusable(false);
            mobileView.setFocusableInTouchMode(false);
            mobileView.setClickable(false);

        } else {
            user_name = SharedPrefHelper.getEntryfromSharedPreferences(this.getContext(), Constants.SharedPrefConstants.KEY_NAME);
            branch = SharedPrefHelper.getEntryfromSharedPreferences(this.getContext(), Constants.SharedPrefConstants.KEY_BRANCH);
            mailId = SharedPrefHelper.getEntryfromSharedPreferences(this.getContext(), Constants.SharedPrefConstants.KEY_MAIL);
            prn = SharedPrefHelper.getEntryfromSharedPreferences(this.getContext(), Constants.SharedPrefConstants.KEY_PRN);

            divView.setFocusable(false);
            divView.setFocusableInTouchMode(false);
            divView.setClickable(false);

            HttpUtils.addRequestToHttpQueue(constructHttpRequest(url, prn), getContext());
        }

        if (user_name != null && branch != null && mailId != null && prn != null) {
            nameHeadingView.setText(user_name);
            nameView.setText(user_name);
            branchView.setText(branch);
            prnView.setText(prn);
            mailView.setText(mailId);
        } else {
            Toast.makeText(v.getContext(), "Error Fetching Data..! Retry Again..!", Toast.LENGTH_SHORT).show();
        }

        save.setOnClickListener(this);

        return v;
    }

    private Request<?> constructHttpRequest(String url, String prn) {
        url = url + prn;
        return new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::processResponse,
                error -> {
                    Log.i("Error", "HTTP Error");
                }
        );
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void processResponse(JSONObject jsonObject) {
        if (jsonObject != null) {
            user = new Gson().fromJson(jsonObject.toString(), UserDto.class);
            setValuesInProfile();
        }
    }

    public void storeSemInList(View v) {

        semResults.put("cgpa", cgpa);
        semResults.put("percentage", percentage);
        semResults.put("sem1", sem1);
        semResults.put("sem2", sem2);
        semResults.put("sem3", sem3);
        semResults.put("sem4", sem4);
        semResults.put("sem5", sem5);
        semResults.put("sem6", sem6);
        semResults.put("sem7", sem7);
        semResults.put("sem8", sem8);
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
            if (flag) {
                Map<String, String> map = new HashMap<>();

                for (Map.Entry<String, EditText> entry : semResults.entrySet())
                    map.put(entry.getKey(), entry.getValue().getText().toString());

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("prn",prn);
                    jsonObject.put("division", div);
                    jsonObject.put("year", year);
                    jsonObject.put("mobile", mobile);

                    JSONObject mapObject = new JSONObject();
                    for(Map.Entry<String,EditText> entry : semResults.entrySet())
                    {
                        if(entry.getValue().length()!=0 && !entry.getKey().equals("cgpa") && !entry.getKey().equals("percentage"))
                            mapObject.put(entry.getKey(),entry.getValue().getText().toString());
                    }

                    jsonObject.put("semResults", mapObject);
                    Log.i("Data", jsonObject.toString());

                    HttpUtils.addRequestToHttpQueue(constructRequest(jsonObject), this.getContext());
                } catch (JSONException e) {
                    Log.i("Error","Http Error");
                }
            }
        } else {
            if (!StringUtils.isNotBlank(yearView.getText().toString())) {
                yearView.setError("Cannot Be Blank");
            }
            if (!StringUtils.isNotBlank(divView.getText().toString())) {
                divView.setError("Cannot Be Blank");
            }
            if (!StringUtils.isNotBlank(mobileView.getText().toString())) {
                mobileView.setError("Cannot Be Blank");
            }
            Toast.makeText(view.getContext(), "These field cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private Request<?> constructRequest(JSONObject jsonObject) {
        return new JsonObjectRequest(
                Request.Method.PUT,
                Constants.HttpConstants.UPDATE_USER_PROFILE_URL,
                jsonObject,
                this::parseResponse,
                error -> {
                    Log.i("Error", "Http Error");
                }
        );
    }

    private void parseResponse(JSONObject resp) {
        if (resp == null) {
            processResponseResult(Constants.StatusEnum.FAILURE);
            return;
        }
        try {
            if (resp.getString(Constants.HttpConstants.KEY_STATUS_CODE).equals(Constants.HttpConstants.SUCCESS)) {
                processResponseResult(Constants.StatusEnum.SUCCESS);
            } else if (resp.getString(Constants.HttpConstants.KEY_STATUS_CODE).equals(Constants.HttpConstants.FAILURE)) {
                processResponseResult(Constants.StatusEnum.FAILURE);
            }
        } catch (JSONException e) {
            processResponseResult(Constants.StatusEnum.FAILURE);
        }
    }

    private void processResponseResult(Constants.StatusEnum result) {
        switch (result) {
            case FAILURE:
                Toast.makeText(this.getContext(), "Profile Update Failed", Toast.LENGTH_SHORT).show();
                break;

            case SUCCESS:
                Toast.makeText(this.getContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
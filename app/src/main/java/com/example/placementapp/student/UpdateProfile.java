package com.example.placementapp.student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Parcelable;
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
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;

public class UpdateProfile extends Fragment implements View.OnClickListener {

    private String user_name;
    private String branch;
    private String mailId;
    private String prn;
    private String year;
    private String div;
    private String mobile;
    private String sscPercentageString;
    private String hscPercentageString;
    private String noOfBacklogsString;
    private String cvUrl;

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
    private EditText sscPercentage;
    private EditText hscPercentage;
    private EditText noOfBacklogs;

    private ProgressDialog loadingbar;

    private boolean adminLoggedIn = false;

    private Button save;
    private Button back;
    private Button uploadCv;
    Uri fileUri;
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
        if (user.getSscPercentage() != null)
            sscPercentage.setText(user.getSscPercentage());
        if (user.getHscPercentage() != null)
            hscPercentage.setText(user.getHscPercentage());
        if (user.getNoOfBacklogs() != null)
            noOfBacklogs.setText(user.getNoOfBacklogs());
        if (user.getSemResults() != null) {

            Map<String,Double> map = user.getSemResults();

            for(Map.Entry<String,Double> entry : map.entrySet())
            {
                semResults.get(entry.getKey()).setText(String.valueOf(entry.getValue()));
                if (adminLoggedIn) {
                    EditText text = semResults.get(entry.getKey());
                    text.setFocusable(false);
                    text.setFocusableInTouchMode(false);
                    text.setClickable(false);
                }
            }
        }
        if(user.getBranch()!= null) branchView.setText(user.getBranch());

        if(user.getName()!= null)
        {
            nameView.setText(user.getName());
            nameHeadingView.setText(user.getName());
        }

        if(user.getPrn()!= null) prnView.setText(user.getPrn());

        if(user.getEmail()!= null) mailView.setText(user.getEmail());

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_update_profile, container, false);

        loadingbar = new ProgressDialog(getContext());
        loadingbar.setTitle("Fetching Student Details");
        loadingbar.setMessage("Please wait while we update your feed");
        loadingbar.setCanceledOnTouchOutside(true);
        loadingbar.show();

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
        uploadCv = v.findViewById(R.id.uploadCvButton);
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
        hscPercentage = v.findViewById(R.id.twelvePercentageValue);
        sscPercentage = v.findViewById(R.id.tenthPercentageValue);
        noOfBacklogs = v.findViewById(R.id.noOfBacklogsValue);

        cgpa.setFocusable(false);
        cgpa.setFocusableInTouchMode(false);
        cgpa.setClickable(false);

        percentage.setFocusable(false);
        percentage.setFocusableInTouchMode(false);
        percentage.setClickable(false);

        adminLoggedIn = SharedPrefHelper.getEntryfromSharedPreferences(v.getContext(), Constants.SharedPrefConstants.KEY_TYPE)
                .equals(Constants.UserTypes.ADMIN);

        storeSemInList(v);

        if (adminLoggedIn) {
            Bundle bundle = getArguments();
            user = (UserDto) bundle.getSerializable("profileDetails");

            HttpUtils.addRequestToHttpQueue(constructHttpRequest(url, user.getPrn()), getContext());

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
            uploadCv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent galleryIntent = new Intent();
                    galleryIntent.setType("application/pdf");
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                    // Chooser of filesystem options.
                    final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

                    // Add the camera options.
                    startActivityForResult(chooserIntent, 1888);
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

            sscPercentage.setFocusable(false);
            sscPercentage.setFocusableInTouchMode(false);
            sscPercentage.setClickable(false);

            hscPercentage.setFocusable(false);
            hscPercentage.setFocusableInTouchMode(false);
            hscPercentage.setClickable(false);

            noOfBacklogs.setFocusable(false);
            noOfBacklogs.setFocusableInTouchMode(false);
            noOfBacklogs.setClickable(false);

        } else {
            prn = SharedPrefHelper.getEntryfromSharedPreferences(this.getContext(), Constants.SharedPrefConstants.KEY_PRN);

            HttpUtils.addRequestToHttpQueue(constructHttpRequest(url, prn), getContext());
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
                    loadingbar.dismiss();
                }
        );
    }
    ProgressDialog dialog;
    @Override
    public  void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            dialog = new ProgressDialog(this.getContext());
            dialog.setMessage("Uploading");
            dialog.show();
            fileUri = data.getData();
            final String timeStamp = ""+ System.currentTimeMillis();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            final String messagePushId = timeStamp;
            final StorageReference filepath = storageReference.child(messagePushId + "." + "pdf");
            filepath.putFile(fileUri).continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri>  task) {
                    if(task.isSuccessful()){
                        dialog.dismiss();
                        Uri uri = task.getResult();
                        cvUrl = uri.toString();
                    }else{
                        dialog.dismiss();
                    }
                }
            });
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void processResponse(JSONObject jsonObject) {
        if (jsonObject != null) {
            user = new Gson().fromJson(jsonObject.toString(), UserDto.class);
            Log.i("UserDtoFromDB",user.toString());
            setValuesInProfile();
            loadingbar.dismiss();
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

        loadingbar = new ProgressDialog(getContext());
        loadingbar.setTitle("Updating Student Profile");
        loadingbar.setMessage("Please wait while we update your feed");
        loadingbar.setCanceledOnTouchOutside(true);
        loadingbar.show();

        year = yearView.getText().toString();
        div = divView.getText().toString();
        mobile = mobileView.getText().toString();
        sscPercentageString = sscPercentage.getText().toString();
        hscPercentageString = hscPercentage.getText().toString();
        noOfBacklogsString = noOfBacklogs.getText().toString();

        boolean flag = true;

        if (StringUtils.isNotBlank(year) && StringUtils.isNotBlank(div) && StringUtils.isNotBlank(mobile) && StringUtils.isNotBlank(sscPercentageString) && StringUtils.isNotBlank(hscPercentageString) && StringUtils.isNotBlank(noOfBacklogsString)) {
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
                    jsonObject.put("sscPercentage", sscPercentageString);
                    jsonObject.put("hscPercentage", hscPercentageString);
                    jsonObject.put("noOfBacklogs", noOfBacklogsString);
                    jsonObject.put("cvUrl", cvUrl);

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
            if (!StringUtils.isNotBlank(sscPercentage.getText().toString())) {
                sscPercentage.setError("Cannot Be Blank");
            }
            if (!StringUtils.isNotBlank(hscPercentage.getText().toString())) {
                hscPercentage.setError("Cannot Be Blank");
            }
            if (!StringUtils.isNotBlank(noOfBacklogs.getText().toString())) {
                noOfBacklogs.setError("Cannot Be Blank");
            }
            Toast.makeText(view.getContext(), "These field cannot be empty", Toast.LENGTH_SHORT).show();
        }
        loadingbar.dismiss();
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
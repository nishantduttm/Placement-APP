package com.example.placementapp.student;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.placementapp.R;
import com.example.placementapp.activities.LoginActivity;
import com.example.placementapp.activities.RegisterActivity;
import com.example.placementapp.constants.Constants;
import com.example.placementapp.helper.FirebaseHelper;
import com.example.placementapp.helper.SharedPrefHelper;
import com.example.placementapp.pojo.ApplicationForm;
import com.example.placementapp.pojo.ApplicationFormDto;
import com.example.placementapp.pojo.FormStatus;
import com.example.placementapp.pojo.UserDto;
import com.example.placementapp.utils.HttpUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class StudentApplicationStatusActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView studentName;
    private TextView studentEmail;
    private TextView studentBranch;
    private TextView studentPRN;
    private TextView companyName;
    private TextView processDate;
    private RadioGroup radioGroup1;
    private RadioGroup radioGroup2;
    RadioButton radioButton1;
    RadioButton radioButton2;
    private DatabaseReference ref;
    private DatabaseReference ref2;
    private DatabaseReference refApplied;
    private FormStatus formStatus;

    private Integer companyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_application_status);
        studentName = findViewById(R.id.student_name);
        studentEmail = findViewById(R.id.student_email_address);
        studentPRN = findViewById(R.id.student_prn);
        companyName = findViewById(R.id.company_name);
        studentBranch = findViewById(R.id.student_branch);
        processDate = findViewById(R.id.process_date);
        radioGroup1 = findViewById(R.id.radioGroup);
        radioGroup2 = findViewById(R.id.radioGroup2);
        Button saveButton = findViewById(R.id.save_button);
        Button resetButton = findViewById(R.id.reset_button);

        setValuesForNonEditable();  //For Setting Values on Non-Editable EditText Views..1

        getAndSetCompanyNameFromIntent();  //For Setting Company Name on EditText..2


        processDate.setOnClickListener(view -> {
            dateDialogInitializer();   //Initializing DateDialog Panel..3
        });

        saveButton.setOnClickListener(this);

        resetButton.setOnClickListener(view -> {

        });
    }

    //    2
    private void getAndSetCompanyNameFromIntent() {
        Intent intent = getIntent();
        companyName.setText(intent.getStringExtra("companyName"));
        companyId = intent.getIntExtra("companyID",-1);
        if(companyId == null)
            companyId = intent.getIntExtra("companyID",-1);
    }

    //    3
    private void dateDialogInitializer() {
        Calendar calendar = Calendar.getInstance();

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePicker = new DatePickerDialog(this, (view, year1, monthOfYear, dayOfMonth) -> processDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1), year, month, day);
        datePicker.show();
    }

    //    1
    private void setValuesForNonEditable() {

        studentName.setText(SharedPrefHelper.getEntryfromSharedPreferences(this, Constants.SharedPrefConstants.KEY_NAME));
        studentEmail.setText(SharedPrefHelper.getEntryfromSharedPreferences(this, Constants.SharedPrefConstants.KEY_MAIL));
        studentBranch.setText(SharedPrefHelper.getEntryfromSharedPreferences(this, Constants.SharedPrefConstants.KEY_BRANCH));
        studentPRN.setText(SharedPrefHelper.getEntryfromSharedPreferences(this, Constants.SharedPrefConstants.KEY_PRN));
    }

    @Override
    public void onClick(View view) {
        if (radioGroup1.getCheckedRadioButtonId() == -1)
            Toast.makeText(this, "Please Select a Process Round!!", Toast.LENGTH_SHORT).show();
        if (radioGroup2.getCheckedRadioButtonId() == -1)
            Toast.makeText(this, "Please Select a Status!!", Toast.LENGTH_SHORT).show();
        if (processDate.length() == 0) {
            Toast.makeText(this, "Please Select the Process Date!!", Toast.LENGTH_SHORT).show();
            processDate.setError("Please Select a Date!!");
        }

        if (radioGroup1.getCheckedRadioButtonId() != -1 && radioGroup2.getCheckedRadioButtonId() != -1 && processDate.length() != 0) {
             radioButton1 = findViewById(radioGroup1.getCheckedRadioButtonId());
             radioButton2 = findViewById(radioGroup2.getCheckedRadioButtonId());

            formStatus = new FormStatus(radioButton1.getText().toString(), processDate.getText().toString());

            updateStatus(constructHttpRequest(radioButton2.getText().toString()));

            Toast.makeText(this, "Application Saved Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    private JsonObjectRequest constructHttpRequest(String overAllStatus) {
        try {
            JSONObject applicationObject = new JSONObject();
            applicationObject.put("prn", studentPRN.getText().toString());
            applicationObject.put("notificationId", companyId);
            applicationObject.put("overallStatus", overAllStatus );

            Log.i("notificationId",applicationObject.toString());

            return new JsonObjectRequest(
                    Request.Method.POST,
                    Constants.HttpConstants.SAVE_APPLICATION_URL,
                    applicationObject,
                    this::parseResponse,
                    error -> processResponseResult(Constants.StatusEnum.FAILURE)
            );
        } catch (JSONException e) {
            return null;
        }
    }

    private void parseResponse(JSONObject resp) {
        if (resp == null) {
            processResponseResult(Constants.StatusEnum.FAILURE);
            return;
        }
        try {
            ApplicationFormDto applicationFormDto = new Gson().fromJson(resp.toString(), ApplicationFormDto.class);
            storeInFireBase(applicationFormDto.getApplicationId());
        } catch (Exception e) {
            processResponseResult(Constants.StatusEnum.FAILURE);
        }
    }

    private void updateStatus(JsonObjectRequest request) {
        if (request == null) {
            processResponseResult(Constants.StatusEnum.FAILURE);
            return;
        }

        HttpUtils.addRequestToHttpQueue(request, this);
    }

    private void processResponseResult(Constants.StatusEnum result) {
    }



    void storeInFireBase(int applicationFormId){
        ref2 = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_APPLICATIONS + applicationFormId + "/" + System.currentTimeMillis());
        ref = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_APPLICATIONS + applicationFormId + "/");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean existsInDb = false;

                if(snapshot != null) {
                    for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                        FormStatus dbFormStatus = childSnapshot.getValue(FormStatus.class);
                        if (formStatus.getProcessRound().equals(dbFormStatus.getProcessRound())) {
                            dbFormStatus.setProcessDate(formStatus.getProcessDate());
                            Map<String,Object> map = new HashMap<>();
                            map.put(childSnapshot.getKey(),dbFormStatus);
                            ref.updateChildren(map);
                            existsInDb = true;
                            break;
                        }
                    }
                    if (!existsInDb)
                        ref2.setValue(formStatus);
                }
                else
                {
                    ref2.setValue(formStatus);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
/*

ref = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_APPLICATIONS + companyId + "/" + studentPRN.getText().toString());
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ApplicationForm retrievedForm = snapshot.getValue(ApplicationForm.class);
                    if (retrievedForm == null) {
                        //Get and set object FormStatus into the FormStatusList
                        retrievedForm = new ApplicationForm(studentEmail.getText().toString(), studentPRN.getText().toString(), studentName.getText().toString(), studentBranch.getText().toString(), companyName.getText().toString(), companyId, new ArrayList<>(), radioButton2.getText().toString());
                        retrievedForm.getFormStatusList().add(formStatus);
                        ref.setValue(retrievedForm);
                    } else {
                        if (!retrievedForm.getFormStatusList().contains(formStatus)) {
                            retrievedForm.getFormStatusList().add(formStatus);
                            retrievedForm.setOverallStatus(radioButton2.getText().toString());
                            ref.setValue(retrievedForm);
                        } else {
                            for (FormStatus status : retrievedForm.getFormStatusList()) {
                                if (status.getProcessRound().equals(formStatus.getProcessRound())) {
                                    status.setProcessDate(formStatus.getProcessDate());
                                    break;
                                }
                            }
                            retrievedForm.setOverallStatus(radioButton2.getText().toString());
                            ref.setValue(retrievedForm);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            refApplied = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_APPILED_COMPANIES + studentPRN.getText().toString() + "/" + companyId);
            refApplied.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ApplicationForm retrievedForm = snapshot.getValue(ApplicationForm.class);
                    if (retrievedForm == null) {
                        //Get and set object FormStatus into the FormStatusList
                        retrievedForm = new ApplicationForm(studentEmail.getText().toString(), studentPRN.getText().toString(), studentName.getText().toString(), studentBranch.getText().toString(), companyName.getText().toString(), companyId, new ArrayList<>(), radioButton2.getText().toString());
                        retrievedForm.getFormStatusList().add(formStatus);
                        refApplied.setValue(retrievedForm);
                    } else {
                        if (!retrievedForm.getFormStatusList().contains(formStatus)) {
                            retrievedForm.getFormStatusList().add(formStatus);
                            retrievedForm.setOverallStatus(radioButton2.getText().toString());
                            refApplied.setValue(retrievedForm);
                        } else {
                            for (FormStatus status : retrievedForm.getFormStatusList()) {
                                if (status.getProcessRound().equals(formStatus.getProcessRound())) {
                                    status.setProcessDate(formStatus.getProcessDate());
                                    break;
                                }
                            }
                            retrievedForm.setOverallStatus(radioButton2.getText().toString());
                            refApplied.setValue(retrievedForm);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
 */
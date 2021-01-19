package com.example.placementapp.student;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.placementapp.R;
import com.example.placementapp.constants.Constants;
import com.example.placementapp.helper.FirebaseHelper;
import com.example.placementapp.helper.SharedPrefHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class StudentApplicationStatusActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView studentName;
    private TextView studentEmail;
    private TextView studentBranch;
    private TextView companyName;
    private EditText processDate;
    private RadioGroup radioGroup1;
    private RadioGroup radioGroup2;
    private RadioButton radioButton1;
    private DatePickerDialog datePicker;
    private Button saveButton;
    private Button resetButton;
    private String radioGroup1input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_application_status);
        studentName = findViewById(R.id.student_name);
        studentEmail = findViewById(R.id.student_email_address);
        companyName = findViewById(R.id.company_name);
        studentBranch = findViewById(R.id.student_branch);
        processDate = findViewById(R.id.process_date);
        radioGroup1 = findViewById(R.id.radioGroup);
        radioGroup2 = findViewById(R.id.radioGroup2);
        saveButton = findViewById(R.id.save_button);
        resetButton = findViewById(R.id.reset_button);

        setValuesForNonEditable();  //For Setting Values on Non-Editable EditText Views..1
        
        getandSetCompanyNameFromIntent();  //For Setting Company Name on EditText..2

        processDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateDialogInitializer();   //Initializing DateDialog Panel..3
            }
        });

        saveButton.setOnClickListener(this);

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    //    2
    private void getandSetCompanyNameFromIntent() {
        Intent intent = getIntent();
        companyName.setText(intent.getStringExtra("companyName"));
    }

    //    3
    private void dateDialogInitializer() {
        Calendar calendar = Calendar.getInstance();

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                processDate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            }
        },day,month,year);
        datePicker.show();
    }

//    1
    private void setValuesForNonEditable() {

        studentName.setText(SharedPrefHelper.getEntryfromSharedPreferences(this, Constants.SharedPrefConstants.KEY_NAME));
        studentEmail.setText(SharedPrefHelper.getEntryfromSharedPreferences(this, Constants.SharedPrefConstants.KEY_MAIL));
        studentBranch.setText(SharedPrefHelper.getEntryfromSharedPreferences(this, Constants.SharedPrefConstants.KEY_BRANCH));

        //Later Add getExtra from Intent for companyName
    }

    @Override
    public void onClick(View view) {
        if(radioGroup1.getCheckedRadioButtonId()==-1)
            Toast.makeText(this, "Please Select a Process Round!!", Toast.LENGTH_SHORT).show();
        if(processDate.length()==0) {
            Toast.makeText(this, "Please Select the Process Date!!", Toast.LENGTH_SHORT).show();
            processDate.setError("Please Select a Date!!");
        }

//        if(radioGroup1.getCheckedRadioButtonId()!=-1 && processDate.length()!=0)
//        {
//            radioGroup1input = (String) ((RadioButton)findViewById(radioGroup1.getCheckedRadioButtonId())).getText();
//            DatabaseReference ref = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.)
//        }
    }
}
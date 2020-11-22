package com.example.placementapp.student;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.placementapp.R;
import com.example.placementapp.constants.Constants;
import com.example.placementapp.helper.SharedPrefHelper;

import java.util.Calendar;

public class StudentApplicationStatusActivity extends AppCompatActivity {

    private TextView studentName;
    private TextView studentEmail;
    private TextView studentBranch;
    private TextView companyName;
    private EditText processDate;
    private DatePickerDialog datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_application_status);
        studentName = findViewById(R.id.student_name);
        studentEmail = findViewById(R.id.student_email_address);
        companyName = findViewById(R.id.company_name);
        studentBranch = findViewById(R.id.student_branch);
        processDate=findViewById(R.id.process_date);

        setValuesForNonEditable();  //For Setting Values on Non-Editable EditText Views..1

        processDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateDialogInitializer();   //Initializing DateDialog Panel..2
            }
        });
    }

//    2
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
}
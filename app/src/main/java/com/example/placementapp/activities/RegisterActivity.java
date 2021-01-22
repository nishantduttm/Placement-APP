package com.example.placementapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.placementapp.Animation.MyBounceInterpolator;
import com.example.placementapp.R;
import com.example.placementapp.constants.Constants;
import com.example.placementapp.helper.FirebaseHelper;
import com.example.placementapp.pojo.StudentUser;
import com.example.placementapp.pojo.User;
import com.example.placementapp.utils.StringUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener, AdapterView.OnItemSelectedListener {

    public FirebaseAuth auth;
    public DatabaseReference ref;
    public Button RegisterButton;
    public EditText passwordView;
    public EditText emailView;
    public EditText nameView;
    public EditText prnView;
    public ProgressBar progressBar;

    public Spinner dropdown;

    public String password;
    public String prn;
    public String name;
    public String branch;
    private TextView loginView;
    public StudentUser su;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);
        RegisterButton = findViewById(R.id.registerButton);
        passwordView = findViewById(R.id.password);
        emailView = findViewById(R.id.email);
        nameView = findViewById(R.id.Name);
        loginView = findViewById(R.id.loginHereView);
        prnView = findViewById(R.id.PRN);
        dropdown = findViewById(R.id.streamDropdown);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        auth = FirebaseAuth.getInstance();


        String[] dropdownArray = {"Select Stream", "Comp", "Mech", "Civil", "MechSandwich"};
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, R.layout.spinnert_row, R.id.streamDropdown, dropdownArray);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);

        loginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
        RegisterButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String checkEmail = null;
        Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce_animation);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.05, 5);
        myAnim.setInterpolator(interpolator);
        view.startAnimation(myAnim);
        password = passwordView.getText().toString();
        name = nameView.getText().toString();
        prn = prnView.getText().toString();
        Pattern pattern1 = Pattern.compile("[a-zA-Z]+[.]+[a-zA-Z]+@indiraicem.ac.in");
        Pattern pattern2 = Pattern.compile("[0-9]{8}+[A-Z]");
        checkEmail = emailView.getText().toString();

        if (StringUtils.isNotBlank(checkEmail) && StringUtils.isNotBlank(prn)) {

            if (StringUtils.isNotBlank(password) && StringUtils.isNotBlank(name) && pattern1.matcher(checkEmail).matches() && pattern2.matcher(prn).matches()) {
                progressBar.setVisibility(View.VISIBLE);
                ref = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_LOGIN + "/" + prn);
                ref.addListenerForSingleValueEvent(this);
            } else {
                if (!pattern1.matcher(checkEmail).matches()) {
                    emailView.setError("Invalid Email Format");
                    Toast.makeText(RegisterActivity.this, "Invalid Email Format", Toast.LENGTH_SHORT).show();
                }
                if (!pattern2.matcher(prn).matches()) {
                    prnView.setError("Invalid PRN!!Check LowerCase");
                    Toast.makeText(RegisterActivity.this, "Invalid PRN Format", Toast.LENGTH_SHORT).show();
                }
                if (!StringUtils.isNotBlank(password)) {
                    passwordView.setError("Cannot Be Blank");
                    Toast.makeText(RegisterActivity.this, "This field cannot be empty", Toast.LENGTH_SHORT).show();
                }
                if (!StringUtils.isNotBlank(name)) {
                    nameView.setError("Cannot Be Blank");
                    Toast.makeText(RegisterActivity.this, "This field cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
                if(!StringUtils.isNotBlank(emailView.getText().toString()))
                {
                    emailView.setError("Cannot Be Blank");
                }
                else
                {
                    prnView.setError("Cannot Be Blank");
                }
            Toast.makeText(RegisterActivity.this, "This field cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        su = snapshot.getValue(StudentUser.class);
        if (su != null) {
            progressBar.setVisibility(View.GONE);
            emailView.setError("Account Already Registered");
            Toast.makeText(RegisterActivity.this, "Account Already Registered.!", Toast.LENGTH_SHORT).show();
        } else {
            su = new StudentUser(emailView.getText().toString(), password, name, Constants.UserTypes.STUDENT, branch,prn);
            ref.setValue(su);
            progressBar.setVisibility(View.GONE);
            Toast.makeText(RegisterActivity.this, "Registered Successfully..Please Login now", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        branch = String.valueOf(dropdown.getSelectedItem());
        if (i == 0) {
            dropdown.setSelection(1);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}

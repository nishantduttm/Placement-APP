package com.example.placementapp.activities;

import android.content.Intent;
import android.os.Bundle;
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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.placementapp.Animation.MyBounceInterpolator;
import com.example.placementapp.R;
import com.example.placementapp.constants.Constants;
import com.example.placementapp.utils.HttpUtils;
import com.example.placementapp.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String[] dropdownArray = {"Select Stream", "Comp", "Mech", "Civil", "MechSandwich"};

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
    private String email = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_register);
        RegisterButton = findViewById(R.id.registerButton);
        passwordView = findViewById(R.id.password);
        emailView = findViewById(R.id.email);
        nameView = findViewById(R.id.Name);
        TextView loginView = findViewById(R.id.loginHereView);
        prnView = findViewById(R.id.PRN);
        dropdown = findViewById(R.id.streamDropdown);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this, R.layout.spinnert_row, R.id.streamDropdown, dropdownArray);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);

        loginView.setOnClickListener(view -> {
            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(i);
        });

        RegisterButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce_animation);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.05, 5);
        myAnim.setInterpolator(interpolator);
        view.startAnimation(myAnim);
        password = passwordView.getText().toString();
        name = nameView.getText().toString();
        prn = prnView.getText().toString();
        Pattern pattern1 = Pattern.compile("[a-zA-Z]+[.]+[a-zA-Z]+@indiraicem.ac.in");
        Pattern pattern2 = Pattern.compile("[0-9]{8}+[A-Z]");
        email = emailView.getText().toString();

        if (StringUtils.isNotBlank(email) && StringUtils.isNotBlank(prn)) {
            if (StringUtils.isNotBlank(password) && StringUtils.isNotBlank(name) && pattern1.matcher(email).matches() && pattern2.matcher(prn).matches()) {
                progressBar.setVisibility(View.VISIBLE);
                // Call the Async Task runner
                registerUser(constructHttpRequest());
            } else {
                if (!pattern1.matcher(email).matches()) {
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
            if (!StringUtils.isNotBlank(emailView.getText().toString())) {
                emailView.setError("Cannot Be Blank");
            } else {
                prnView.setError("Cannot Be Blank");
            }
            Toast.makeText(RegisterActivity.this, "This field cannot be empty", Toast.LENGTH_SHORT).show();
        }
    }

    private JsonObjectRequest constructHttpRequest() {
        try {
            JSONObject registrationObject = new JSONObject();
            registrationObject.put("prn", prn);
            registrationObject.put("name", name);
            registrationObject.put("email", email);
            registrationObject.put("password", password);
            registrationObject.put("userType", Constants.UserTypes.STUDENT);
            registrationObject.put("branch", branch);

            return new JsonObjectRequest(
                    Request.Method.POST,
                    Constants.HttpConstants.USER_REGISTRATION_URL,
                    registrationObject,
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
            if (resp.getString(Constants.HttpConstants.KEY_STATUS_CODE).equals(Constants.HttpConstants.USER_REGISTERED)) {
                processResponseResult(Constants.StatusEnum.SUCCESS);
            } else if (resp.getString(Constants.HttpConstants.KEY_STATUS_CODE).equals(Constants.HttpConstants.EXISTING_USER)) {
                processResponseResult(Constants.StatusEnum.INVALID);
            }
        } catch (JSONException e) {
            processResponseResult(Constants.StatusEnum.FAILURE);
        }
    }

    private void registerUser(JsonObjectRequest request) {
        if (request == null) {
            processResponseResult(Constants.StatusEnum.FAILURE);
            return;
        }

        HttpUtils.addRequestToHttpQueue(request, this);
    }

    private void processResponseResult(Constants.StatusEnum result) {
        progressBar.setVisibility(View.GONE);
        switch (result) {
            case FAILURE:
                Toast.makeText(RegisterActivity.this, "System Error. Please try again", Toast.LENGTH_SHORT).show();
                break;

            case INVALID:
                emailView.setError("Account Already Registered");
                Toast.makeText(RegisterActivity.this, "Account Already Registered.!", Toast.LENGTH_SHORT).show();
                break;

            case SUCCESS:
                Toast.makeText(RegisterActivity.this, "Registered Successfully..Please Login now", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(i);
        }
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

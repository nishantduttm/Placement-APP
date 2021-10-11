package com.example.placementapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.placementapp.Animation.MyBounceInterpolator;
import com.example.placementapp.R;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public DatabaseReference ref;
    public Button loginButton;
    public EditText prnView;
    public EditText passwordView;
    public TextView registerView;
    public ProgressBar progressBar;
    public String password;
    public String prn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.loginButton);
        prnView = findViewById(R.id.studentprn);
        passwordView = findViewById(R.id.password);
        registerView = findViewById(R.id.registerHereView);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        loginButton.setOnClickListener(this);

        registerView.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);
        });
    }

    @Override
    public void onClick(View view) {
        Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce_animation);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.05, 5);
        myAnim.setInterpolator(interpolator);
        view.startAnimation(myAnim);
        progressBar.setVisibility(View.VISIBLE);
        prn = prnView.getText().toString();
        password = passwordView.getText().toString();
        checkLoginStatus(prn, password);
    }

    private void checkLoginStatus(String username, String password) {
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            loginUser(constructHttpRequest(username, password));
        } else {
            if (!StringUtils.isNotBlank(username)) {
                prnView.setError("Cannot Be Blank");
                Toast.makeText(LoginActivity.this, "PRN or Password cannot be empty", Toast.LENGTH_SHORT).show();
            }
            if (!StringUtils.isNotBlank(password)) {
                passwordView.setError("Cannot Be Blank");
                Toast.makeText(LoginActivity.this, "PRN or Password cannot be empty", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loginUser(JsonObjectRequest jsonObjectRequest) {
        if (jsonObjectRequest == null)
            processResponseResult(Constants.StatusEnum.FAILURE);

        else {
            HttpUtils.addRequestToHttpQueue(jsonObjectRequest, this);
        }
    }

    private JsonObjectRequest constructHttpRequest(String username, String password) {

        try {
            JSONObject loginObject = new JSONObject();
            loginObject.put("prn", username);
            loginObject.put("password", password);

            return new JsonObjectRequest(
                    Request.Method.POST,
                    Constants.HttpConstants.USER_LOGIN_URL,
                    loginObject,
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
            UserDto userDto = new Gson().fromJson(resp.toString(), UserDto.class);
            Log.i("User", userDto.toString());

            if (userDto.isLoginSuccessful()) {
                processResponseResult(Constants.StatusEnum.SUCCESS);
                storeInSharedPref(userDto);
                redirectIntent();
            } else
                processResponseResult(Constants.StatusEnum.INVALID);

        } catch (Exception e) {
            processResponseResult(Constants.StatusEnum.FAILURE);
        }
    }

    private void processResponseResult(Constants.StatusEnum result) {
        progressBar.setVisibility(View.GONE);
        switch (result) {
            case FAILURE:
                Toast.makeText(LoginActivity.this, "System Error. Please try again", Toast.LENGTH_SHORT).show();
                break;

            case INVALID:
                Toast.makeText(LoginActivity.this, "Invalid Login Credentials", Toast.LENGTH_SHORT).show();
                break;

            case SUCCESS:
                Toast.makeText(LoginActivity.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public boolean checkPassword(String password, User u) {
        return password.equals(u.getPassword());
    }

    public void storeInSharedPref(UserDto u) {
        SharedPrefHelper.saveEntryinSharedPreferences(getContext(), Constants.SharedPrefConstants.KEY_NAME, u.getName());
        SharedPrefHelper.saveEntryinSharedPreferences(getContext(), Constants.SharedPrefConstants.KEY_PRN, u.getPrn());

        if (StringUtils.isNotBlank(u.getBranch()) && StringUtils.isNotBlank(u.getEmail())) {
            SharedPrefHelper.saveEntryinSharedPreferences(getContext(), Constants.SharedPrefConstants.KEY_BRANCH, u.getBranch());
            SharedPrefHelper.saveEntryinSharedPreferences(getContext(), Constants.SharedPrefConstants.KEY_MAIL, u.getEmail());
        }

        SharedPrefHelper.saveEntryinSharedPreferences(getContext(), Constants.SharedPrefConstants.KEY_TYPE, u.getUserType());
    }

    public void redirectIntent() {
        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
    }

    public Context getContext() {
        return this.getApplicationContext();
    }


    public void onBackPressed() {
        finishAffinity();
    }

}
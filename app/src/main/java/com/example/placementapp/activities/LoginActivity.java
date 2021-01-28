package com.example.placementapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.placementapp.Animation.MyBounceInterpolator;
import com.example.placementapp.R;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {
    public DatabaseReference ref;
    public Button loginButton;
    public EditText prnView;
    public EditText passwordView;
    public TextView registerView;
    public ProgressBar progressBar;
    public String password;
    public String prn;

    private int loginStatus;

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
        prn = prnView.getText().toString();
        password = passwordView.getText().toString();
        checkLoginStatus(prn, password);
    }

    private class LoginTaskRunner extends AsyncTask<DataSnapshot, String, Constants.StatusEnum> {

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Constants.StatusEnum doInBackground(DataSnapshot... snapshots) {
            return validateLogin(snapshots[0]);
        }

        @Override
        protected void onPostExecute(Constants.StatusEnum result) {
            progressBar.setVisibility(View.GONE);
            switch (result) {
                case SUCCESS:
                    Toast.makeText(LoginActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                    redirectIntent();
                    break;
                case INCORRECT:
                    passwordView.setError("Incorrect Password!");
                    Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                    break;
                case INVALID:
                    Toast.makeText(LoginActivity.this, "No Account Registered! Please Register First!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkLoginStatus(String username, String password) {
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            ref = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_LOGIN + "/" + username);
            ref.addListenerForSingleValueEvent(this);
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

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        new LoginTaskRunner().execute(snapshot);
    }

    private Constants.StatusEnum validateLogin(@NonNull DataSnapshot snapshot) {
        StudentUser su = snapshot.getValue(StudentUser.class);
        if (su != null) {
            if (checkPassword(password, su)) {
                storeInSharedPref(su);
                return Constants.StatusEnum.SUCCESS;
            } else {
                return Constants.StatusEnum.INCORRECT;
            }
        } else {
            User u = snapshot.getValue(User.class);
            if (u != null) {
                if (checkPassword(password, u)) {
                    storeInSharedPref(u);
                    return Constants.StatusEnum.SUCCESS;
                } else {
                    return Constants.StatusEnum.INCORRECT;
                }
            } else {
                return Constants.StatusEnum.INVALID;
            }
        }
    }

    public boolean checkPassword(String password, User u) {
        return password.equals(u.getPassword());
    }

    public void storeInSharedPref(User u) {
        SharedPrefHelper.saveEntryinSharedPreferences(getContext(), Constants.SharedPrefConstants.KEY_NAME, u.getName());
        if (u instanceof StudentUser) {
            StudentUser su = (StudentUser) u;
            SharedPrefHelper.saveEntryinSharedPreferences(getContext(), Constants.SharedPrefConstants.KEY_BRANCH, su.getBranch());
            SharedPrefHelper.saveEntryinSharedPreferences(getContext(), Constants.SharedPrefConstants.KEY_PRN, su.getPrn());
        }
        SharedPrefHelper.saveEntryinSharedPreferences(getContext(), Constants.SharedPrefConstants.KEY_PASSWORD, password);
        SharedPrefHelper.saveEntryinSharedPreferences(getContext(), Constants.SharedPrefConstants.KEY_MAIL, u.getMail());
        SharedPrefHelper.saveEntryinSharedPreferences(getContext(), Constants.SharedPrefConstants.KEY_TYPE, String.valueOf(u.getType()));
    }

    public void redirectIntent() {
        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
    }

    public Context getContext() {
        return this.getApplicationContext();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Toast.makeText(LoginActivity.this, "System Issue.. Please try again later", Toast.LENGTH_SHORT).show();
    }
    public void onBackPressed() {
       finishAffinity();
    }

}
package com.example.placementapp.activities;

import android.content.Context;
import android.content.Intent;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {

    public DatabaseReference ref;
    public FirebaseAuth auth;
    public Button loginButton;
    public EditText emailView;
    public EditText passwordView;
    public TextView registerView;
    public ProgressBar progressBar;
    public String email;
    public String emailCropped;
    public String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        String check = SharedPrefHelper.getEntryfromSharedPreferences(this.getApplicationContext(), Constants.SharedPrefConstants.KEY_MAIL);
        if (check != null) {
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(intent);
        }
        loginButton = findViewById(R.id.loginButton);
        emailView = findViewById(R.id.email);
        passwordView = findViewById(R.id.password);
        registerView = findViewById(R.id.registerHereView);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        auth = FirebaseAuth.getInstance();
        loginButton.setOnClickListener(this);
        registerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onClick(View view) {
        Animation myAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounce_animation);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.05, 5);
        myAnim.setInterpolator(interpolator);
        view.startAnimation(myAnim);
        email = emailView.getText().toString();
        password = passwordView.getText().toString();
        checkLoginStatus(email, password);
    }

    private void checkLoginStatus(String username, String password) {
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            String[] temp = emailView.getText().toString().split("@");
            String firstValue = temp[0];
            String[] temp1 = firstValue.split("\\.");
            emailCropped = temp1[0] + temp1[1];
            ref = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_LOGIN + "/" + emailCropped);
            progressBar.setVisibility(View.VISIBLE);
            ref.addListenerForSingleValueEvent(this);
        } else {
            if (!StringUtils.isNotBlank(username)) {
                emailView.setError("Cannot Be Blank");
                Toast.makeText(LoginActivity.this, "Email or Password cannot be empty", Toast.LENGTH_SHORT).show();
            }
            if (!StringUtils.isNotBlank(password)) {
                passwordView.setError("Cannot Be Blank");
                Toast.makeText(LoginActivity.this, "Email or Password cannot be empty", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        StudentUser su = snapshot.getValue(StudentUser.class);
        if (su != null) {
            if (checkPassword(password,su)) {
                progressBar.setVisibility(View.GONE);
                storeInSharedPref(su);
                Toast.makeText(LoginActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
            }
            else {
                progressBar.setVisibility(View.GONE);
                passwordView.setError("Incorrect Password!");
                Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            User u = snapshot.getValue(User.class);
            if(u!=null)
            {
                if(checkPassword(password,u))
                {
                    progressBar.setVisibility(View.GONE);
                    storeInSharedPref(u);
                    Toast.makeText(LoginActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                    passwordView.setError("Incorrect Password!");
                    Toast.makeText(LoginActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "No Account Registered! Please Register First!", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public boolean checkPassword(String password, User u)
    {
        return password.equals(u.getPassword());
    }

    public void storeInSharedPref(User u)
    {
        SharedPrefHelper.saveEntryinSharedPreferences(getContext(), Constants.SharedPrefConstants.KEY_NAME, u.getName());
        if(u instanceof StudentUser)
        {
            StudentUser su = (StudentUser) u;
            SharedPrefHelper.saveEntryinSharedPreferences(getContext(), Constants.SharedPrefConstants.KEY_BRANCH, su.getBranch());
        }
        SharedPrefHelper.saveEntryinSharedPreferences(getContext(), Constants.SharedPrefConstants.KEY_PASSWORD, password);
        SharedPrefHelper.saveEntryinSharedPreferences(getContext(), Constants.SharedPrefConstants.KEY_MAIL, u.getMail());
        SharedPrefHelper.saveEntryinSharedPreferences(getContext(), Constants.SharedPrefConstants.KEY_TYPE, String.valueOf(u.getType()));
        redirectIntent();
    }


    public void redirectIntent()
    {
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(intent);
    }


    public Context getContext() {
        return this.getApplicationContext();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}
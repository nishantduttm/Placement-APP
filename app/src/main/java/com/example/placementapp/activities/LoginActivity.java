package com.example.placementapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.placementapp.R;
import com.example.placementapp.helper.FirebaseHelper;
import com.example.placementapp.utils.StringUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {

    public DatabaseReference ref;
    public Button loginButton;
    public EditText usernameView;
    public EditText passwordView;
    public String username;
    public String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton = findViewById(R.id.loginButton);
        usernameView = findViewById(R.id.username);
        passwordView = findViewById(R.id.password);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        username = usernameView.getText().toString();
        password = passwordView.getText().toString();
        checkLoginStatus(username,password);
    }

    private void checkLoginStatus(String username, String password) {
        if(StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password))
        {
            ref = FirebaseHelper.getFirebaseReference("Login" + "/" + username);
            if(ref!=null)
            {
                ref.addListenerForSingleValueEvent(this);
            }
        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if(snapshot!=null)
        {

        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}
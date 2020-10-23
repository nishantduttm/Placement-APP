package com.example.placementapp.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.placementapp.R;
import com.example.placementapp.constants.Constants;
import com.example.placementapp.helper.FirebaseHelper;
import com.example.placementapp.helper.GMailSender;
import com.example.placementapp.helper.SharedPrefHelper;
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
    public EditText usernameView;
    public EditText passwordView;
    public ProgressBar progressBar;
    public String username;
    public String password;

    private class MyTask extends AsyncTask<Void, Void, Void> {
        String result;

        @Override
        protected Void doInBackground(Void... voids) {
            GMailSender sender = new GMailSender("nishantduttmishra@gmail.com", "9709848722");
            try {
                sender.sendMail("This is Subject",
                        "This is Body",
                        "nishantduttmishra@gmail.com",
                        "nishantd.mishra@gmail.com");
            } catch (Exception e) {
                Log.e("SendMail ", e.getMessage(), e);
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);
        loginButton = findViewById(R.id.loginButton);
        usernameView = findViewById(R.id.username);
        passwordView = findViewById(R.id.password);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        loginButton.setOnClickListener(this);
        new MyTask().execute();
}

    @Override
    public void onClick(View view) {
        username = usernameView.getText().toString();
        password = passwordView.getText().toString();
        checkLoginStatus(username, password);
    }

    private void checkLoginStatus(String username, String password) {
        if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
            ref = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_LOGIN + "/" + username);
            progressBar.setVisibility(View.VISIBLE);
            ref.addListenerForSingleValueEvent(this);
        } else {
            if (!StringUtils.isNotBlank(username)) {
                usernameView.setError("Cannot Be Blank");
                Toast.makeText(LoginActivity.this, "Username or Password cannot be empty", Toast.LENGTH_SHORT).show();
            }
            if (!StringUtils.isNotBlank(username)) {
                passwordView.setError("Cannot Be Blank");
                Toast.makeText(LoginActivity.this, "Username or Password cannot be empty", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        User u = snapshot.getValue(User.class);
        if (u != null) {
            if (password.equals(u.getPassword())) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Logged In Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                SharedPrefHelper.saveEntryinSharedPreferences(this.getApplicationContext(), Constants.SharedPrefConstants.KEY_PASSWORD, password);
                SharedPrefHelper.saveEntryinSharedPreferences(this.getApplicationContext(), Constants.SharedPrefConstants.KEY_MAIL, u.getMail());
                SharedPrefHelper.saveEntryinSharedPreferences(this.getApplicationContext(), Constants.SharedPrefConstants.KEY_USERNAME, username);
                SharedPrefHelper.saveEntryinSharedPreferences(this.getApplicationContext(), Constants.SharedPrefConstants.KEY_TYPE, String.valueOf(u.getType()));
                startActivity(intent);
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                passwordView.setError("Wrong Password!");
            }
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}
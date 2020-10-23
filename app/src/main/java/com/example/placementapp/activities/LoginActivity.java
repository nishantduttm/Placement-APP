package com.example.placementapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.placementapp.R;
import com.example.placementapp.helper.FirebaseHelper;
import com.example.placementapp.helper.GMailSender;
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
                Log.e("SendMail", e.getMessage(), e);
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
        setContentView(R.layout.activity_login);
        loginButton = findViewById(R.id.loginButton);
        usernameView = findViewById(R.id.username);
        passwordView = findViewById(R.id.password);
        loginButton.setOnClickListener(this);
        new MyTask().execute();
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
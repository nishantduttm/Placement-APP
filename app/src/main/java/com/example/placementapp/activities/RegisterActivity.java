package com.example.placementapp.activities;

        import android.content.Intent;
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

        import com.example.placementapp.Animation.MyBounceInterpolator;
        import com.example.placementapp.R;
        import com.example.placementapp.constants.Constants;
        import com.example.placementapp.helper.FirebaseHelper;
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
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, ValueEventListener {

    public FirebaseAuth auth;
    public DatabaseReference ref;
    public Button RegisterButton;
    public EditText passwordView;
    public EditText emailView;
    public ProgressBar progressBar;
    public String password;
    public String email;
    public int check;
    private TextView loginView;
    public User u;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);
        RegisterButton = findViewById(R.id.registerButton);
        passwordView = findViewById(R.id.password);
        emailView = findViewById(R.id.email);
        loginView = findViewById(R.id.loginHereView);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        auth = FirebaseAuth.getInstance();
        loginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
            }
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
        if(StringUtils.isNotBlank(emailView.getText().toString()) && StringUtils.isNotBlank(password)) {
            String [] temp = emailView.getText().toString().split("@");
            String firstValue = temp[0];
            String[] temp1 = firstValue.split("\\.");
            email = temp1[0]+temp1[1];
            progressBar.setVisibility(View.VISIBLE);
            check = 1;
            ref = FirebaseHelper.getFirebaseReference(Constants.FirebaseConstants.PATH_LOGIN + "/" + email);
            ref.addListenerForSingleValueEvent(this);
            if(check == 1) {
                auth.createUserWithEmailAndPassword(emailView.getText().toString(), password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            auth.getCurrentUser().sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                u = new User(emailView.getText().toString(),password,Constants.UserTypes.STUDENT);
                                                ref.setValue(u);
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(RegisterActivity.this, "Registered Successfully..Please Check your Email and Login Again", Toast.LENGTH_SHORT).show();
                                                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                                                startActivity(i);
                                            } else{
                                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        }
                    }
                });
            }
        }
        else{
            if (!StringUtils.isNotBlank(email)) {
                emailView.setError("Cannot Be Blank");
                Toast.makeText(RegisterActivity.this, "This field cannot be empty", Toast.LENGTH_SHORT).show();
            }
            if (!StringUtils.isNotBlank(password)) {
                passwordView.setError("Cannot Be Blank");
                Toast.makeText(RegisterActivity.this, "Username or Password cannot be empty", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        u = snapshot.getValue(User.class);
        if(u!=null)
        {
            progressBar.setVisibility(View.GONE);
            emailView.setError("Email Already Registered");
            Toast.makeText(RegisterActivity.this, "Email Already Registered.!", Toast.LENGTH_SHORT).show();
            check = 0;
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}

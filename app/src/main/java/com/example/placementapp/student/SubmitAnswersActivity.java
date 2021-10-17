package com.example.placementapp.student;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.placementapp.R;
import com.example.placementapp.constants.Constants;
import com.example.placementapp.helper.SharedPrefHelper;
import com.example.placementapp.utils.HttpUtils;
import com.example.placementapp.utils.StringUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

public class SubmitAnswersActivity extends AppCompatActivity {

    private TextView questionTV;
    private EditText answerET;
    private Button submit;
    private String question;
    private Integer questionID;
    private String answer, answerID, username;
    private String studentName;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_answers);

        questionTV = findViewById(R.id.questionTV);
        answerET = findViewById(R.id.answerET);
        submit = findViewById(R.id.save_answer);

        Intent intent = getIntent();
        question = intent.getStringExtra("question");
        questionID = intent.getIntExtra("questionID",-1);

        studentName = SharedPrefHelper.getEntryfromSharedPreferences(this.getApplicationContext(), Constants.SharedPrefConstants.KEY_NAME);

        questionTV.setText(question);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answer = answerET.getText().toString();

                if(StringUtils.isNotBlank(answer))
                {
                    submitAnswer(answer);
                }
                else
                {
                    answerET.setError("Answer Cannot Be Empty");
                    Toast.makeText(SubmitAnswersActivity.this, "Answer Cannot Be Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void submitAnswer(String answer) {
        HttpUtils.addRequestToHttpQueue(constructHttpRequest(answer),this);
    }

    private Request<?> constructHttpRequest(String answer) {
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("questionId",questionID);
            jsonObject.put("answer",answer);
            jsonObject.put("studentName",studentName);

            return new JsonObjectRequest(
                    Request.Method.POST,
                    Constants.HttpConstants.SUBMIT_ANSWER_URL,
                    jsonObject,
                    this::parseResponse,
                    error -> {processResponseResult(Constants.StatusEnum.FAILURE);}
            );
        }
        catch(JSONException e)
        {
            processResponseResult(Constants.StatusEnum.FAILURE);
        }
        return null;
    }

    private void processResponseResult(Constants.StatusEnum result) {
        switch (result) {
            case FAILURE:
                Toast.makeText(SubmitAnswersActivity.this, "System Error. Please try again", Toast.LENGTH_SHORT).show();
                break;

            case SUCCESS:
                Toast.makeText(SubmitAnswersActivity.this, "Answer Posted Successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private void parseResponse(JSONObject resp) {
        if (resp == null) {
            processResponseResult(Constants.StatusEnum.FAILURE);
            return;
        }
        try {
            if (resp.getString(Constants.HttpConstants.KEY_STATUS_CODE).equals(Constants.HttpConstants.SUCCESS)) {
                processResponseResult(Constants.StatusEnum.SUCCESS);
            } else if (resp.getString(Constants.HttpConstants.KEY_STATUS_CODE).equals(Constants.HttpConstants.FAILURE)) {
                processResponseResult(Constants.StatusEnum.FAILURE);
            }
        } catch (JSONException e) {
            processResponseResult(Constants.StatusEnum.FAILURE);
        }
    }
}

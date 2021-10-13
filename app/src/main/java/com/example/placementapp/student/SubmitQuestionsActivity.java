package com.example.placementapp.student;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import com.example.placementapp.constants.Constants;
import com.example.placementapp.utils.HttpUtils;
import com.example.placementapp.utils.StringUtils;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.placementapp.R;

import org.json.JSONException;
import org.json.JSONObject;

public class SubmitQuestionsActivity extends AppCompatActivity {

    private EditText questionET;
    private Button submit;

    String question;
    String url = Constants.HttpConstants.SUBMIT_QUESTION_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_questions);

        questionET = findViewById(R.id.questionET);
        submit = findViewById(R.id.save_question);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(questionET!=null)
                {
                    question = questionET.getText().toString();
                    if(StringUtils.isNotBlank(question))
                    {
                        submitQuestion(question);
                    }
                    else
                    {
                        questionET.setError("Cannot Be Blank..!");
                        Toast.makeText(getBaseContext(), "Question Cannot be Blank", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
    }

    private void submitQuestion(String question) {
        HttpUtils.addRequestToHttpQueue(constructHttpRequest(question),this);
    }

    private Request<?> constructHttpRequest(String question) {
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("question",question);

            return new JsonObjectRequest(
                    Request.Method.POST,
                    url,
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

    private void processResponseResult(Constants.StatusEnum result) {
        switch (result) {
            case FAILURE:
                Toast.makeText(SubmitQuestionsActivity.this, "System Error. Please try again", Toast.LENGTH_SHORT).show();
                break;

            case SUCCESS:
                Toast.makeText(SubmitQuestionsActivity.this, "Question Posted Successfully", Toast.LENGTH_SHORT).show();
        }
    }

}
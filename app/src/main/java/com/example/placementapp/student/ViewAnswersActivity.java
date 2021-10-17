package com.example.placementapp.student;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.placementapp.Adapters.RecyclerViewAdapterViewAnswers;
import com.example.placementapp.Adapters.RecyclerViewAdapterViewQuestions;
import com.example.placementapp.R;
import com.example.placementapp.constants.Constants;
import com.example.placementapp.pojo.AnswerDto;
import com.example.placementapp.pojo.AnswersList;
import com.example.placementapp.pojo.QuestionDto;
import com.example.placementapp.pojo.QuestionsList;
import com.example.placementapp.utils.HttpUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewAnswersActivity extends AppCompatActivity {

    private List<AnswerDto> answerList;
    private TextView questionTV;
    private String question;
    private Integer questionID;
    private RecyclerViewAdapterViewAnswers madapter;
    private RecyclerView recyclerView;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_answers);

        questionTV = findViewById(R.id.questionTV);
        recyclerView = findViewById(R.id.list);

        Intent intent = getIntent();
        question = intent.getStringExtra("question");
        questionID = intent.getIntExtra("questionID",-1);

        Log.i("Check",String.valueOf(questionID));

        questionTV.setText(question);

        answerList = new ArrayList<>();

        getNotificationsFromDb();

        madapter = new RecyclerViewAdapterViewAnswers(ViewAnswersActivity.this,answerList);
        recyclerView.setAdapter(madapter);

        loadingbar = new ProgressDialog(this);
        loadingbar.setTitle("Fetching answers");
        loadingbar.setMessage("Please wait while we are fetching latest answers for you");
        loadingbar.setCanceledOnTouchOutside(true);
        loadingbar.show();
    }

    private void getNotificationsFromDb() {
        String url = Constants.HttpConstants.GET_ANSWERS_URL + String.valueOf(questionID);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                this::parseResponse,
                error -> {
                    Log.i("Error","JsonObjectRequestError");}
        );

        HttpUtils.addRequestToHttpQueue(jsonObjectRequest,this);
    }

    private void parseResponse(JSONObject jsonObject) {
        if(jsonObject!=null)
        {
            answerList.clear();
            answerList.addAll(new Gson().fromJson(jsonObject.toString(), AnswersList.class).getAnswerDtos());
            if (answerList.isEmpty()) {
                Log.i("Data",answerList.toString());
                loadingbar.dismiss();
                Toast.makeText(this, "No Answers Posted Yet..!", Toast.LENGTH_SHORT).show();
            }
            else {
                if (recyclerView != null && madapter != null) {
                    loadingbar.dismiss();
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(madapter);
                    madapter.notifyDataSetChanged();
                }
            }
        }
        else
        {
            Toast.makeText(this, "Empty List Returned", Toast.LENGTH_SHORT).show();
        }
    }
}
package com.example.placementapp.student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.placementapp.Adapters.RecyclerViewAdapterViewQuestions;
import com.example.placementapp.R;
import com.example.placementapp.constants.Constants;
import com.example.placementapp.pojo.NotificationsList;
import com.example.placementapp.pojo.QuestionDto;
import com.example.placementapp.pojo.QuestionsList;
import com.example.placementapp.utils.HttpUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewQuestionsFragment extends Fragment {

    private List<QuestionDto> questionList;
    private RecyclerViewAdapterViewQuestions madapter;
    private RecyclerView recyclerView;
    private ProgressDialog loadingbar;

    public ViewQuestionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        questionList = new ArrayList<>();
        getNotificationsFromDb();
    }

    private void getNotificationsFromDb() {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                Constants.HttpConstants.GET_QUESTIONS_URL,
                null,
                this::parseResponse,
                error -> {
                    Log.i("Error","JsonObjectRequestError");}
        );

        HttpUtils.addRequestToHttpQueue(jsonObjectRequest,this.getContext());
    }

    private void parseResponse(JSONObject jsonObject) {
        if(jsonObject!=null)
        {
            questionList.clear();
            questionList.addAll(new Gson().fromJson(jsonObject.toString(), QuestionsList.class).getQuestionDtos());
            if (questionList.isEmpty()) {
                Log.i("Data",questionList.toString());
                loadingbar.dismiss();
                Toast.makeText(this.getContext(), "No Questions Posted Yet..!", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this.getContext(), "Empty List Returned", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_view_questions, container, false);

        recyclerView = view.findViewById(R.id.list);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), SubmitQuestionsActivity.class);
                startActivity(i);
            }
        });

        madapter = new RecyclerViewAdapterViewQuestions(getContext(), questionList);
        recyclerView.setAdapter(madapter);

        loadingbar = new ProgressDialog(view.getContext());
        loadingbar.setTitle("Fetching questions");
        loadingbar.setMessage("Please wait while we update your feed");
        loadingbar.setCanceledOnTouchOutside(true);
        loadingbar.show();

        return view;
    }
}
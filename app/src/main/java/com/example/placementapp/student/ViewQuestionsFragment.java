package com.example.placementapp.student;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.placementapp.R;
import com.example.placementapp.pojo.QuestionDto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ViewQuestionsFragment extends Fragment {

    private List<QuestionDto> questionList;
    //private RecyclerViewAdapterViewQuestions madapter;
    private RecyclerView recyclerView;
    private ProgressDialog loadingbar;

    public ViewQuestionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        questionList = new ArrayList<>();
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

//        madapter = new RecyclerViewAdapterViewQuestions(getContext(), questionList);
//        recyclerView.setAdapter(madapter);
//
//        loadingbar = new ProgressDialog(view.getContext());
//        loadingbar.setTitle("Fetching questions");
//        loadingbar.setMessage("Please wait while we update your feed");
//        loadingbar.setCanceledOnTouchOutside(true);
//        loadingbar.show();

        return view;
    }
}
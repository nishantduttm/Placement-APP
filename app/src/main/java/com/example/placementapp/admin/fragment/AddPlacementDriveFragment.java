package com.example.placementapp.admin.fragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CancellationSignal;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.placementapp.R;

import java.util.ArrayList;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddPlacementDriveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPlacementDriveFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText companyId;
    private EditText comapanyName;
    private EditText companydescription;
    private EditText companyskillsRequired;
    private ArrayList<EditText> allEditTexts;

    public AddPlacementDriveFragment() {
        // Required empty public constructor
    }

    void initialiseViews(View v){
        allEditTexts=new ArrayList<>();
        companyId=v.findViewById(R.id.companyid);
        comapanyName=v.findViewById(R.id.companyname);
        companyskillsRequired=v.findViewById(R.id.companyskills);
        companydescription=v.findViewById(R.id.companydescription);
         allEditTexts.add(companyId);
         allEditTexts.add(comapanyName);
         allEditTexts.add(companydescription);
         allEditTexts.add(companyskillsRequired);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment addPlacementDrive.
     */
    // TODO: Rename and change types and number of parameters
    public static AddPlacementDriveFragment newInstance(String param1, String param2) {
        AddPlacementDriveFragment fragment = new AddPlacementDriveFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public static AddPlacementDriveFragment newInstance() {
        AddPlacementDriveFragment fragment = new AddPlacementDriveFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_placement_drive, container, false);
        initialiseViews(v);

        Button b = (Button) v.findViewById(R.id.action_reset);
        b.setOnClickListener(this);
        return v;

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.action_reset:
            for (EditText e : allEditTexts) {
                e.setText("");
            }
        }
    }
}
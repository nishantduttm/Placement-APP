package com.example.placementapp.student;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.placementapp.R;
import com.example.placementapp.Adapters.PrepMaterialAdapter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrepMaterialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrepMaterialFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    ArrayList name = new ArrayList<>(Arrays.asList("Aptitude Test","Logical reasoning","Verbal ability","Data Structure","Algorithms","GATE","Competitive programming","Google","Amazon","Flipkart","Tata Consultancy","Cognizant","Directi","Nvidia","Morgan Stanley","NTT data","Accenture","Tech Mahindra","Atos Syntel","Xoriant","JP Morgan","Juspay"));
    ArrayList preplink = new ArrayList<>(Arrays.asList("https://www.indiabix.com/aptitude/questions-and-answers/","https://www.indiabix.com/logical-reasoning/questions-and-answers/","https://www.indiabix.com/verbal-ability/questions-and-answers/","https://www.geeksforgeeks.org/data-structures/","https://www.geeksforgeeks.org/fundamentals-of-algorithms/","https://www.geeksforgeeks.org/important-topics-prepare-gate-2020-computer-science-paper/","https://www.geeksforgeeks.org/category/competitive-programming/","https://www.geeksforgeeks.org/google-interview-preparation/","https://www.geeksforgeeks.org/amazon-interview-preparation/","https://www.geeksforgeeks.org/flipkart-interview-preparation/","https://www.geeksforgeeks.org/tcs-recruitment-process/","https://www.geeksforgeeks.org/cognizant-recruitment-process/","https://www.geeksforgeeks.org/directi-interview-preparation/","https://www.geeksforgeeks.org/nvidia-recruitment-process/","https://www.geeksforgeeks.org/morgan-stanley-interview-preparation/","https://www.geeksforgeeks.org/ntt-data-recruitment-process/","https://www.geeksforgeeks.org/accenture-recruitment-process/","https://www.geeksforgeeks.org/tcs-recruitment-process/","https://www.geeksforgeeks.org/tcs-recruitment-process/","https://www.geeksforgeeks.org/tcs-recruitment-process/","https://www.geeksforgeeks.org/tcs-recruitment-process/","https://www.geeksforgeeks.org/juspay-recruitment-process/"));
    ArrayList companyIcon = new ArrayList<>(Arrays.asList(R.drawable.aptitude_icon,R.drawable.logical_reasoning_icon,R.drawable.verbal_ability_icon,R.drawable.data_structure_icon,R.drawable.algorithm_icon,R.drawable.gate_icon,R.drawable.competitive_programming,R.drawable.google_icon,R.drawable.amazon_icon,R.drawable.flipkart_icon,R.drawable.tcs_logo,R.drawable.cognizant_icon,R.drawable.directi_icon,R.drawable.nvidia_icon,R.drawable.morgan_stanley_icon,R.drawable.ntt_data_icon,R.drawable.accenture_icon,R.drawable.tech_mahindra_icon,R.drawable.atos_syntel_icon,R.drawable.xoriant_icon,R.drawable.jp_morgan_icon,R.drawable.juspay_icon));


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    public PrepMaterialFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PrepMaterialFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PrepMaterialFragment newInstance(int columnCount) {
        PrepMaterialFragment fragment = new PrepMaterialFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_prep_material, container, false);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
            recyclerView.setNestedScrollingEnabled(false);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            //recyclerView.setAdapter(new MyItemRecyclerViewAdapter(DummyContent.ITEMS));
            PrepMaterialAdapter adapter = new PrepMaterialAdapter(getContext(), name,preplink,companyIcon);
            recyclerView.setAdapter(adapter);

           /* TextView linkTV = (TextView) view.findViewById(R.id.preplinkTV);
            String link = linkTV.getText().toString();
            Intent intent = new Intent(getActivity(),WebActivity.class);
            intent.putExtra("url",link);
            startActivity(intent);*/

        }
        return view;
    }
}
package com.example.placementapp.admin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.example.placementapp.R;
import com.example.placementapp.helper.SharedPrefHelper;
import com.example.placementapp.pojo.Statistics;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;

import java.util.ArrayList;

public class StatisticsActivity extends AppCompatActivity {

    private Statistics statistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        Gson gson = new Gson();
        String json = SharedPrefHelper.getEntryfromSharedPreferences(this.getApplicationContext(),"stat");
        statistics = gson.fromJson(json, Statistics.class);

        if (statistics != null) {

            Log.i("stat_Value", statistics.toString());

            PieChart pieChart = findViewById(R.id.pieChart);

            ArrayList<PieEntry> students = new ArrayList<>();
            if(statistics.getPlacedCount()!=0)
                students.add(new PieEntry(statistics.getPlacedCount(), "Placed Students"));
            if(statistics.getNotPlacedCount()!=0)
                students.add(new PieEntry(statistics.getNotPlacedCount(), "Not Placed Students"));
            if(statistics.getInProcessCount()!=0)
                students.add(new PieEntry(statistics.getInProcessCount(), "In Process Students"));
            if(statistics.getOnHoldCount()!=0)
                students.add(new PieEntry(statistics.getOnHoldCount(), "On Hold Students"));

            PieDataSet pieDataSet = new PieDataSet(students, "");
            pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            pieDataSet.setValueTextColor(Color.BLACK);
            pieDataSet.setValueTextSize(16f);

            PieData pieData = new PieData(pieDataSet);

            pieChart.setData(pieData);
            pieChart.getDescription().setEnabled(false);
            pieChart.setCenterText("Students");
            pieChart.setCenterTextSize(28f);
            pieChart.animate();
        }

    }
}
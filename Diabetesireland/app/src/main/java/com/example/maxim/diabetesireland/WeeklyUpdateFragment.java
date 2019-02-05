package com.example.maxim.diabetesireland;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class WeeklyUpdateFragment extends Fragment {
    View view;
    //FETCH Steps of the week from DATABASE
    int sundaySteps=2000,mondaySteps=5000,tuesdaySteps=9000,wedSteps=8000,thursSteps=5000,fridaySteps=7000,saturSteps=2000;
    boolean stepsSet = true, carbsSet = false;
    public static Button steps, carbs;
    GraphView graphView;
    BarGraphSeries<DataPoint> stepSeries;
    LineGraphSeries<DataPoint>  carbSeries;
    //STEPS TAKEN ARRAY FOR GRAPH (Last datapoint is for padding)
    DataPoint[] stepsTaken = {new DataPoint(0.15,sundaySteps), new DataPoint(0.64,mondaySteps), new DataPoint(1.16,tuesdaySteps), new DataPoint(1.74,wedSteps), new DataPoint(2.34,thursSteps), new DataPoint(2.9,fridaySteps), new DataPoint(3.35,saturSteps),new DataPoint(5.1,0)};
    //CARBS CONSUMED ARRAY FOR GRAPH
    DataPoint[] carbsConsumed = {new DataPoint(0,7), new DataPoint(1,6), new DataPoint(2,5), new DataPoint(3,4), new DataPoint(4,3), new DataPoint(5,2), new DataPoint(6,1)};

    public static WeeklyUpdateFragment newInstance() {
        return new WeeklyUpdateFragment();
    }
    public WeeklyUpdateFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_weekly_update, container, false);
        graphView = (GraphView) view.findViewById(R.id.graph);
        steps = (Button) view.findViewById(R.id.stepButton);
        carbs = (Button) view.findViewById(R.id.carbButton);


        //GRAPHVIEW FOR STEPS
        stepSeries = new BarGraphSeries<DataPoint>(stepsTaken);
        graphView.addSeries(stepSeries);
        graphView.setTitle("         Weekly Report");
        stepSeries.setSpacing(50);
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"Sun","   Mon","Tues","Wed","Thurs","Fri","Sat"});
        graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graphView.getGridLabelRenderer().setPadding(50);
        graphView.getGridLabelRenderer().setVerticalAxisTitle("Steps");
        graphView.getGridLabelRenderer().setHorizontalAxisTitle("                    Days");
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(3.5);
        graphView.getViewport().setXAxisBoundsManual(true);
        //GRAPHVIEW FOR CARBS
        carbSeries = new LineGraphSeries<DataPoint>(carbsConsumed);

        steps.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!stepsSet) {
                    stepsSet = true;
                    carbsSet = false;
                    steps.setBackgroundColor(0xff3F1451);
                    steps.setTextColor(0xffffffff);
                    carbs.setBackgroundColor(0xffdbdbdb);
                    carbs.setTextColor(0xffffffff);
                    graphView.getGridLabelRenderer().setVerticalAxisTitle("Steps");
                    graphView.getGridLabelRenderer().setHorizontalAxisTitle("                    Days");
                    graphView.removeAllSeries();
                    graphView.addSeries(stepSeries);
                    stepSeries.setSpacing(50);
                    //stepSeries.setSpacing(50);

                }
            }
        }));
        carbs.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!carbsSet)
                {
                    carbsSet = true;
                    stepsSet = false;
                    carbs.setBackgroundColor(0xff3F1451);
                    carbs.setTextColor(0xffffffff);
                    steps.setBackgroundColor(0xffdbdbdb);
                    steps.setTextColor(0xffffffff);
                    graphView.getGridLabelRenderer().setVerticalAxisTitle("Carbohydrates");
                    graphView.getGridLabelRenderer().setHorizontalAxisTitle("            Days");
                    graphView.removeAllSeries();
                    graphView.addSeries(carbSeries);
                }
            }
        }));
        return view;
    }
}

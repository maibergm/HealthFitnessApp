package com.example.maxim.diabetesireland;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * A placeholder fragment containing a simple view.
 */
public class ProfileFragment extends Fragment {
    View view;
    double weight,height,age;
    String db_weight,db_height;
    String gender,bmi;
    TextView weight_text,height_text;
    final String [] weight_metrics = {"kg","lbs","st and lbs"};
    final String [] height_metrics = {"cm","ft and in"};
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_profile, container, false);
        weight_text = (TextView) view.findViewById(R.id.updated_weight);
        //FETCH WEIGHT FROM DATABASE
        weight_text.setText("Weight: " +db_weight);
        height_text = (TextView) view.findViewById(R.id.updated_height);
        //FETCH HEIGHT FROM DATABASE
        height_text.setText("Height: "+db_height);
        TextView age_text = (TextView) view.findViewById(R.id.age_text);
        //FETCH AGE FROM DATA BASE
        age_text.setText("Age: " + (int)age + " years old");
        TextView bmi_text = (TextView) view.findViewById(R.id.bmi_text);
        bmi_text.setText(bmi);
        calcBMI();
        TextView gender_text = (TextView) view.findViewById(R.id.gender_text);
        //FETCH GENDER TO DATABASE
        gender_text.setText("Gender: " +gender);
        Button height_update = (Button) view.findViewById(R.id.height_update);
        Button weight_update = (Button) view.findViewById(R.id.weight_update);
        weight_update.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String msg ="What is your weight?";
                showPopUp2(weight_metrics, msg);
            }
        }));
        height_update.setOnClickListener((new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String msg = "What is your height?";
                showPopUp2(height_metrics,msg);
            }
        }));
        return view;
    }

    private void showPopUp2(String [] spinner,String title) {

        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(getActivity());
        helpBuilder.setTitle(title);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.weight_height_layout, null);
        final EditText input = (EditText) view.findViewById(R.id.cmFt);
        final EditText input2 = (EditText) view.findViewById(R.id.inches);
        final Spinner metrics = (Spinner) view.findViewById(R.id.metrics_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        metrics.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ((metrics.getSelectedItem().toString()).equals("cm") || (metrics.getSelectedItem().toString()).equals("ft and in")) {
                    if ((metrics.getSelectedItem().toString()).equals("ft and in")) {
                        input2.setVisibility(view.VISIBLE);
                        input.getLayoutParams().width = 200;
                        input2.getLayoutParams().width = 200;

                    } else {
                        input2.setVisibility(view.GONE);
                        input.getLayoutParams().width = 300;

                    }

                } else {
                    if ((metrics.getSelectedItem().toString()).equals("st and lbs")) {
                        input2.setVisibility(view.VISIBLE);
                        input.getLayoutParams().width = 200;
                        input2.getLayoutParams().width = 200;
                    } else {
                        input2.setVisibility(view.GONE);
                        input.getLayoutParams().width = 300;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        metrics.setAdapter(adapter);
        helpBuilder.setView(view);
        helpBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if ((metrics.getSelectedItem().toString()).equals("cm") || (metrics.getSelectedItem().toString()).equals("ft and in")) {
                    if ((metrics.getSelectedItem().toString()).equals("ft and in")) {
                        height_text.setText("Height: "+ input.getText() + " ft " + input2.getText() + " in");
                        getHeight(height, input, input2);
                        calcBMI();
                    } else {
                        height_text.setText("Height: "+ input.getText() + " cm");
                        getHeight(height, input, input2);
                        calcBMI();
                    }
                } else {
                    if ((metrics.getSelectedItem().toString()).equals("st and lbs")) {
                        weight_text.setText("Weight: "+ input.getText() + " st " + input2.getText() + " lbs");
                        getWeight(weight, input, input2, "st and lbs");
                        calcBMI();
                    } else {
                        if ((metrics.getSelectedItem().toString()).equals("lbs")) {
                            weight_text.setText("Weight: "+ input.getText() + " lbs");
                            getWeight(weight, input, input2, "lbs");
                            calcBMI();
                        } else {
                            weight_text.setText("Weight: "+ input.getText() + " kg");
                            getWeight(weight, input, input2, "kg");
                            calcBMI();
                        }
                    }
                }
                dialog.dismiss();
            }
        });
        helpBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
            }
        });
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }
    private void getHeight(double height,EditText first,EditText second){
        if((second.getText().toString()).equals("")) {
            // UPDATE TO DATABASE => height
            height =(Double.parseDouble(first.getText().toString()));
        }
        else{
            double feet = (Double.parseDouble(first.getText().toString())) * 30.48;
            double inches = (Double.parseDouble(second.getText().toString())) * 2.54;
            // UPDATE TO DATABASE => height
            height= feet + inches;
        }
    }
    private void getWeight(double weight,EditText first,EditText second,String type){
        if((second.getText().toString()).equals("")) {
            if(type.equals("lbs")) {
                // UPDATE TO DATABASE => weight
                weight = ((Double.parseDouble(first.getText().toString())) * 0.453592);
            }
            else {
                // UPDATE TO DATABASE => weight
                weight = ((Double.parseDouble(first.getText().toString())));
            }

        }
        else {
            double stoneNum = (Double.parseDouble(first.getText().toString())) * 6.35029;
            double pounds = (Double.parseDouble(second.getText().toString())) * 0.453592;
            // UPDATE TO DATABASE => weight
            weight = stoneNum + pounds;
        }
    }
    private void calcBMI(){
        double calc_bmi = (weight) / ((height*0.01) * (height*0.01));
        if(calc_bmi < 18.5){
            bmi = "BMI: Underweight " + calc_bmi;
        }
        else if ((calc_bmi > 18.5) && (calc_bmi <24.9)) {
            bmi = "BMI: Healthy Weight " + calc_bmi;
        }
        else if((calc_bmi > 25.0) && (calc_bmi <29.9)){
            bmi = "BMI: OverWeight " + calc_bmi;
        }
        else {
            bmi ="BMI: Obese " + calc_bmi;
        }
    }
}

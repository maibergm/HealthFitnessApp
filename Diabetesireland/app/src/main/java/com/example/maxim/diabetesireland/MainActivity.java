package com.example.maxim.diabetesireland;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    boolean touchedMale, touchedFemale= false,dobset=false,weightset=false,heightset=false;

    String registered = "notRegistered";String gender = "Male",db_weight,db_height;
    int user_age;
    double user_height,user_weight;
    public static Button maleButton, femaleButton,nextScreen,dateOfBirth,weight,height;
    RadioGroup plan;
    Intent intent;
    Calendar calendar = Calendar.getInstance();
    final String [] weight_metrics = {"kg","lbs","st and lbs"};
    final String [] height_metrics = {"cm","ft and in"};
    //Max/min heights/weights are in cm/kg
    public static final int MAX_HEIGHT = 300, /*7 ft*/
                            MIN_HEIGHT = 50, /*4 ft 6*/
                            MAX_WEIGHT = 300,
                            MIN_WEIGHT = 30;
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // fetch String registered from database
        if((registered.equals("registered"))){
            Intent i =  new Intent(MainActivity.this, TabActivity.class);
            startActivity(i);
            finish();
        }else {
            setContentView(R.layout.activity_main);
            nextScreen = (Button) findViewById(R.id.continueButton);
            maleButton = (Button) findViewById(R.id.maleButton);
            femaleButton = (Button) findViewById(R.id.femaleButton);
            weight = (Button) findViewById(R.id.weight_button);
            height = (Button) findViewById(R.id.height_button);
            dateOfBirth = (Button) findViewById(R.id.datepicker);
            plan =(RadioGroup) findViewById(R.id.radioGroup);
            maleButton.setOnClickListener(this);
            femaleButton.setOnClickListener(this);
            nextScreen.setOnClickListener(this);
            weight.setOnClickListener((new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String msg = "What is your weight?";
                    showPopUp(weight_metrics, msg);
                }
            }));
            height.setOnClickListener((new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String msg = "What is your height?";
                    showPopUp(height_metrics, msg);
                }
            }));
            dateOfBirth.setOnClickListener(this);
        }
    }
    //Returns height in centimeters
    private double getHeight(EditText first,EditText second) {
        if ((second.getText().toString()).equals("")) {
            if ((first.getText().toString()).equals("")) {
                return 0;
            } else {
                heightset = true;
                // ADD TO DATABASE => height
                return(Double.parseDouble(first.getText().toString()));
            }
        } else {
            if (((first.getText().toString()).equals("")) && ((second.getText().toString()).equals(""))) {
                return 0;
            } else {
                double feet = (Double.parseDouble(first.getText().toString())) * 30.48;
                double inches = (Double.parseDouble(second.getText().toString())) * 2.54;
                // ADD TO DATABASE => height
                return (feet + inches);
            }
        }
    }
    //Returns weight in kilogram
    private double getWeight(EditText first,EditText second,String type){
        if((second.getText().toString()).equals("")) {
            if ((first.getText().toString()).equals("")) {
                return 0;
            } else {
                if (type.equals("lbs")) {
                    weightset=true;
                    // ADD TO DATABASE => weight
                    return((Double.parseDouble(first.getText().toString())) * 0.453592);
                } else {
                    weightset=true;
                    return((Double.parseDouble(first.getText().toString())));
                }

            }
        }
        else {
            if (((first.getText().toString()).equals("")) && ((second.getText().toString()).equals(""))) {
                return 0;
            } else {
                weightset=true;
                double stoneNum = (Double.parseDouble(first.getText().toString())) * 6.35029;
                double pounds = (Double.parseDouble(second.getText().toString())) * 0.453592;
                // ADD TO DATABASE => weight
                return(stoneNum + pounds);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.maleButton:
                if (!touchedMale) {
                    touchedFemale = false;
                    touchedMale = true;
                    // ADD TO DATABASE
                    gender="Male";
                    maleButton.setBackgroundColor(0xff3F1451); //purple
                    maleButton.setTextColor(0xffffffff); //white
                    femaleButton.setBackgroundColor(0xffdbdbdb); //light grey
                    femaleButton.setTextColor(0xffffffff); //dark grey
                }
                break;
            case R.id.femaleButton:
                if (!touchedFemale) {
                    touchedFemale = true;
                    touchedMale = false;
                    //ADD TO DATABASE
                    gender="Female";
                    femaleButton.setBackgroundColor(0xff3F1451);//purple
                    femaleButton.setTextColor(0xffffffff); // white text
                    maleButton.setBackgroundColor(0xffdbdbdb); // dark grey background
                    maleButton.setTextColor(0xffffffff); //light grey text
                }
                break;
            case R.id.continueButton:
                if((touchedMale == true || touchedFemale == true)){
                    if(dobset==true && heightset==true && weightset==true){
                        if(plan.getCheckedRadioButtonId()!=-1) {
                            // ADD TO DATABASE = registered
                            registered = "registered";
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setMessage(R.string.disclaimer)
                                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            intent = new Intent(MainActivity.this, TabActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                            builder.show();
                        }
                    }

                }
                else {
                }
                break;
            case R.id.datepicker:
                dobset=true;
                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, R.style.DialogTheme,listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                GregorianCalendar cal = new GregorianCalendar();
                cal.add(Calendar.YEAR, -100);
                dialog.getDatePicker().setMinDate(cal.getTimeInMillis());
                GregorianCalendar cal2 = new GregorianCalendar();
                cal2.add(Calendar.YEAR, -1);

                dialog.getDatePicker().setMaxDate(cal2.getTimeInMillis());
                dialog.show();
                break;

        }
    }

    private void showPopUp(String [] spinner,String title) {

        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(MainActivity.this);
        helpBuilder.setTitle(title);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        view = inflater.inflate(R.layout.weight_height_layout, null);
        final EditText input = (EditText) view.findViewById(R.id.cmFt);
        final EditText input2 = (EditText) view.findViewById(R.id.inches);
        final Spinner metrics = (Spinner) view.findViewById(R.id.metrics_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        metrics.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ((metrics.getSelectedItem().toString()).equals("cm") || (metrics.getSelectedItem().toString()).equals("ft and in")) {
                    if ((metrics.getSelectedItem().toString()).equals("ft and in")) {
                        input2.setVisibility(view.VISIBLE);
                        input.getLayoutParams().width = 200;
                        input2.getLayoutParams().width = 200;
                        input2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
                        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
                    } else {
                        input2.setVisibility(view.GONE);
                        input.getLayoutParams().width = 300;
                        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
                    }

                } else {
                    if ((metrics.getSelectedItem().toString()).equals("st and lbs")) {
                        input2.setVisibility(view.VISIBLE);
                        input.getLayoutParams().width = 200;
                        input2.getLayoutParams().width = 200;
                        input2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
                        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
                    } else {
                        input2.setVisibility(view.GONE);
                        input.getLayoutParams().width = 300;
                        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
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
                        if (checkHeightValid(input, input2)) {
                            height.setText(input.getText() + " ft" + input2.getText() + " in");
                            //ADD TO DATABASE
                            db_height = height.getText().toString();
                            user_height=getHeight(input, input2);
                        } else dialog.dismiss();
                    } else {
                        if (checkHeightValid(input, input2)) {
                            height.setText(input.getText() + " cm");
                            //ADD TO DATABASE
                            db_height = height.getText().toString();
                            user_height=getHeight(input, input2);
                        } else dialog.dismiss();
                    }
                } else {
                    if ((metrics.getSelectedItem().toString()).equals("st and lbs")) {
                        if (checkWeightValid(input, input2, "st and lbs")) {
                            weight.setText(input.getText() + " st" + input2.getText() + " lbs"); /*maybe put thing here*/
                            //ADD TO DATABASE
                            db_weight = weight.getText().toString();
                            user_weight=getWeight(input, input2, "st and lbs");
                            Log.v("Weight"," "+user_weight);
                        } else dialog.dismiss();

                    } else {
                        if ((metrics.getSelectedItem().toString()).equals("lbs")) {
                            if (checkWeightValid(input, input2, "lbs")) {
                                weight.setText(input.getText() + " lbs");
                                //ADD TO DATABASE
                                db_weight = weight.getText().toString();
                                user_weight=getWeight(input, input2, "lbs");
                                Log.v("Weight"," "+user_weight);
                            } else dialog.dismiss();
                        } else {
                            if (checkWeightValid(input, input2, "kg")) {
                                weight.setText(input.getText() + " kg");
                                //ADD TO DATABASE
                                db_weight = weight.getText().toString();

                                user_weight=getWeight(input, input2, "kg");
                                Log.v("Weight"," "+user_weight);
                            }
                            else dialog.dismiss();
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

public static boolean checkHeightValid(EditText first, EditText second){
    if((second.getText().toString()).equals("")){
        if((Double.parseDouble(first.getText().toString())) < MAX_HEIGHT
                && (Double.parseDouble(first.getText().toString())) > MIN_HEIGHT){
            return true;
        }
        else return false;
    }
    else{
        double feet = (Double.parseDouble(first.getText().toString())) * 30.48;
        double inches = (Double.parseDouble(second.getText().toString())) * 2.54;
        if(feet + inches < MAX_HEIGHT && feet + inches > MIN_HEIGHT){
            return true;
        }
        else return false;
    }
}
    public static boolean checkWeightValid(EditText first, EditText second, String type){
        if((second.getText().toString()).equals("")) {
            if (type.equals("lbs")) {
                if (((Double.parseDouble(first.getText().toString())) * 0.453592) < MAX_WEIGHT
                        && ((Double.parseDouble(first.getText().toString())) * 0.453592) > MIN_WEIGHT)
                    return true;
                else return false;
            } else {
                if (((Double.parseDouble(first.getText().toString()))) < MAX_WEIGHT
                        && (Double.parseDouble(first.getText().toString())) > MIN_WEIGHT)
                    return true;
                else return false;
            }
        }
        else{
            double stoneNum = (Double.parseDouble(first.getText().toString())) * 6.35029;
            double pounds = (Double.parseDouble(second.getText().toString())) * 0.453592;
            double weight = stoneNum + pounds;
            if(weight < MAX_WEIGHT && weight > MIN_WEIGHT)
                return true;
            else return false;
        }

    }

    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateOfBirth.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
            if(year != 0){
                GregorianCalendar cal = new GregorianCalendar();
                int y, m, d;
                y = cal.get(Calendar.YEAR);
                m = cal.get(Calendar.MONTH);
                d = cal.get(Calendar.DAY_OF_MONTH);
                cal.set(year, monthOfYear, dayOfMonth);
                user_age = y - cal.get(Calendar.YEAR);
                if ((m < cal.get(Calendar.MONTH)) || ((m == cal.get(Calendar.MONTH)) && (d < cal.get(Calendar.DAY_OF_MONTH)))) {
                    --user_age;
                }
                else if(user_age < 0) {
                    throw new IllegalArgumentException("Age < 0");
                }
                else{
                    // ADD age to DATABASE
                }
            }
        }};


}




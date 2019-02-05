package com.example.maxim.diabetesireland;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.lang.CharSequence;


/**
 * A placeholder fragment containing a simple view.
 */
public class ExerciseFragment extends Fragment {
    View view;
    private View helpLayout;


    private Spinner exerciseSpinner;
    private static final String[] exerciseDurations = {"15", "30", "45", "60"};
    public Button lightButton, mediumButton, vigorousButton, lightHelpButton, mediumHelpButton, vigorousHelpButton, submitButton;
    boolean touchedLight, touchedMedium, touchedVigorous = false;
    int currentDuration = 0;
    int exerciseDuration=0;
    String exerciseType ="";

    public static ExerciseFragment newInstance() {
        return new ExerciseFragment();
    }
    public ExerciseFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_exercise, container, false);
        lightButton = (Button) view.findViewById(R.id.lightbutton);
        mediumButton = (Button) view.findViewById(R.id.mediumbutton);
        vigorousButton = (Button) view.findViewById(R.id.vigorousbutton);
        lightHelpButton = (Button) view.findViewById(R.id.lighthelpbutton);
        mediumHelpButton = (Button) view.findViewById(R.id.mediumhelpbutton);
        vigorousHelpButton = (Button) view.findViewById(R.id.vigoroushelpbutton);
        exerciseSpinner = (Spinner) view.findViewById(R.id.exercisespinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, exerciseDurations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                currentDuration = (position + 1) * 15;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                currentDuration = 0;
            }
        });
        exerciseSpinner.setAdapter(adapter);
        submitButton = (Button) view.findViewById(R.id.submitbutton);

        lightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchedLight = true;
                touchedMedium = false;
                touchedVigorous = false;
                lightButton.setBackgroundColor(0xff3F1451); //green
                mediumButton.setBackgroundColor(0xffdbdbdb); //light grey
                vigorousButton.setBackgroundColor(0xffdbdbdb); //light grey
            }
        });
        mediumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchedLight = false;
                touchedMedium = true;
                touchedVigorous = false;
                lightButton.setBackgroundColor(0xffdbdbdb); //light grey
                mediumButton.setBackgroundColor(0xff3F1451); //orange
                vigorousButton.setBackgroundColor(0xffdbdbdb); //light grey
            }
        });
        vigorousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                touchedLight = false;
                touchedMedium = false;
                touchedVigorous = true;
                lightButton.setBackgroundColor(0xffdbdbdb); //light grey
                mediumButton.setBackgroundColor(0xffdbdbdb); //light grey
                vigorousButton.setBackgroundColor(0xff3F1451); //red
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lightButton.setBackgroundColor(0xffdbdbdb); //light grey
                mediumButton.setBackgroundColor(0xffdbdbdb); //light grey
                vigorousButton.setBackgroundColor(0xffdbdbdb); //light grey
                if(touchedLight){
                    //Add to database
                    setExercise("Light");setDuration(currentDuration);}
                else if(touchedMedium){
                    //Add to database
                    setExercise("Medium");setDuration(currentDuration);}
                else if(touchedVigorous){
                    //Add to database
                    setExercise("Vigorous");setDuration(currentDuration);}
                touchedLight = false;
                touchedMedium = false;
                touchedVigorous = false;
            }
        });
        lightHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpExercise("Walking—slowly\n" +
                        "Sitting—using computer\n" +
                        "Standing—light work\n" +
                        "Fishing—sitting\n" +
                        "Playing most instruments", "Examples of Light Exercise");
            }
        });
        mediumHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpExercise("Walking—very brisk\n" +
                        "Cleaning—heavy\n" +
                        "Mowing lawn\n" +
                        "Cycling—light effort\n" +
                        "Badminton—recreational\n" +
                        "Tennis—doubles", "Examples of Medium Exercise");
            }
        });
        vigorousHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpExercise("Hiking\n" +
                        "Jogging\n" +
                        "Shoveling\n" +
                        "Carrying heavy loads\n" +
                        "Bicycling fast\n" +
                        "Basketball game\n" +
                        "Soccer game\n" +
                        "Tennis—singles", "Examples of Vigorous Exercise");
            }
        });

        // exerciseSpinner = (Spinner) view.findViewById(R.id.spinner);
        // ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
        //        android.R.layout.simple_spinner_item, exerciseList);
        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //exerciseSpinner.setAdapter(adapter);
        return view;
    }



    private void setExercise(String type){
        exerciseType =type;
    }
    private void setDuration(int duration){
        exerciseDuration=duration;
    }
    private void popUpExercise(String exampleList, String type) {

        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(getActivity());
        helpBuilder.setTitle(type);
        helpBuilder.setMessage(exampleList); // ORIGINAL METHOD OF DISPLAYING POP-UP
        LayoutInflater inflater = getActivity().getLayoutInflater();
        helpLayout = inflater.inflate(R.layout.exercise_layout, null);
        helpBuilder.setView(helpLayout);

        //TextView listPopUp = (TextView) view.findViewById(R.id.popuptext);
        //listPopUp.setText(exampleList);
        //NON-FUNCTIONAL METHOD, AIMS TO CENTRE TEXT BUT SETTEXT NOT WORKING


        helpBuilder.setNegativeButton("Back", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
            }
        });
        // Remember, create doesn't show the dialog
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();

    }
}

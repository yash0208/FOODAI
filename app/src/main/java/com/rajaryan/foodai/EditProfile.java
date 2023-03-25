package com.rajaryan.foodai;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditProfile extends AppCompatActivity {
    Spinner spinner;
    RadioGroup genderRadioGroup;
    String gender="Male";
    EditText name,weight,height,email,target,calorie,week,age;
    ImageView refresh;
    private List<String> activityLevels = new ArrayList<>(Arrays.asList(
            "Sedentary",
            "Lightly Active",
            "Moderately Active",
            "Very Active",
            "Super Active"
    ));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        spinner=findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, activityLevels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        name=findViewById(R.id.name);
        weight=findViewById(R.id.weight);
        calorie=findViewById(R.id.calorie);
        age=findViewById(R.id.age);
        height=findViewById(R.id.height);
        genderRadioGroup = findViewById(R.id.radio_group_gender);
        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                RadioButton selectedGender = radioGroup.findViewById(checkedId);
                gender = selectedGender.getText().toString();

                // Do something with the selected gender
            }
        });
        email=findViewById(R.id.email);
        target=findViewById(R.id.target);
        refresh=findViewById(R.id.refresh);
        week=findViewById(R.id.week);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(weight.getText().toString()) && !TextUtils.isEmpty(target.getText().toString()) && !TextUtils.isEmpty(week.getText().toString()) && !TextUtils.isEmpty(height.getText().toString()) && !TextUtils.isEmpty(age.getText().toString()) ){
                    float currentWeight=Float.valueOf(weight.getText().toString());
                    float currentHeight=Float.valueOf(height.getText().toString());
                    float targetedWeight=Float.valueOf(target.getText().toString());
                    float cur_age=Float.valueOf(age.getText().toString());
                    float we=Float.valueOf(week.getText().toString());
                    float bmr=0;
                    float per_week=(currentWeight - targetedWeight)/we;
                    if(gender.equals("Male")){
                        bmr = (float) (66 + (6.23 * currentWeight) + (12.7 * currentHeight) - (6.8 * cur_age));
                    }
                    if(gender.equals("Female")){
                        bmr = (float) (65.5 + (4.35 * currentWeight) + (4.7 * currentHeight) - (4.7 * cur_age));
                    }
                    float calorieIntake = 0;
                    if(spinner.getSelectedItem().toString().equals("Sedentary")){
                        calorieIntake = (float) (bmr * 1.2 - (per_week * 500));
                    }
                    if(spinner.getSelectedItem().toString().equals("Lightly Active")){
                        calorieIntake = (float) (bmr * 1.375 - (per_week * 500));
                    }
                    if(spinner.getSelectedItem().toString().equals("Moderately Active")){
                        calorieIntake = (float) (bmr * 1.55 - (per_week * 500));
                    }
                    if(spinner.getSelectedItem().toString().equals("Very Active")){
                        calorieIntake = (float) (bmr * 1.725 - (per_week * 500));
                    }
                    if(spinner.getSelectedItem().toString().equals("Super Active")){
                        calorieIntake = (float) (bmr * 1.9 - (per_week * 500));
                    }

                    Log.e("Calorie",String.valueOf(calorieIntake));
                }
            }
        });
    }
}
package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class InputActivity extends AppCompatActivity {

    EditText etAge, etBP, etGlucose, etCreatinine, etGFR, etUrea;
    RadioGroup rgAlbumin, rgGender;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        // Inputs
        rgGender = findViewById(R.id.rgGender);
        etAge = findViewById(R.id.etAge);
        etBP = findViewById(R.id.etBP);
        etGlucose = findViewById(R.id.etGlucose);
        etCreatinine = findViewById(R.id.etCreatinine);
        etGFR = findViewById(R.id.etGFR);
        etUrea = findViewById(R.id.etUrea);
        rgAlbumin = findViewById(R.id.rgAlbumin);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });
    }

    private void submitForm() {
        // Read values
        int selectedGenderId = rgGender.getCheckedRadioButtonId();
        String ageStr = etAge.getText().toString().trim();
        String bpStr = etBP.getText().toString().trim();
        String glucoseStr = etGlucose.getText().toString().trim();
        String creatStr = etCreatinine.getText().toString().trim();
        String gfrStr = etGFR.getText().toString().trim();
        String ureaStr = etUrea.getText().toString().trim();
        int selectedAlbuminId = rgAlbumin.getCheckedRadioButtonId();

        if (selectedGenderId == -1 || TextUtils.isEmpty(ageStr) || TextUtils.isEmpty(bpStr)
                || TextUtils.isEmpty(glucoseStr) || TextUtils.isEmpty(creatStr)
                || TextUtils.isEmpty(gfrStr) || TextUtils.isEmpty(ureaStr)
                || selectedAlbuminId == -1) {
            Toast.makeText(this, "Please fill all fields and select gender & albumin", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Parse values
            int age = Integer.parseInt(ageStr);
            double bp = Double.parseDouble(bpStr);
            double glucose = Double.parseDouble(glucoseStr);
            double creatinine = Double.parseDouble(creatStr);
            double gfr = Double.parseDouble(gfrStr);
            double urea = Double.parseDouble(ureaStr);

            // Gender check
            RadioButton rbGender = findViewById(selectedGenderId);
            String gender = rbGender.getText().toString();

            boolean albuminPresent = false;
            RadioButton rbAlbumin = findViewById(selectedAlbuminId);
            if (rbAlbumin != null && rbAlbumin.getText().toString().equalsIgnoreCase("Yes"))
                albuminPresent = true;

            // Evaluate risk using scoring system
            int score = 0;

            // 🔹 Gender-based creatinine threshold
            if (gender.equalsIgnoreCase("Male") && creatinine >= 1.5) score += 3;
            if (gender.equalsIgnoreCase("Female") && creatinine >= 1.3) score += 3;

            if (albuminPresent) score += 3;
            if (bp >= 140) score += 2;
            if (glucose >= 180) score += 1;
            if (age >= 60) score += 1;
            if (gfr < 60) score += 3;
            if (urea > 40) score += 2;

            String risk;
            if (score >= 7) risk = "High Risk of CKD";
            else if (score >= 4) risk = "Moderate Risk of CKD";
            else risk = "Low Risk of CKD";

            String details = "Score: " + score +
                    "\nGender: " + gender +
                    "\nAge: " + age +
                    "\nBP: " + bp +
                    "\nGlucose: " + glucose +
                    "\nCreatinine: " + creatinine +
                    "\nGFR: " + gfr +
                    "\nUrea: " + urea +
                    "\nAlbumin Present: " + (albuminPresent ? "Yes" : "No");

            // send to result activity
            Intent intent = new Intent(InputActivity.this, com.example.myapplication.ResultActivity.class);
            intent.putExtra("risk", risk);
            intent.putExtra("details", details);
            startActivity(intent);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numeric values", Toast.LENGTH_SHORT).show();
        }
    }
}

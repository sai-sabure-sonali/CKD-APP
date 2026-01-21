package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    TextView tvRisk, tvDetails;
    Button btnRetest, btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        tvRisk = findViewById(R.id.tvRisk);
        tvDetails = findViewById(R.id.tvDetails);
        btnRetest = findViewById(R.id.btnRetest);
        btnHome = findViewById(R.id.btnHome);

        Intent intent = getIntent();
        String risk = intent.getStringExtra("risk");
        String details = intent.getStringExtra("details");

        tvRisk.setText(risk != null ? risk : "No result");
        tvDetails.setText(details != null ? details : "");

        btnRetest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ResultActivity.this, InputActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ResultActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
                finish();
            }
        });
    }
}

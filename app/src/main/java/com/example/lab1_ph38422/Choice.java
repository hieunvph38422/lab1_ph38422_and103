package com.example.lab1_ph38422;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Choice extends AppCompatActivity {

    private Button btnemail,btnOtp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        btnemail = findViewById(R.id.btnemail);
        btnOtp = findViewById(R.id.btnOtp);

        btnemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Choice.this, MainActivity.class);
                startActivity(in);
            }
        });
        btnOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Choice.this, MainActivity2.class);
                startActivity(in);

            }
        });

    }
}
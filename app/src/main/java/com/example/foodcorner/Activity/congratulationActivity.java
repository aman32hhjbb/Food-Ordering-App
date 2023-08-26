package com.example.foodcorner.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.foodcorner.R;

public class congratulationActivity extends AppCompatActivity {
String UserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulation);
        UserId=getIntent().getStringExtra("UserId");
        findViewById(R.id.congActivityBackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("UserId",UserId);
                startActivity(intent);
                finish();
            }
        });
    }
}
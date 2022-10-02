package com.example.ujisertifikasi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button registerActivity = findViewById(R.id.btn_register);
        Button listMemberActivity = findViewById(R.id.btn_list_member);

        registerActivity.setOnClickListener(v -> {
            Intent i =  new Intent(this, FormRegisterActivity.class);
            startActivity(i);
        });

        listMemberActivity.setOnClickListener(v -> {
            Intent i = new Intent(this, ListMemberActivity.class);
            startActivity(i);
        });
    }
}
package com.grocerydash.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;

public class StoreSelectActivity extends AppCompatActivity {
    Spinner spinner;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.splashScreenTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_select);

        spinner = findViewById(R.id.spinner_storeList);

        button = findViewById(R.id.button_start);
        button.setOnClickListener(v -> {
            if(spinner.getSelectedItem().equals("SM Savemore Market Laon-Laan")){
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
            }
            else{
                Snackbar.make(this.findViewById(R.id.coordinator_layout_store_select), "Application is currently not affiliated with the selected branch.", Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
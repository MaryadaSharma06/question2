package com.example.question2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

    Spinner fromSpinner, toSpinner;
    Button convertButton, settingsButton;
    TextView resultText;
    EditText inputValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isDark = prefs.getBoolean("dark_mode", false);
        AppCompatDelegate.setDefaultNightMode(
                isDark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fromSpinner = findViewById(R.id.fromUnitSpinner);
        toSpinner = findViewById(R.id.toUnitSpinner);
        convertButton = findViewById(R.id.convertButton);
        settingsButton = findViewById(R.id.openSettingsButton);
        resultText = findViewById(R.id.resultText);
        inputValue = findViewById(R.id.inputValue);

        String[] units = {"Feet", "Meter", "Centimeter"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, units);
        fromSpinner.setAdapter(adapter);
        toSpinner.setAdapter(adapter);


        convertButton.setOnClickListener(v -> {
            String fromUnit = fromSpinner.getSelectedItem().toString();
            String toUnit = toSpinner.getSelectedItem().toString();
            String inputStr = inputValue.getText().toString();

            if (inputStr.isEmpty()) {
                resultText.setText("Please enter a value.");
                return;
            }

            double input = Double.parseDouble(inputStr);
            double result;

            if (fromUnit.equals("Feet") && toUnit.equals("Meter")) {
                result = input * 0.3048;
            } else if (fromUnit.equals("Meter") && toUnit.equals("Feet")) {
                result = input / 0.3048;
            } else if (fromUnit.equals("Feet") && toUnit.equals("Centimeter")) {
                result = input * 30.48;
            } else if (fromUnit.equals("Centimeter") && toUnit.equals("Feet")) {
                result = input / 30.48;
            } else if (fromUnit.equals("Meter") && toUnit.equals("Centimeter")) {
                result = input * 100;
            } else if (fromUnit.equals("Centimeter") && toUnit.equals("Meter")) {
                result = input / 100;
            } else {
                result = input;
            }

            resultText.setText("Result: " + result);
        });


        settingsButton.setOnClickListener(v -> {
            String[] options = {"Light Mode", "Dark Mode"};
            new AlertDialog.Builder(this)
                    .setTitle("Choose Theme")
                    .setItems(options, (dialog, which) -> {
                        boolean dark = which == 1;
                        AppCompatDelegate.setDefaultNightMode(
                                dark ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
                        );
                        SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
                        editor.putBoolean("dark_mode", dark);
                        editor.apply();
                        recreate(); // Apply theme change
                    })
                    .show();
        });
    }
}


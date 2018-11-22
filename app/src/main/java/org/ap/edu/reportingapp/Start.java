package org.ap.edu.reportingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Start extends AppCompatActivity {

    Spinner cmbLokaal = findViewById(R.id.cmbLokaal);

    String[] verdiepingen = new String[]{"-1", "gelijkvloers", "1ste", "2de", "3de", "4de", "dak"};

    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, verdiepingen);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


    }

}

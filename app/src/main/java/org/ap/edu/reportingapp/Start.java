package org.ap.edu.reportingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Start extends AppCompatActivity {
    String[] verdiepingen, lokaalMin1, lokaalGelijkVloers, lokaal1ste, lokaal2de, lokaal3de, lokaal4de, lokaalDak, lokalen, leeg = {""};
    String cmbVerdiepingValue;
    Spinner cmbLokaal, cmbVerdieping;
    ArrayAdapter<String> adapterLokaal, adapterVerdieping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        verdiepingen = getResources().getStringArray(R.array.verdiepingen);
        adapterVerdieping = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, verdiepingen);

        lokaalMin1 = getResources().getStringArray(R.array.lokaalMin1);
        lokaalGelijkVloers = getResources().getStringArray(R.array.lokaalGelijkVloers);
        lokaal1ste = getResources().getStringArray(R.array.lokaal1ste);
        lokaal2de = getResources().getStringArray(R.array.lokaal2de);
        lokaal3de = getResources().getStringArray(R.array.lokaal3de);
        lokaal4de = getResources().getStringArray(R.array.lokaal4de);
        lokaalDak = getResources().getStringArray(R.array.lokaalDak);

        cmbVerdieping = findViewById(R.id.cmbVerdieping);
        cmbLokaal = findViewById(R.id.cmbLokaal);

        cmbVerdieping.setAdapter(adapterVerdieping);

        cmbVerdieping.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View selectedItemView, int position, long id) {
                cmbVerdiepingValue = cmbVerdieping.getSelectedItem().toString();
                cmbLokaal.setEnabled(true);

                //veranderen in switch?
                if (cmbVerdiepingValue.equals("-1")){
                    lokalen = lokaalMin1;
                }
                else if (cmbVerdiepingValue.equals("Gelijkvloers")){
                    lokalen = lokaalGelijkVloers;
                }
                else if (cmbVerdiepingValue.equals("1ste")){
                    lokalen = lokaal1ste;
                }
                else if (cmbVerdiepingValue.equals("2de")){
                    lokalen = lokaal2de;
                }
                else if (cmbVerdiepingValue.equals("3de")){
                    lokalen = lokaal3de;
                }
                else if (cmbVerdiepingValue.equals("4de")){
                    lokalen = lokaal4de;
                }
                else if (cmbVerdiepingValue.equals("Dak")){
                    lokalen = lokaalDak;
                }
                else{
                    lokalen = leeg;
                    cmbLokaal.setEnabled(false);
                }
                adapterLokaal = new ArrayAdapter<>(Start.this, android.R.layout.simple_spinner_dropdown_item, lokalen);
                cmbLokaal.setAdapter(adapterLokaal);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //niet mogelijk denk ik? default waarde
                //is deze code nodig?
                cmbLokaal.setEnabled(false);
            }
        });
    }

}

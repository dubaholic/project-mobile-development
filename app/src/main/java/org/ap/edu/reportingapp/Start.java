package org.ap.edu.reportingapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.IOException;

public class Start extends AppCompatActivity {
    private String[] categorie, verdiepingen, lokaalMin1, lokaalGelijkVloers, lokaal1ste, lokaal2de, lokaal3de, lokaal4de, lokaalDak, lokalen, leeg = {""};
    private String cmbVerdiepingValue;
    private Spinner cmbLokaal, cmbVerdieping, cmbCategorie;
    private ArrayAdapter<String> adapterLokaal, adapterVerdieping, adapterCategorie;
    private Button btnFoto;
    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        verdiepingen = getResources().getStringArray(R.array.verdiepingen);
        lokaalMin1 = getResources().getStringArray(R.array.lokaalMin1);
        lokaalGelijkVloers = getResources().getStringArray(R.array.lokaalGelijkVloers);
        lokaal1ste = getResources().getStringArray(R.array.lokaal1ste);
        lokaal2de = getResources().getStringArray(R.array.lokaal2de);
        lokaal3de = getResources().getStringArray(R.array.lokaal3de);
        lokaal4de = getResources().getStringArray(R.array.lokaal4de);
        lokaalDak = getResources().getStringArray(R.array.lokaalDak);
        categorie = getResources().getStringArray(R.array.categorie);

        cmbVerdieping = findViewById(R.id.cmbVerdieping);
        cmbLokaal = findViewById(R.id.cmbLokaal);
        cmbCategorie = findViewById(R.id.cmbCategorie);
        btnFoto = findViewById(R.id.btnFoto);

        adapterVerdieping = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, verdiepingen);
        adapterCategorie = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categorie);

        cmbVerdieping.setAdapter(adapterVerdieping);
        cmbCategorie.setAdapter(adapterCategorie);

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

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecteer een foto"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                //Bitmap is de afbeelding
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Log.d("IMAGE", bitmap.toString() + " from: " + filePath);
                //imgView.setImageBitmap(bitmap); //om imgView te veranderen naar de gekozen afbeelding
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}

package org.ap.edu.reportingapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;

public class Start extends AppCompatActivity {
    //moet dit private zijn?
    private String[] verdiepingen, lokaalMin1, lokaalGelijkVloers, lokaal1ste, lokaal2de, lokaal3de,
            lokaal4de, lokaalDak, leeg = {""},  lokalen, categorie, urgenties;
    private String verdiepingValue, urgentieColorString = "#FFA500";
    private int seekBarValue = 2;

    private Spinner cmbLokaal, cmbVerdieping, cmbCategorie;
    private ArrayAdapter<String> adapterLokaal, adapterVerdieping, adapterCategorie;
    private Button btnFoto, btnVerzenden;
    private SeekBar sldUrgentie;
    private TextView txtUrgentieValue;

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
        urgenties = getResources().getStringArray(R.array.urgenties);

        cmbVerdieping = findViewById(R.id.cmbVerdieping);
        cmbLokaal = findViewById(R.id.cmbLokaal);
        cmbCategorie = findViewById(R.id.cmbCategorie);
        btnFoto = findViewById(R.id.btnFoto);
        btnVerzenden = findViewById(R.id.btnVerzenden);
        sldUrgentie = findViewById(R.id.sldUrgentie);
        txtUrgentieValue = findViewById(R.id.txtViewUrgentieValue);

        adapterVerdieping = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, verdiepingen);
        adapterCategorie = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categorie);

        cmbVerdieping.setAdapter(adapterVerdieping);
        cmbCategorie.setAdapter(adapterCategorie);

        //Default values voor seekerbar
        sldUrgentie.getProgressDrawable().setColorFilter(Color.parseColor(urgentieColorString), PorterDuff.Mode.MULTIPLY);
        txtUrgentieValue.setText(urgenties[seekBarValue]);

        cmbVerdieping.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View selectedItemView, int position, long id) {
                verdiepingValue = cmbVerdieping.getSelectedItem().toString();
                cmbLokaal.setEnabled(true);

                //veranderen in switch?
                if (verdiepingValue.equals("-1")){
                    lokalen = lokaalMin1;
                }
                else if (verdiepingValue.equals("Gelijkvloers")){
                    lokalen = lokaalGelijkVloers;
                }
                else if (verdiepingValue.equals("1ste")){
                    lokalen = lokaal1ste;
                }
                else if (verdiepingValue.equals("2de")){
                    lokalen = lokaal2de;
                }
                else if (verdiepingValue.equals("3de")){
                    lokalen = lokaal3de;
                }
                else if (verdiepingValue.equals("4de")){
                    lokalen = lokaal4de;
                }
                else if (verdiepingValue.equals("Dak")){
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

        sldUrgentie.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarValue = progress;
                txtUrgentieValue.setText(urgenties[seekBarValue]);
                if (seekBarValue == 0){
                    urgentieColorString = "#00FF00";
                }
                else if (seekBarValue == 1){
                    urgentieColorString = "#FFFF00";
                }
                else if (seekBarValue == 2){
                    urgentieColorString = "#FFA500";
                }
                else if (seekBarValue == 3){
                    urgentieColorString = "#FF0000";
                }
                else {
                    urgentieColorString = "#8B0000";
                }
                sldUrgentie.getProgressDrawable().setColorFilter(Color.parseColor(urgentieColorString), PorterDuff.Mode.MULTIPLY);
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });

        btnVerzenden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Item is verzonden
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

package org.ap.edu.reportingapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Start extends AppCompatActivity {
    //moet dit private zijn?
    private String[] verdiepingen, lokaalMin1, lokaalGelijkVloers, lokaal1ste, lokaal2de, lokaal3de,
            lokaal4de, lokaalDak, leeg = {""},  lokalen, categorie, urgenties;
    private String verdiepingValue, lokaalValue, categorieValue, urgentieColorString = "#FFA500",
            apMail, opmerking, fotoNaam;
    private int seekBarValue = 2;
    private boolean isAfgehandeld = false;
    private UUID schadeId;
    private Date now;

    private Spinner cmbLokaal, cmbVerdieping, cmbCategorie;
    private ArrayAdapter<String> adapterLokaal, adapterVerdieping, adapterCategorie;
    private Button btnFoto, btnFotoMaken, btnVerzenden;
    private SeekBar sldUrgentie;
    private TextView txtUrgentieValue;
    private EditText txtApMail, txtOpmerking;

    private Uri filePath, photoURI;
    File file = null;
    private Bitmap bitmap;
    Schade schadeMelding;

    private static final int CREATE_IMAGE_REQUEST = 100;
    private final int PICK_IMAGE_REQUEST = 71;

    /*Firebase dingen
    //Storage is voor bestanden, Database is voor data
    final FirebaseDatabase database;
    final FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference databaseReference;

    storage = FirebaseStorage.getInstance();
    storageReference = storage.getReference();
    database = FirebaseDatabase.getInstance()
    databaseReference = database.getReference()
    */

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
        btnFotoMaken = findViewById(R.id.btnFotoMaken);
        btnVerzenden = findViewById(R.id.btnVerzenden);
        sldUrgentie = findViewById(R.id.sldUrgentie);
        txtUrgentieValue = findViewById(R.id.txtViewUrgentieValue);
        txtApMail = findViewById(R.id.txtApMail);
        txtOpmerking = findViewById(R.id.txtOpmerking);

        adapterVerdieping = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, verdiepingen);
        adapterCategorie = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categorie);

        cmbVerdieping.setAdapter(adapterVerdieping);
        cmbCategorie.setAdapter(adapterCategorie);

        //Default values
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
                lokaalValue = cmbLokaal.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                cmbLokaal.setEnabled(false);
            }
        });

        cmbCategorie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View selectedItemView, int position, long id) {
                categorieValue = cmbCategorie.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //Do nothing
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

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnFotoMaken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeImage();
            }
        });

        btnVerzenden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apMail = txtApMail.getText().toString();
                opmerking = txtOpmerking.getText().toString();
                schadeId = UUID.randomUUID();
                now = new Date();

                //Invoer controle
                if (opmerking.isEmpty()){opmerking = "NONE";}
                if (apMail.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Vul je AP email adres in",Toast.LENGTH_SHORT).show();
                }
                else if (verdiepingValue.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Kies de juiste verdieping",Toast.LENGTH_SHORT).show();
                }
                else if (lokaalValue.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Kies het lokaal",Toast.LENGTH_SHORT).show();
                }
                else if (categorieValue.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Kies de categorie",Toast.LENGTH_SHORT).show();
                }
                else if (bitmap == null || filePath == null){
                    Toast.makeText(getApplicationContext(),"Kies of maak een foto",Toast.LENGTH_SHORT).show();
                }
                else if (seekBarValue > 4 || seekBarValue < 0){
                    Toast.makeText(getApplicationContext(),"Ongeldige urgentie",Toast.LENGTH_SHORT).show();
                }
                else if (!apMail.endsWith("@ap.be") && !apMail.endsWith("@student.ap.be")){
                    Toast.makeText(getApplicationContext(),"Geef een geldig AP email adres op",Toast.LENGTH_SHORT).show();
                }
                else {
                    //De gekozen foto uploaden op Firebase (voor het object zal worden aangemaakt)
                    //-> fotoNaam wordt pas na uploadImage() gegenereerd
                    uploadImage();
                    if (fotoNaam == null || fotoNaam.isEmpty()) {
                        fotoNaam = "images/" + UUID.randomUUID().toString(); //Tijdelijk, tot Firebase op staat
                    }
                    schadeMelding = new Schade(schadeId, apMail, verdiepingValue, lokaalValue, categorieValue, fotoNaam, seekBarValue, opmerking, now, isAfgehandeld);
                    Log.d("INGEZONDEN ITEM", schadeMelding.toString());
                    Toast.makeText(getApplicationContext(), "Item verzonden!", Toast.LENGTH_SHORT).show();
                    resetScreen();
                    //Hier naar database sturen
                    /*DatabaseReference meldingenRef = databaseReference.child("meldingen");
                    Map<String, Schade> meldingen = new HashMap<>();
                    meldingenRef.put(schadeMelding.getSchadeId(), schadeMelding);
                    meldingenRef.setValueAsync(meldingen);
                    */
                }
            }
        });
    }
    private void resetScreen() {
        txtApMail.setText("");
        txtOpmerking.setText("");
        cmbVerdieping.setSelection(0);
        cmbLokaal.setSelection(0);
        cmbCategorie.setSelection(0);
        sldUrgentie.setProgress(2);

        filePath = null;
        bitmap = null;
        fotoNaam = null;
        apMail = null;
        verdiepingValue = null;
        seekBarValue = 2;
        lokaalValue = null;
        categorieValue = null;
        opmerking = null;

        Log.d("NIEUWE VALUES", txtApMail.getText().toString() + txtOpmerking.getText().toString() + cmbVerdieping.getSelectedItem() + cmbLokaal.getSelectedItem() + cmbCategorie.getSelectedItem() + sldUrgentie.getProgress());
    }
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecteer een foto"), PICK_IMAGE_REQUEST);
    }
    private void takeImage() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            file = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (file != null) {
            filePath = FileProvider.getUriForFile(this,
                    "org.ap.edu.reportingapp.provider",
                    file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, filePath);
            startActivityForResult(intent, CREATE_IMAGE_REQUEST);
        }
    }
    private void uploadImage() {
        //Aanzetten als Firebase klaar is
        /*if(filePath != null)
        {
            //fotoNaam is de naam/ID van de foto die geupload zal worden
            fotoNaam = "images/"+ UUID.randomUUID().toString()
            StorageReference ref = storageReference.child(fotoNaam);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //Hier toast voor success
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Hier toast voor failed
                           }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //Progressbalk nodig?
                        }
                    });
        }*/
    }
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",   /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Log.d("IMAGE", bitmap.toString() + filePath.toString());
                //imgView.setImageBitmap(bitmap); //om imgView te veranderen naar de gekozen afbeelding
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        if (requestCode == CREATE_IMAGE_REQUEST && resultCode == RESULT_OK) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

package org.ap.edu.reportingapp.activities.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.os.Bundle;
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
import java.util.UUID;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.ap.edu.reportingapp.R;
import org.ap.edu.reportingapp.models.Schade;
import org.ap.edu.reportingapp.models.Scoren;

import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnItemSelected;


public class Start extends Activity {
    private String[] verdiepingen, lokaalMin1, lokaalGelijkVloers, lokaal1ste, lokaal2de, lokaal3de,
            lokaal4de, lokaalDak, leeg = {""},  lokalen, categorie, urgenties;
    private String verdiepingValue, lokaalValue, categorieValue, urgentieColorString = "#FFA500",
            apMail, opmerking, fotoNaam;
    private int seekBarValue = 2;
    private boolean isAfgehandeld = false, isNewImage = false;
    private UUID schadeId;
    private Date now;

    private ArrayAdapter<String> adapterLokaal, adapterVerdieping, adapterCategorie;

    private Uri filePath;
    File createdImage = null;
    private Bitmap bitmap;
    Schade schadeMelding;
    Scoren scorenMelding;

    private static final int CREATE_IMAGE_REQUEST = 1;
    private final int PICK_IMAGE_REQUEST = 2;

    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    final DatabaseReference meldingenReference = databaseReference.child("meldingen");
    final DatabaseReference scoresReference = databaseReference.child("scores");
    final StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @BindView(R.id.txtApMail) EditText txtApMail;
    @BindView(R.id.txtOpmerking) EditText txtOpmerking;
    @BindView(R.id.cmbVerdieping) Spinner cmbVerdieping;
    @BindView(R.id.cmbLokaal) Spinner cmbLokaal;
    @BindView(R.id.cmbCategorie) Spinner cmbCategorie;
    @BindView(R.id.txtViewUrgentieValue) TextView txtUrgentieValue;
    @BindView(R.id.imgThumbnail) ImageView imgThumbnail;
    @BindView(R.id.sldUrgentie) SeekBar sldUrgentie;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        ButterKnife.bind(this);
        fillDatabase();
    }

    @OnClick(R.id.btnFoto)
    public void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Selecteer een foto"), PICK_IMAGE_REQUEST);
    }

    @OnClick(R.id.btnFotoMaken)
    public void takeImage() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            createdImage = createImageFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (createdImage != null) {
            filePath = FileProvider.getUriForFile(this,
                    "org.ap.edu.reportingapp.provider",
                    createdImage);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, filePath);
            startActivityForResult(intent, CREATE_IMAGE_REQUEST);
        }
    }

    @OnClick(R.id.btnVerzenden)
    public void send() {
        apMail = txtApMail.getText().toString();
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
            opmerking = txtOpmerking.getText().toString();
            if (opmerking.isEmpty()){opmerking = "Geen opmerking";}
            schadeId = UUID.randomUUID();
            now = new Date();

            uploadImage();
            schadeMelding = new Schade(schadeId.toString(), apMail, verdiepingValue, lokaalValue, categorieValue, fotoNaam, seekBarValue, opmerking, now, isAfgehandeld);
            scorenMelding = new Scoren(apMail, schadeId.toString());

            Toast.makeText(getApplicationContext(), "Item verzonden!", Toast.LENGTH_SHORT).show();
            finish();
            meldingenReference.child(schadeId.toString()).setValue(schadeMelding);



            if(apMail.contains("@ap.be")) {
                apMail.replace("@ap.be", " ");
                String cleanMail = apMail.replace("@ap.be", "");
                System.out.println(cleanMail);
                scoresReference.child(cleanMail.toString()).child(schadeId.toString()).setValue(scorenMelding);

            }
            else if(apMail.contains("@student.ap.be")) {
                apMail.replace("@student.ap.be", "");
                String cleanMail = apMail.replace("@student.ap.be", "");
                System.out.println(cleanMail);
                scoresReference.child(cleanMail.toString()).child(schadeId.toString()).setValue(scorenMelding);
            }
            if (isNewImage) {
                createdImage.delete();
            }
            resetScreen();
        }

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
        schadeId = null;
    }

    private void uploadImage() {
        if(filePath != null)
        {
            fotoNaam = UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("fotos/" + fotoNaam);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getApplicationContext(), "Foto Geupload!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Foto niet verzonden", Toast.LENGTH_SHORT).show();
                           }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        }
                    });
        }
    }
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
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
                isNewImage = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == CREATE_IMAGE_REQUEST && resultCode == RESULT_OK) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                isNewImage = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imgThumbnail.setImageBitmap(bitmap);
    }

    private void fillDatabase(){ verdiepingen = getResources().getStringArray(R.array.verdiepingen);
        lokaalMin1 = getResources().getStringArray(R.array.lokaalMin1);
        lokaalGelijkVloers = getResources().getStringArray(R.array.lokaalGelijkVloers);
        lokaal1ste = getResources().getStringArray(R.array.lokaal1ste);
        lokaal2de = getResources().getStringArray(R.array.lokaal2de);
        lokaal3de = getResources().getStringArray(R.array.lokaal3de);
        lokaal4de = getResources().getStringArray(R.array.lokaal4de);
        lokaalDak = getResources().getStringArray(R.array.lokaalDak);

        categorie = getResources().getStringArray(R.array.categorie);

        urgenties = getResources().getStringArray(R.array.urgenties);

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

                switch (verdiepingValue) {
                    case "-1":
                        lokalen = lokaalMin1;
                        break;
                    case "Gelijkvloers":
                        lokalen = lokaalGelijkVloers;
                        break;
                    case "1ste":
                        lokalen = lokaal1ste;
                        break;
                    case "2de":
                        lokalen = lokaal2de;
                        break;
                    case "3de":
                        lokalen = lokaal3de;
                        break;
                    case "4de":
                        lokalen = lokaal4de;
                        break;
                    case "Dak":
                        lokalen = lokaalDak;
                        break;
                    default:
                        lokalen = leeg;
                        cmbLokaal.setEnabled(false);
                        break;
                }
                adapterLokaal = new ArrayAdapter<>(Start.this, android.R.layout.simple_spinner_dropdown_item, lokalen);
                cmbLokaal.setAdapter(adapterLokaal);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                cmbLokaal.setEnabled(false);
            }
        });

        cmbLokaal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override public void onItemSelected(AdapterView<?> parent, View selectedItemView, int position, long id)
            {
                lokaalValue = cmbLokaal.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        cmbCategorie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View selectedItemView, int position, long id)
            {
                categorieValue = cmbCategorie.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {
                //When no item is selected
            }
        });

        sldUrgentie.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
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
            public void onStartTrackingTouch(SeekBar seekBar)
            {
                //when the user touch hold the seekbar
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
                // when the user stopped touching the seekbar
            }
        });
    }
}

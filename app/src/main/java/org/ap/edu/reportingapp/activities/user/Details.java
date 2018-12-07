package org.ap.edu.reportingapp.activities.user;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.ap.edu.reportingapp.R;

import static java.lang.Integer.parseInt;

/**
 * Created by Maarten on 30/11/2018.
 */

public class Details extends Activity {
    private String[] urgenties;
    private String apMail, verdieping, lokaal, categorie, opmerking, timeStampStringDag,
            timeStampStringMaand, timeStampStringUur, timeStampStringMinuut, fotoNaam;
    private int urgentie;
    private TextView txtViewApMailIngevuld, txtViewVerdiepingIngevuld, txtViewLokaalIngevuld,
            txtViewCategorieIngevuld, txtViewOpmerkingIngevuld, txtViewTimeIngevuld,
            txtUrgentieValueIngevuld;
    private Button btnTerug;

    private ImageView imgMelding;

    private static final int MY_PERMISSIONS_REQUEST = 100;
    private Context context = Details.this;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        final String id = getIntent().getExtras().getString("id","Leeg");
        final DatabaseReference databaseReference = database.getReference();
        final StorageReference storageReference = storage.getReference();

        urgenties = getResources().getStringArray(R.array.urgenties);

        txtViewApMailIngevuld = findViewById(R.id.txtViewApMailIngevuld);
        txtViewVerdiepingIngevuld = findViewById(R.id.txtViewVerdiepingIngevuld);
        txtViewLokaalIngevuld = findViewById(R.id.txtViewLokaalIngevuld);
        txtViewCategorieIngevuld = findViewById(R.id.txtViewCategorieIngevuld);
        txtViewOpmerkingIngevuld = findViewById(R.id.txtViewOpmerkingIngevuld);
        txtViewTimeIngevuld = findViewById(R.id.txtViewTimeIngevuld);
        txtUrgentieValueIngevuld = findViewById(R.id.txtViewUrgentieValueIngevuld);
        imgMelding = findViewById(R.id.imgMelding);
        btnTerug = findViewById(R.id.btnTerug);
        requestStoragePermission();

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    if (String.valueOf(postSnapshot.child("schadeId").getValue()) != null
                            && String.valueOf(postSnapshot.child("schadeId").getValue()).equals(id)) {
                        verdieping = postSnapshot.child("verdieping").getValue().toString();
                        apMail = postSnapshot.child("email").getValue().toString();
                        lokaal = postSnapshot.child("lokaal").getValue().toString();
                        opmerking = postSnapshot.child("opmerking").getValue().toString();
                        categorie = postSnapshot.child("categorie").getValue().toString();
                        urgentie = parseInt(postSnapshot.child("urgentie").getValue().toString());
                        timeStampStringDag = postSnapshot.child("timeStamp/date").getValue().toString();
                        timeStampStringMaand = postSnapshot.child("timeStamp/month").getValue().toString();
                        timeStampStringUur = postSnapshot.child("timeStamp/hours").getValue().toString();
                        timeStampStringMinuut = postSnapshot.child("timeStamp/minutes").getValue().toString();
                        fotoNaam = postSnapshot.child("fotoNaam").getValue().toString();
                        Log.d("fotolog", fotoNaam);
                        Log.d("fotoUrl", String.valueOf(storageReference.child("fotos/" + fotoNaam +".jpg").getDownloadUrl()));
                        Picasso.with(Details.this)
                                .load(storageReference.child("fotos/"+fotoNaam+".jpg").getDownloadUrl().toString())
                                .into(imgMelding);

                        /*storageReference.child("fotos/" + fotoNaam +".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d("fotoLog", "de foto is succesvol ingeladen");
                                Glide.with(Details.this)
                                        .load(storageReference.child("fotos/"+fotoNaam+".jpg").getStream())
                                        .fitCenter()
                                        .into(imgMelding);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        }); */


                        txtViewApMailIngevuld.setText(apMail);
                        txtViewVerdiepingIngevuld.setText(verdieping);
                        txtViewLokaalIngevuld.setText(lokaal);
                        txtViewCategorieIngevuld.setText(categorie);
                        txtViewOpmerkingIngevuld.setText(opmerking);
                        txtUrgentieValueIngevuld.setText(urgenties[urgentie]);



                        txtViewTimeIngevuld.setText("Melding gemaakt op " +
                                String.format("%02d", Integer.parseInt(timeStampStringDag)) + "/" +
                                String.format("%02d", Integer.parseInt(timeStampStringMaand)) + " om " +
                                String.format("%02d", Integer.parseInt(timeStampStringUur)) + ":" +
                                String.format("%02d", Integer.parseInt(timeStampStringMinuut)));
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        btnTerug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(Details.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Details.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST);
        }
    }

    private void loadImageFromFirebase() {

    }
}

package org.ap.edu.reportingapp.activities.user;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.ap.edu.reportingapp.R;

import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.lang.Integer.parseInt;

/**
 * Created by Maarten on 30/11/2018.
 */

public class Details extends Activity {
    private String[] urgenties;
    private static final int MY_PERMISSIONS_REQUEST = 100;
    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    final StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @BindView(R.id.txtViewApMailIngevuld) TextView txtViewApMailIngevuld;
    @BindView(R.id.txtViewVerdiepingIngevuld) TextView txtViewVerdiepingIngevuld;
    @BindView(R.id.txtViewLokaalIngevuld) TextView txtViewLokaalIngevuld;
    @BindView(R.id.txtViewCategorieIngevuld) TextView txtViewCategorieIngevuld;
    @BindView(R.id.txtViewOpmerkingIngevuld) TextView txtViewOpmerkingIngevuld;
    @BindView(R.id.txtViewTimeIngevuld) TextView txtViewTimeIngevuld;
    @BindView(R.id.txtViewUrgentieValue) TextView txtUrgentieValueIngevuld;
    @BindView(R.id.imgMelding) ImageView imgMelding;
    @BindView(R.id.btnTerug) Button btnTerug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        //final String id = getIntent().getExtras().getString("id","Leeg");
        ButterKnife.bind(this);
        urgenties = getResources().getStringArray(R.array.urgenties);
        requestStoragePermission();
        getDetails();
    }

    @OnClick(R.id.btnTerug)
    public void submit() {
        finish();
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

    private void getDetails() {
        final String id = getIntent().getExtras().getString("id","Leeg");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    if (String.valueOf(postSnapshot.child("schadeId").getValue()) != null
                            && String.valueOf(postSnapshot.child("schadeId").getValue()).equals(id)) {
                        String verdieping = postSnapshot.child("verdieping").getValue().toString();
                        String apMail = postSnapshot.child("email").getValue().toString();
                        String lokaal = postSnapshot.child("lokaal").getValue().toString();
                        String opmerking = postSnapshot.child("opmerking").getValue().toString();
                        String categorie = postSnapshot.child("categorie").getValue().toString();
                        int urgentie = parseInt(postSnapshot.child("urgentie").getValue().toString());
                        String timeStampStringDag = postSnapshot.child("timeStamp/date").getValue().toString();
                        String timeStampStringMaand = postSnapshot.child("timeStamp/month").getValue().toString();
                        String timeStampStringUur = postSnapshot.child("timeStamp/hours").getValue().toString();
                        String timeStampStringMinuut = postSnapshot.child("timeStamp/minutes").getValue().toString();
                        String fotoNaam = postSnapshot.child("fotoNaam").getValue().toString();
                        Log.d("fotolog", fotoNaam);
                        //Log.d("fotoUrl", String.valueOf(storageReference.child("fotos/" + fotoNaam +".jpg").getDownloadUrl()));

                        /* Picasso.with(Details.this)
                                .load(storageReference.child("fotos/"+fotoNaam+".jpg").getDownloadUrl().toString())
                                .into(imgMelding); */

                        String downloadUrl = storageReference.child("fotos/"+fotoNaam+".jpg").getDownloadUrl().toString();
                        Log.d("fotoUrl", downloadUrl);
                        Glide.with(Details.this)
                                .load(downloadUrl)
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

    }
}

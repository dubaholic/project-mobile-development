package org.ap.edu.reportingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Integer.parseInt;

/**
 * Created by Maarten on 30/11/2018.
 */

public class Details extends Activity {
    private String apMail, verdieping, lokaal, categorie, opmerking, fotoNaam, timeStampString;
    private int urgentie;
    private Date timeStamp;

    private TextView txtViewApMailIngevuld, txtViewVerdiepingIngevuld, txtViewLokaalIngevuld,
            txtViewCategorieIngevuld, txtViewFotoIngevuld, txtViewOpmerkingIngevuld;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        final String id = getIntent().getExtras().getString("id","Leeg");
        final DatabaseReference databaseReference = database.getReference();

        txtViewApMailIngevuld = findViewById(R.id.txtViewApMailIngevuld);
        txtViewVerdiepingIngevuld = findViewById(R.id.txtViewVerdiepingIngevuld);
        txtViewLokaalIngevuld = findViewById(R.id.txtViewLokaalIngevuld);
        txtViewCategorieIngevuld = findViewById(R.id.txtViewCategorieIngevuld);
        txtViewFotoIngevuld = findViewById(R.id.txtViewFotoIngevuld);
        txtViewOpmerkingIngevuld = findViewById(R.id.txtViewOpmerkingIngevuld);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    if (postSnapshot.child("schadeId").getValue() == id
                            || postSnapshot.child("lokaal").getValue() != null) {
                        verdieping = postSnapshot.child("verdieping").getValue().toString();
                        apMail = postSnapshot.child("email").getValue().toString();
                        lokaal = postSnapshot.child("lokaal").getValue().toString();
                        opmerking = postSnapshot.child("opmerking").getValue().toString();
                        fotoNaam = postSnapshot.child("fotoNaam").getValue().toString();
                        categorie = postSnapshot.child("categorie").getValue().toString();
                        urgentie = parseInt(postSnapshot.child("urgentie").getValue().toString());
                        timeStampString = postSnapshot.child("timeStamp").getValue().toString();
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        timeStamp = null;
                        try {
                            timeStamp = formatter.parse(timeStampString);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        txtViewApMailIngevuld.setText(apMail);
                        txtViewVerdiepingIngevuld.setText(verdieping);
                        txtViewLokaalIngevuld.setText(lokaal);
                        txtViewCategorieIngevuld.setText(categorie);
                        txtViewFotoIngevuld.setText(fotoNaam);
                        txtViewOpmerkingIngevuld.setText(opmerking);
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

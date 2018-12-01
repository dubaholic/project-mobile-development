package org.ap.edu.reportingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static java.lang.Integer.parseInt;

/**
 * Created by Maarten on 30/11/2018.
 */

public class Details extends Activity {
    private String[] urgenties;
    private String apMail, verdieping, lokaal, categorie, opmerking, timeStampStringDag,
            timeStampStringMaand, timeStampStringUur, timeStampStringMinuut;
    private int urgentie;

    private TextView txtViewApMailIngevuld, txtViewVerdiepingIngevuld, txtViewLokaalIngevuld,
            txtViewCategorieIngevuld, txtViewOpmerkingIngevuld, txtViewTimeIngevuld,
            txtUrgentieValueIngevuld;
    private Button btnTerug;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        final String id = getIntent().getExtras().getString("id","Leeg");
        final DatabaseReference databaseReference = database.getReference();

        urgenties = getResources().getStringArray(R.array.urgenties);

        txtViewApMailIngevuld = findViewById(R.id.txtViewApMailIngevuld);
        txtViewVerdiepingIngevuld = findViewById(R.id.txtViewVerdiepingIngevuld);
        txtViewLokaalIngevuld = findViewById(R.id.txtViewLokaalIngevuld);
        txtViewCategorieIngevuld = findViewById(R.id.txtViewCategorieIngevuld);
        txtViewOpmerkingIngevuld = findViewById(R.id.txtViewOpmerkingIngevuld);
        txtViewTimeIngevuld = findViewById(R.id.txtViewTimeIngevuld);
        txtUrgentieValueIngevuld = findViewById(R.id.txtViewUrgentieValueIngevuld);
        btnTerug = findViewById(R.id.btnTerug);

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
}

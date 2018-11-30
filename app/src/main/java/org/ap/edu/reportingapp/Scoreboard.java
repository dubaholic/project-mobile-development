package org.ap.edu.reportingapp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashSet;

public class Scoreboard extends Activity {

    private ListView lstScores;
    private Button btnTerug;
    private ArrayList<String> scoreArrayList = new ArrayList<>();
    private HashSet<String> uniekeEmails = new HashSet<>();
    private ArrayAdapter<String> scoresAadapter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        final DatabaseReference databaseReference = database.getReference();
        final DatabaseReference meldingenReference = databaseReference.child("meldingen");

        lstScores = findViewById(R.id.lstScores);
        btnTerug = findViewById(R.id.btnTerug);

        scoresAadapter = new ArrayAdapter<>(Scoreboard.this, android.R.layout.simple_list_item_1, scoreArrayList );

        lstScores.setAdapter(scoresAadapter);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                scoresAadapter.clear();

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    int teller = 0;
                    databaseReference.orderByChild("email");
                    String email = postSnapshot.child("email").getValue().toString();
                    if(!uniekeEmails.contains(email)) {
                        uniekeEmails.add(email);
                        scoresAadapter.add(email + " - " +  dataSnapshot.getChildrenCount());

                        }
                }

                }
                @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

        btnTerug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Scoreboard.this, Listing.class));

            }
        });

    }



}

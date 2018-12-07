package org.ap.edu.reportingapp.activities.user;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.ap.edu.reportingapp.R;

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
        final DatabaseReference scoresReference = databaseReference.child("scores");


        lstScores = findViewById(R.id.lstScores);
        btnTerug = findViewById(R.id.btnTerug);

        scoresAadapter = new ArrayAdapter<>(Scoreboard.this, android.R.layout.simple_list_item_1, scoreArrayList );

        lstScores.setAdapter(scoresAadapter);

        scoresReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                scoresAadapter.clear();

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                   // if(postSnapshot.child("scores").getValue() != null) {
                        databaseReference.orderByChild("email");
                        String apMail = postSnapshot.child("email").getValue().toString();
                        if(apMail.contains("@ap.be")) {
                            apMail.replace("@ap.be", " ");
                            String cleanMail = apMail.replace("@ap.be", "");
                            System.out.println(cleanMail);
                           // scoresReference.child(cleanMail.toString()).child(schadeId.toString()).setValue(scorenMelding);
                            if (!uniekeEmails.contains(apMail)) {
                                uniekeEmails.add(apMail);
                                System.out.println(dataSnapshot.child(cleanMail).getChildrenCount());
                                scoresAadapter.add(apMail + " - " + (int)dataSnapshot.child(cleanMail).getChildrenCount());
                                Log.d("Test", "komt wel hier");
                            }
                        }
                        if(apMail.contains("@student.ap.be")) {
                            apMail.replace("@student.ap.be", "");
                            String cleanMail = apMail.replace("@student.ap.be", "");
                            System.out.println(cleanMail);
                            if (!uniekeEmails.contains(apMail)) {
                                uniekeEmails.add(apMail);
                                scoresAadapter.add(apMail + " - " + dataSnapshot.child(cleanMail).getChildrenCount());
                                Log.d("Test", "komt wel hier");
                            }

                           // scoresReference.child(cleanMail.toString()).child(schadeId.toString()).setValue(scorenMelding);
                        }

                    }
                    Log.d("Test", "komt niet hier");
               // }

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
                finish();

            }
        });

    }



}

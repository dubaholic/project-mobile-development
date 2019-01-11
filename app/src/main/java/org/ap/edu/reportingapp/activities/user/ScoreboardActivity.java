package org.ap.edu.reportingapp.activities.user;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.ap.edu.reportingapp.R;

import java.util.ArrayList;
import java.util.HashSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScoreboardActivity extends Activity {
    private ArrayList<String> scoreArrayList = new ArrayList<>();
    private HashSet<String> uniekeEmails = new HashSet<>();
    private ArrayAdapter<String> scoresAadapter;

    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    final DatabaseReference scoresReference = databaseReference.child("scores");

    @BindView(R.id.lstScores) ListView lstScores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        ButterKnife.bind(this);
        getScoreboard();
    }

    @OnClick(R.id.btnTerug)
    public void submit() {
        finish();
    }

    private void getScoreboard() {
        scoresAadapter = new ArrayAdapter<>(ScoreboardActivity.this, android.R.layout.simple_list_item_1, scoreArrayList );
        scoresReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                //scoresAadapter.clear();

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    // if(postSnapshot.child("scores").getValue() != null) {
                    databaseReference.orderByChild("email");
                    int score = 1;
                    String apMail = postSnapshot.child("email").getValue().toString();
                    Log.d("email", apMail);
                    if(apMail.contains("@ap.be")) {
                        //apMail.replace("@ap.be", " ");
                        score++;
                        String cleanMail = apMail.replace("@ap.be", "");
                        Log.d("cleanmail", cleanMail);
                        // scoresReference.child(cleanMail.toString()).child(schadeId.toString()).setValue(scorenMelding);
                        if (!uniekeEmails.contains(apMail)) {
                            uniekeEmails.add(apMail);
                            Log.d("uniekeMail", cleanMail);
                            //int score = (int) dataSnapshot.child(cleanMail).getChildrenCount();
                            Log.d("scoren",cleanMail + String.valueOf(score));
                            scoreArrayList.add(apMail + " - " + score);

                        }
                    }
                    else if(apMail.contains("@student.ap.be")) {
                        apMail.replace("@student.ap.be", "");
                        String cleanMail = apMail.replace("@student.ap.be", "");
                        score++;
                        Log.d("cleanmail", cleanMail);
                        if (!uniekeEmails.contains(apMail)) {
                            uniekeEmails.add(apMail);
                            Log.d("uniekeMail", cleanMail);
                            // int score = (int) dataSnapshot.child(cleanMail).getChildrenCount();
                            Log.d("scoren", cleanMail + String.valueOf(score));
                            scoreArrayList.add(apMail + " - " + score);
                        }

                        // scoresReference.child(cleanMail.toString()).child(schadeId.toString()).setValue(scorenMelding);
                    }
                }
                lstScores.setAdapter(scoresAadapter);
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
    }
}

package org.ap.edu.reportingapp.activities.user;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.ap.edu.reportingapp.R;
import org.ap.edu.reportingapp.adapters.Adapter_Default;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.util.Map.Entry.comparingByValue;

public class ScoreboardActivity extends Activity {
    private ArrayList<String> scoreStringArrayList = new ArrayList<>();
    HashMap<String, Integer> scoreMap = new HashMap<>();
    private Adapter_Default scoresAadapter;
    private String cleanMail;

    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    final DatabaseReference scoresReference = databaseReference.child("scores");

    @BindView(R.id.lstScores) RecyclerView lstScores;

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
        lstScores.setLayoutManager(new LinearLayoutManager(ScoreboardActivity.this));
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL  );
        lstScores.addItemDecoration(decoration);

        scoresReference.addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String apMail = postSnapshot.child("email").getValue().toString();
                    cleanMail = apMail.replace("@ap.be", "");
                    cleanMail = cleanMail.replace("@student.ap.be", "");
                    cleanMail = cleanMail.replace(".", " ");
                    if (scoreMap.containsKey(cleanMail)) {
                        int score = scoreMap.get(cleanMail) + 1;
                        scoreMap.replace(cleanMail, score);
                    }
                    else {
                        scoreMap.put(cleanMail, 1);
                    }
                }
                scoreMap = sortByValue(scoreMap);
                scoreStringArrayList = new ArrayList<>();
                for (Map.Entry<String, Integer> score : scoreMap.entrySet()) {
                    scoreStringArrayList.add(score.getKey() + " - " + score.getValue() + " Meldingen");
                }
                scoresAadapter = new Adapter_Default(ScoreboardActivity.this, scoreStringArrayList);
                lstScores.setAdapter(scoresAadapter);
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

    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm)
    {
        List<Map.Entry<String, Integer> > list = new LinkedList<>(hm.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2)
            {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        HashMap<String, Integer> temp = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
}


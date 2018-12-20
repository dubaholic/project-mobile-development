package org.ap.edu.reportingapp.activities.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.ap.edu.reportingapp.R;
import org.ap.edu.reportingapp.activities.admin.Admin_Meldingen;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Maarten on 23/11/2018.
 */

public class Listing extends Activity {
    private String[] verdiepingen, lokaalMin1, lokaalGelijkVloers, lokaal1ste, lokaal2de, lokaal3de,
            lokaal4de, lokaalDak, leeg = {""}, lokalen;
    private String verdiepingValue, lokaalValue;

    private ListView lstBestaand;
    private Spinner cmbLokaal, cmbVerdieping;
    //private Button btnAdmin;
    private ArrayAdapter<String> adapterLokaal, adapterVerdieping, bestaandDataAdapter;
    ArrayList<String> bestaandDataArrayList = new ArrayList<>();
    ArrayList<String> bestaandDataIdArrayList = new ArrayList<>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @BindView(R.id.btnAdmin) Button btnAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        //FirebaseDatabase.getInstance().setPersistenceEnabled(false);
        final DatabaseReference databaseReference = database.getReference();
        final DatabaseReference meldingenReference = databaseReference.child("meldingen");
        final Boolean isAdmin = getIntent().getExtras().getBoolean("isAdmin",false);
        //final String apMailAuth = getIntent().getExtras().getString("apMailAuth","Leeg");

        //final Boolean isAdmin = getIntent().getExtras().getBoolean("isAdmin",false);

        verdiepingen = getResources().getStringArray(R.array.verdiepingen);
        lokaalMin1 = getResources().getStringArray(R.array.lokaalMin1);
        lokaalGelijkVloers = getResources().getStringArray(R.array.lokaalGelijkVloers);
        lokaal1ste = getResources().getStringArray(R.array.lokaal1ste);
        lokaal2de = getResources().getStringArray(R.array.lokaal2de);
        lokaal3de = getResources().getStringArray(R.array.lokaal3de);
        lokaal4de = getResources().getStringArray(R.array.lokaal4de);
        lokaalDak = getResources().getStringArray(R.array.lokaalDak);

        cmbVerdieping = findViewById(R.id.cmbVerdieping);
        cmbLokaal = findViewById(R.id.cmbLokaal);
        lstBestaand = findViewById(R.id.lstBestaand);

        ///btnAdmin = findViewById(R.id.btnAdmin);

        adapterVerdieping = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, verdiepingen);
        bestaandDataAdapter = new ArrayAdapter<>(Listing.this, android.R.layout.simple_list_item_1, bestaandDataArrayList);

        cmbVerdieping.setAdapter(adapterVerdieping);
        lstBestaand.setAdapter(bestaandDataAdapter);

        if (isAdmin){
            btnAdmin.setVisibility(View.VISIBLE);
        }

        cmbVerdieping.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View selectedItemView, int position, long id) {
                verdiepingValue = cmbVerdieping.getSelectedItem().toString();
                cmbLokaal.setEnabled(true);

                if (verdiepingValue.equals("-1")) {
                    lokalen = lokaalMin1;
                } else if (verdiepingValue.equals("Gelijkvloers")) {
                    lokalen = lokaalGelijkVloers;
                } else if (verdiepingValue.equals("1ste")) {
                    lokalen = lokaal1ste;
                } else if (verdiepingValue.equals("2de")) {
                    lokalen = lokaal2de;
                } else if (verdiepingValue.equals("3de")) {
                    lokalen = lokaal3de;
                } else if (verdiepingValue.equals("4de")) {
                    lokalen = lokaal4de;
                } else if (verdiepingValue.equals("Dak")) {
                    lokalen = lokaalDak;
                } else {
                    lokalen = leeg;
                    cmbLokaal.setEnabled(false);
                }
                adapterLokaal = new ArrayAdapter<>(Listing.this, android.R.layout.simple_spinner_dropdown_item, lokalen);
                cmbLokaal.setAdapter(adapterLokaal);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                cmbLokaal.setEnabled(false);
            }
        });

        cmbLokaal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View selectedItemView, final int position, long id) {
                lokaalValue = cmbLokaal.getSelectedItem().toString();
                bestaandDataIdArrayList = new ArrayList<>();
                bestaandDataAdapter.clear();
                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            if (postSnapshot.child("verdieping").getValue() != null
                                    || postSnapshot.child("lokaal").getValue() != null){
                                String verdieping = postSnapshot.child("verdieping").getValue().toString();
                                String lokaal = postSnapshot.child("lokaal").getValue().toString();
                                if (verdiepingValue.equals(verdieping) && lokaalValue.equals(lokaal)) {
                                    String categorie = postSnapshot.child("categorie").getValue().toString();
                                    String opmerking = postSnapshot.child("opmerking").getValue().toString();
                                    String id = postSnapshot.child("schadeId").getValue().toString();
                                    bestaandDataIdArrayList.add(id);
                                    bestaandDataAdapter.add(categorie + " - " + opmerking);
                                    }
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lstBestaand.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Listing.this, Details.class);
                intent.putExtra("id", bestaandDataIdArrayList.get(position));
                startActivity(intent);
            }
        });

    }

    @OnClick(R.id.btnNieuw)
    public void submit(){
        startActivity(new Intent(Listing.this, Start.class));
    }

    @OnClick(R.id.btnScoreBoard)
    public  void showScoreboard() {
        startActivity(new Intent(Listing.this, Scoreboard.class));
    }

    @OnClick(R.id.btnAdmin)
    public void showAdminButton{
        startActivity(new Intent(Listing.this, Admin_Meldingen.class));
    }

}
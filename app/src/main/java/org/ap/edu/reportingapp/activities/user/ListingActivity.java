package org.ap.edu.reportingapp.activities.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.ap.edu.reportingapp.R;
import org.ap.edu.reportingapp.activities.admin.Admin_Meldingen;
import org.ap.edu.reportingapp.adapters.Adapter_Default;
import org.ap.edu.reportingapp.models.Mededeling;
import org.ap.edu.reportingapp.models.Schade;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListingActivity extends Activity implements Adapter_Default.ItemClickListener{
    private String[] lokaalMin1;
    private String[] lokaalGelijkVloers;
    private String[] lokaal1ste;
    private String[] lokaal2de;
    private String[] lokaal3de;
    private String[] lokaal4de;
    private String[] lokaalDak;
    private String[] leeg = {""};
    private String[] lokalen;
    private String verdiepingValue, lokaalValue;

    private ArrayAdapter<String> adapterLokaal;
    private Adapter_Default bestaandDataAdapter;
    ArrayList<String> bestaandDataStringArrayList = new ArrayList<>();
    ArrayList<Schade> bestaandDataArrayList = new ArrayList<>();
    ArrayList<Mededeling> bestaandDataMededelingArrayList = new ArrayList<>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @BindView(R.id.btnAdmin) Button btnAdmin;
    @BindView(R.id.cmbVerdieping) Spinner cmbVerdieping;
    @BindView(R.id.cmbLokaal) Spinner cmbLokaal;
    @BindView(R.id.lstBestaand) RecyclerView lstBestaand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);
        final DatabaseReference databaseReference = database.getReference();
        final Boolean isAdmin = getIntent().getExtras().getBoolean("isAdmin",false);
        configureAdapters();

        if (isAdmin){
            btnAdmin.setVisibility(View.VISIBLE);
        }

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
                adapterLokaal = new ArrayAdapter<>(ListingActivity.this, android.R.layout.simple_spinner_dropdown_item, lokalen);
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
                bestaandDataArrayList = new ArrayList<>();
                bestaandDataStringArrayList = new ArrayList<>();
                bestaandDataMededelingArrayList = new ArrayList<>();

                bestaandDataAdapter = new Adapter_Default(ListingActivity.this, bestaandDataStringArrayList);
                lstBestaand.setAdapter(bestaandDataAdapter);
                databaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            if (postSnapshot.child("verdieping").getValue() != null
                                    && postSnapshot.child("lokaal").getValue() != null){
                                Schade schade = postSnapshot.getValue(Schade.class);
                                if (verdiepingValue.equals(schade.getVerdieping()) && lokaalValue.equals(schade.getLokaal())) {
                                    bestaandDataArrayList.add(schade);
                                    bestaandDataStringArrayList.add(schade.getCategorie() + " - " + schade.getOpmerking());
                                    }
                            }
                            else if (postSnapshot.child("mededelingText").getValue() != null
                                    && postSnapshot.child("dateRepaired").getValue() != null){
                                Mededeling mededeling = postSnapshot.getValue(Mededeling.class);
                                bestaandDataMededelingArrayList.add(mededeling);
                            }
                        }
                        bestaandDataAdapter.setClickListener(ListingActivity.this);
                        lstBestaand.setAdapter(bestaandDataAdapter);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        //unused
                    }
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        //unused
                    }
                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        //unused
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        //unused
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //unused
            }
        });

    }

    @OnClick(R.id.btnNieuw)
    public void submit(){
        startActivity(new Intent(ListingActivity.this, SubmitActivity.class));
    }

    @OnClick(R.id.btnScoreBoard)
    public  void showScoreboard() {
        startActivity(new Intent(ListingActivity.this, ScoreboardActivity.class));
    }

    @OnClick(R.id.btnAdmin)
    public void showAdminButton(){
        startActivity(new Intent(ListingActivity.this, Admin_Meldingen.class));
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(ListingActivity.this, DetailsActivity.class);
        intent.putExtra("id", bestaandDataArrayList.get(position).getSchadeId());
        startActivity(intent);
    }

    public void configureAdapters() {
        String[] verdiepingen = getResources().getStringArray(R.array.verdiepingen);
        lokaalMin1 = getResources().getStringArray(R.array.lokaalMin1);
        lokaalGelijkVloers = getResources().getStringArray(R.array.lokaalGelijkVloers);
        lokaal1ste = getResources().getStringArray(R.array.lokaal1ste);
        lokaal2de = getResources().getStringArray(R.array.lokaal2de);
        lokaal3de = getResources().getStringArray(R.array.lokaal3de);
        lokaal4de = getResources().getStringArray(R.array.lokaal4de);
        lokaalDak = getResources().getStringArray(R.array.lokaalDak);

        ArrayAdapter<String> adapterVerdieping = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, verdiepingen);

        cmbVerdieping.setAdapter(adapterVerdieping);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        lstBestaand.setLayoutManager(mLayoutManager);
        lstBestaand.setLayoutManager(new LinearLayoutManager(ListingActivity.this));
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL  );
        lstBestaand.addItemDecoration(decoration);
        lstBestaand.setAdapter(bestaandDataAdapter);
    }
}

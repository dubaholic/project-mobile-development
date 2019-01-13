package org.ap.edu.reportingapp.activities.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.ap.edu.reportingapp.R;
import org.ap.edu.reportingapp.adapters.Adapter_Default;
import org.ap.edu.reportingapp.models.Schade;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class Admin_Meldingen extends Activity implements Adapter_Default.ItemClickListener{

    private Adapter_Default bestaandDataAdapter;

    ArrayList<String> bestaandDataArrayList = new ArrayList<>();
    ArrayList<String> bestaandDataIdArrayList = new ArrayList<>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    final DatabaseReference databaseReference = database.getReference();

    @BindView(R.id.lstMeldingen) RecyclerView lstMeldingen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list);
        ButterKnife.bind(this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);

        lstMeldingen.setLayoutManager(mLayoutManager);
        lstMeldingen.setLayoutManager(new LinearLayoutManager(Admin_Meldingen.this));

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL  );
        lstMeldingen.addItemDecoration(decoration);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    if ((postSnapshot.child("categorie").getValue() != null
                            || postSnapshot.child("opmerking").getValue() != null) && !((Boolean) postSnapshot.child("afgehandeld").getValue())){
                        Schade schade = postSnapshot.getValue(Schade.class);

                        bestaandDataIdArrayList.add(schade.getSchadeId());
                        bestaandDataArrayList.add(schade.getCategorie() + " - " + schade.getOpmerking());
                        }
                    }
                bestaandDataAdapter = new Adapter_Default(Admin_Meldingen.this, bestaandDataArrayList);
                bestaandDataAdapter.setClickListener(Admin_Meldingen.this);
                lstMeldingen.setAdapter(bestaandDataAdapter);
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

    @OnClick(R.id.btnTerug)
    public void submit() {
        finish();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(Admin_Meldingen.this, Admin_Melding_Afhandeling.class);
        intent.putExtra("id", bestaandDataIdArrayList.get(position));
        startActivity(intent);
    }

}

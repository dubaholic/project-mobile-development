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
import org.ap.edu.reportingapp.activities.user.Details;
import org.ap.edu.reportingapp.adapters.Adapter_Admin_Meldingen;

import java.util.ArrayList;

/**
 * Created by Maarten on 7/12/2018.
 */

public class Admin_Meldingen extends Activity implements Adapter_Admin_Meldingen.ItemClickListener{

    private RecyclerView lstMeldingen;
    private Adapter_Admin_Meldingen bestaandDataAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    ArrayList<String> bestaandDataArrayList = new ArrayList<>();
    ArrayList<String> bestaandDataIdArrayList = new ArrayList<>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list);

        final DatabaseReference databaseReference = database.getReference();
        final DatabaseReference meldingenReference = databaseReference.child("meldingen");

        lstMeldingen = findViewById(R.id.lstMeldingen);

        mLayoutManager = new LinearLayoutManager(this);
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
                        String categorie = postSnapshot.child("categorie").getValue().toString();
                        String opmerking = postSnapshot.child("opmerking").getValue().toString();
                        String id = postSnapshot.child("schadeId").getValue().toString();

                        bestaandDataIdArrayList.add(id);
                        bestaandDataArrayList.add(categorie + " - " + opmerking);
                        }
                    }
                bestaandDataAdapter= new Adapter_Admin_Meldingen(Admin_Meldingen.this, bestaandDataArrayList);
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
    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(Admin_Meldingen.this, Details.class);
        intent.putExtra("id", bestaandDataIdArrayList.get(position));
        startActivity(intent);
    }

}

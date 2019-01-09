package org.ap.edu.reportingapp.activities.user;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.ap.edu.reportingapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.lang.Integer.parseInt;

/**
 * Created by Maarten on 9/01/2019.
 */

public class Mededeling_Details extends Activity {

    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @BindView(R.id.txtViewUitgevoerdIngevuld) TextView txtViewUitgevoerdIngevuld;
    @BindView(R.id.txtViewUitgevoerdPersoonIngevuld) TextView txtViewUitgevoerdPersoonIngevuld;
    @BindView(R.id.txtViewExtraOpmerkingIngevuld) TextView txtViewExtraOpmerkingIngevuld;
    @BindView(R.id.txtViewDateReparatieIngevuld) TextView txtViewDateReparatieIngevuld;
    @BindView(R.id.txtViewVerlopen) TextView txtViewVerlopen;
    @BindView(R.id.btnTerug) Button btnTerug;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mededeling_details);
        //final String id = getIntent().getExtras().getString("id","Leeg");
        ButterKnife.bind(this);
        getDetails();
    }

    private void getDetails() {
        final String id = getIntent().getExtras().getString("id","Leeg");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    if (String.valueOf(postSnapshot.child("uitgevoerdDoor").getValue()) != null
                            && postSnapshot.child("mededelingText").getValue() != null
                            && String.valueOf(postSnapshot.child("schadeId").getValue()).equals(id)) {
                        String uitgevoerdDoor = postSnapshot.child("uitgevoerdDoor").getValue().toString();
                        String uitgevoerdDoorPersoon = postSnapshot.child("uitgevoerdDoorNaam").getValue().toString();
                        String mededelingText = postSnapshot.child("mededelingText").getValue().toString();
                        Long dateRepaired = Long.parseLong(postSnapshot.child("dateRepaired").getValue().toString());

                        Date d = new Date(dateRepaired);
                        Date now = new Date();
                        if (d.before(now)){
                            txtViewVerlopen.setText("Is gerepareerd");
                        }
                        else {
                            txtViewVerlopen.setText("Nog niet gerepareerd");
                        }
                        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        String dateRepairedFormatted = formatter.format(d);

                        txtViewDateReparatieIngevuld.setText(dateRepairedFormatted);
                        txtViewUitgevoerdIngevuld.setText(uitgevoerdDoor);
                        txtViewUitgevoerdPersoonIngevuld.setText(uitgevoerdDoorPersoon);
                        txtViewExtraOpmerkingIngevuld.setText(mededelingText);
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



    @OnClick(R.id.btnTerug)
    public void submit() {
        finish();
    }
}

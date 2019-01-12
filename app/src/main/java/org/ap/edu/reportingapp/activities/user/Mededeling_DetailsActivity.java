package org.ap.edu.reportingapp.activities.user;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.ap.edu.reportingapp.R;
import org.ap.edu.reportingapp.models.Mededeling;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Mededeling_DetailsActivity extends Activity {

    DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
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
                        Mededeling mededeling = postSnapshot.getValue(Mededeling.class);

                        Date now = new Date();
                        Date d = new Date(mededeling.getDateRepaired());
                        if (d.before(now)){
                            txtViewVerlopen.setText("Is gerepareerd");
                        }
                        else {
                            txtViewVerlopen.setText("Nog niet gerepareerd");
                        }

                        String dateRepairedFormatted = dateFormatter.format(d);

                        txtViewDateReparatieIngevuld.setText(dateRepairedFormatted);
                        txtViewUitgevoerdIngevuld.setText(mededeling.getUitgevoerdDoor());
                        txtViewUitgevoerdPersoonIngevuld.setText(mededeling.getUitgevoerdDoorNaam());
                        txtViewExtraOpmerkingIngevuld.setText(mededeling.getMededelingText());
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

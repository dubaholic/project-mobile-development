package org.ap.edu.reportingapp.activities.user;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.ap.edu.reportingapp.R;
import org.ap.edu.reportingapp.models.Schade;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailsActivity extends Activity {
    private String[] urgenties;
    final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @BindView(R.id.txtViewApMailIngevuld) TextView txtViewApMailIngevuld;
    @BindView(R.id.txtViewVerdiepingIngevuld) TextView txtViewVerdiepingIngevuld;
    @BindView(R.id.txtViewLokaalIngevuld) TextView txtViewLokaalIngevuld;
    @BindView(R.id.txtViewCategorieIngevuld) TextView txtViewCategorieIngevuld;
    @BindView(R.id.txtViewOpmerkingIngevuld) TextView txtViewOpmerkingIngevuld;
    @BindView(R.id.txtViewTimeIngevuld) TextView txtViewTimeIngevuld;
    @BindView(R.id.txtViewUrgentieValueIngevuld) TextView txtUrgentieValueIngevuld;
    @BindView(R.id.txtViewIsAfgehandeldValue) TextView txtViewIsAfgehandeldValue;
    @BindView(R.id.btnTerug) Button btnTerug;
    @BindView(R.id.btnMededelingDetails) Button btnMededelingDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        getUrgenties();
        getDetails();
    }

    @OnClick(R.id.btnTerug)
    public void submit() {
        finish();
    }

    @OnClick(R.id.btnMededelingDetails)
    public void mededelingDetails(){
        final String id = getIntent().getExtras().getString("id","Leeg");
        Intent intent = new Intent(DetailsActivity.this, Mededeling_DetailsActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    private void getUrgenties() {
        urgenties = getResources().getStringArray(R.array.urgenties);
    }

    private void getDetails() {
        final String id = getIntent().getExtras().getString("id","Leeg");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    if (String.valueOf(postSnapshot.child("verdieping").getValue()) != null
                            && postSnapshot.child("lokaal").getValue() != null
                            && String.valueOf(postSnapshot.child("schadeId").getValue()).equals(id)) {
                        Schade schade = postSnapshot.getValue(Schade.class);
                        Date schadeTimeStamp = new Date(schade.getTimeStamp());
                        @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        String schadeTimeStampFormatted = formatter.format(schadeTimeStamp);

                        txtViewApMailIngevuld.setText(schade.getEmail());
                        txtViewVerdiepingIngevuld.setText(schade.getVerdieping());
                        txtViewLokaalIngevuld.setText(schade.getLokaal());
                        txtViewCategorieIngevuld.setText(schade.getCategorie());
                        txtViewOpmerkingIngevuld.setText(schade.getOpmerking());
                        txtUrgentieValueIngevuld.setText(urgenties[schade.getUrgentie()]);
                        txtViewTimeIngevuld.setText("Gemaakt op " + schadeTimeStampFormatted);

                        Boolean isAfgehandeld = (Boolean) postSnapshot.child("afgehandeld").getValue();
                        if (isAfgehandeld) {
                            txtViewIsAfgehandeldValue.setText(R.string.afhandeling);
                            btnMededelingDetails.setVisibility(View.VISIBLE);
                        }
                        else {
                            txtViewIsAfgehandeldValue.setText(R.string.niet_afgehandeld);
                            btnMededelingDetails.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {
                //unused
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                //unused
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {
                //unused
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //unused
            }
        });

    }
}

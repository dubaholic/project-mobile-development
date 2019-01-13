package org.ap.edu.reportingapp.activities.admin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.ap.edu.reportingapp.R;
import org.ap.edu.reportingapp.activities.user.DetailsActivity;
import org.ap.edu.reportingapp.models.Mededeling;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Admin_Melding_Afhandeling extends Activity {
    private Date dateRepaired;
    private long dateRepairedLong;
    private Mededeling mededelingObject;

    private String dateText, mededeling, uitvoerenDoorNaam, uitvoerenValue;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

    final Calendar newCalendar = Calendar.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference databaseReference = database.getReference();
    final DatabaseReference mededelingenReference = databaseReference.child("mededelingen");
    final DatabaseReference meldingenReference = databaseReference.child("meldingen");
    final DatabaseReference archiveringReference = databaseReference.child("archief");

    @BindView(R.id.txtViewReparatiedatum) TextView txtViewReparatiedatum;
    @BindView(R.id.txtMededeling) TextView txtMedeling;
    @BindView(R.id.txtUitvoerenDoor) TextView txtUitvoerenDoor;
    @BindView(R.id.btnVerzendenMededeling) Button btnVerzendenMededeling;
    @BindView(R.id.btnInfoMelding) Button btnInfoMelding;
    @BindView(R.id.btnDatumKiezen) Button btnDatumKiezen;
    @BindView(R.id.cmbUitvoeren) Spinner cmbUitvoeren;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_afhandelen);
        ButterKnife.bind(this);

        final String id = getIntent().getExtras().getString("id", "Leeg");

        String[] uitvoerenDoor = getResources().getStringArray(R.array.uitvoering);
        ArrayAdapter<String> adapterUitvoerenDoor = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, uitvoerenDoor);

        cmbUitvoeren.setAdapter(adapterUitvoerenDoor);
        final DatePickerDialog  StartTime = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar chosenDate = Calendar.getInstance();
                chosenDate.set(year, monthOfYear, dayOfMonth);
                if (newCalendar.before(chosenDate)) {
                    dateRepaired = chosenDate.getTime();
                    dateText = dateFormatter.format(dateRepaired);
                }
                else {
                    dateText = "";
                    Toast.makeText(getApplicationContext(), "Kies een geldige datum", Toast.LENGTH_SHORT).show();
                    dateRepaired = null;
                }
                txtViewReparatiedatum.setText(dateText);
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        btnDatumKiezen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartTime.show();
            }
        });

        btnInfoMelding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Admin_Melding_Afhandeling.this, DetailsActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        btnVerzendenMededeling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uitvoerenDoorNaam = txtUitvoerenDoor.getText().toString();
                uitvoerenValue = cmbUitvoeren.getSelectedItem().toString();
                if (uitvoerenDoorNaam.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Vul de naam in wie dit moet repareren",Toast.LENGTH_SHORT).show();
                }
                else if (uitvoerenValue.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Kies door wie de schade gerepareerd moet worden",Toast.LENGTH_SHORT).show();
                }
                else if (id.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Fout met de id",Toast.LENGTH_SHORT).show();
                }
                else if (dateRepaired == null){
                    Toast.makeText(getApplicationContext(),"Kies de datum van reparatie",Toast.LENGTH_SHORT).show();
                }
                else {
                    mededeling = txtMedeling.getText().toString();
                    if (mededeling.isEmpty()){mededeling = "Geen mededeling";}
                    dateRepairedLong = dateRepaired.getTime();
                    mededelingObject = new Mededeling(id, uitvoerenDoorNaam, dateRepairedLong, mededeling, uitvoerenValue);

                    startActivity(new Intent(Admin_Melding_Afhandeling.this, Admin_Meldingen.class));
                    mededelingenReference.child(id).setValue(mededelingObject);
                    meldingenReference.child(id).child("afgehandeld").setValue(true);
                    moveMelding(meldingenReference.child(id), archiveringReference.child(id));
                    meldingenReference.child(id).removeValue();
                    Toast.makeText(getApplicationContext(), "Item verzonden!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @OnClick(R.id.btnTerug)
    public void submit() {
        finish();
    }

    private void moveMelding(final DatabaseReference fromPath, final DatabaseReference toPath) {
        fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError firebaseError, DatabaseReference firebase) {
                        //unused
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //unused
            }
        });
    }

}

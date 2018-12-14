package org.ap.edu.reportingapp.activities.admin;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.ap.edu.reportingapp.R;
import org.ap.edu.reportingapp.activities.user.Details;
import org.ap.edu.reportingapp.activities.user.Listing;
import org.ap.edu.reportingapp.activities.user.Start;
import org.ap.edu.reportingapp.models.Mededeling;
import org.ap.edu.reportingapp.models.Schade;
import org.ap.edu.reportingapp.models.Scoren;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Maarten on 12/12/2018.
 */

public class Admin_Melding_Afhandeling extends Activity {
    private Button btnInfoMelding, btnDatumKiezen, btnVerzendenMededeling;
    private ArrayAdapter<String> adapterUitvoerenDoor;
    private Spinner cmbUitvoeren;
    private DatePicker datePicker;
    private TextView txtViewReparatiedatum, txtMededeling, txtUitvoerenDoor;
    private String[] uitvoerenDoor;
    private Date dateRepaired;
    private Mededeling mededelingObject;


    private String dateText, mededeling, uitvoerenDoorNaam, uitvoerenValue;
    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

    final Calendar newCalendar = Calendar.getInstance();

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_afhandelen);

        final DatabaseReference databaseReference = database.getReference();
        final DatabaseReference mededelingenReference = databaseReference.child("mededelingen");
        final DatabaseReference medlingenReference = databaseReference.child("meldingen");

        final String id = getIntent().getExtras().getString("id", "Leeg");

        uitvoerenDoor = getResources().getStringArray(R.array.uitvoering);

        btnInfoMelding = findViewById(R.id.btnInfoMelding);
        btnDatumKiezen = findViewById(R.id.btnDatumKiezen);
        btnVerzendenMededeling = findViewById(R.id.btnVerzendenMededeling);
        cmbUitvoeren = findViewById(R.id.cmbUitvoeren);
        txtUitvoerenDoor = findViewById(R.id.txtUitvoerenDoor);
        txtMededeling = findViewById(R.id.txtMededeling);

        adapterUitvoerenDoor = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, uitvoerenDoor);

        cmbUitvoeren.setAdapter(adapterUitvoerenDoor);
        final DatePickerDialog  StartTime = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar chosenDate = Calendar.getInstance();
                chosenDate.set(year, monthOfYear, dayOfMonth);
                if (newCalendar.before(chosenDate)) {
                    dateText = dateFormatter.format(chosenDate.getTime());
                    dateRepaired = chosenDate.getTime();
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

        txtViewReparatiedatum = findViewById(R.id.txtViewReparatiedatum);
        btnInfoMelding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Admin_Melding_Afhandeling.this, Details.class);
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
                    mededeling = txtMededeling.getText().toString();
                    if (mededeling.isEmpty()){mededeling = "Geen mededeling";}

                    mededelingObject = new Mededeling(id, uitvoerenDoorNaam, dateRepaired, mededeling, uitvoerenValue);
                    Toast.makeText(getApplicationContext(), "Item verzonden!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Admin_Melding_Afhandeling.this, Admin_Meldingen.class));

                    mededelingenReference.child(id).setValue(mededelingObject);
                    medlingenReference.child(id).child("afgehandeld").setValue(true);
                }
            }
        });
    }
}

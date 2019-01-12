package org.ap.edu.reportingapp.activities.admin;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.ap.edu.reportingapp.R;
import org.ap.edu.reportingapp.activities.user.ListingActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthenticationActivity extends Activity {
    private String apMailAuth, inputAuth, adminPassword, adminEmail;
    private EditText  input;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference databaseReference = database.getReference();

    @BindView(R.id.txtViewAdminPass) TextView txtViewAdminPass;
    @BindView(R.id.txtApMailAuth) EditText txtApMailAuth;
    @BindView(R.id.btnInvoeren) Button btnInvoeren;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        ButterKnife.bind(this);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adminPassword = dataSnapshot.child("admin/admin/password").getValue().toString();
                adminEmail = dataSnapshot.child("admin/admin/adminEmail").getValue().toString();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        btnInvoeren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(AuthenticationActivity.this, ListingActivity.class);
                apMailAuth = txtApMailAuth.getText().toString();
                if (apMailAuth.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Vul je AP email adres in",Toast.LENGTH_SHORT).show();
                }
                else if (!apMailAuth.endsWith("@ap.be") && !apMailAuth.endsWith("@student.ap.be")){
                    Toast.makeText(getApplicationContext(),"Geef een geldig AP email adres op",Toast.LENGTH_SHORT).show();
                }
                else if (apMailAuth.equals(adminEmail)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(AuthenticationActivity.this);
                    builder.setTitle("Admin paswoord");
                    input = new EditText(AuthenticationActivity.this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    builder.setView(input);

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            inputAuth = input.getText().toString();
                            if (inputAuth.equals(adminPassword)){
                                intent.putExtra("isAdmin", true);
                                intent.putExtra("apMailAuth", apMailAuth);
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Ingelogd als admin", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Foute code", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    builder.setNegativeButton("Annuleren", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
                else {
                    intent.putExtra("isAdmin", false);
                    intent.putExtra("apMailAuth", apMailAuth);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "Ingelogd met " + apMailAuth, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

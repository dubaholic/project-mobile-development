package org.ap.edu.reportingapp;

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

import java.util.Date;
import java.util.UUID;

/**
 * Created by Maarten on 5/12/2018.
 */

public class Authentication extends Activity {
    private String apMailAuth, inputAuth;

    private Button btnInvoeren;
    private EditText txtApMailAuth;
    private TextView adminPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        txtApMailAuth = findViewById(R.id.txtApMailAuth);
        btnInvoeren = findViewById(R.id.btnInvoeren);
        adminPass = findViewById(R.id.adminPass);

        btnInvoeren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apMailAuth = txtApMailAuth.getText().toString();
                if (apMailAuth.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Vul je AP email adres in",Toast.LENGTH_SHORT).show();
                }
                else if (!apMailAuth.endsWith("@ap.be") && !apMailAuth.endsWith("@student.ap.be")){
                    Toast.makeText(getApplicationContext(),"Geef een geldig AP email adres op",Toast.LENGTH_SHORT).show();
                }
                else {
                    final Intent intent = new Intent(Authentication.this, Listing.class);

                    if (apMailAuth.equals("admin@ap.be")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(Authentication.this);
                        builder.setTitle("Admin paswoord");
                        final EditText input = new EditText(Authentication.this);
                        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        builder.setView(input);

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                intent.putExtra("apMailAuth", apMailAuth);
                                //inputAuth is het paswoord
                                inputAuth = input.getText().toString();
                                startActivity(intent);
                                Toast.makeText(getApplicationContext(), "Ingelogd met " + apMailAuth, Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        builder.show();
                    }
                    else {
                        intent.putExtra("apMailAuth", apMailAuth);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Ingelogd met " + apMailAuth, Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }
}

package com.example.careify2;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class Patient extends AppCompatActivity {

    private static final String KEY_NAME = "Name";
    private static final String KEY_AGE = "Alter";
    private static final String KEY_ROOM = "Raum";
    private static final String KEY_DIAGNOSIS = "Diagnose";
    private static final String KEY_MEDICATION = "Medikation";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        setSupportActionBar(findViewById(R.id.toolbarPatient));

        String PatientName = getIntent().getStringExtra("PatientName");             //Facility und Category muss auch übergeben werden

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        getSupportActionBar().setTitle(PatientName);

        loadPatientOnCreate(PatientName);

    }

    public void loadPatientOnCreate(String patientName){                                                                //Facility und Category muss Variabel sein
        DocumentReference documentRef = db.collection("Facility").document("Paulinenstift")
                .collection("Category").document("1OG")
                .collection("Patient").document(patientName);
        documentRef.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists()){

                                    EditText editName = findViewById(R.id.patientName);
                                    EditText editAge = findViewById(R.id.patientAge);
                                    EditText editRoom = findViewById(R.id.patientRoom);
                                    EditText editDiagnosis = findViewById(R.id.patientDiagnosis);
                                    EditText editMedication = findViewById(R.id.patientMedication);

                                    String name = documentSnapshot.getString(KEY_NAME);
                                    String age = documentSnapshot.getString(KEY_AGE);
                                    String room = documentSnapshot.getString(KEY_ROOM);
                                    String diagnosis = documentSnapshot.getString(KEY_DIAGNOSIS);
                                    String medication = documentSnapshot.getString(KEY_MEDICATION);

                                    editName.setText(name, TextView.BufferType.EDITABLE);
                                    editAge.setText(age, TextView.BufferType.EDITABLE);
                                    editRoom.setText(room, TextView.BufferType.EDITABLE);
                                    editDiagnosis.setText(diagnosis, TextView.BufferType.EDITABLE);
                                    editMedication.setText(medication, TextView.BufferType.EDITABLE);

                                } else {
                                    Toast.makeText(Patient.this, "Patient doesn't exist!", Toast.LENGTH_SHORT).show();

                                }
                                Toast.makeText(Patient.this, "Patient loaded!", Toast.LENGTH_SHORT).show();
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Patient.this, "Failure!", Toast.LENGTH_SHORT).show();
                                    }
                                });
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.app_bar_edit) {
            Snackbar.make(findViewById(android.R.id.content), R.string.to_be_implemented, Snackbar.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.app_bar_delete) {
            Snackbar.make(findViewById(android.R.id.content), R.string.to_be_implemented, Snackbar.LENGTH_SHORT).show();
            return true;
        }

        if (id == R.id.app_bar_settings) {
            Snackbar.make(findViewById(android.R.id.content), R.string.to_be_implemented, Snackbar.LENGTH_SHORT).show();
            return true;
        }

        if (id == android.R.id.home) {
            startActivity(new Intent(Patient.this, AllPatients.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
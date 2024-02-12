package com.example.careify2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;



public class AddPatient extends AppCompatActivity {

    private static final String KEY_NAME = "Name";
    private static final String KEY_AGE = "Alter";
    private static final String KEY_ROOM = "Raum";
    private static final String KEY_DIAGNOSIS = "Diagnose";
    private static final String KEY_MEDICATION = "Medikation";
    private String CategoryName;
    private String FacilityName;
    private EditText editTextName;
    private EditText editTextAge;
    private EditText editTextRoom;
    private EditText editTextDiagnosis;
    private EditText editTextMedication;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);
        setSupportActionBar(findViewById(R.id.toolbarAddPatient));

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        getSupportActionBar().setTitle(R.string.add_patient);

        CategoryName = getIntent().getStringExtra("CategoryName");
        FacilityName = getIntent().getStringExtra("FacilityName");

        editTextName = findViewById(R.id.addPatientName);
        editTextAge = findViewById(R.id.addPatientAge);
        editTextRoom = findViewById(R.id.addPatientRoom);
        editTextDiagnosis = findViewById(R.id.addPatientDiagnosis);
        editTextMedication = findViewById(R.id.addPatientMedication);
    }

    public void savePatient (View v){
        String name = editTextName.getText().toString();
        String age = editTextAge.getText().toString();
        String room = editTextRoom.getText().toString();
        String diagnosis = editTextDiagnosis.getText().toString();
        String medication = editTextMedication.getText().toString();

        if(name.equals("")) {
            Toast.makeText(this, "Please enter the Patient's name!", Toast.LENGTH_SHORT).show();
        } else {
        Map<String, Object> patient = new HashMap<>();
        patient.put(KEY_NAME, name);
        patient.put(KEY_AGE, age);
        patient.put(KEY_ROOM, room);
        patient.put(KEY_DIAGNOSIS, diagnosis);
        patient.put(KEY_MEDICATION, medication);

        db.collection("Facility").document(FacilityName)
                .collection("Category").document(CategoryName)
                .collection("Patient").document(name).set(patient)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(AddPatient.this, "Success!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddPatient.this, AllPatients.class);
                        intent.putExtra("CategoryName", CategoryName);
                        intent.putExtra("FacilityName", FacilityName);
                        startActivity(intent);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddPatient.this, "Failure!", Toast.LENGTH_SHORT).show();
                    }
                });
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            Intent intent = new Intent(AddPatient.this, AllPatients.class);
            intent.putExtra("CategoryName", CategoryName);
            intent.putExtra("FacilityName", FacilityName);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
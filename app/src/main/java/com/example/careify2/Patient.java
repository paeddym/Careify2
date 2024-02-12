package com.example.careify2;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Patient extends AppCompatActivity {

    ImageView imageView;

    private static final String KEY_NAME = "Name";
    private static final String KEY_AGE = "Alter";
    private static final String KEY_ROOM = "Raum";
    private static final String KEY_DIAGNOSIS = "Diagnose";
    private static final String KEY_MEDICATION = "Medikation";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean inEditMode = false;
    private String PatientName;
    private String CategoryName;
    private String FacilityName;

    private FloatingActionButton fabEdit;
    private FloatingActionButton fabSave;
    private EditText editTextName;
    private EditText editTextAge;
    private EditText editTextRoom;
    private EditText editTextDiagnosis;
    private EditText editTextMedication;

    private TextView currentName;
    private TextView currentAge;
    private TextView currentRoom;
    private TextView currentDiagnosis;
    private TextView currentMedication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        setSupportActionBar(findViewById(R.id.toolbarPatient));

        fabEdit = findViewById(R.id.floatingActionButtonEdit);
        fabSave = findViewById(R.id.floatingActionButtonSave);

        currentName = findViewById(R.id.patientName);
        currentAge = findViewById(R.id.patientAge);
        currentRoom = findViewById(R.id.patientRoom);
        currentDiagnosis = findViewById(R.id.patientDiagnosis);
        currentMedication = findViewById(R.id.patientMedication);

        editTextName = findViewById(R.id.patientNameEdit);
        editTextAge = findViewById(R.id.patientAgeEdit);
        editTextRoom = findViewById(R.id.patientRoomEdit);
        editTextDiagnosis = findViewById(R.id.patientDiagnosisEdit);
        editTextMedication = findViewById(R.id.patientMedicationEdit);

        PatientName = getIntent().getStringExtra("PatientName");
        CategoryName = getIntent().getStringExtra("CategoryName");
        FacilityName = getIntent().getStringExtra("FacilityName");
        imageView=findViewById(R.id.imgPatient);
        imageView.setImageResource(R.drawable.passant);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        getSupportActionBar().setTitle(PatientName);

        loadPatientOnCreate();

    }

    public void loadPatientOnCreate(){
        DocumentReference documentRef = db.collection("Facility").document(FacilityName)
                .collection("Category").document(CategoryName)
                .collection("Patient").document(PatientName);
        documentRef.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists()){

                                    String name = documentSnapshot.getString(KEY_NAME);
                                    String age = documentSnapshot.getString(KEY_AGE);
                                    String room = documentSnapshot.getString(KEY_ROOM);
                                    String diagnosis = documentSnapshot.getString(KEY_DIAGNOSIS);
                                    String medication = documentSnapshot.getString(KEY_MEDICATION);

                                    currentName.setText(name);
                                    currentAge.setText(age);
                                    currentRoom.setText(room);
                                    currentDiagnosis.setText(diagnosis);
                                    currentMedication.setText(medication);

                                    editTextName.setText(name, TextView.BufferType.EDITABLE);
                                    editTextAge.setText(age, TextView.BufferType.EDITABLE);
                                    editTextRoom.setText(room, TextView.BufferType.EDITABLE);
                                    editTextDiagnosis.setText(diagnosis, TextView.BufferType.EDITABLE);
                                    editTextMedication.setText(medication, TextView.BufferType.EDITABLE);

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

    public void toggleEdit(View view) {
        if(!inEditMode) {
            fabEdit.setVisibility(View.GONE);
            fabSave.setVisibility(View.VISIBLE);
            currentName.setVisibility(View.GONE);
            currentAge.setVisibility(View.GONE);
            currentRoom.setVisibility(View.GONE);
            currentDiagnosis.setVisibility(View.GONE);
            currentMedication.setVisibility(View.GONE);
            editTextName.setVisibility(View.VISIBLE);
            editTextAge.setVisibility(View.VISIBLE);
            editTextRoom.setVisibility(View.VISIBLE);
            editTextDiagnosis.setVisibility(View.VISIBLE);
            editTextMedication.setVisibility(View.VISIBLE);
        } else {
            fabEdit.setVisibility(View.VISIBLE);
            fabSave.setVisibility(View.GONE);
            currentName.setVisibility(View.VISIBLE);
            currentAge.setVisibility(View.VISIBLE);
            currentRoom.setVisibility(View.VISIBLE);
            currentDiagnosis.setVisibility(View.VISIBLE);
            currentMedication.setVisibility(View.VISIBLE);
            editTextName.setVisibility(View.GONE);
            editTextAge.setVisibility(View.GONE);
            editTextRoom.setVisibility(View.GONE);
            editTextDiagnosis.setVisibility(View.GONE);
            editTextMedication.setVisibility(View.GONE);
            saveChanges();
        }
        inEditMode = !inEditMode;
    }

    public void saveChanges(){
        String newName = editTextName.getText().toString();
        String newAge = editTextAge.getText().toString();
        String newRoom = editTextRoom.getText().toString();
        String newDiagnosis = editTextDiagnosis.getText().toString();
        String newMedication = editTextMedication.getText().toString();

        if(newName.equals("")) {
            Toast.makeText(this, "Please enter the Patient's name!", Toast.LENGTH_SHORT).show();
        } else {
            Map<String, Object> patient = new HashMap<>();
            patient.put(KEY_NAME, newName);
            patient.put(KEY_AGE, newAge);
            patient.put(KEY_ROOM, newRoom);
            patient.put(KEY_DIAGNOSIS, newDiagnosis);
            patient.put(KEY_MEDICATION, newMedication);

            db.collection("Facility").document(FacilityName)
                    .collection("Category").document(CategoryName)
                    .collection("Patient").document(PatientName).delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Patient.this, "Old Patient removed!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

            db.collection("Facility").document(FacilityName)
                    .collection("Category").document(CategoryName)
                    .collection("Patient").document(newName).set(patient)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Patient.this, "Success!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Patient.this, "Failure!", Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(Patient.this, AllPatients.class);
            intent.putExtra("CategoryName", CategoryName);
            intent.putExtra("FacilityName", FacilityName);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
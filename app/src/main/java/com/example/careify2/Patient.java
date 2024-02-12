package com.example.careify2;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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
    private String databaseName;
    private String databaseAge;
    private String databaseRoom;
    private String databaseDiagnosis;
    private String databaseMedication;

    private String PatientName;
    private String CategoryName;
    private String FacilityName;

    private FloatingActionButton fabEdit;
    private FloatingActionButton fabSave;
    private boolean inEditMode = false;

    private TextView currentName;
    private TextView currentAge;
    private TextView currentRoom;
    private TextView currentDiagnosis;
    private TextView currentMedication;

    private EditText editTextName;
    private EditText editTextAge;
    private EditText editTextRoom;
    private EditText editTextDiagnosis;
    private EditText editTextMedication;

    final FirebaseFirestore db = FirebaseFirestore.getInstance();
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

        imageView=findViewById(R.id.imgPatient);
        imageView.setImageResource(R.drawable.passant);

        PatientName = getIntent().getStringExtra("PatientName");
        CategoryName = getIntent().getStringExtra("CategoryName");
        FacilityName = getIntent().getStringExtra("FacilityName");

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        getSupportActionBar().setTitle(PatientName);

        loadPatientOnCreate();

    }

    public void loadPatientOnCreate(){
        db.collection("Facility").document(FacilityName)
                .collection("Category").document(CategoryName)
                .collection("Patient").document(PatientName).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists()){

                                    databaseName = documentSnapshot.getString(KEY_NAME);
                                    databaseAge = documentSnapshot.getString(KEY_AGE);
                                    databaseRoom = documentSnapshot.getString(KEY_ROOM);
                                    databaseDiagnosis = documentSnapshot.getString(KEY_DIAGNOSIS);
                                    databaseMedication = documentSnapshot.getString(KEY_MEDICATION);

                                    currentName.setText(databaseName);
                                    currentAge.setText(databaseAge);
                                    currentRoom.setText(databaseRoom);
                                    currentDiagnosis.setText(databaseDiagnosis);
                                    currentMedication.setText(databaseMedication);

                                    editTextName.setText(databaseName, TextView.BufferType.EDITABLE);
                                    editTextAge.setText(databaseAge, TextView.BufferType.EDITABLE);
                                    editTextRoom.setText(databaseRoom, TextView.BufferType.EDITABLE);
                                    editTextDiagnosis.setText(databaseDiagnosis, TextView.BufferType.EDITABLE);
                                    editTextMedication.setText(databaseMedication, TextView.BufferType.EDITABLE);

                                } else {
                                    Toast.makeText(Patient.this, R.string.patientDoesntExist, Toast.LENGTH_SHORT).show();

                                }
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Patient.this, R.string.noConnection, Toast.LENGTH_SHORT).show();
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
            saveChanges();
        }
        inEditMode = !inEditMode;
    }

    public void saveChanges(){
        new AlertDialog.Builder(this)
                .setTitle(R.string.edit)
                .setMessage(R.string.editConfirm)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        String newName = editTextName.getText().toString();
                        String newAge = editTextAge.getText().toString();
                        String newRoom = editTextRoom.getText().toString();
                        String newDiagnosis = editTextDiagnosis.getText().toString();
                        String newMedication = editTextMedication.getText().toString();

                        if(newName.equals("")) {
                            Toast.makeText(Patient.this, "Please enter the Patient's name!", Toast.LENGTH_SHORT).show();
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
                            Intent intent = new Intent(Patient.this, AllPatients.class);
                            intent.putExtra("CategoryName", CategoryName);
                            intent.putExtra("FacilityName", FacilityName);
                            startActivity(intent);
                        }
                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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

                        currentName.setText(editTextName.getText().toString());
                        currentAge.setText(editTextAge.getText().toString());
                        currentRoom.setText(editTextRoom.getText().toString());
                        currentDiagnosis.setText(editTextDiagnosis.getText().toString());
                        currentMedication.setText(editTextMedication.getText().toString());
                    }
                }).show();

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
            if(inEditMode   || !databaseName.equals(editTextName.getText().toString())                      //if any change has been made, confirm discard
                            || !databaseAge.equals(editTextAge.getText().toString())
                            || !databaseRoom.equals(editTextRoom.getText().toString())
                            || !databaseDiagnosis.equals(editTextDiagnosis.getText().toString())
                            || !databaseMedication.equals(editTextMedication.getText().toString())) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.discard)
                        .setMessage(R.string.discardConfirm)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                returnToCategory();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            } else {
                returnToCategory();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void returnToCategory(){
        Intent intent = new Intent(Patient.this, AllPatients.class);
        intent.putExtra("CategoryName", CategoryName);
        intent.putExtra("FacilityName", FacilityName);
        startActivity(intent);
    }
}

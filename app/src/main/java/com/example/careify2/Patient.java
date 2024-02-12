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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
    private boolean inEditMode = false;
    private String PatientName;
    private String CategoryName;
    private String FacilityName;

    private View fabEdit;
    private View fabSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        setSupportActionBar(findViewById(R.id.toolbarPatient));

        PatientName = getIntent().getStringExtra("PatientName");
        CategoryName = getIntent().getStringExtra("CategoryName");
        FacilityName = getIntent().getStringExtra("FacilityName");

        fabEdit = findViewById(R.id.floatingActionButtonEdit);
        fabSave = findViewById(R.id.floatingActionButtonSave);

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

                                    TextView editName = findViewById(R.id.patientName);
                                    TextView  editAge = findViewById(R.id.patientAge);
                                    TextView  editRoom = findViewById(R.id.patientRoom);
                                    TextView  editDiagnosis = findViewById(R.id.patientDiagnosis);
                                    TextView  editMedication = findViewById(R.id.patientMedication);

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

    public void toggleEdit(){
        if(inEditMode){
            fabEdit.setVisibility(View.GONE);
            fabSave.setVisibility(View.VISIBLE);
            inEditMode = !inEditMode;
        } else {
            fabEdit.setVisibility(View.VISIBLE);
            fabSave.setVisibility(View.GONE);
            inEditMode = !inEditMode;
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
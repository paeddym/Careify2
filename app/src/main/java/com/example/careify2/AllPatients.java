package com.example.careify2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AllPatients extends AppCompatActivity implements RecyclerViewInterface{

    ArrayList<PatientModel> patientModels = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String KEY_NAME = "Name";
    private String CategoryName;
    private CollectionReference collectionReference;

    private String[] allPatients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_patients);
        setSupportActionBar(findViewById(R.id.toolbarAllPatients));

        CategoryName = getIntent().getStringExtra("CategoryName");
        collectionReference = db.collection("Facility").document("Paulinenstift")
                .collection("Category").document(CategoryName).collection("Patient");
        loadPatientNames(collectionReference);

        FloatingActionButton fab;
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButtonAllPatients);
        fab.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(AllPatients.this, AddPatient.class);
                intent.putExtra("CategoryName", CategoryName);
                startActivity(intent);
            }
        });

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        getSupportActionBar().setTitle(CategoryName);
    }

    private void loadPatientNames(CollectionReference collectionReference){
        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        int i = 0;
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            i++;
                        }
                        allPatients = new String[i];
                        i = 0;
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            allPatients[i] = documentSnapshot.getString(KEY_NAME);
                            i++;
                        }
                        Toast.makeText(AllPatients.this, "Loaded Patients!", Toast.LENGTH_SHORT).show();
                        setPatientModels(allPatients);
                        RecyclerView recyclerView = findViewById(R.id.Patients);
                        Patient_RecyclerViewAdapter adapter = new Patient_RecyclerViewAdapter(AllPatients.this, patientModels, AllPatients.this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(AllPatients.this));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AllPatients.this, "Failed to load Patients!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setPatientModels(String[] patients){

        for (int i=0; i<patients.length; i++){
            patientModels.add(new PatientModel(patients[i]));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.app_bar_search) {
            Snackbar.make(findViewById(android.R.id.content), R.string.to_be_implemented, Snackbar.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.app_bar_settings) {
            Snackbar.make(findViewById(android.R.id.content), R.string.to_be_implemented, Snackbar.LENGTH_SHORT).show();
            return true;
        }

        if (id == android.R.id.home) {
            startActivity(new Intent(AllPatients.this, Category.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCardClick(int position) {
        Intent intent = new Intent(AllPatients.this, Patient.class);
        intent.putExtra("PatientName", patientModels.get(position).PatientName);
        intent.putExtra("CategoryName", CategoryName);
        startActivity(intent);
    }
}
package com.example.careify2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class AllPatients extends AppCompatActivity implements RecyclerViewInterface{

    ArrayList<PatientModel> patientModels = new ArrayList<>();
    final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String KEY_NAME = "Name";
    private String CategoryName;
    private String FacilityName;

    Patient_RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_patients);
        setSupportActionBar(findViewById(R.id.toolbarAllPatients));

        CategoryName = getIntent().getStringExtra("CategoryName");
        FacilityName = getIntent().getStringExtra("FacilityName");

        loadPatientNames();

        FloatingActionButton fab;
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButtonAllPatients);
        fab.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(AllPatients.this, AddPatient.class);
                intent.putExtra("CategoryName", CategoryName);
                intent.putExtra("FacilityName", FacilityName);
                startActivity(intent);
            }
        });

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        getSupportActionBar().setTitle(CategoryName);
    }

    private void loadPatientNames(){
        db.collection("Facility").document(FacilityName)
                .collection("Category").document(CategoryName)
                .collection("Patient").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        List<String> patientList = new ArrayList<>();
                        String[] allPatients;

                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            patientList.add(documentSnapshot.getString(KEY_NAME));
                        }

                        allPatients = new String[patientList.size()];
                        patientList.toArray(allPatients);

                        for (int i=0; i<allPatients.length; i++){
                            patientModels.add(new PatientModel(allPatients[i]));
                        }

                        RecyclerView recyclerView = findViewById(R.id.Patients);
                        adapter = new Patient_RecyclerViewAdapter(AllPatients.this, patientModels, AllPatients.this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(AllPatients.this));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AllPatients.this, "Failed to reach Database!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Type here");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void filter(String text) {
        ArrayList<PatientModel> filteredList = new ArrayList<>();

        for(PatientModel model : patientModels){
            if(model.PatientName.toLowerCase().contains(text.toLowerCase())){
                filteredList.add(model);
            }
        }
        adapter.filteredList(filteredList);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.app_bar_search) {
            return true;
        }

        if (id == android.R.id.home) {
            Intent intent = new Intent(AllPatients.this, Category.class);
            intent.putExtra("FacilityName", FacilityName);
            intent.putExtra("CategoryName", CategoryName);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCardClick(int position) {
        Intent intent = new Intent(AllPatients.this, Patient.class);
        intent.putExtra("PatientName", patientModels.get(position).PatientName);
        intent.putExtra("CategoryName", CategoryName);
        intent.putExtra("FacilityName", FacilityName);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(int position) {

        new AlertDialog.Builder(this)
                .setTitle(R.string.delete)
                .setMessage(R.string.deletePatientConfirm)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        String itemToDelete = patientModels.get(position).PatientName;
                        db.collection("Facility").document(FacilityName)
                                .collection("Category").document(CategoryName)
                                .collection("Patient").document(itemToDelete).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        patientModels.remove(position);
                                        adapter.notifyItemRemoved(position);
                                        Toast.makeText(AllPatients.this, "Patient removed!", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                    }})
                .setNegativeButton(android.R.string.no, null).show();
        
    }
}
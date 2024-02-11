package com.example.careify2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class AllPatients extends AppCompatActivity implements RecyclerViewInterface{

    ArrayList<PatientModel> patientModels = new ArrayList<>();

    Patient_RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_patients);
        setSupportActionBar(findViewById(R.id.toolbarAllPatients));        //MAGIC CODE

        RecyclerView recyclerView = findViewById(R.id.Patients);

        setPatientModels();

        adapter = new Patient_RecyclerViewAdapter(this, patientModels, this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String CategoryName = getIntent().getStringExtra("CategoryName");

        FloatingActionButton fab;
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButtonAllPatients);
        fab.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                startActivity(new Intent(AllPatients.this, AddPatient.class));          //MAGIC CODE
            }
        });

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        getSupportActionBar().setTitle(CategoryName);
    }

    private void setPatientModels(){
        String[] patientNames = getResources().getStringArray(R.array.patient_names);

        for (int i=0; i<patientNames.length; i++){
            patientModels.add(new PatientModel(patientNames[i]));
        }
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
                filter(newText.toString());

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
        startActivity(intent);
    }
}
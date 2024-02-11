package com.example.careify2;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.google.android.material.snackbar.Snackbar;

public class Patient extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        setSupportActionBar(findViewById(R.id.toolbarPatient));

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        getSupportActionBar().setTitle("Patient");
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
            Snackbar.make(findViewById(android.R.id.content), "To be implemented", Snackbar.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.app_bar_delete) {
            Snackbar.make(findViewById(android.R.id.content), "To be implemented", Snackbar.LENGTH_SHORT).show();
            return true;
        }

        if (id == R.id.app_bar_settings) {
            Snackbar.make(findViewById(android.R.id.content), "To be implemented", Snackbar.LENGTH_SHORT).show();
            return true;
        }

        if (id == android.R.id.home) {
            startActivity(new Intent(Patient.this, AllPatients.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
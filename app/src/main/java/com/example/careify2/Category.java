package com.example.careify2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

public class Category extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        setSupportActionBar(findViewById(R.id.toolbarCategory));        //MAGIC CODE
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.app_bar_settings) {
            Snackbar.make(findViewById(android.R.id.content), "Action", Snackbar.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.app_bar_search) {
            Snackbar.make(findViewById(android.R.id.content), "Info", Snackbar.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
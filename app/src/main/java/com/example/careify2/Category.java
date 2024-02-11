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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class Category extends AppCompatActivity implements RecyclerViewInterface {

    ArrayList<CategoryModel> categoryModels = new ArrayList<>();
    Category_RecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        setSupportActionBar(findViewById(R.id.toolbarCategory));        //MAGIC CODE

        RecyclerView recyclerView = findViewById(R.id.Bereiche);

        setCategoryModels();

        adapter = new Category_RecyclerViewAdapter(this, categoryModels, this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab;
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButtonCategory);
        fab.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                startActivity(new Intent(Category.this, AddCategory.class));          //MAGIC CODE
            }
        });

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        getSupportActionBar().setTitle(R.string.category);
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
        ArrayList<CategoryModel> filteredList = new ArrayList<>();

        for(CategoryModel model : categoryModels){
            if(model.BereichName.toLowerCase().contains(text.toLowerCase())){
                filteredList.add(model);
            }
        }
        adapter.filteredList(filteredList);

    }

    private void setCategoryModels(){
        String[] categoryNames = getResources().getStringArray(R.array.bereich_names);

        for (int i=0; i<categoryNames.length; i++){
            categoryModels.add(new CategoryModel(categoryNames[i]));
        }
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
            startActivity(new Intent(Category.this, Login.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCardClick(int position) {
        Intent intent = new Intent(Category.this, AllPatients.class);
        intent.putExtra("CategoryName", categoryModels.get(position).getBereichName());
        startActivity(intent);
    }
}
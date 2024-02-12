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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Category extends AppCompatActivity implements RecyclerViewInterface {

    ArrayList<CategoryModel> categoryModels = new ArrayList<>();

    private static final String KEY_NAME = "Name";
    private String facilityName;
    Category_RecyclerViewAdapter adapter;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        setSupportActionBar(findViewById(R.id.toolbarCategory));

        facilityName = getIntent().getStringExtra("FacilityName");

        loadCategoryNames();

        FloatingActionButton fab;
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButtonCategory);
        fab.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(Category.this, AddCategory.class);
                intent.putExtra("FacilityName", facilityName);
                startActivity(intent);
            }
        });

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        getSupportActionBar().setTitle(R.string.category);
    }

    public void loadCategoryNames(){
        db.collection("Facility").document(facilityName)
                .collection("Category").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        List<String> categoryList = new ArrayList<>();
                        String[] allCategories;

                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            categoryList.add(documentSnapshot.getString(KEY_NAME));
                        }

                        allCategories = new String[categoryList.size()];
                        categoryList.toArray(allCategories);

                        RecyclerView recyclerView = findViewById(R.id.Bereiche);

                        for (int i=0; i<allCategories.length; i++){
                            categoryModels.add(new CategoryModel(allCategories[i]));
                        }

                        adapter = new Category_RecyclerViewAdapter(Category.this, categoryModels, Category.this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(Category.this));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Category.this, R.string.noConnection, Toast.LENGTH_SHORT).show();                    }
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
        ArrayList<CategoryModel> filteredList = new ArrayList<>();

        for(CategoryModel model : categoryModels){
            if(model.BereichName.toLowerCase().contains(text.toLowerCase())){
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

        if (id == android.R.id.home) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.logout)
                    .setMessage(R.string.logoutConfirm)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            startActivity(new Intent(Category.this, Login.class));
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCardClick(int position) {
        Intent intent = new Intent(Category.this, AllPatients.class);
        intent.putExtra("CategoryName", categoryModels.get(position).getBereichName());
        intent.putExtra("FacilityName", facilityName);
        startActivity(intent);
    }

    @Override
    public void onItemLongClick(int position) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete)
                .setMessage(R.string.deleteCategoryConfirm)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        String itemToDelete = categoryModels.get(position).getBereichName();
                        db.collection("Facility").document(facilityName)
                                .collection("Category").document(itemToDelete).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        categoryModels.remove(position);
                                        adapter.notifyItemRemoved(position);
                                        Toast.makeText(Category.this, R.string.categoryRemoved, Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Category.this, R.string.noConnection, Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

}
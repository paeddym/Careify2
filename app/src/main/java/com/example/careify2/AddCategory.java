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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.WriteResult;

import java.util.HashMap;
import java.util.Map;

public class AddCategory extends AppCompatActivity {

    private static final String KEY_NAME = "Name";
    private EditText editTextName;
    private String facilityName;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        setSupportActionBar(findViewById(R.id.toolbarAddCategory));

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        getSupportActionBar().setTitle(R.string.add_category);

        facilityName = getIntent().getStringExtra("FacilityName");

        editTextName = findViewById(R.id.enterNewCategoryName);

        findViewById(R.id.addCategoryButton).setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                String name = editTextName.getText().toString();
                if(name.equals("")) {
                    Toast.makeText(AddCategory.this, "Please enter the new category's name!", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> category = new HashMap<>();
                    category.put(KEY_NAME, name);
                

                    db.collection("Facility").document(facilityName)
                            .collection("Category").document(name).set(category)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(AddCategory.this, "Success!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(AddCategory.this, Category.class);
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddCategory.this, "Failure!", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
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

        if (id == R.id.app_bar_settings) {
            Snackbar.make(findViewById(android.R.id.content), R.string.to_be_implemented, Snackbar.LENGTH_SHORT).show();
            return true;
        }

        if (id == android.R.id.home) {
            startActivity(new Intent(AddCategory.this, Category.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
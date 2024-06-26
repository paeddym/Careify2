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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddCategory extends AppCompatActivity {

    private static final String KEY_NAME = "Name";
    private EditText editTextName;
    private String facilityName;
    final FirebaseFirestore db = FirebaseFirestore.getInstance();

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
                    Toast.makeText(AddCategory.this, R.string.enterCategory, Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> category = new HashMap<>();
                    category.put(KEY_NAME, name);
                

                    db.collection("Facility").document(facilityName)
                            .collection("Category").document(name).set(category)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Intent intent = new Intent(AddCategory.this, Category.class);
                                    intent.putExtra("FacilityName", facilityName);
                                    startActivity(intent);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddCategory.this, R.string.noConnection, Toast.LENGTH_SHORT).show();
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

        if (id == android.R.id.home) {
            Intent intent = new Intent(AddCategory.this, Category.class);
            intent.putExtra("FacilityName", facilityName);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
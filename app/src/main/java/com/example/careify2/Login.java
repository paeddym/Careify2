package com.example.careify2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    private static final String KEY_PASSWORD = "Passwort";
    private EditText editTextName;
    private EditText editTextPassword;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextName = findViewById(R.id.loginFacility);
        editTextPassword = findViewById(R.id.loginPassword);
    }

    public void superSafePasswordChecker(View v){               //Definitely very safe!
        if(editTextName.getText().toString().equals("") || editTextPassword.getText().toString().equals("")){
            Toast.makeText(this, "Please enter a facility and password", Toast.LENGTH_SHORT).show();
        } else {
            String name = editTextName.getText().toString();
            String password = editTextPassword.getText().toString();

            db.collection("Facility").document(name).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                if (password.equals(documentSnapshot.getString(KEY_PASSWORD))) {                //This line will put me in jail
                                    Intent intent = new Intent(Login.this, Category.class);
                                    intent.putExtra("FacilityName", name);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(Login.this, "Password incorrect!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Login.this, "Facility is not registered!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
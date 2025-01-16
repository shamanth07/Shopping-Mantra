package com.example.shoppingmantra.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.shoppingmantra.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {

    private EditText editEmail, editPassword;
    private Button btnSignIn, btnGuest;
    private TextView txtSignUpRedirect;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_sign_in);

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        txtSignUpRedirect = findViewById(R.id.txtSignUpRedirect);
        btnGuest = findViewById(R.id.btnGuest);

        // Initialize Firebase Auth and Database Reference
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Initialize Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing In...");
        progressDialog.setCancelable(false);

        // Sign In Button Click
        btnSignIn.setOnClickListener(view -> {
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(SignInActivity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            loginUser(email, password);
        });

        txtSignUpRedirect.setOnClickListener(view -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        // Continue as Guest Button Click
        btnGuest.setOnClickListener(view -> continueAsGuest());
    }

    private void loginUser(String email, String password) {
        progressDialog.show();
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            progressDialog.dismiss();
            if (task.isSuccessful()) {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    String userId = user.getUid();
                    checkUserRole(userId);
                }
            } else {
                Toast.makeText(SignInActivity.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserRole(String userId) {
        progressDialog.setMessage("Checking User Role...");
        progressDialog.show();

        databaseReference.child("admins").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.dismiss();
                if (snapshot.exists()) {
                    Intent intent = new Intent(SignInActivity.this, AdminActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    checkIfRegularUser(userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
                Toast.makeText(SignInActivity.this, "Error checking admin role: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkIfRegularUser(String userId) {
        databaseReference.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignInActivity.this, "User role not found!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SignInActivity.this, "Error checking user role: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void continueAsGuest() {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        intent.putExtra("isGuest", true);

        startActivity(intent);
        finish();
    }
}

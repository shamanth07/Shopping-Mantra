package com.example.shoppingcartapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText emailField, passwordField;
    private Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        auth = FirebaseAuth.getInstance();
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        signInButton = findViewById(R.id.signInButton);

        signInButton.setOnClickListener(view -> signInUser());
    }

    private void signInUser() {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(SignInActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(SignInActivity.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}

package com.hamzashami.coronaproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hamzashami.coronaproject.model.User;

class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference userRef;


    private TextInputEditText et_name, et_username, et_email, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Register");

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("users");

        et_name = findViewById(R.id.et_name);
        et_username = findViewById(R.id.et_username);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);

        findViewById(R.id.btn_register).setOnClickListener(v -> {
            checkRegister();
        });

        findViewById(R.id.tv_goToLogin).setOnClickListener(y -> {
            goToLogin();
        });
    }

    private void checkRegister() {
        String name = et_name.getText().toString().trim();
        String username = et_username.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "Enter Name!", Toast.LENGTH_SHORT).show();
            et_name.setError("Enter Name!");
            return;
        }

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getApplicationContext(), "Enter Username!", Toast.LENGTH_SHORT).show();
            et_username.setError("Enter Username!");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            et_email.setError("Enter email address!");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            et_password.setError("Enter password!");
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            et_password.setError("Password too short");
            return;
        }

        //create user
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignUpActivity.this, task -> {
                    Log.d(TAG, "checkRegister: createUserWithEmail:onComplete:" + task.isSuccessful());
                    if (!task.isSuccessful()) {
                        Toast.makeText(this, "Can't Register Now, Please try again later", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "checkRegister: Authentication failed." + task.getException());
                    } else {
                        String userUid = auth.getCurrentUser().getUid();
                        User user = new User(userUid, name, email, username, getString(R.string.no_image));
                        userRef.child(userUid).setValue(user).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(this, "Can't Register Now, Please try again later", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "checkRegister: Database failed." + task.getException());

                            }
                        });

                    }
                });

    }

    private void goToLogin() {
        finish();
    }
}

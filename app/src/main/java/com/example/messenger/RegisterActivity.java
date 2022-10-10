package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextTextPassword;
    private EditText editTextName;
    private EditText editTextLastName;
    private EditText editTextAge;
    private Button btnSignUp;
    private RegistrationViewModel registrationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        registrationViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        observeViewModel();
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = trimmedText(editTextEmail);
                String password = trimmedText(editTextTextPassword);
                String name = trimmedText(editTextName);
                String lastName = trimmedText(editTextLastName);
                int age=0;
                if  (!editTextAge.getText().toString().trim().equals("")) {
                    age = Integer.parseInt(trimmedText(editTextAge));
                }else{
                    Toast.makeText(RegisterActivity.this,"please",Toast.LENGTH_SHORT).show();
                }
                registrationViewModel.signUp(email,password,name,lastName,age);
            }
        });
    }

    private void observeViewModel(){
        registrationViewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                    if (firebaseUser   != null){
                        Intent intent = UsersActivity.newIntent(RegisterActivity.this,firebaseUser.getUid());
                        startActivity(intent);
                        finish();
                    }
            }
        });
        registrationViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if  (s != null){
                    Toast.makeText(RegisterActivity.this, s,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init() {
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextTextPassword = findViewById(R.id.editTextTextPassword);
        editTextName = findViewById(R.id.editTextName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextAge = findViewById(R.id.editTextAge);
        btnSignUp = findViewById(R.id.btnSignUp);
    }

    private String trimmedText(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, RegisterActivity.class);
    }
}
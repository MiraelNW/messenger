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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private EditText editTextTextEmailAddress;
    private EditText editTextTextPassword;
    private TextView tvForgotPassword;
    private TextView tvRegistration;
    private Button btnLogin;
    public static final String TAG = "MAinActivity";

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        init();
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        observeViewModel();
        setUpClickListener();
    }

        private void setUpClickListener () {
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String email = editTextTextEmailAddress.getText().toString().trim();
                    String password = editTextTextPassword.getText().toString().trim();
                    loginViewModel.login(email, password);
                }
            });
            tvForgotPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = ResetPasswordActivity.newIntent(LoginActivity.this,
                            editTextTextEmailAddress.getText().toString().trim());
                    startActivity(intent);
                }
            });
            tvRegistration.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = RegisterActivity.newIntent(LoginActivity.this);
                    startActivity(intent);
                }
            });
        }
    private void observeViewModel(){
        loginViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if (errorMessage!=null) {
                    Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
        loginViewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if  (firebaseUser != null){
                    Intent intent = UsersActivity.newIntent(LoginActivity.this,firebaseUser.getUid());
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    private void init() {
        editTextTextEmailAddress = findViewById(R.id.editTextTextEmailAddress);
        editTextTextPassword = findViewById(R.id.editTextTextPassword);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvRegistration = findViewById(R.id.tvRegistration);
        btnLogin = findViewById(R.id.btnLogin);
    }

    public static Intent newIntent(Context context){
        return new Intent(context, LoginActivity.class);
    }
}
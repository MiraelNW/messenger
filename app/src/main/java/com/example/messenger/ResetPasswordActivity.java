package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText etEmail;
    private Button btnResetPassword;
    private  ResetPasswordViewModel resetPasswordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        init();
        resetPasswordViewModel = new ViewModelProvider(this).get(ResetPasswordViewModel.class);
        observeViewModel();
        String email = getIntent().getStringExtra("email");
        etEmail.setText(email);
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String  email  =  etEmail.getText().toString().trim();
                resetPasswordViewModel.resetPassword(email);
            }
        });
    }

    private void observeViewModel(){
        resetPasswordViewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    Toast.makeText(ResetPasswordActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            }

        });
        resetPasswordViewModel.isSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                if(success){
                    Toast.makeText(ResetPasswordActivity.this, R.string.sent, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void init() {
        etEmail = findViewById(R.id.etEmail);
        btnResetPassword = findViewById(R.id.btnResetPassword);
    }

    public   static Intent newIntent(Context context, String email){
        Intent intent = new Intent(context, ResetPasswordActivity.class);
        intent.putExtra("email",email);
        return intent;
    }
}
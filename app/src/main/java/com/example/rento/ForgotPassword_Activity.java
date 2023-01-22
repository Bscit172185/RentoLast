package com.example.rento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword_Activity extends AppCompatActivity {
    EditText mail;
    Button send;
    String email;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        auth=FirebaseAuth.getInstance();
        mail=findViewById(R.id.editText7);
        send=findViewById(R.id.sendmail);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validatedata();
            }
        });
    }

    private void validatedata() {
        email=mail.getText().toString();
        if(!email.isEmpty()&& Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            forgetpass();
        }
        else {
            mail.setError("Enter vaild email");
        }
    }

    private void forgetpass() {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword_Activity.this, "Check you email", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(ForgotPassword_Activity.this,Login.class));
                }
                else {
                    Toast.makeText(ForgotPassword_Activity.this, "Error:"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
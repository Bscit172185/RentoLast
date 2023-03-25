package com.example.rento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    TextView dothacc,Forgotpass;
    EditText Email,Pass;
    Button login;
    ImageView imgview1;
    int count=0;
    private FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Email=findViewById(R.id.editText);
        Pass=findViewById(R.id.editText2);
        login=findViewById(R.id.login);
        Forgotpass=findViewById(R.id.textView2);
        dothacc=findViewById(R.id.dontacc);
        imgview1=findViewById(R.id.img1);
        imgview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count==0){
                    Pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    count=1;
                }
                else {
                    Pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    count=0;
                }



            }
        });
        dothacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(Login.this,Registration.class);
                startActivity(intent);
            }
        });
        Forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this,ForgotPassword_Activity.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=Email.getText().toString();
                String pass=Pass.getText().toString();
                if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    if(!pass.isEmpty()){
                        firebaseAuth.signInWithEmailAndPassword(email,pass)
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Toast.makeText(Login.this,"Logged in Successfully!!!",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(Login.this,MainActivity.class));
                                        finish();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(Login.this, "error!: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                    else {
                        Pass.setError("Enter Password..");
                    }
                }
                else {
                    Email.setError("Enter Valid Email");
                }
            }
        });
    }
}
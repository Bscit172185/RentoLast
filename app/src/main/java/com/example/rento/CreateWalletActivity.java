package com.example.rento;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Objects;

public class CreateWalletActivity extends AppCompatActivity {
    EditText bankname,name,accno,ifsccode;
    Button submit;
    ImageView ImageView;
    String bname="",oname="",anum="",ifsc="";
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser user=auth.getCurrentUser();
    String UID=user.getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);
        bankname=findViewById(R.id.bankname);
        name=findViewById(R.id.name);
        accno=findViewById(R.id.accno);
        ifsccode=findViewById(R.id.ifsccode);
        submit=findViewById(R.id.submit);
        ImageView=findViewById(R.id.imageView17);

        ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateWalletActivity.this,MainActivity.class));
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bname=bankname.getText().toString();
                oname=name.getText().toString();
                anum=accno.getText().toString();
                ifsc=ifsccode.getText().toString();
                if(!bname.isEmpty()){
                    if(!oname.isEmpty()){
                        if(!anum.isEmpty()&&anum.length()==14){
                            if(!ifsc.isEmpty()){
                                Customprogressbar dilog=new Customprogressbar(CreateWalletActivity.this);
                                dilog.show();
                                HashMap<String, Object> s=new HashMap<String, Object>();
                                s.put("UID",UID);
                                s.put("BANK Name",bname);
                                s.put("holderName",oname);
                                s.put("acnum",anum);
                                s.put("IFSC",ifsc);
                                s.put("Balance","0");
                                s.put("brocrage","0");
                                db.collection("Wallet").document(UID).set(s)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                dilog.dismiss();
                                                Toast.makeText(CreateWalletActivity.this, "Wallet created successfully.....", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(CreateWalletActivity.this,WalletActivity.class));
                                            }
                                        });
                            }
                            else {
                                ifsccode.setError("IFSC CODE REQUIRED");
                            }
                        }
                        else {
                            accno.setError("ACCOUNT NUMBER REQUIRED");
                        }
                    }
                    else {
                        name.setError("ACCOUNT HOLDER NAME REQUIRED");
                    }
                }
                else {
                    bankname.setError("BANK NAME REQUIRED");
                }
            }
        });
    }
}
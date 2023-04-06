package com.example.rento;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;

public class WalletActivity extends AppCompatActivity {
    TextView balance,bankname,name,accno,ifsccode;
    ImageView imageView;

    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser user=auth.getCurrentUser();
    String UID=user.getUid();
    int p=0,b=0,a,a1,res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        imageView=findViewById(R.id.imageView16);
        balance=findViewById(R.id.balance);
        bankname=findViewById(R.id.bankname);
        name=findViewById(R.id.name);
        accno=findViewById(R.id.accno);
        ifsccode=findViewById(R.id.ifsccode);
        db.collection("Wallet").document(UID).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                int a,b,re;
                                a=Integer.parseInt(documentSnapshot.getString("Balance"));
                                b=Integer.parseInt(documentSnapshot.getString("brocrage"));
                                re=a+b;
                                balance.setText(String.valueOf(re));
                                bankname.setText(documentSnapshot.getString("BANK Name"));
                                name.setText(documentSnapshot.getString("holderName"));
                                ifsccode.setText(documentSnapshot.getString("IFSC"));
                                accno.setText(documentSnapshot.getString("acnum"));
                            }
                        });
        db.collection("order").get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot d:list){
                                    String x=d.getString("proUID");
                                    if(UID.equals(x)){
                                        String price,bro;
                                        price=d.getString("price");
                                        bro=d.getString("bro");
                                        showbalance(price,bro);
                                    }
                                    else if(UID.equals(d.getString("orderUserID"))){
                                        String price,bro;
                                        price="0";
                                        bro=d.getString("bro");
                                        showbalance(price,bro);
                                    }
                                }
                            }
                        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WalletActivity.this,MainActivity.class));
            }
        });
    }
    private void showbalance(String price,String bro) {
         p=p+Integer.parseInt(price);
         b=b+Integer.parseInt(bro);
         if(p==0){
             HashMap<String,Object> s=new HashMap<String, Object>();
             s.put("brocrage",String.valueOf(b));
             db.collection("Wallet").document(UID).update(s);
         }
         else {
             res=p-b;
             HashMap<String,Object> s=new HashMap<String, Object>();
             s.put("Balance",String.valueOf(res));
             db.collection("Wallet").document(UID).update(s);
         }
    }
}
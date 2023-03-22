package com.example.rento;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Profile_details_Activity extends AppCompatActivity {
    ShapeableImageView img;
    TextView logout,uname,unum,uemail,uadd,ulog,ulat,upcode,edit;
    String A,uid;
    Uri url;
    ImageView back;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    String name,email,number,add,log,lat,postcode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);
        FirebaseUser user=firebaseAuth.getCurrentUser();
        edit=findViewById(R.id.editpro);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile_details_Activity.this,MainActivity.class));
                finish();
            }
        });


        if(user==null){
            finish();
        }
        logout=findViewById(R.id.logout);
        img=findViewById(R.id.img);
        Intent intent=getIntent();
        A=intent.getStringExtra("url");
        uid=intent.getStringExtra("uid");
        if(A!="noko"){
            url=Uri.parse(A);
            Picasso.get().load(url).into(img);
        }
        uname=findViewById(R.id.Name);
        uemail=findViewById(R.id.Email);
        uadd=findViewById(R.id.Add);
        ulog=findViewById(R.id.Log);
        ulat=findViewById(R.id.Lat);
        upcode=findViewById(R.id.UPostcode);
        unum=findViewById(R.id.num);


        db.collection("user").document(uid).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                name=documentSnapshot.getString("Name");
                                email=documentSnapshot.getString("Email");
                                number=documentSnapshot.getString("Number");
                                add=documentSnapshot.getString("Address");
                                log=documentSnapshot.getString("Longitude");
                                lat=documentSnapshot.getString("Latitude");
                                postcode=documentSnapshot.getString("PostalCode");
                                uname.setText(name);
                                uemail.setText(email);
                                unum.setText(number);
                                uadd.setText(add);
                                ulog.setText(log);
                                ulat.setText(lat);
                                upcode.setText(postcode);



                            }
                        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(Profile_details_Activity.this,Login.class));
                finish();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile_details_Activity.this,ProfileUpdateActivity.class));
                finish();
            }
        });

    }
}
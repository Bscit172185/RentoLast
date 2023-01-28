package com.example.rento;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

public class Profile_details_Activity extends AppCompatActivity {
    ShapeableImageView img;
    TextView logout;
    String A;
    Uri url;
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user==null){
            finish();
        }
        logout=findViewById(R.id.logout);
        img=findViewById(R.id.img);
        Intent intent=getIntent();
        A=intent.getStringExtra("url");
        url=Uri.parse(A);
        Picasso.get().load(url).into(img);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(Profile_details_Activity.this,Login.class));
                finish();
            }
        });

    }
}
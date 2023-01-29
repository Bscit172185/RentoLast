package com.example.rento;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.HashMap;

public class Item_details_of_recycleview extends AppCompatActivity {
    ShapeableImageView img;
    TextView name,dis,price,brorate,UID;
    String i;
    Uri uri=null;
    Button cart;
    String ProId;
    String pname,pprice,brok;
    FirebaseFirestore db=FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details_of_recycleview);
        img=findViewById(R.id.img);
        name=findViewById(R.id.text);
        price=findViewById(R.id.text3);
        brorate=findViewById(R.id.text5);
        cart=findViewById(R.id.button1);
        Intent intent=getIntent();
        ProId=intent.getStringExtra("id");
        db.collection("Product").document(ProId).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    pname=documentSnapshot.getString("Product_Name");
                                    pprice=documentSnapshot.getString("Product_Price");
                                    brok=documentSnapshot.getString("Product_brocrage");
                                    name.setText(pname);
                                    price.setText(pprice);
                                    brorate.setText(brok);
                                    i=documentSnapshot.getString("Product_ImgUrl");
                                    uri=Uri.parse(i);
                                    Picasso.get().load(uri).into(img);
                            }
                        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(ProId);



            }
        });


    }
}
package com.example.rento;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.net.URI;

public class Item_details_of_recycleview extends AppCompatActivity {
    ShapeableImageView img;
    TextView name,dis,price,brorate;
    String i;
    Uri uri=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details_of_recycleview);
        img=findViewById(R.id.img);
        name=findViewById(R.id.text);
        price=findViewById(R.id.text3);
        brorate=findViewById(R.id.text5);
        Intent intent=getIntent();
        i=intent.getStringExtra("imgurl");
        uri=Uri.parse(i);
        Picasso.get().load(uri).into(img);
        name.setText(intent.getStringExtra("name"));
        price.setText(intent.getStringExtra("price"));
        brorate.setText(intent.getStringExtra("brorate"));


    }
}
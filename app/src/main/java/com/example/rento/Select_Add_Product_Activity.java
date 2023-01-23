package com.example.rento;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Select_Add_Product_Activity extends AppCompatActivity {
    CardView a1,a2,a3,a4,a5,a6,a7;
    ImageButton but;
    public String cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_add_product);
        but=findViewById(R.id.imageButton2);
        a1=findViewById(R.id.cardview1);
        a2=findViewById(R.id.cardview2);
        a3=findViewById(R.id.cardview3);
        a4=findViewById(R.id.cardview4);
        a5=findViewById(R.id.cardview5);
        a6=findViewById(R.id.cardview6);
        a7=findViewById(R.id.cardview7);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Select_Add_Product_Activity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cat="Furniture";
                send();
            }
        });
        a2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cat="Appliances";
                send();
            }
        });
        a3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cat="Electronics";
                send();
            }
        });
        a4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cat="Books";
                send();
            }
        });
        a5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cat="Clothes";
                send();
            }
        });
        a6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cat="Instruments";
                send();
            }
        });
        a7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cat="Vehicles";
                send();
            }
        });

    }

    private void send() {
        Intent intent=new Intent(Select_Add_Product_Activity.this,Add_Product_Activity.class);
        intent.putExtra("catagory",cat);
        startActivity(intent);
    }

}
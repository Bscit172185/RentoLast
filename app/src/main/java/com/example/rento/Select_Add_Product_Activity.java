package com.example.rento;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

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
                Toast.makeText(Select_Add_Product_Activity.this, "Furniture", Toast.LENGTH_SHORT).show();
                send();
            }
        });
        a2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cat="Electronics";
                Toast.makeText(Select_Add_Product_Activity.this, "Electronics", Toast.LENGTH_SHORT).show();
                send();
            }
        });
        a3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cat="home appliances";
                Toast.makeText(Select_Add_Product_Activity.this, "Home appliances", Toast.LENGTH_SHORT).show();
                send();
            }
        });
        a4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cat="Cloths";
                Toast.makeText(Select_Add_Product_Activity.this, "Clothes", Toast.LENGTH_SHORT).show();
                send();
            }
        });
        a5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cat="Books";
                Toast.makeText(Select_Add_Product_Activity.this, "Books", Toast.LENGTH_SHORT).show();
                send();
            }
        });
        a6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cat="Instruments";
                Toast.makeText(Select_Add_Product_Activity.this, "Instruments", Toast.LENGTH_SHORT).show();
                send();
            }
        });
        a7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cat="Vehicles";
                Toast.makeText(Select_Add_Product_Activity.this, "Vehicles", Toast.LENGTH_SHORT).show();
                send();
            }
        });

    }

    private void send() {
        Intent intent=new Intent(Select_Add_Product_Activity.this,Add_Product_Activity.class);
        intent.putExtra("category",cat);
        startActivity(intent);
    }

}
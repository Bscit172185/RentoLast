package com.example.rento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView navigationView;
    FloatingActionButton floatbut;
    int counter=0;
    private final int close=10;
    private FirebaseAuth auth=FirebaseAuth.getInstance();

    @Override
    public void onBackPressed() {

        counter++;
        if(counter==2){
            finishActivity(close);
            super.onBackPressed();

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView = findViewById(R.id.navigationbar);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                System.out.println(id);
                if(id==R.id.Home){
                    loadFarg(new HomeFragment());

                }
                else if(id==R.id.Menu){
                    loadFarg(new MenuFragment());
                }
                else if (id==R.id.Cart){
                    loadFarg(new CartFragment());
                }
                else if(id==R.id.list){
                    loadFarg(new UsersListedProductFragment());
                }

                return false;
            }
            public void loadFarg(Fragment fragment){
                FragmentManager fm= getSupportFragmentManager();
                FragmentTransaction ft= fm.beginTransaction();
                ft.add(R.id.Frame_layout,fragment);
                ft.commit();
            }
        });
        navigationView.setSelectedItemId(R.id.Home);
        floatbut=findViewById(R.id.floatbut);
        floatbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Product= new Intent(MainActivity.this,Select_Add_Product_Activity.class);
                startActivity(Product);
            }
        });
    }
}
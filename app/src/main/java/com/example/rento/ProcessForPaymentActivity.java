package com.example.rento;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class ProcessForPaymentActivity extends AppCompatActivity {
    Button payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_for_payment);
        payment=findViewById(R.id.button1);
    }
}
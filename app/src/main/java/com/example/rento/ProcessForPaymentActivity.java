package com.example.rento;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class ProcessForPaymentActivity extends AppCompatActivity implements PaymentResultListener {
    ShapeableImageView imgview;
    TextView pname,pprice,pbroc,pdes,paddr;
    Button payment,nevi;
    ImageView mapView;
    Activity activity=this;
    String pid;
    String img,name,dec,uid,price,broc,Addr,Uname,Uemail,Uphone,latitude,longitute,ulati,ulangi;
    Uri uri;
    int a,b,c,d;
    String finalamount;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser user=auth.getCurrentUser();
    String userid=user.getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_for_payment);
        pname=findViewById(R.id.text);
        pprice=findViewById(R.id.text3);
        pbroc=findViewById(R.id.text5);
        pdes=findViewById(R.id.text1);
        paddr=findViewById(R.id.text2);
        mapView=findViewById(R.id.MapView);
        payment=findViewById(R.id.button1);
        nevi=findViewById(R.id.navi);
        imgview=findViewById(R.id.img);
        Checkout.preload(getApplicationContext());
        Intent intent=getIntent();
         pid=intent.getStringExtra("pid");
         db.collection("Product").document(pid).get()
                         .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                             @Override
                             public void onSuccess(DocumentSnapshot documentSnapshot) {
                                 img=documentSnapshot.getString("Product_ImgUrl");
                                 name=documentSnapshot.getString("Product_Name");
                                 uid=documentSnapshot.getString("UID");
                                 price=documentSnapshot.getString("Product_Price");
                                 broc=documentSnapshot.getString("Product_brocrage");
                                 dec=documentSnapshot.getString("Product_Descreiption");
                                 uri=Uri.parse(img);
                                 Picasso.get().load(uri).into(imgview);
                                 pname.setText(name);
                                 pdes.setText(dec);
                                 pprice.setText(price);
                                 pbroc.setText(broc);
                                 db.collection("user").document(uid).get()
                                         .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                             @Override
                                             public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                 Addr=documentSnapshot.getString("Address");
                                                 paddr.setText(Addr);
                                             }
                                         });
                             }
                         });
         db.collection("user").document(userid).get()
                         .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                             @Override
                             public void onSuccess(DocumentSnapshot documentSnapshot) {
                                 Uname=documentSnapshot.getString("Name");
                                 Uemail=documentSnapshot.getString("Email");
                                 Uphone=documentSnapshot.getString("Number");
                                 latitude=documentSnapshot.getString("Latitude");
                                 longitute=documentSnapshot.getString("Longitude");

                             }
                         });


        db.collection("user").document(userid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        ulati=documentSnapshot.getString("Latitude");
                        ulangi=documentSnapshot.getString("Longitude");
                    }
                });

        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent map=new Intent(ProcessForPaymentActivity.this,AddressMapsActivity.class);
                map.putExtra("ulati",ulati);
                map.putExtra("ulogi",ulangi);
                map.putExtra("lati",latitude);
                map.putExtra("longi",longitute);
                startActivity(map);
                finish();
            }
        });
        nevi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a="google.navigation:q="+latitude+","+longitute+"&mode=1";
                Intent ltm=new Intent(Intent.ACTION_VIEW,Uri.parse(a));
                intent.setPackage("com.google.android.apps.maps");
                startActivity(ltm);
            }
        });
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a=Integer.parseInt(price);
                b=Integer.parseInt(broc);
                System.out.println(a);
                c=a+b;
                d=c*100;
                finalamount=String.valueOf(d);
                System.out.println(Uemail);
                startpayment(finalamount,Uname,Uemail,Uphone);
            }
        });


    }
    private void startpayment(String amu,String name,String email,String mob) {

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_aErLjoKUqCb7rU");
        checkout.setImage(R.drawable.logoicon);
        try {
            JSONObject options = new JSONObject();
            options.put("name",name);
            options.put("description", "Reference No. #123456");
            options.put("image", "https://firebasestorage.googleapis.com/v0/b/rento-f1b52.appspot.com/o/banner.jpg?alt=media&token=ade5458d-75fa-4e71-9d44-3f434608c20b");
            options.put("theme.color", "#3399CC");
            options.put("currency", "INR");
            options.put("amount",amu);//pass amount in currency subunits
            options.put("prefill.email",email);
            options.put("prefill.contact",mob);
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);
            checkout.open(activity, options);

        } catch(Exception e) {
            Toast.makeText(activity, "error"+e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }

    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "success....", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "faild...", Toast.LENGTH_SHORT).show();
    }
}
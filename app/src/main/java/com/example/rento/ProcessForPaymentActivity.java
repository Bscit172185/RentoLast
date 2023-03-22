package com.example.rento;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;

public class ProcessForPaymentActivity extends AppCompatActivity implements PaymentResultListener {
    ShapeableImageView imgview;
    TextView pname,pprice,pbroc,pdes,paddr;
    Button payment,nevi;
    ImageView mapView;
    Activity activity=this;
    String pid="",qut="",reqid;
    String img,name,dec,uid,price,broc,Addr,Uname,Uemail,Uphone,latitude,longitute,ulati,ulongi,prouid;
    Uri uri;
    int a,b,c,d,total,intpr,intbro;
    String finalamount,PaymentStu,tot;
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
        PaymentStu=intent.getStringExtra("paystus");
        reqid=intent.getStringExtra("reqid");
        pid=intent.getStringExtra("pid");
        tot=intent.getStringExtra("totalamount");
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
                                 prouid=documentSnapshot.getString("UID");
                                 uri=Uri.parse(img);
                                 Picasso.get().load(uri).into(imgview);
                                 pname.setText(name);
                                 pdes.setText(dec);
                                 pprice.setText(price);
                                 pbroc.setText(broc);
                                 intpr=Integer.parseInt(price);
                                 intbro=Integer.parseInt(broc);

                                 total=intpr+intbro;
                                 db.collection("user").document(uid).get()
                                         .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                             @Override
                                             public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                 Addr=documentSnapshot.getString("Address");
                                                 paddr.setText(Addr);
                                                 ulati=documentSnapshot.getString("Latitude");
                                                 ulongi=documentSnapshot.getString("Longitude");
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
        db.collection("Rent_Request").document(reqid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        int qut=Integer.parseInt(documentSnapshot.getString("qut"));
                        int finl=total*qut;
                        if(PaymentStu.equals("PAID")){
                            nevi.setVisibility(View.INVISIBLE);
                            payment.setText("Download Invoice");
                            payment.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    AlertDialog.Builder dilog=new AlertDialog.Builder(ProcessForPaymentActivity.this);
                                    dilog.setTitle("Order Invoice");
                                    dilog.setIcon(R.drawable.applogo);
                                    dilog.setMessage("Product Name:   "+name+
                                            "\nproduct Amount:    "+price+
                                            "\nProduct Brocrage:    "+broc+
                                            "\nNO of Months: "+qut+
                                            "\n------------------------------------"+
                                            "\nTotal Amount:    "+finl+
                                            "\n------------------------------------"+
                                            "\nPayment Status: "+PaymentStu);
                                    dilog.setIcon(null);
                                    dilog.show();
                                }
                            });
                        }
                    }
                });
        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent map=new Intent(ProcessForPaymentActivity.this,AddressMapsActivity.class);
                map.putExtra("ulati",ulati);
                map.putExtra("ulogi",ulongi);
                startActivity(map);
                finish();
            }
        });
        nevi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a="google.navigation:q="+ulati+","+ulongi+"&mode=1";
                Intent ltm=new Intent(Intent.ACTION_VIEW,Uri.parse(a));
                ltm.setPackage("com.google.android.apps.maps");
                startActivity(ltm);
            }
        });
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a=Integer.parseInt(price);
                b=Integer.parseInt(broc);
                System.out.println(a);
                d=Integer.parseInt(tot)*100;
                finalamount=String.valueOf(d);
                AlertDialog.Builder alert=new AlertDialog.Builder(ProcessForPaymentActivity.this);
                alert.setTitle("D E C L A R A T I O N");
                alert.setIcon(R.drawable.applogo);
                alert.setMessage("Get product first and"+
                        "\nEnsure quality,"+
                        "\nand then proceed for payment.");
                alert.setPositiveButton("Yes, I've Read", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startpayment(finalamount,Uname,Uemail,Uphone);
                    }
                });
                alert.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert.show();

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
            options.put("amount",amu);
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
        db.collection("Rent_Request").document(reqid).update("Payment","PAID");
        Toast.makeText(this, "success....", Toast.LENGTH_SHORT).show();
        HashMap<String,Object> sa=new HashMap<String, Object>();
        sa.put("proUID",prouid);
        sa.put("ProId",pid);
        sa.put("orderUserID",userid);
        int a=Integer.parseInt(finalamount)/100;
        sa.put("price",String.valueOf(a));
        sa.put("bro",broc);
        db.collection("order").add(sa)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(activity, "Order Confremd.....!", Toast.LENGTH_SHORT).show();
                HashMap<String,Object> s=new HashMap<String, Object>();
                s.put("pro_status","DEACTIVE");
                db.collection("Product").document(pid).update(s);
            }
        });


    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "faild...", Toast.LENGTH_SHORT).show();
    }


}
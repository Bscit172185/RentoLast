package com.example.rento;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class Item_details_of_recycleview extends AppCompatActivity {
    ShapeableImageView img;
    TextView name,dis,price,brorate,UID,conteon,qutid;
    ImageView whatapp,submon,addmon,subqut,addqut;
    EditText montqut,proqut;
    String qut,proqut1;
    String i,Status="Pending";
    String requid,reqpro;
    Uri uri=null;
    Button cart,rent;
    String ProId;
    int anum=1;
    int bnum=1;
    String pname,pprice,brok,User_Id;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser user=auth.getCurrentUser();
    String UserID=user.getUid();
    public static final String channel_id="product add";
    public int notify_id=100;
    int Rentid=0;
    int quted=0;
    int ref=0;

    @Override
    protected void onStart() {
        db.collection("Rent_Request").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d:list){
                    requid=d.getString("ReqUserID");
                    reqpro=d.getString("ProId");
                    if(requid.equals(UserID)){
                        if(reqpro.equals(ProId)){
                            Rentid=100;
                        }
                        else {
                            Rentid=0;
                        }
                    }
                    else {
                        Rentid=0;
                    }
                }
            }
        });
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details_of_recycleview);
        img=findViewById(R.id.img);
        name=findViewById(R.id.text);
        price=findViewById(R.id.text3);
        brorate=findViewById(R.id.text5);
        conteon=findViewById(R.id.contectno);
        dis=findViewById(R.id.text1);
        qutid=findViewById(R.id.text2);
        whatapp=findViewById(R.id.whatsappicon);
        submon=findViewById(R.id.submon);
        addmon=findViewById(R.id.addmon);
        subqut=findViewById(R.id.subqut);
        addqut=findViewById(R.id.addqut);
        rent=findViewById(R.id.button2);
        cart=findViewById(R.id.button1);
        conteon.setVisibility(View.INVISIBLE);
        montqut=findViewById(R.id.qut1);
        proqut=findViewById(R.id.pqut);
        whatapp.setVisibility(View.INVISIBLE);
        Intent intent=getIntent();
        User_Id=intent.getStringExtra("UsersId");
        ProId=intent.getStringExtra("id");
        db.collection("Product").document(ProId).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    pname=documentSnapshot.getString("Product_Name");
                                    pprice=documentSnapshot.getString("Product_Price");
                                    brok=documentSnapshot.getString("Product_brocrage");
                                    String diss=documentSnapshot.getString("Product_Descreiption");
                                    String qutd=documentSnapshot.getString("pro_qut");

                                    name.setText(pname);
                                    price.setText(pprice);
                                    brorate.setText(brok);
                                    dis.setText(diss);
                                    qutid.setText("Qut: "+qutd);
                                    quted=Integer.parseInt(qutd);
                                    i=documentSnapshot.getString("Product_ImgUrl");
                                    uri=Uri.parse(i);
                                    Picasso.get().load(uri).into(img);
                            }
                        });
        if(User_Id.equals(UserID)){
            cart.setVisibility(View.INVISIBLE);
            rent.setVisibility(View.INVISIBLE);
        }
        else {
            cart.setVisibility(View.VISIBLE);
            rent.setVisibility(View.VISIBLE);
        }
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("cart").get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();
                                        for(DocumentSnapshot d:list){
                                            String a=d.getString("Uid");
                                            String b=d.getString("ProId");
                                            if(!d.getData().isEmpty()){
                                                if(a.equals(UserID)){
                                                   if(b.equals(ProId)){
                                                       ref=1;

                                                   }
                                                }
                                            }
                                            else {
                                                addtocart();
                                            }
                                        }
                                        if(ref==0){
                                            addtocart();
                                        }
                                        else {
                                            ref=0;
                                            Toast.makeText(Item_details_of_recycleview.this, "product is in cart", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
            }
        });
        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qut=montqut.getText().toString();
                proqut1=proqut.getText().toString();
                HashMap<String,Object> s=new HashMap<String, Object>();
                s.put("ProId",ProId);
                s.put("ReqUserID",UserID);
                s.put("Status",Status);
                s.put("qut",qut);
                s.put("Payment","pending");
                s.put("pro_qut",proqut1);
                if (Rentid != 100) {
                    db.collection("Rent_Request").add(s).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(Item_details_of_recycleview.this, "Requested...!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    Toast.makeText(Item_details_of_recycleview.this, "Already in request", Toast.LENGTH_SHORT).show();
                }

            }
        });
        montqut.setText("1");

        submon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(anum>=2){
                    int b=anum-1;
                    anum=b;
                    montqut.setText(String.valueOf(anum));

                }
                else {
                    Toast.makeText(Item_details_of_recycleview.this, "Minimum No of Month should be 1", Toast.LENGTH_SHORT).show();
                }
            }
        });
        addmon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(anum<=11){
                    int b=anum+1;
                    anum=b;
                    montqut.setText(String.valueOf(anum));
                }
                else {
                    Toast.makeText(Item_details_of_recycleview.this, "Maximum No of months should be 12", Toast.LENGTH_SHORT).show();
                }
            }
        });

        subqut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bnum>=2){
                    int b=bnum-1;
                    bnum=b;
                    proqut.setText(String.valueOf(bnum));
                }
                else {
                    Toast.makeText(Item_details_of_recycleview.this, "Minimum Quantity should be 1", Toast.LENGTH_SHORT).show();
                }
            }
        });
        addqut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quted>bnum){
                    int b=bnum+1;
                    bnum=b;
                    proqut.setText(String.valueOf(bnum));

                }
                else {
                    Toast.makeText(Item_details_of_recycleview.this, "Quantity exceeded", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void addtocart() {
        String qut1=montqut.getText().toString();
        String proqut2=proqut.getText().toString();
        HashMap<String,Object>m=new HashMap<String,Object>();
        m.put("Uid",UserID);
        m.put("ProId",ProId);
        m.put("qut",qut1);
        m.put("pro_qut",proqut2);
        db.collection("cart").document().set(m)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(Item_details_of_recycleview.this, "Product added in cart", Toast.LENGTH_SHORT).show();
                        NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        Notification notfy= null;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            notfy = new Notification.Builder(Item_details_of_recycleview.this)
                                    .setSmallIcon(R.drawable.icon)
                                    .setContentTitle("Notification")
                                    .setContentText(" new product added in cart")
                                    .setChannelId(channel_id)
                                    .build();
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            manager.createNotificationChannel(new NotificationChannel(channel_id,"new chaeenl",NotificationManager.IMPORTANCE_HIGH));
                        }
                        manager.notify(notify_id,notfy);


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Item_details_of_recycleview.this, "error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
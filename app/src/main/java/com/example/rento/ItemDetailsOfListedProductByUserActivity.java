package com.example.rento;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemDetailsOfListedProductByUserActivity extends AppCompatActivity {
   ShapeableImageView img;
   TextView name,rento,name1,address,months;
   String proid,uname,price,requserid;
   String pid,pname,pprice,urli;
   RecyclerView regview;
   ArrayList<Model> datalist;
   Button delpro;
   int pro_stid=0;
   itemRequestRecycleView myadapter;
   Uri uri=null;
   CardView cardview1;
   Switch deactivate;
   String orderproid;
   String pro_status;
   FirebaseFirestore db=FirebaseFirestore.getInstance();
   FirebaseAuth auth=FirebaseAuth.getInstance();
   FirebaseUser user=auth.getCurrentUser();
   String UserID=user.getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details_of_listed_product_by_user);
        rento=findViewById(R.id.rento);
        rento.setVisibility(View.INVISIBLE);
        cardview1=findViewById(R.id.cardview1);
        cardview1.setVisibility(View.INVISIBLE);
        name1=findViewById(R.id.name1);
        address=findViewById(R.id.address);
        months=findViewById(R.id.months);
        regview=findViewById(R.id.recycleview1);
        delpro=findViewById(R.id.delpro);
        regview.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        img=findViewById(R.id.img);
        deactivate=findViewById(R.id.switch1);
        name=findViewById(R.id.text);
        Intent intent=getIntent();
        pid=intent.getStringExtra("id");
        datalist=new ArrayList<>();
        myadapter=new itemRequestRecycleView(datalist,this);
        regview.setAdapter(myadapter);
        db.collection("order").get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                               List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                               for (DocumentSnapshot d:list){
                                   if(pid.equals(d.getString("ProId"))){
                                       orderproid=d.getString("orderUserID");
                                       db.collection("user").document(orderproid).get()
                                               .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                   @Override
                                                   public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                       cardview1.setVisibility(View.VISIBLE);
                                                       rento.setVisibility(View.VISIBLE);
                                                       name1.setText(documentSnapshot.getString("Name"));
                                                       address.setText(documentSnapshot.getString("Address"));
                                                       months.setText(documentSnapshot.getString("Number"));
                                                   }
                                               });
                                   }
                               }
                            }
                        });
        db.collection("Product").document(pid).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String a=documentSnapshot.getString("pro_status");
                                if(a.equals("ON")){
                                    deactivate.setChecked(true);
                                    pro_stid=1;
                                }
                                else {
                                    deactivate.setChecked(false);
                                    pro_stid=2;
                                }
                            }
                        });

        deactivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pro_stid==1){
                    HashMap<String,Object> s=new HashMap<String, Object>();
                    s.put("pro_status","DEACTIVE");
                    AlertDialog.Builder alert=new AlertDialog.Builder(ItemDetailsOfListedProductByUserActivity.this);
                    alert.setTitle("Make Unvailable this product");
                    alert.setMessage("Click yes if you sure..");
                    alert.setIcon(R.drawable.applogo);
                    alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db.collection("Product").document(pid).update(s)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(ItemDetailsOfListedProductByUserActivity.this, "Deactivate prodect", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deactivate.setChecked(true);
                        }
                    });
                    alert.show();

                }
                else if (pro_stid==2){
                    HashMap<String,Object> s=new HashMap<String, Object>();
                    s.put("pro_status","ON");
                    AlertDialog.Builder alert=new AlertDialog.Builder(ItemDetailsOfListedProductByUserActivity.this);
                    alert.setTitle("Make Available this product");
                    alert.setMessage("Click yes if you sure..");
                    alert.setIcon(R.drawable.applogo);
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db.collection("Rent_Request").get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                                                    for (DocumentSnapshot d:list){
                                                        String id=d.getString("ProId");
                                                        if(id.equals(pid)){
                                                            String rid=d.getId();
                                                            HashMap<String,Object>s=new HashMap<String,Object>();
                                                            s.put("Status","OFF");
                                                            db.collection("Rent_Request").document(rid).update(s);
                                                        }
                                                    }
                                                }
                                            });
                            db.collection("Product").document(pid).update(s)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(ItemDetailsOfListedProductByUserActivity.this, "Activate prodect", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(ItemDetailsOfListedProductByUserActivity.this,ListedProductsFragment.class));
                                        }
                                    });
                        }
                    });
                    alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deactivate.setChecked(false);
                        }
                    });
                    alert.show();

                }
            }
        });
        db.collection("Product").document(pid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        pname=documentSnapshot.getString("Product_Name");
                        name.setText(pname);
                        urli=documentSnapshot.getString("Product_ImgUrl");
                        pro_status=documentSnapshot.getString("pro_status");
                        pro_status=documentSnapshot.getString("pro_status");
                        uri=Uri.parse(urli);
                        Picasso.get().load(uri).into(img);
                    }
                });
        db.collection("Rent_Request").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list =queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d:list){
                            proid=d.getString("ProId");
                            String status=d.getString("Status");
                            if(proid.equals(pid)){
                                if(status.equals("Pendding")||status.equals("Accepted")){
                                    String a=d.getId();
                                    System.out.println(a);
                                    requserid=d.getString("requserid");
                                    Model obj=d.toObject(Model.class);
                                    obj.pid=pid;
                                    obj.reqid=a;
                                    obj.qut=d.getString("qut");
                                    obj.requid=d.getString("ReqUserID");
                                    obj.reqproid=proid;
                                    obj.proqut=d.getString("pro_qut");
                                    datalist.add(obj);
                                }

                            }

                        }
                        myadapter.notifyDataSetChanged();
                    }
                });

        delpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert=new AlertDialog.Builder(ItemDetailsOfListedProductByUserActivity.this);
                alert.setIcon(R.drawable.applogo);
                alert.setTitle("Delete Product !");
                alert.setMessage("Delete this product from Listing");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        HashMap<String,Object>s=new HashMap<String, Object>();
                        s.put("pro_status","OFF");
                        db.collection("Product").document(pid).update(s)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(ItemDetailsOfListedProductByUserActivity.this, "Product remove successfully...!", Toast.LENGTH_SHORT).show();
                                    }
                                });
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

}
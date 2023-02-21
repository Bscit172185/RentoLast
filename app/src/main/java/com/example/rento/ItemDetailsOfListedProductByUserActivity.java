package com.example.rento;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

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
import java.util.List;

public class ItemDetailsOfListedProductByUserActivity extends AppCompatActivity {
   ShapeableImageView img;
   TextView name;
   String pid,pname,pprice,urli;
   RecyclerView regview;
   ArrayList<Model> datalist;
   itemRequestRecycleView myadapter;
   Uri uri=null;
   FirebaseFirestore db=FirebaseFirestore.getInstance();
   FirebaseAuth auth=FirebaseAuth.getInstance();
   FirebaseUser user=auth.getCurrentUser();
   String UserID=user.getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details_of_listed_product_by_user);
        regview=findViewById(R.id.recycleview1);
        regview.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        img=findViewById(R.id.img);
        name=findViewById(R.id.text);
        Intent intent=getIntent();
        pid=intent.getStringExtra("id");
        datalist=new ArrayList<>();
        db.collection("Product").document(pid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        pname=documentSnapshot.getString("Product_Name");
                        name.setText(pname);
                        urli=documentSnapshot.getString("Product_ImgUrl");
                        uri=Uri.parse(urli);
                        Picasso.get().load(uri).into(img);
                    }
                });
        db.collection("Rent_Request").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d:list){
                            String proid=d.getString("ProId");
                                Model obj=d.toObject(Model.class);
                                obj.qut=d.getString("qut");
                                datalist.add(obj);
                                myadapter.notifyDataSetChanged();

                        }

                    }
                });


    }
}
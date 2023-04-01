package com.example.rento;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ItemDetailsOfAddToCartActivity extends AppCompatActivity {
    String Itemid;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser user=auth.getCurrentUser();
    String UID=user.getUid();
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    String pname,pdisp,price,broc,uri;
    TextView name,disp,Price,bro;
    ShapeableImageView img;
    Button but1,but2;
    Uri uri1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details_of_add_to_cart);
        Intent intent=getIntent();
        Itemid=intent.getStringExtra("Itemid");
        name=findViewById(R.id.text);
        disp=findViewById(R.id.text1);
        Price=findViewById(R.id.text3);
        but1=findViewById(R.id.button1);
        img=findViewById(R.id.img);
        bro=findViewById(R.id.text5);
        db.collection("Product").document(Itemid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        pname=documentSnapshot.getString("Product_Name");
                        pdisp=documentSnapshot.getString("Product_Descreiption");
                        price=documentSnapshot.getString("Product_Price");
                        broc=documentSnapshot.getString("Product_brocrage");
                        uri=documentSnapshot.getString("Product_ImgUrl");
                        uri1=Uri.parse(uri);
                        Picasso.get().load(uri1).into(img);
                        name.setText(pname);
                        disp.setText(pdisp);
                        Price.setText(price+" ");
                        bro.setText(broc+" ");

                    }
                });
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("cart").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d:list){
                            if(UID.equals(d.getString("Uid"))){
                                if(Itemid.equals(d.getString("ProId"))){
                                    db.collection("cart").document(d.getId()).delete();
                                    Toast.makeText(ItemDetailsOfAddToCartActivity.this, "item deleted from cart", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(ItemDetailsOfAddToCartActivity.this,CartFragment.class));
                                }
                            }
                        }
                    }
                });
            }
        });




    }
}
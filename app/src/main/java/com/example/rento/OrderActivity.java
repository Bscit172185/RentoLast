package com.example.rento;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.security.AccessControlContext;
import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity {

    ArrayList<Model1> datalist1;
    RecyclerView regview;
    AcceptedRequestAdapter myadapter1;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser user=auth.getCurrentUser();
    String uid= user.getUid();
    String requserid,b,paystu,reqid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        regview=findViewById(R.id.regview);
        regview.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        datalist1=new ArrayList<>();
        db.collection("Rent_Request").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list1=queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d:list1){
                            requserid=d.getString("ReqUserID");
                            if(requserid.equals(uid)){
                                String stu=d.getString("Status");
                                if(stu.equals("Accepted")){
                                    reqid=d.getId();
                                    b=d.getString("ProId");
                                    paystu=d.getString("Payment");
                                    Accepteditem(OrderActivity.this,b,paystu);
                                }
                            }
                        }
                    }
                });


    }
    private void Accepteditem(Context context,String ID,String Paystu1) {
        myadapter1=new AcceptedRequestAdapter(datalist1,context);
        regview.setAdapter(myadapter1);
        db.collection("Product").document(b).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Model1 obj=documentSnapshot.toObject(Model1.class);
                        obj.pid=ID;
                        obj.reqid=reqid;
                        obj.paystu=Paystu1;
                        obj.stutus="yes";
                        datalist1.add(obj);
                        myadapter1.notifyDataSetChanged();
                    }
                });

    }
}
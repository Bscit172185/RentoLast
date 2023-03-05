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
    String requserid,b;
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
                                    b=d.getString("ProId");
                                    Accepteditem(OrderActivity.this);
                                }
                            }
                        }
                    }
                });


    }
    private void Accepteditem(Context context) {
        myadapter1=new AcceptedRequestAdapter(datalist1,context);
        regview.setAdapter(myadapter1);
        db.collection("Product").document(b).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Model1 obj=documentSnapshot.toObject(Model1.class);
                        obj.pid=b;
                        obj.stutus="yes";
                        datalist1.add(obj);
                        myadapter1.notifyDataSetChanged();
                    }
                });

    }
}
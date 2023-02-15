package com.example.rento;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class CartFragment extends Fragment {
    RecyclerView regview;
    ArrayList<Model>datalist;
    CartAdapter myadapter;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser user=auth.getCurrentUser();
    String uid= user.getUid();
    String a="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_cart, container, false);
        regview=view.findViewById(R.id.Regview1);
        regview.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        datalist=new ArrayList<>();
        db.collection("cart").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d:list)
                        {
                            String b=d.getString("Uid");
                            if(b.equals(uid)){
                                a=d.getString("ProId");
                                System.out.println(a);
                                getcarddata();
                            }

                        }

                    }
                });

        System.out.println(datalist);
        return view;
    }

    public void getcarddata() {
        myadapter=new CartAdapter(datalist);
        regview.setAdapter(myadapter);
        db.collection("Product").document(a).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                       Model obj=documentSnapshot.toObject(Model.class);
                       datalist.add(obj);
                       myadapter.notifyDataSetChanged();
                    }
                });

    }
}
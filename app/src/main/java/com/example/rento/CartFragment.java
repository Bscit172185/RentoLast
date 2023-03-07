package com.example.rento;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CartFragment extends Fragment {
    RecyclerView regview;
    ArrayList<Model>datalist;
    CartAdapter myadapter;
    ImageView alldel,back;
    Button chekout;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser user=auth.getCurrentUser();
    String uid= user.getUid();
    String a,b;
    String itemid;
    ArrayList<String> arrayList=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_cart, container, false);
        chekout=view.findViewById(R.id.checkout);
        back=view.findViewById(R.id.back);
        alldel=view.findViewById(R.id.deleteall);
        regview=view.findViewById(R.id.Regview1);
        regview.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
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
                                getcarddata();
                            }

                        }

                    }
                });

        alldel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("cart").get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<DocumentSnapshot> list1=queryDocumentSnapshots.getDocuments();
                                for(DocumentSnapshot d:list1){
                                    String iduser=d.getString("Uid");
                                    if(uid.equals(iduser)){
                                        itemid=d.getId();
                                        delete();

                                    }
                                }
                                Toast.makeText(getActivity(), "All item deleted....", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getActivity(),MainActivity.class));
                            }
                        });
            }
        });

        chekout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Customprogressbar dilog=new Customprogressbar(getActivity());
                dilog.show();
                db.collection("cart").get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();
                                for(DocumentSnapshot d:list){
                                    String ProId,Uid,qut,Status,itemidofcart,id;
                                    itemidofcart=d.getId();
                                    id=d.getString("Uid");
                                    ProId=d.getString("ProId");
                                    Uid=d.getString("Uid");
                                    qut=d.getString("qut");
                                    Status="Pendding";
                                    if(uid.equals(id)){
                                        checkout(ProId,Uid,qut,Status,itemidofcart);
                                        startActivity(new Intent(getActivity(),MainActivity.class));
                                    }
                                }

                            }
                        });
            }

        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                startActivity(new Intent(getActivity(),MainActivity.class));
            }
        });

        return view;
    }
public void checkout(String ProId,String Uid,String  qut,String Status,String itemidofcart){

    db.collection("Rent_Request").get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<DocumentSnapshot> list1=queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot b:list1){
                        String ReqUserID=b.getString("ReqUserID");
                        String ProId1=b.getString("ProId");
                        if(ReqUserID.equals(Uid)||!ReqUserID.equals(Uid)){
                            if(ProId1.equals(ProId)){
                                arrayList.add(b.getId());
                            }
                        }
                    }
                    System.out.println("this is arrery:   "+arrayList);
                    if(arrayList.size()==0){
                        HashMap<String,Object> sa=new HashMap<String, Object>();
                        sa.put("ProId",ProId);
                        sa.put("ReqUserID",uid);
                        sa.put("Status",Status);
                        sa.put("qut",qut);
                        db.collection("Rent_Request").add(sa)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(getActivity(), "Prodects requested...", Toast.LENGTH_SHORT).show();
                                        db.collection("cart").document(itemidofcart).delete();

                                    }
                                });
                    }
                    else {
                        Toast.makeText(getActivity(), "Already in request", Toast.LENGTH_SHORT).show();
                        db.collection("cart").document(itemidofcart).delete();

                    }
                }

            });
}

    private void delete() {
        db.collection("cart").document(itemid).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
    }

    public void getcarddata() {
        myadapter=new CartAdapter(datalist,getContext());
        regview.setAdapter(myadapter);
        db.collection("Product").document(a).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                       Model obj=documentSnapshot.toObject(Model.class);
                       obj.Itemid=b;
                       datalist.add(obj);
                       myadapter.notifyDataSetChanged();

                    }
                });

    }
}
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
    int qud=0;
    String adapter="0";
    String ProId;
    String length="empty";
    String itemid,productid;
    ArrayList<String> arlist=new ArrayList<>();
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
        regview.setBackgroundResource(R.drawable.emptyback);
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
                                String proqut=d.getString("pro_qut");
                                String qut=d.getString("qut");
                                getcarddata(qut,proqut,a);
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
                if(length.equals("zero")){
                    db.collection("cart").get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    List<DocumentSnapshot>list=queryDocumentSnapshots.getDocuments();
                                    for(DocumentSnapshot d:list){
                                        String Uid,qut,Status,itemidofcart,id,pro_qut,Proid;
                                        itemidofcart=d.getId();
                                        id=d.getString("Uid");
                                        Proid=d.getString("ProId");
                                        ProId=Proid;
                                        Uid=d.getString("Uid");
                                        qut=d.getString("qut");
                                        pro_qut=d.getString("pro_qut");
                                        Status="Pending";
                                        db.collection("order").get()
                                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                        List<DocumentSnapshot>l1=queryDocumentSnapshots.getDocuments();
                                                        for(DocumentSnapshot D:l1){
                                                            productid=D.getString("ProId");
                                                            if(productid.equals(ProId)){
                                                                qud=qud+1;
                                                                arlist.add(productid);
                                                            }
                                                        }
                                                        if(uid.equals(id)){
                                                            if(arlist.size()==0){
                                                                checkout(Proid,Uid,qut,Status,itemidofcart,pro_qut);
                                                                startActivity(new Intent(getActivity(),MainActivity.class));
                                                            }
                                                            else {
                                                                Toast.makeText(getActivity(), "SORRY...! Product is on rent...", Toast.LENGTH_SHORT).show();
                                                                dilog.dismiss();
                                                                db.collection("cart").document(itemidofcart).delete();
                                                                startActivity(new Intent(getActivity(),MainActivity.class));
                                                            }

                                                        }

                                                    }
                                                });


                                    }

                                }
                            });
                }
                else if (length.equals("empty")){
                    Toast.makeText(getActivity(), "Cart is Empty", Toast.LENGTH_SHORT).show();
                    dilog.dismiss();
                }

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
public void checkout(String ProId,String Uid,String  qut,String Status,String itemidofcart,String pro_qut){

    db.collection("Rent_Request").get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<DocumentSnapshot> list1=queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot b:list1){
                        String ReqUserID=b.getString("ReqUserID");
                        String ProId1=b.getString("ProId");
                        String stupro=b.getString("Status");
                            if(ProId1.equals(ProId)){
                                if(ReqUserID.equals(Uid)){
                                    if(stupro.equals("Accepted")||stupro.equals("Pending")){
                                        arrayList.add(b.getId());
                                    }
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
                        sa.put("pro_qut",pro_qut);
                        sa.put("Payment","pending");
                        db.collection("Rent_Request").add(sa)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Toast.makeText(getActivity(), "Products requested...", Toast.LENGTH_SHORT).show();
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

    public void getcarddata(String qut,String proqut,String c) {
        myadapter=new CartAdapter(datalist,getContext());
        regview.setAdapter(myadapter);
        db.collection("Product").document(a).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                       Model obj=documentSnapshot.toObject(Model.class);
                       obj.Itemid=c;
                       obj.qut=qut;
                       obj.proqut=proqut;
                       datalist.add(obj);
                       myadapter.notifyDataSetChanged();
                       adapter=String.valueOf(myadapter.datalist.size());
                       if(!adapter.equals("0")){
                           regview.setBackgroundResource(R.color.white);
                           length="zero";
                       }

                    }
                });

    }
}
package com.example.rento;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment {
    RecyclerView regview,regview1;
    requesteditemAdepter myadapter;
    AcceptedRequestAdapter myadapter1;
    ArrayList<Model> datalist;
    ArrayList<Model1>datalist1;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser user=auth.getCurrentUser();
    String uid= user.getUid();
    String status;
    String reid="0",re1id="0";
    String proid,requserid,a,b;
    String stu;
    ImageView back;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_menu, container, false);
        regview1=view.findViewById(R.id.itemrequest);
        regview=view.findViewById(R.id.requsteditem);
        back=view.findViewById(R.id.imageView3);
        regview.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        regview1.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        datalist=new ArrayList<>();
        datalist1=new ArrayList<>();
        if(reid.equals("0")){
            regview.setBackgroundResource(R.drawable.emptyback);
        }
        if(re1id.equals("0")){
            regview1.setBackgroundResource(R.drawable.emptyback);
        }

        db.collection("Rent_Request").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot d:list){
                    requserid=d.getString("ReqUserID");
                    String stu=d.getString("Status");
                    if(requserid.equals(uid)){
                        if(stu.equals("Pending")){
                            a=d.getString("ProId");
                            String proqut=d.getString("pro_qut");
                            String qut=d.getString("qut");
                            additem(a,proqut,qut);

                        }
                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Fail...!!!", Toast.LENGTH_SHORT).show();
            }
        });
        db.collection("Rent_Request").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list1=queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d:list1){
                            requserid=d.getString("ReqUserID");
                            if(requserid.equals(uid)){
                                stu=d.getString("Status");
                                if(stu.equals("Rejected")){
                                    b=d.getString("ProId");
                                    Accepteditem();

                                }
                            }
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


    private void additem(String a1 ,String proqut,String qut) {
        myadapter=new requesteditemAdepter(datalist);
        regview.setAdapter(myadapter);
        db.collection("Product").document(a1).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Model obj=documentSnapshot.toObject(Model.class);
                obj.proqut=proqut;
                obj.qut=qut;
                datalist.add(obj);
                myadapter.notifyDataSetChanged();
                reid=String.valueOf(datalist.size());
                if(!reid.equals("0")){
                    regview.setBackgroundResource(R.color.white);
                }

            }
        });

    }
    private void Accepteditem() {
        myadapter1=new AcceptedRequestAdapter(datalist1,getContext());
        regview1.setAdapter(myadapter1);
        db.collection("Product").document(b).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Model1 obj=documentSnapshot.toObject(Model1.class);
                        obj.pid=b;
                        obj.stutus="no";
                        datalist1.add(obj);
                        myadapter1.notifyDataSetChanged();
                         re1id=String.valueOf(myadapter1.datalist.size());
                         if(!re1id.equals("0")){
                             regview1.setBackgroundResource(R.color.white);
                         }
                    }
                });

    }

}
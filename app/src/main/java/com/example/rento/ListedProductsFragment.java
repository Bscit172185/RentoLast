package com.example.rento;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ListedProductsFragment extends Fragment {
    RecyclerView regview;
        ArrayList<Model> datalist;
    Myadapter myadapter;
    String id;
    public String categoryid;
    String  count;
    FirebaseFirestore db=FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_listed_products, container, false);
        regview=view.findViewById(R.id.Regview);
        regview.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        datalist=new ArrayList<>();
        myadapter=new Myadapter(datalist,getContext());
        regview.setAdapter(myadapter);
        getParentFragmentManager().setFragmentResultListener("categoryid", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                categoryid=result.getString("categoryid");
            }
        });

        db.collection("Product").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d:list){
                            System.out.println(d.getString("Categories"));
                            if(categoryid!=null){
                                if(categoryid==d.getString("Categories")){
                                    Model obj=d.toObject(Model.class);
                                    obj.id=d.getId();
                                    datalist.add(obj);
                                }
                            }
                            else {
                                Model obj=d.toObject(Model.class);
                                obj.id=d.getId();
                                datalist.add(obj);

                            }
                        }
                        myadapter.notifyDataSetChanged();
                    }

                });


        return view;
    }
}
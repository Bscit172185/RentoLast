package com.example.rento;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ListedProductsFragment extends Fragment {
    RecyclerView regview;
    FrameLayout layout;
    ArrayList<Model> datalist;
    Myadapter myadapter;
    String id;
    SearchView searchview;
    public String categoryid;
    String  count;
    FirebaseFirestore db=FirebaseFirestore.getInstance();

    @Override
    public void onStart() {
        categoryid="";
        getParentFragmentManager().setFragmentResultListener("fil", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                categoryid=result.getString("id");
            }
        });
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_listed_products, container, false);
        regview=view.findViewById(R.id.Regview);
        regview.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        datalist=new ArrayList<>();
        searchview=view.findViewById(R.id.searchView2);
        myadapter=new Myadapter(datalist,getContext());
        regview.setAdapter(myadapter);
        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                myadapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myadapter.getFilter().filter(newText);
                return false;
            }
        });
        db.collection("Product").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d:list){
                            if(!categoryid.equals("")){
                                if(categoryid.equals(d.getString("Categories"))){
                                    if(d.getString("pro_status").equals("ON")){
                                        Model obj=d.toObject(Model.class);
                                        obj.id=d.getId();
                                        datalist.add(obj);
                                    }
                                }
                            }
                            else {
                                if(d.getString("pro_status").equals("ON")){
                                Model obj=d.toObject(Model.class);
                                obj.id=d.getId();
                                datalist.add(obj);
                                }
                            }
                        }
                        myadapter.notifyDataSetChanged();
                        String size=String.valueOf(myadapter.datalist.size());
                        System.out.println("this is a size 0f data: "+size);
                        if(size.equals("0")){
                            regview.setBackgroundResource(R.drawable.emptyback1);
                        }
                    }
                });
        return view;
    }
}
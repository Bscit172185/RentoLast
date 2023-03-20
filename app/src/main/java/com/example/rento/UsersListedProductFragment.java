package com.example.rento;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class UsersListedProductFragment extends Fragment {
    RecyclerView regview;
    ArrayList<Model> datalist;
    UserListedAdapter myadapter;
    ImageView back;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FirebaseUser user=firebaseAuth.getCurrentUser();
    String id=user.getUid();
    String  re,pro_status;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getParentFragmentManager().setFragmentResultListener("Myadapter", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {

            }
        });
        View view= inflater.inflate(R.layout.fragment_users_listed_product, container, false);
        regview=view.findViewById(R.id.Regview1);
        back=view.findViewById(R.id.imageView2);
        regview.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        datalist=new ArrayList<>();
        myadapter=new UserListedAdapter(datalist,getContext());
        regview.setAdapter(myadapter);
        db.collection("Product").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d:list){
                            pro_status=d.getString("pro_status");
                            re=d.getString("UID");
                            if(pro_status.equals("ON")||pro_status.equals("DEACTIVE")){
                                if(re.equals(id)){
                                    Model obj=d.toObject(Model.class);
                                    obj.id=d.getId();
                                    datalist.add(obj);
                                }
                            }
                        }
                        myadapter.notifyDataSetChanged();
                        String size=String.valueOf(myadapter.datalist.size());
                        if(size.equals("0")){
                            regview.setBackgroundResource(R.drawable.emptyback1);
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
}
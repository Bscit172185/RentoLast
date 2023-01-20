package com.example.rento;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Document;


public class HomeFragment extends Fragment {
    String A;
    Uri url;
    CardView cardView;
    LinearLayout layout;
    ImageView img;
    FrameLayout Flayout;
    FirebaseFirestore Root=FirebaseFirestore.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser user=auth.getCurrentUser();
    String UID=user.getUid();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Flayout=getActivity().findViewById(R.id.layout);
        FirebaseFirestore firestore= FirebaseFirestore.getInstance();
        DocumentReference reference=firestore.collection("user").document(UID);
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                A=documentSnapshot.getString("ImgUrl");
                url=Uri.parse(A);
                Picasso.get().load(url).into(img);
            }
        });
        View view= inflater.inflate(R.layout.fragment_home, container, false);
        FragmentManager fragmentManager=getChildFragmentManager();
        FragmentTransaction ft =fragmentManager.beginTransaction();
        ft.add(R.id.layout,new ListedProductsFragment());
        ft.commit();
        img=view.findViewById(R.id.imageView2);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(getActivity(),Login.class));
                getActivity().finish();
            }
        });
        return view;
    }

    }
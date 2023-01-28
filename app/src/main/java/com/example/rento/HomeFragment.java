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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Document;


public class HomeFragment extends Fragment {
    String A;
    Uri url,banner;
    CardView cardView;
    LinearLayout layout;
    ImageView img,poster;
    FrameLayout Flayout;
    FirebaseFirestore Root=FirebaseFirestore.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser user=auth.getCurrentUser();
    String UID=user.getUid();

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user=auth.getCurrentUser();
        if(user==null){
            getActivity().finish();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Flayout=getActivity().findViewById(R.id.layout);

        View view= inflater.inflate(R.layout.fragment_home, container, false);
        FragmentManager fragmentManager=getChildFragmentManager();
        FragmentTransaction ft =fragmentManager.beginTransaction();
        ft.add(R.id.layout,new ListedProductsFragment());
        ft.commit();
        poster=view.findViewById(R.id.imageView14);
        FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();
        StorageReference ref=firebaseStorage.getReference("banner.jpg");
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                banner=uri;
                Picasso.get().load(banner).into(poster);
            }
        });


        img=view.findViewById(R.id.imageView12);
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
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),Profile_details_Activity.class);
                intent.putExtra("url",A);
                intent.putExtra("uid",UID);
                startActivity(intent);

            }
        });
        return view;
    }

    }
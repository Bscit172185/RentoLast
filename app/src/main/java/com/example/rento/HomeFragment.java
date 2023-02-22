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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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
    CardView fu,el,ke,cl,bk,im,vc;
    ImageButton cls;
    LinearLayout layout;
    ImageView img,poster;
    FrameLayout Flayout;
    String categoryid;
    int  count=0;
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
        fu=view.findViewById(R.id.cardview1);
        el=view.findViewById(R.id.cardview2);
        ke=view.findViewById(R.id.cardview3);
        cl=view.findViewById(R.id.cardview4);
        bk=view.findViewById(R.id.cardview5);
        im=view.findViewById(R.id.cardview6);
        vc=view.findViewById(R.id.cardview7);
        cls=view.findViewById(R.id.filtercls);
        FragmentManager fragmentManager=getChildFragmentManager();
        FragmentTransaction ft =fragmentManager.beginTransaction();
        ListedProductsFragment lpfr =new ListedProductsFragment();
        ft.add(R.id.layout,lpfr);
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
                if(!A.isEmpty()){
                    url=Uri.parse(A);
                    Picasso.get().load(url).into(img);
                }

            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),Profile_details_Activity.class);
                if(!A.isEmpty()){
                    intent.putExtra("url",A);
                }
                else {
                    intent.putExtra("url","noko");
                }
                intent.putExtra("uid",UID);
                startActivity(intent);

            }
        });
        fu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count!=1){
                    categoryid="Furniture";
                    Bundle b=new Bundle();
                    b.putString("id",categoryid);
                    ListedProductsFragment lpf=new ListedProductsFragment();
                    lpf.setArguments(b);
                    getParentFragmentManager().setFragmentResult("fil",b);
                    FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.layout,lpf).commit();
                    Toast.makeText(getActivity(), ""+categoryid, Toast.LENGTH_SHORT).show();
                    count=1;
                }
                else {
                    getActivity().recreate();
                }

            }
        });
        el.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count!=1){
                    categoryid="Electronics";
                    Bundle b=new Bundle();
                    b.putString("id",categoryid);
                    ListedProductsFragment lpf=new ListedProductsFragment();
                    lpf.setArguments(b);
                    getParentFragmentManager().setFragmentResult("fil",b);
                    FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.layout,lpf).commit();
                    Toast.makeText(getActivity(), ""+categoryid, Toast.LENGTH_SHORT).show();
                    count=1;
                }
                else {
                    getActivity().recreate();
                    lpfr.getActivity().recreate();

                }
            }
        });
        ke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count!=1){
                    categoryid="Electronics";
                    Bundle b=new Bundle();
                    b.putString("id",categoryid);
                    ListedProductsFragment lpf=new ListedProductsFragment();
                    lpf.setArguments(b);
                    getParentFragmentManager().setFragmentResult("fil",b);
                    FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                    ft.add(R.id.layout,lpf).commit();
                    Toast.makeText(getActivity(), ""+categoryid, Toast.LENGTH_SHORT).show();
                    count=1;
                }
                else {
                    getActivity().recreate();

                }

            }
        });
        cls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                categoryid="";
                Toast.makeText(getActivity(), "Filter removed", Toast.LENGTH_SHORT).show();
                Bundle b=new Bundle();
                b.putString("id",categoryid);
                ListedProductsFragment lpf=new ListedProductsFragment();
                lpf.setArguments(b);
                getParentFragmentManager().setFragmentResult("fil",b);
                FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                ft.add(R.id.layout,lpf).commit();
            }
        });

        return view;
    }

    }
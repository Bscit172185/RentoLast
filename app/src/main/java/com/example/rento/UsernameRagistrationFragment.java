package com.example.rento;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


public class UsernameRagistrationFragment extends Fragment {
    EditText Name,Email;
    Button Next;
    FloatingActionButton imgget;
    ShapeableImageView viewimg;
    Uri IMGURI;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser user=auth.getCurrentUser();
    String url;
    FirebaseFirestore db=FirebaseFirestore.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_username_ragistration, container, false);
        Name=view.findViewById(R.id.editText22);
        Email=view.findViewById(R.id.editText23);
        viewimg=view.findViewById(R.id.shapeableImageView);
        int a=0;
        imgget=view.findViewById(R.id.floatingActionButton);
        if(user!=null){
            String uid=user.getUid();
            Email.setVisibility(view.INVISIBLE);
            db.collection("user").document(uid).get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Name.setText(documentSnapshot.getString("Name"));
                            Email.setText(documentSnapshot.getString("Email"));
                            url=documentSnapshot.getString("ImgUrl");
                            IMGURI=Uri.parse(url);
                            Picasso.get().load(IMGURI).into(viewimg);
                        }
                    });


        }
        imgget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(UsernameRagistrationFragment.this)
                        .crop()
                        .maxResultSize(1080,1080)
                        .start(1001);

            }
        });
        Next=view.findViewById(R.id.button4);
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=Name.getText().toString();
                String email=Email.getText().toString();
                if(!name.isEmpty()){
                    if(!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        Bundle bundle= new Bundle();
                        bundle.putString("name",name);
                        bundle.putString("email",email);
                        bundle.putString("imgurl",IMGURI.toString());
                        SetPasswordForRegistrationFragment sender = new SetPasswordForRegistrationFragment();
                        sender.setArguments(bundle);
                        getParentFragmentManager().setFragmentResult("userpage",bundle);
                        Fragment fragment= new MobileVerifyFragment();
                        FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.ragistraionFreg,fragment).commit();
                    }
                    else {
                        Email.setError("Enter Valid Email");
                    }
                }
                else {
                    Name.setError("Empty field not allowed");
                }

            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1001){
            IMGURI=data.getData();
            System.out.println(IMGURI);
            viewimg.setImageURI(data.getData());

        }
        else {

        }

    }
}
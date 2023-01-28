package com.example.rento;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;


public class SetPasswordForRegistrationFragment extends Fragment {
    private final FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser user=auth.getCurrentUser();
    String id=user.getUid();
    EditText Pass,ConPass;
    Button Register;
    Uri a;
    Uri b;
    public String name,email,number,pass,cpass,imgurl,SimgUrl,Address,Latitude,Longitude,postalcose;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_set_password_for_registration, container, false);
        Pass=view.findViewById(R.id.editText4);
        ConPass=view.findViewById(R.id.editText5);
        Register=view.findViewById(R.id.register);
        getParentFragmentManager().setFragmentResultListener("userpage", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                name=result.getString("name");
                email=result.getString("email");
                imgurl=result.getString("imgurl");

            }
        });
        getParentFragmentManager().setFragmentResultListener("Mobileverify", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                number=result.getString("mnum");
            }
        });
        getParentFragmentManager().setFragmentResultListener("AddressDetails", this, new FragmentResultListener() {
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                Address=result.getString("Address");
                Latitude=result.getString("Latitude");
                Longitude=result.getString("Longitude");
                postalcose=result.getString("postalcode");


            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pass=Pass.getText().toString();
                cpass=ConPass.getText().toString();
                if(!pass.isEmpty() && !cpass.isEmpty()){
                    if(pass.equals(cpass)){
                        sendtoFirebase();


                    }
                    else {
                        ConPass.setError("Password is not matched");
                    }
                }
                else {
                    Pass.setError("Empty field not allowed");
                    ConPass.setError("Empty field not allowed");
                }
            }
        });

        return view;
    }
    public void sendtoFirebase(){
        a=Uri.parse(imgurl);
        String ImgID=email;
        FirebaseStorage storage=FirebaseStorage.getInstance();
        StorageReference upload=storage.getReference("Profile/"+ImgID);
        upload.putFile(a).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                upload.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        SimgUrl=uri.toString();
                        auth.createUserWithEmailAndPassword(email,pass)
                                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()){
                                            HashMap<String, Object> m= new HashMap<String,Object>();
                                            m.put("Email",email);
                                            m.put("ImgUrl",SimgUrl);
                                            m.put("Name",name);
                                            m.put("Number",number);
                                            m.put("Address",Address);
                                            m.put("Latitude",Latitude);
                                            m.put("Longitude",Longitude);
                                            m.put("PostalCode",postalcose);
                                            FirebaseAuth auth=FirebaseAuth.getInstance();
                                            FirebaseUser user=auth.getCurrentUser();
                                            String UID=user.getUid();
                                            FirebaseFirestore root=FirebaseFirestore.getInstance();
                                            root.collection("user").document(UID).set(m).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(getActivity(),"successful!!",Toast.LENGTH_SHORT).show();
                                                        Intent intent=new Intent(getActivity(),Login.class);
                                                        startActivity(intent);
                                                    }

                                                }
                                            });
                                        }
                                        else {
                                            Toast.makeText(getActivity(),"Failde"+task.getException(),Toast.LENGTH_LONG).show();
                                        }
                                    }

                                });

                    }
                });
            }
        });

    }

}
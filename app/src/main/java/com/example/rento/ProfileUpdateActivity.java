package com.example.rento;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class ProfileUpdateActivity extends AppCompatActivity {
    EditText editname,editphone;
    FloatingActionButton imgget;
    ShapeableImageView viewimg;
    Button editprofile;
    ImageView imageView1;
    Uri IMGURI;
    String Email;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser user=auth.getCurrentUser();
    String UID=user.getUid();
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseStorage storage=FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);
        editprofile=findViewById(R.id.editprofile);
        editname=findViewById(R.id.editname);
        editphone=findViewById(R.id.editphone);
        viewimg=findViewById(R.id.shapeableImageView);
        imgget=findViewById(R.id.floatingActionButton);
        imageView1=findViewById(R.id.imageView1);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileUpdateActivity.this,MainActivity.class));
                finish();
            }
        });
        db.collection("user").document(UID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Email=documentSnapshot.getString("Email");
                    }
                });
        imgget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(ProfileUpdateActivity.this)
                        .crop()
                        .maxResultSize(1080,1080)
                        .start(1001);

            }
        });
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Customprogressbar bar=new Customprogressbar(ProfileUpdateActivity.this);
                bar.show();
                String name,mobile,prourl;
                name=editname.getText().toString();
                mobile=editphone.getText().toString();
                prourl=String.valueOf(IMGURI);
                if(!name.isEmpty()){
                    if(!mobile.isEmpty()&&mobile.length()==10){
                        if(!prourl.equals(null)){
                            StorageReference upload=storage.getReference("Profile/"+Email);
                            Uri a=Uri.parse(prourl);
                            upload.putFile(a).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    upload.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String b=uri.toString();
                                            System.out.println(b);
                                            HashMap<String,Object>s=new HashMap<String, Object>();
                                            s.put("Name",name);
                                            s.put("Number",mobile);
                                            s.put("ImgUrl",b);
                                            db.collection("user").document(UID).update(s)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(ProfileUpdateActivity.this, "Profile Update Successfully.....!", Toast.LENGTH_SHORT).show();
                                                            startActivity(new Intent(ProfileUpdateActivity.this,MainActivity.class));
                                                            bar.dismiss();
                                                        }
                                                    });
                                        }
                                    }) ;

                                }
                            });
                        }
                        else {
                            HashMap<String,Object>s=new HashMap<String, Object>();
                            s.put("Name",name);
                            s.put("Number",mobile);
                            db.collection("user").document(UID).update(s)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(ProfileUpdateActivity.this, "Profile Update Successfully.....!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(ProfileUpdateActivity.this,MainActivity.class));
                                            bar.dismiss();
                                        }
                                    });
                        }
                    }
                    else {
                        editphone.setError("Empty fields not allowed...");
                    }
                }
                else {
                    editname.setError("Empty fields not allowed...");
                }
            }
        });

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
            Toast.makeText(ProfileUpdateActivity.this, "error...", Toast.LENGTH_SHORT).show();
            IMGURI=null;
        }

    }
}
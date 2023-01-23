package com.example.rento;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Random;


public class Add_Product_Activity extends AppCompatActivity {
    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    FirebaseUser user=firebaseAuth.getCurrentUser();
    String ID=user.getUid();
    ImageButton imageButton,back;
    Button addpro;
    String productUrl;
    Uri URL;
    EditText PName,Pdecrip,PPrice,PBrok;
    ShapeableImageView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        back=findViewById(R.id.back);
        PName=findViewById(R.id.editText3);
        Pdecrip=findViewById(R.id.editText6);
        PPrice=findViewById(R.id.editPrice);
        PBrok=findViewById(R.id.editBrokerageAmt);
        addpro=findViewById(R.id.Addpro);
        img=findViewById(R.id.imageView13);
        imageButton=findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(Add_Product_Activity.this)
                        .crop()
                        .maxResultSize(1080,1080)
                        .start(101);

            }
        });
        addpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String PName1,Pdecrip1,PPrice1,PBrok1,URL1;
                PName1=PName.getText().toString();
                Pdecrip1=Pdecrip.getText().toString();
                PPrice1=PPrice.getText().toString();
                PBrok1=PBrok.getText().toString();
                HashMap<String, Object>pro=new HashMap<String,Object>();
                pro.put("Product_Name",PName1);
                pro.put("Product_Descreiption",Pdecrip1);
                pro.put("Product_Price",PPrice1);
                pro.put("Product_brocrage",PBrok1);
                pro.put("Product_ImgUrl",productUrl);
                FirebaseFirestore root=FirebaseFirestore.getInstance();
                root.collection("Product").document(ID).collection(new Random().nextInt(500)+ID).document().set(pro)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Add_Product_Activity.this,"successful!!",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Add_Product_Activity.this,Select_Add_Product_Activity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==101)
        {
            URL=data.getData();
            System.out.println(URL);
            img.setImageURI(data.getData());
            FirebaseStorage storage=FirebaseStorage.getInstance();
            StorageReference upload=storage.getReference("/Product/"+ID+new Random().nextInt(500));
            upload.putFile(URL).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(Add_Product_Activity.this, "Image Uploded", Toast.LENGTH_SHORT).show();
                  upload.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                      @Override
                      public void onSuccess(Uri uri) {
                          productUrl=uri.toString();
                          System.out.println(productUrl);
                      }
                  });
                }

            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    Toast.makeText(Add_Product_Activity.this, "Please Wait", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
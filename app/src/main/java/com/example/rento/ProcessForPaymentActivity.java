package com.example.rento;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.app.Dialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class ProcessForPaymentActivity extends AppCompatActivity implements PaymentResultListener {
    ShapeableImageView imgview;
    TextView pname,pprice,pbroc,pdes,paddr;
    Button payment,nevi;
    ImageView mapView;
    String accno="12345678912457";
    Activity activity=this;
    String pid="",qut="",reqid;
    int pro_qut;
    int finl;
    String img,name,dec,uid,price,broc,Addr,Uname,Uemail,Uphone,latitude,longitute,ulati,ulongi,prouid;
    Uri uri;
    int code=100;
    PdfDocument document;
    int a,b,c,d,total,intpr,intbro,bro;
    String finalamount,PaymentStu,tot;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseAuth auth=FirebaseAuth.getInstance();
    FirebaseUser user=auth.getCurrentUser();
    String userid=user.getUid();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_for_payment);
        pname=findViewById(R.id.text);
        pprice=findViewById(R.id.text3);
        pbroc=findViewById(R.id.text5);
        pdes=findViewById(R.id.text1);
        paddr=findViewById(R.id.text2);
        mapView=findViewById(R.id.MapView);
        payment=findViewById(R.id.button1);
        nevi=findViewById(R.id.navi);
        imgview=findViewById(R.id.img);
        Checkout.preload(getApplicationContext());
        Intent intent=getIntent();
        PaymentStu=intent.getStringExtra("paystus");
        reqid=intent.getStringExtra("reqid");
        pid=intent.getStringExtra("pid");
         db.collection("Product").document(pid).get()
                         .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                             @Override
                             public void onSuccess(DocumentSnapshot documentSnapshot) {
                                 img=documentSnapshot.getString("Product_ImgUrl");
                                 name=documentSnapshot.getString("Product_Name");
                                 uid=documentSnapshot.getString("UID");
                                 price=documentSnapshot.getString("Product_Price");
                                 broc=documentSnapshot.getString("Product_brocrage");
                                 dec=documentSnapshot.getString("Product_Descreiption");
                                 prouid=documentSnapshot.getString("UID");
                                 uri=Uri.parse(img);
                                 Picasso.get().load(uri).into(imgview);
                                 pname.setText(name);
                                 pdes.setText(dec);
                                 pprice.setText(price);
                                 pbroc.setText(broc);
                                 intpr=Integer.parseInt(price);
                                 intbro=Integer.parseInt(broc);
                                 total=intpr;
                                 bro=intbro;
                                 db.collection("user").document(uid).get()
                                         .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                             @Override
                                             public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                 Addr=documentSnapshot.getString("Address");
                                                 paddr.setText(Addr);
                                                 ulati=documentSnapshot.getString("Latitude");
                                                 ulongi=documentSnapshot.getString("Longitude");
                                             }
                                         });
                             }
                         });
         db.collection("user").document(userid).get()
                         .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                             @Override
                             public void onSuccess(DocumentSnapshot documentSnapshot) {
                                 Uname=documentSnapshot.getString("Name");
                                 Uemail=documentSnapshot.getString("Email");
                                 Uphone=documentSnapshot.getString("Number");
                                 latitude=documentSnapshot.getString("Latitude");
                                 longitute=documentSnapshot.getString("Longitude");

                             }
                         });
        db.collection("Rent_Request").document(reqid).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        pro_qut=Integer.parseInt(documentSnapshot.getString("pro_qut"));
                        int qut2=Integer.parseInt(documentSnapshot.getString("qut"));
                        finl=(total*qut2*pro_qut)+bro;
                        if(PaymentStu.equals("PAID")){
                            nevi.setVisibility(View.INVISIBLE);
                            payment.setText("Download Invoice");
                            payment.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Dialog da=new Dialog(ProcessForPaymentActivity.this);
                                    da.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    da.setContentView(R.layout.invoice);
                                    da.setCancelable(true);
                                    WindowManager.LayoutParams lp= new WindowManager.LayoutParams();
                                    lp.copyFrom(da.getWindow().getAttributes());
                                    lp.width=WindowManager.LayoutParams.MATCH_PARENT;
                                    lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
                                    da.getWindow().setAttributes(lp);
                                    Button downloadf=da.findViewById(R.id.download);
                                    TextView pname,pmou,pbroc,nom,qut1,tamount,paystu;
                                    pname=da.findViewById(R.id.pname);
                                    pmou=da.findViewById(R.id.pmou);
                                    pbroc=da.findViewById(R.id.pbroc);
                                    nom=da.findViewById(R.id.nom);
                                    qut1=da.findViewById(R.id.qut);
                                    tamount=da.findViewById(R.id.tamount);
                                    paystu=da.findViewById(R.id.paystu);
                                    pname.setText(name);
                                    pmou.setText(price);
                                    pbroc.setText(broc);
                                    nom.setText(String.valueOf(qut2));
                                    qut1.setText(String.valueOf(pro_qut));
                                    tamount.setText(String.valueOf(finl));
                                    paystu.setText(PaymentStu);
                                    da.show();

                                    downloadf.setOnClickListener(view1 -> {
                                        makepdffromview(da.findViewById(R.id.invoiceid));
                                    });
                                }
                            });
                        }
                    }
                });
        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent map=new Intent(ProcessForPaymentActivity.this,AddressMapsActivity.class);
                map.putExtra("ulati",ulati);
                map.putExtra("ulogi",ulongi);
                startActivity(map);
                finish();
            }
        });
        nevi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a="google.navigation:q="+ulati+","+ulongi+"&mode=1";
                Intent ltm=new Intent(Intent.ACTION_VIEW,Uri.parse(a));
                ltm.setPackage("com.google.android.apps.maps");
                startActivity(ltm);
            }
        });
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a=Integer.parseInt(price);
                b=Integer.parseInt(broc);
                d=finl*100;
                finalamount=String.valueOf(d);
                AlertDialog.Builder alert=new AlertDialog.Builder(ProcessForPaymentActivity.this);
                alert.setTitle("D E C L A R A T I O N");
                alert.setIcon(R.drawable.applogo);
                alert.setMessage("Get product first and"+
                        "\nEnsure quality,"+
                        "\nand then proceed for payment.");
                alert.setPositiveButton("Yes, I've Read", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startpayment(finalamount,Uname,Uemail,Uphone);
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert.show();

            }
        });
    }

    private void requestforper() {
        ActivityCompat.requestPermissions(this,new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE},100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean checkPermission() {
        int permition1=ContextCompat.checkSelfPermission(getApplicationContext(),WRITE_EXTERNAL_STORAGE);
        int permition2=ContextCompat.checkSelfPermission(getApplicationContext(),READ_EXTERNAL_STORAGE);
        return permition1== PackageManager.PERMISSION_GRANTED && permition2==PackageManager.PERMISSION_GRANTED;
    }

    private void makepdffromview(View view) {
        Bitmap bitmap= getBitmap(view);
        document=new PdfDocument();
        PdfDocument.PageInfo pageInfo=new PdfDocument.PageInfo.Builder(view.getWidth(),view.getHeight() ,1).create();
        PdfDocument.Page page=document.startPage(pageInfo);
        Canvas canvas=page.getCanvas();
        canvas.drawBitmap(bitmap,0,0,null);
        document.finishPage(page);
        createpdf();

    }

    private Bitmap getBitmap(View view) {
        Bitmap map=Bitmap.createBitmap(view.getWidth(),view.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(map);
        Drawable bgdraw=view.getBackground();
        if(bgdraw !=null){
            bgdraw.draw(canvas);
        }
        else {
            canvas.drawColor(Color.WHITE);
        }
        view.draw(canvas);
        return map;
    }

    private void createpdf() {
        Intent intent=new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_TITLE,"invoice.pdf");
        startActivityForResult(intent,code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==code){
            Uri uri1=null;
            if(data !=null){
                uri1=data.getData();
                if(document !=null){
                    ParcelFileDescriptor pdf=null;
                    try{
                        pdf= getContentResolver().openFileDescriptor(uri1,"w");
                        FileOutputStream fileOutputStream=new FileOutputStream(pdf.getFileDescriptor());
                        document.writeTo(fileOutputStream);
                        document.close();
                        Toast.makeText(this, "Pdf saved...", Toast.LENGTH_SHORT).show();
                    }
                    catch (IOException e){
                        try {
                            DocumentsContract.deleteDocument(getContentResolver(),uri1);
                        }catch (FileNotFoundException ex){
                            Toast.makeText(this, "error"+ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(this, "error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

        }

    }

    private void startpayment(String amu, String name, String email, String mob) {

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_RYhbssfekFowZO");
        checkout.setImage(R.drawable.logoicon);
        try {
            JSONObject options = new JSONObject();
            options.put("name",name);
            options.put("description", " "+accno+"   UID: "+userid);
            options.put("image", "https://firebasestorage.googleapis.com/v0/b/" +
                    "rento-f1b52.appspot.com/o/banner.jpg?alt=media&token=ade5458d-75fa-4e71-9d44-3f434608c20b");
            options.put("theme.color", "#3399CC");
            options.put("currency", "INR");
            options.put("amount",amu);
            options.put("prefill.email",email);
            options.put("prefill.contact",mob);
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);
            checkout.open(activity, options);

        } catch(Exception e) {
            Toast.makeText(activity, "error"+e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }

    }

    @Override
    public void onPaymentSuccess(String s) {
        db.collection("Rent_Request").document(reqid).update("Payment","PAID");
        Toast.makeText(this, "Success....", Toast.LENGTH_SHORT).show();
        HashMap<String,Object> sa=new HashMap<String, Object>();
        sa.put("proUID",prouid);
        sa.put("ProId",pid);
        sa.put("orderUserID",userid);
        int a=Integer.parseInt(finalamount)/100;
        sa.put("price",String.valueOf(a));
        sa.put("bro",broc);
        db.collection("order").add(sa)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(activity, "Order Confirmed.....!", Toast.LENGTH_SHORT).show();
                HashMap<String,Object> s=new HashMap<String, Object>();
                s.put("pro_status","DEACTIVE");
                db.collection("Product").document(pid).update(s);
                startActivity(new Intent(ProcessForPaymentActivity.this,OrderActivity.class));
            }
        });
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "failed...", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ProcessForPaymentActivity.this,OrderActivity.class));
    }
}
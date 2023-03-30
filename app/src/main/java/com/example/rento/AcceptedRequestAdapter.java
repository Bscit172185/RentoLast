package com.example.rento;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AcceptedRequestAdapter extends RecyclerView.Adapter<AcceptedRequestAdapter.myviewholder> {
    ArrayList<Model1> datalist;
    Context context;
    String status;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    int tot;
    public AcceptedRequestAdapter(ArrayList<Model1> datalist, Context context) {
        this.datalist= datalist;
        this.context=context;

    }
    @NonNull
    @Override
    public AcceptedRequestAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_cardview_layout,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AcceptedRequestAdapter.myviewholder holder, int position) {
        String a,paystu,reqid;
        String name;
        String price;
        String pid;
        String bro;
        String proqut;
        String qut;
        Uri uri1;
        reqid=datalist.get(position).reqid;
        paystu=datalist.get(position).paystu;
        proqut=datalist.get(position).proqut;
        a=datalist.get(position).getProduct_ImgUrl();
        name=datalist.get(position).getProduct_Name();
        price=datalist.get(position).getProduct_Price();
        qut=datalist.get(position).qut;
        bro=datalist.get(position).getProduct_brocrage();
        status=datalist.get(position).stutus;
        pid=datalist.get(position).pid;
        System.out.println(name);
        uri1=Uri.parse(a);
        Picasso.get().load(uri1).into(holder.img);
        holder.t1.setText(name);
        holder.t2.setText(price);
        holder.qut.setText("Qut  "+proqut);
        holder.t3.setText("Brokerage: "+bro);
        tot=Integer.parseInt(price)*Integer.parseInt(proqut)*Integer.parseInt(qut)+Integer.parseInt(bro);
        holder.t4.setText(" Total: "+String.valueOf(tot));
        holder.mon.setText("Mon: "+qut);
        holder.t5.setText("Payment: "+paystu);
        if(paystu.equals("PAID")){
            holder.t5.setBackgroundResource(R.color.teal_200);
        }

        if(status=="yes"){
            holder.img.setOnClickListener(new View.OnClickListener() {
                String a;
                @Override
                public void onClick(View view) {
                    db.collection("Rent_Request").document(pid).get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    Intent intent=new Intent(context,ProcessForPaymentActivity.class);
                                    intent.putExtra("pid",pid);
                                    intent.putExtra("reqid",reqid);
                                    intent.putExtra("paystus",paystu);
                                    intent.putExtra("totalamount",String.valueOf(tot));
                                    context.startActivity(intent);

                                }
                            });

                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        TextView t1,t2,t3,t4,t5,qut,mon;
        ShapeableImageView img;
        CardView card;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            t1=itemView.findViewById(R.id.name);
            t2=itemView.findViewById(R.id.price);
            t3=itemView.findViewById(R.id.brokerage);
            t4=itemView.findViewById(R.id.totalamt);
            t5=itemView.findViewById(R.id.paymentstu);
            qut=itemView.findViewById(R.id.qty);
            mon=itemView.findViewById(R.id.mon);
            img=itemView.findViewById(R.id.img);
            card=itemView.findViewById(R.id.crd);
        }
    }
}

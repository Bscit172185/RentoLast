package com.example.rento;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AcceptedRequestAdapter extends RecyclerView.Adapter<AcceptedRequestAdapter.myviewholder> {
    ArrayList<Model1> datalist;
    Context context;
    String status;
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
        String a ,name,price,pid,bro;
        Uri uri1;
        a=datalist.get(position).getProduct_ImgUrl();
        name=datalist.get(position).getProduct_Name();
        price=datalist.get(position).getProduct_Price();
        bro=datalist.get(position).getProduct_brocrage();
        status=datalist.get(position).stutus;
        pid=datalist.get(position).pid;
        System.out.println(name);
        uri1=Uri.parse(a);
        Picasso.get().load(uri1).into(holder.img);
        holder.t1.setText(name);
        holder.t2.setText(price);
        holder.t3.setText(bro+" borcrage");
        tot=Integer.parseInt(price)+Integer.parseInt(bro);
        holder.t4.setText(" TotalAmount: "+String.valueOf(tot)+" ");
        if(status=="yes"){
            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(context,ProcessForPaymentActivity.class);
                    intent.putExtra("pid",pid);
                    context.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    public class myviewholder extends RecyclerView.ViewHolder {
        TextView t1,t2,t3,t4;
        ShapeableImageView img;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            t1=itemView.findViewById(R.id.name);
            t2=itemView.findViewById(R.id.price);
            t3=itemView.findViewById(R.id.brokerage);
            t4=itemView.findViewById(R.id.totalamt);
            img=itemView.findViewById(R.id.img);
        }
    }
}

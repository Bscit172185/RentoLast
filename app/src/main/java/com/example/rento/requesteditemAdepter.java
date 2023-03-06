package com.example.rento;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class requesteditemAdepter extends RecyclerView.Adapter<requesteditemAdepter.myviewholder> {
    ArrayList<Model> datalist;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    int count;
    public requesteditemAdepter(ArrayList<Model> datalist) {
        this.datalist= datalist;

    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_cardview_layout,parent,false);
        return new myviewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        String a ,name,price,brok;
        Uri uri1;
        a=datalist.get(position).getProduct_ImgUrl();
        name=datalist.get(position).getProduct_Name();
        price=datalist.get(position).getProduct_Price();
        brok=datalist.get(position).getProduct_brocrage();
        System.out.println(name);
        uri1=Uri.parse(a);
        Picasso.get().load(uri1).into(holder.img);
        holder.t1.setText(name);
        holder.t2.setText(price);
        holder.t3.setText(brok+" /mon");
    }

    @Override
    public int getItemCount() {
        count=datalist.size();
        return count;

    }

    class myviewholder extends RecyclerView.ViewHolder {
        CardView crd;
        TextView t1,t2,t3,t4,t5;
        ShapeableImageView img;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            t1=itemView.findViewById(R.id.name);
            t2=itemView.findViewById(R.id.price);
            img=itemView.findViewById(R.id.img);
            crd=itemView.findViewById(R.id.crd);
            t3=itemView.findViewById(R.id.brokerage);
            t4=itemView.findViewById(R.id.totalamt);
            t5=itemView.findViewById(R.id.qty);

        }
    }
}

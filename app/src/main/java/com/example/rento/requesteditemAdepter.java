package com.example.rento;

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

public class requesteditemAdepter extends RecyclerView.Adapter<requesteditemAdepter.myviewholder> {
    ArrayList<Model> datalist;
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
        String a ,name,price;
        Uri uri1;
        a=datalist.get(position).getProduct_ImgUrl();
        name=datalist.get(position).getProduct_Name();
        price=datalist.get(position).getProduct_Price();
        System.out.println(name);
        uri1=Uri.parse(a);
        Picasso.get().load(uri1).into(holder.img);
        holder.t1.setText(name);
        holder.t2.setText(price);
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class myviewholder extends RecyclerView.ViewHolder {
        TextView t1,t2;
        ShapeableImageView img;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            t1=itemView.findViewById(R.id.name);
            t2=itemView.findViewById(R.id.price);
            img=itemView.findViewById(R.id.img);
        }
    }
}

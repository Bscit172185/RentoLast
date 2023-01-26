package com.example.rento;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Myadapter extends RecyclerView.Adapter<Myadapter.myviewholder>{
    Context context;
    String pname,pprice,img;
    ArrayList<Model> datalist;

    public Myadapter(ArrayList<Model> datalist) {
        this.datalist = datalist;
    }




    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_addproduct_cardview_layout,parent,false);
       return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {

        Uri uri1;
        pname=datalist.get(position).getProduct_Name();
        pprice=datalist.get(position).getProduct_Price();
        img=datalist.get(position).getProduct_ImgUrl();
        Bundle bundle=new Bundle();
        bundle.putString("name",pname);
        bundle.putString("pprice",pprice);
        bundle.putString("url",img);
        UsersListedProductFragment freg=new UsersListedProductFragment();
        freg.setArguments(bundle);
        uri1=Uri.parse(img);
        Picasso.get().load(uri1).into(holder.img);
        holder.t1.setText(pname);
        holder.t2.setText(pprice);

    }


    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class myviewholder extends RecyclerView.ViewHolder{
        TextView t1,t2;
        ImageView img;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            t1=itemView.findViewById(R.id.name);
            t2=itemView.findViewById(R.id.price);
            img=itemView.findViewById(R.id.img);

        }
    }
}

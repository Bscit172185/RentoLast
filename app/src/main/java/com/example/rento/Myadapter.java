package com.example.rento;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Myadapter extends RecyclerView.Adapter<Myadapter.myviewholder>{
    Context context;
    ArrayList<Model> datalist;

    public Myadapter(ArrayList<Model> datalist,Context context) {
        this.datalist = datalist;
        this.context=context;
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
        String pname,pprice,imgurl,brorate;
        pname=datalist.get(position).getProduct_Name();
        pprice=datalist.get(position).getProduct_Price();
        imgurl=datalist.get(position).getProduct_ImgUrl();
        brorate=datalist.get(position).getProduct_brocrage();
        uri1=Uri.parse(imgurl);
        Picasso.get().load(uri1).into(holder.img);
        holder.t1.setText(pname);
        holder.t2.setText(pprice);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,Item_details_of_recycleview.class);
                intent.putExtra("name",pname);
                intent.putExtra("price",pprice);
                intent.putExtra("imgurl",imgurl);
                intent.putExtra("brorate",brorate);
                context.startActivity(intent);

            }
        });

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

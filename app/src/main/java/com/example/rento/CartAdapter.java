package com.example.rento;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.myviewholder> {
    Context context;
    ArrayList<Model> datalist;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    public CartAdapter(ArrayList<Model> datalist ,Context context) {
        this.datalist= datalist;
        this.context=context;

    }

    @NonNull
    @Override
    public CartAdapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_cardview_layout,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.myviewholder holder, @SuppressLint("RecyclerView") int position) {
        String a ,name,price,b;
        Uri uri1;
        a=datalist.get(position).getProduct_ImgUrl();
        name=datalist.get(position).getProduct_Name();
        price=datalist.get(position).getProduct_Price();
        uri1=Uri.parse(a);
        Picasso.get().load(uri1).into(holder.img);
        holder.t1.setText(name);
        holder.t2.setText(price);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ItemDetailsOfAddToCartActivity.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }
    class myviewholder extends RecyclerView.ViewHolder {
        TextView t1,t2;
        ShapeableImageView img;
        ImageView del;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            t1=itemView.findViewById(R.id.name);
            t2=itemView.findViewById(R.id.price);
            img=itemView.findViewById(R.id.img);
        }
    }
}

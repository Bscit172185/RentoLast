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
import android.widget.Toast;

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
        String a ,name,price,brokerage,months,quantity,c;
        Uri uri1;
        a=datalist.get(position).getProduct_ImgUrl();
        c=datalist.get(position).Itemid;
        name=datalist.get(position).getProduct_Name();
        price=datalist.get(position).getProduct_Price();
        brokerage=datalist.get(position).getProduct_brocrage();
        months=datalist.get(position).qut;
        quantity=datalist.get(position).proqut;

        uri1=Uri.parse(a);
        Picasso.get().load(uri1).into(holder.img);
        holder.t1.setText(name);
        holder.t2.setText(price);
        holder.t3.setText(brokerage);
        holder.t4.setText("Mon: "+months);
        holder.t5.setText("Qty: "+quantity);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ItemDetailsOfAddToCartActivity.class);
                intent.putExtra("Itemid",c);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class myviewholder extends RecyclerView.ViewHolder {
        TextView t1,t2,t3,t4,t5;
        ShapeableImageView img;
        ImageView del;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            t1=itemView.findViewById(R.id.name);
            t2=itemView.findViewById(R.id.price);
            t3=itemView.findViewById(R.id.brokerage);
            t4=itemView.findViewById(R.id.mon);
            t5=itemView.findViewById(R.id.qty);
            img=itemView.findViewById(R.id.img);
        }
    }
}

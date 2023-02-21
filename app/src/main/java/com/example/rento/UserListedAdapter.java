package com.example.rento;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserListedAdapter extends RecyclerView.Adapter<UserListedAdapter.myviewholder>{
    Context context;
    ArrayList<Model> datalist;

    public UserListedAdapter(ArrayList<Model> datalist, Context context) {
        this.datalist= datalist;
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
        String imgurl ,pname,pprice,ProId;
        Uri uri1;
        imgurl=datalist.get(position).getProduct_ImgUrl();
        pname=datalist.get(position).getProduct_Name();
        pprice=datalist.get(position).getProduct_Price();
        uri1=Uri.parse(imgurl);
        Picasso.get().load(uri1).into(holder.img);
        holder.t1.setText(pname);
        holder.t2.setText(pprice);
        ProId=datalist.get(position).id;
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ItemDetailsOfListedProductByUserActivity.class);
                intent.putExtra("id",ProId);
                context.startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {return datalist.size();}

        class myviewholder extends RecyclerView.ViewHolder {
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

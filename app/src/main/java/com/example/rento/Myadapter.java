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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class Myadapter extends RecyclerView.Adapter<Myadapter.myviewholder> implements Filterable {
    Context context;
    ArrayList<Model> datalist;
    ArrayList<Model> listall;
    ArrayList<Model> finalist1;

    public Myadapter(ArrayList<Model> datalist,Context context) {
        this.datalist = datalist;
        this.listall=datalist;
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
        String pname,pprice,imgurl,brorate,ProId,UserId;
        pname=listall.get(position).getProduct_Name();
        pprice=listall.get(position).getProduct_Price();
        imgurl=listall.get(position).getProduct_ImgUrl();
        UserId=listall.get(position).getUID();
        brorate=listall.get(position).getProduct_brocrage();
        ProId=listall.get(position).id;
        uri1=Uri.parse(imgurl);
        Picasso.get().load(uri1).into(holder.img);
        holder.t1.setText(pname);
        holder.t2.setText(pprice);
        ListedProductsFragment ls=new ListedProductsFragment();

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,Item_details_of_recycleview.class);
                intent.putExtra("id",ProId);
                intent.putExtra("name",pname);
                intent.putExtra("price",pprice);
                intent.putExtra("imgurl",imgurl);
                intent.putExtra("UsersId",UserId);
                intent.putExtra("brorate",brorate);
                context.startActivity(intent);

            }
        });

    }


    @Override
    public int getItemCount() {
        return listall.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter= new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            String a;
            a=charSequence.toString();
            if(a.isEmpty()){
                listall=datalist;
           }else
           {
                List<Model> filterlist=new ArrayList<>();
                String filterpattern=charSequence.toString().toLowerCase().trim();
                 for(Model news: listall){
                    if(news.getProduct_Name().toLowerCase().contains(filterpattern.toLowerCase())){
                       filterlist.add(news);
                     }
                   }
                listall=new ArrayList<>(filterlist);

           }
            FilterResults filterResults=new FilterResults();
            filterResults.values= listall;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            listall=(ArrayList<Model>)filterResults.values;
            notifyDataSetChanged();
        }
    };

    static class myviewholder extends RecyclerView.ViewHolder{
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

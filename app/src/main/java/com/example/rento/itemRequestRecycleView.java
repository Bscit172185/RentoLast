package com.example.rento;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class itemRequestRecycleView extends RecyclerView.Adapter<itemRequestRecycleView.myviewholder> {
    Context context;
    ArrayList<Model>datalist;
    public itemRequestRecycleView(ArrayList<Model> datalist,Context context) {
        this.datalist=datalist;
        this.context=context;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.request_acceptor_carview_layout,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        String qut;
        qut=datalist.get(position).qut;
        holder.months.setText(qut);
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }
    static class myviewholder extends RecyclerView.ViewHolder{
       TextView name,months;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            months=itemView.findViewById(R.id.months);

        }
    }
}

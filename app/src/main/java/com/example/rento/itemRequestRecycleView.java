package com.example.rento;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class itemRequestRecycleView extends RecyclerView.Adapter<itemRequestRecycleView.myviewholder> {
    Context context;
    ArrayList<Model>datalist;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
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
        String qut,requid,rqeproid,reqid,pid,proqut;
        qut=datalist.get(position).qut;
        requid=datalist.get(position).requid;
        rqeproid=datalist.get(position).reqproid;
        reqid=datalist.get(position).reqid;
        pid=datalist.get(position).pid;
        proqut=datalist.get(position).proqut;
        System.out.println("this is your listing: "+proqut);
        db.collection("user").document(requid).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            String iname;
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                iname=documentSnapshot.getString("Name");
                                holder.uname.setText(iname);
                            }
                        });
        db.collection("Product").document(rqeproid).get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                int a,b,d,e,res;
                                String c;
                                a=Integer.parseInt(documentSnapshot.getString("Product_Price"));
                                b=Integer.parseInt(documentSnapshot.getString("Product_brocrage"));
                                d=Integer.parseInt(qut);
                                e=Integer.parseInt(proqut);
                                res=a*d*e+b;
                                c=String.valueOf(res);
                                holder.totalam.setText("Total: "+c);
                            }
                        });

        holder.months.setText("Month: "+qut);
        holder.proqut.setText("Quantity: "+proqut);
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object> s=new HashMap<String, Object>();
                s.put("Status","Accepted");
                db.collection("Rent_Request").document(reqid).update(s)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "Accepted", Toast.LENGTH_SHORT).show();
                                HashMap<String,Object> s=new HashMap<String, Object>();
                                s.put("pro_status","DEACTIVE");
                                db.collection("Product").document(pid).update(s);
                                db.collection("Rent_Request").get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                List<DocumentSnapshot> list1=queryDocumentSnapshots.getDocuments();
                                                for(DocumentSnapshot d:list1){
                                                    String proid=d.getString("ProId");
                                                    if(proid.equals(pid)){
                                                        String rqid=d.getId();
                                                        String status=d.getString("Status");
                                                        if(status.equals("Pending")){
                                                            HashMap<String,Object> s=new HashMap<String, Object>();
                                                            s.put("Status","Rejected");
                                                            db.collection("Rent_Request").document(rqid).update(s)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {

                                                                        }
                                                                    });
                                                        }

                                                    }
                                                }
                                            }
                                        });

                                context.startActivity(new Intent(context,MainActivity.class));
                            }
                        });

            }
        });
        db.collection("Rent_Request").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> li=queryDocumentSnapshots.getDocuments();
                        for(DocumentSnapshot d:li){
                            String sta=d.getString("Status");
                            String proid=d.getString("ProId");
                            if(proid.equals(pid)){
                                if(sta.equals("Accepted")){
                                    holder.accept.setBackgroundResource(R.drawable.button_add_green_product);
                                }
                            }
                        }
                    }
                });

    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }
    static class myviewholder extends RecyclerView.ViewHolder{
       TextView months,uname,totalam,proqut;
       Button accept;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            months=itemView.findViewById(R.id.months);
            uname=itemView.findViewById(R.id.name);
            totalam=itemView.findViewById(R.id.totalamt);
            accept=itemView.findViewById(R.id.accept);
            proqut=itemView.findViewById(R.id.proqut);

        }
    }
}

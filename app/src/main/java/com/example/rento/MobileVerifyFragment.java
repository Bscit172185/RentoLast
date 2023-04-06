package com.example.rento;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MobileVerifyFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EditText MobN;
        Button Next;
        View view= inflater.inflate(R.layout.fragment_mobile_verify, container, false);
        MobN=view.findViewById(R.id.editTextPhone);
        Next=view.findViewById(R.id.button4);
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num =MobN.getText().toString();
                if(!num.isEmpty()){
                    if(num.length()==10){
                        Bundle bundle=new Bundle();
                        bundle.putString("mnum",num);
                        getParentFragmentManager().setFragmentResult("Mobileverify",bundle);
                        Fragment fragment=new AddressRegistrationFragment();
                        FragmentTransaction ft= getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.ragistraionFreg,fragment).commit();
                    }
                    else {
                        MobN.setError("Number Should Be 10 Digits");
                    }
                }
                else {
                    MobN.setError("Empty field not allowed");
                }

            }
        });
        return view;
    }
}
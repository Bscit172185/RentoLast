package com.example.rento;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class AddressRegistrationFragment extends Fragment {
    int Location_Request_Code = 1001;
    Button but,location;
    EditText addressname,cityname,countryname,code;
    FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address_registration, container, false);
        addressname=view.findViewById(R.id.addressname);
        cityname=view.findViewById(R.id.cityname);
        countryname=view.findViewById(R.id.countryname);
        code=view.findViewById(R.id.codename);
        but = view.findViewById(R.id.next);
        location=view.findViewById(R.id.GPSbut);

        TextView Address,City,Country,codea;
        Address=view.findViewById(R.id.Address);
        City=view.findViewById(R.id.City);
        Country=view.findViewById(R.id.Country);
        codea=view.findViewById(R.id.code);


        Address.setVisibility(view.INVISIBLE);
        City.setVisibility(View.INVISIBLE);
        Country.setVisibility(View.INVISIBLE);
        codea.setVisibility(View.INVISIBLE);
        addressname.setVisibility(View.INVISIBLE);
        cityname.setVisibility(View.INVISIBLE);
        countryname.setVisibility(View.INVISIBLE);
        code.setVisibility(View.INVISIBLE);
        but.setVisibility(View.INVISIBLE);







        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new SetPasswordForRegistrationFragment();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.ragistraionFreg, fragment).commit();
            }
        });
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    Address.setVisibility(view.VISIBLE);
                    City.setVisibility(View.VISIBLE);
                    Country.setVisibility(View.VISIBLE);
                    codea.setVisibility(View.VISIBLE);
                    addressname.setVisibility(View.VISIBLE);
                    cityname.setVisibility(View.VISIBLE);
                    countryname.setVisibility(View.VISIBLE);
                    code.setVisibility(View.VISIBLE);
                    but.setVisibility(View.VISIBLE);
                    getLastLocation();
                } else {


                    AskLocationPermition();

                }
            }
        });

        return view;
    }

    private void getLastLocation() {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    Geocoder geocoder=new Geocoder(getActivity(), Locale.getDefault());
                    List<Address> addresses= null;
                    try {
                        addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String Adress,city,contry,codename,Latitude,Longitude;
                    Latitude=String.valueOf(location.getLatitude());
                    Longitude=String.valueOf(location.getLongitude());
                    Adress=addresses.get(0).getAddressLine(0);
                    city=addresses.get(0).getLocality();
                    contry=addresses.get(0).getCountryName();
                    codename=addresses.get(0).getPostalCode();
                    addressname.setText(Adress);
                    cityname.setText(city);
                    countryname.setText(contry);
                    code.setText(codename);
                    Bundle bundle=new Bundle();
                    bundle.putString("Address",Adress);
                    bundle.putString("Latitude",Latitude);
                    bundle.putString("Longitude",Longitude);
                    bundle.putString("postalcode",codename);
                    getParentFragmentManager().setFragmentResult("AddressDetails",bundle);

                }
            }
        });
    }

    private void AskLocationPermition() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION },Location_Request_Code);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==Location_Request_Code){
            if(grantResults.length >0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }
        }
    }
}
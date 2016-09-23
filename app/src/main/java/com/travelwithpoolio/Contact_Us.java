package com.travelwithpoolio;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class Contact_Us extends Fragment {

    ImageView phone;
    ImageView email;
    ImageView fb;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contact__us, container, false);
        phone = (ImageView)v.findViewById(R.id.phone);
        email = (ImageView)v.findViewById(R.id.email);
        fb = (ImageView)v.findViewById(R.id.fb);

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "tel:" + "9414568550";
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(number));
                intent.putExtra(Intent.EXTRA_PHONE_NUMBER, number);
                Intent chosenIntent = Intent.createChooser(intent, "Contact us!");
                chosenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(chosenIntent);
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.facebook.com/travelwithpoolio"));
                startActivity(intent);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + "poolio.travel@gmail.com"));
                startActivity(intent);
            }
        });
        // Inflate the layout for this fragment
        return v;
    }





    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

package com.travelwithpoolio;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;


public class profile extends android.support.v4.app.Fragment {
    public final String ADD_PROFILE_URL="http://www.poolio.in/pooqwerty123lio/addprofile.php";//Sumit's pc

    SharedPreferences mSharedPreferences;
    String mobile, name, gender, email, vehicle_name, vehicle_number, driving_license;
    TextView nameET, genderET;
    EditText mobileET, emailET, vehicle_nameET, vehicle_numberET, driving_licenseET;
    ImageView vehicle_nameIV, vehicle_numberIV, dlIV,edit_image,emailIV,dp;
    Button add_button;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_profile, container, false);
        mSharedPreferences = getActivity().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        mobile = mSharedPreferences.getString("mobile", "null");
        name = mSharedPreferences.getString("name", "name");
        gender = mSharedPreferences.getString("gender", "gender");
        email = mSharedPreferences.getString("email", "email");
        vehicle_name = mSharedPreferences.getString("vehicle_name", "vehicle_name");
        vehicle_number = mSharedPreferences.getString("vehicle_number", "vehicle_number");
        driving_license = mSharedPreferences.getString("driving_license", "driving_license");
        //Toast.makeText(getContext(),mobile,Toast.LENGTH_LONG).show();
        mobileET = (EditText) v.findViewById(R.id.mobileTV);
        nameET = (TextView) v.findViewById(R.id.user_profile_name);
        genderET = (TextView) v.findViewById(R.id.gender);
        emailET = (EditText) v.findViewById(R.id.email);
        vehicle_nameET = (EditText) v.findViewById(R.id.v_name);
        vehicle_numberET = (EditText) v.findViewById(R.id.v_no);
        driving_licenseET = (EditText) v.findViewById(R.id.dl);
        vehicle_nameIV = (ImageView) v.findViewById(R.id.vnplus);
        vehicle_numberIV = (ImageView) v.findViewById(R.id.vnoplus);
        dlIV = (ImageView) v.findViewById(R.id.dlplus);
        emailIV=(ImageView)v.findViewById(R.id.emailplus) ;
        add_button = (Button)v.findViewById(R.id.add_button);
        edit_image=(ImageView)v.findViewById(R.id.edit_button);
        dp=(ImageView)v.findViewById(R.id.user_profile_photo);

        mobileET.setText(mobile);
        nameET.setText(name);
        genderET.setText(gender);
        emailET.setText(email);
        vehicle_numberET.setText(vehicle_number);
        vehicle_nameET.setText(vehicle_name);
        driving_licenseET.setText(driving_license);
        if (vehicle_number.equalsIgnoreCase("null")) {
            vehicle_numberET.setText("Add your vehicle number");
            vehicle_numberIV.setVisibility(View.VISIBLE);
            vehicle_numberIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addDetails(v);
                }
            });
        }
        if (vehicle_name.equalsIgnoreCase("null")) {
            vehicle_nameET.setText("Add your vehicle number");
            vehicle_nameIV.setVisibility(View.VISIBLE);
            vehicle_nameIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addDetails(v);
                }
            });
        }
        if (driving_license.equalsIgnoreCase("null")) {
            driving_licenseET.setText("Add your license number");
            dlIV.setVisibility(View.VISIBLE);
            dlIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addDetails(v);
                }
            });

        }
        edit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit_image.setVisibility(View.GONE);
                vehicle_nameIV.setVisibility(View.VISIBLE);
                vehicle_nameIV.setImageResource(R.drawable.edit);
                vehicle_numberIV.setVisibility(View.VISIBLE);
                vehicle_numberIV.setImageResource(R.drawable.edit);
                dlIV.setVisibility(View.VISIBLE);
                dlIV.setImageResource(R.drawable.edit);
                emailIV.setVisibility(View.VISIBLE);
                emailIV.setImageResource(R.drawable.edit);

                edit_image.setVisibility(View.GONE);
                mobileET.setEnabled(false);
                emailET.setEnabled(true);
                vehicle_nameET.setEnabled(true);
                vehicle_numberET.setEnabled(true);
                driving_licenseET.setEnabled(true);
                add_button.setVisibility(View.VISIBLE);
                add_button.setText("UPDATE");
                add_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addDetailsToDB(v,mobile);
                    }
                });


            }
        });

        setImage(name);

        return v;
    }


    void addDetails(View v) {
        mobileET.setEnabled(false);
        emailET.setEnabled(false);
        add_button.setVisibility(View.VISIBLE);
        vehicle_numberIV.setVisibility(View.GONE);
        vehicle_nameIV.setVisibility(View.GONE);
        dlIV.setVisibility(View.GONE);
        vehicle_nameET.setText("");
        vehicle_nameET.setHint("Vehicle Name*");
        vehicle_nameET.setEnabled(true);
        vehicle_numberET.setText("");
        vehicle_numberET.setHint("Vehicle Number*");
        vehicle_numberET.setEnabled(true);
        driving_licenseET.setText("");
        driving_licenseET.setHint("Driving License Number*");
        driving_licenseET.setEnabled(true);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDetailsToDB(v,mobile);
            }
        });
    }

    void addDetailsToDB(View view,String mobile)
    {
        String v_name = vehicle_nameET.getText().toString();
        String v_number = vehicle_numberET.getText().toString();
        String dl = driving_licenseET.getText().toString();
        String email = emailET.getText().toString();
//        Toast.makeText(view.getContext(),v_name+" "+v_number+" "+dl+" "+mobile,Toast.LENGTH_SHORT).show();

        if(email==""||v_name=="" || v_number=="" || dl=="")
        {
            Snackbar snackbar = Snackbar.make(getView(),"One or more fields are empty",Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
        else
        {
            addProfile(view,mobile,email,v_name,v_number,dl);
        }
    }


    void addProfile(final View view,String mobile,String email, String v_name, String v_number, String dl)
    {
        //Toast.makeText(view.getContext(),"Message saved in db",Toast.LENGTH_SHORT).show();
        class addProfileClass extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc=new RegisterUserClass();


            protected void onPreExecute() {

                super.onPreExecute();
                loading = ProgressDialog.show(view.getContext(), "Adding your details","Please wait while we connect to our server", true, true);
            }
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                loading.dismiss();
                if("".equals(s))
                {
                    s="Server error, Please try again after some time!";
                }
                else if("successfully added".equalsIgnoreCase(s)){

                    Snackbar snackbar = Snackbar.make(getView(),"Profile updated",Snackbar.LENGTH_SHORT);
                    snackbar.show();

                    Intent in = new Intent(view.getContext(),Home.class);
                    in.putExtra("switch","ride");
                    startActivity(in);
                }

                Toast.makeText(view.getContext(),s,Toast.LENGTH_LONG).show();


            }


            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<String,String>();
                data.put("mobile",params[0]);
                data.put("email",params[1]);
                data.put("vehicle_name",params[2]);
                data.put("vehicle_number",params[3]);
                data.put("driving_license",params[4]);
                String result = ruc.sendPostRequest(ADD_PROFILE_URL,data);
                //Log.i("@doinBackground:", result);
                return  result;

            }
        }
        addProfileClass apc = new addProfileClass();
        apc.execute(mobile,email,v_name, v_number,dl);

    }

    void setImage(String name)
    {
        String namel = name.toLowerCase();
        char firstletter;
        firstletter= namel.charAt(0);
        switch (firstletter)
        {
            case 'a':
                dp.setImageResource(R.drawable.a);
                break;
            case 'b':
                dp.setImageResource(R.drawable.b);
                break;
            case 'c':
                dp.setImageResource(R.drawable.c);
                break;
            case 'd':
                dp.setImageResource(R.drawable.d);
                break;
            case 'e':
                dp.setImageResource(R.drawable.e);
                break;
            case 'f':
                dp.setImageResource(R.drawable.f);
                break;
            case 'g':
                dp.setImageResource(R.drawable.g);
                break;
            case 'h':
                dp.setImageResource(R.drawable.h);
                break;
            case 'i':
                dp.setImageResource(R.drawable.i);
                break;
            case 'j':
                dp.setImageResource(R.drawable.j);
                break;
            case 'k':
                dp.setImageResource(R.drawable.k);
                break;
            case 'l':
                dp.setImageResource(R.drawable.l);
                break;
            case 'm':
                dp.setImageResource(R.drawable.m);
                break;
            case 'n':
                dp.setImageResource(R.drawable.n);
                break;
            case 'o':
                dp.setImageResource(R.drawable.o);
                break;
            case 'p':
                dp.setImageResource(R.drawable.p);
                break;
            case 'q':
                dp.setImageResource(R.drawable.q);
                break;
            case 'r':
                dp.setImageResource(R.drawable.r);
                break;
            case 's':
                dp.setImageResource(R.drawable.s);
                break;
            case 't':
                dp.setImageResource(R.drawable.t);
                break;
            case 'u':
                dp.setImageResource(R.drawable.u);
                break;
            case 'v':
                dp.setImageResource(R.drawable.v);
                break;
            case 'w':
                dp.setImageResource(R.drawable.w);
                break;
            case 'x':
                dp.setImageResource(R.drawable.x);
                break;
            case 'y':
                dp.setImageResource(R.drawable.y);
                break;
            case 'z':
                dp.setImageResource(R.drawable.z);
                break;

        }
    }
}

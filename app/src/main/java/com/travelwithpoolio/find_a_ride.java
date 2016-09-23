package com.travelwithpoolio;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class find_a_ride extends Fragment {
    String[] locations ={"SRM Arch Gate","Abode Valley","Estancia","SRM Backgate","Potheri Station/Main Campus","Green Pearl","Safa", "Akshaya","Airport","Central Station","Egmore Station"};//need to make it dynamic
    AutoCompleteTextView actv,actv2;
    EditText dateET, timeET;
    Button b;
    String pickup, drop, time, date;
    String dateforsql;
    private ImageView dateIv, timeIV;
    private int mYear, mMonth, mDay, mHour, mMinute;
    CoordinatorLayout mCoordinatorLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.activity_find_a_ride, container, false);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.select_dialog_item,locations);

        actv= (AutoCompleteTextView)v.findViewById(R.id.pickup);
        actv.setThreshold(1);
        actv.setAdapter(adapter);
        actv.setTextColor(Color.RED);
        actv2= (AutoCompleteTextView)v.findViewById(R.id.drop);
        actv2.setThreshold(0);
        actv2.setAdapter(adapter);
        actv2.setTextColor(Color.RED);

        dateET = (EditText)v.findViewById(R.id.date);
        timeET = (EditText)v.findViewById(R.id.time);
        b=(Button) v.findViewById(R.id.btn_find);
        dateIv = (ImageView) v.findViewById(R.id.dateIv);
        timeIV = (ImageView) v.findViewById(R.id.img_time);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!InternetConnectionClass.isConnected(getActivity())){
                    Toast.makeText(getActivity(), "Please connect to the internet!", Toast.LENGTH_LONG).show();
                    return;
                }
                pickup = actv.getText().toString();
                drop= actv2.getText().toString();
                date= dateET.getText().toString();
                time= timeET.getText().toString();
                if(drop.equalsIgnoreCase(pickup)){
                    Snackbar snackbar = Snackbar.make(getView(),"drop and pickup locations should be different.",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    return;
                }
                if(pickup.equals("")||drop.equals("")||date.equals("")||time.equals(""))
                {
                    //Toast.makeText(getContext(),"Please enter all values",Toast.LENGTH_SHORT).show();
                    Snackbar snackbar = Snackbar.make(getView(),"Please fill all values",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                else
                {
                    Intent in = new Intent(getActivity(),available_rides.class);
                    in.putExtra("pickup",pickup);
                    in.putExtra("drop",drop);
                    in.putExtra("date",dateforsql);
                    in.putExtra("time",time);
                    startActivity(in);
                }


        }});

        dateIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!InternetConnectionClass.isConnected(getActivity())){
                    Toast.makeText(getActivity(), "Please connect to the internet!", Toast.LENGTH_LONG).show();
                    return;
                }
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext() , new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                               dateforsql= year + "-" + (monthOfYear + 1)+"-"+dayOfMonth;
                                dateET.setText(dayOfMonth+ "-" + (monthOfYear + 1)+"-"+year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();


            }

        });

        timeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!InternetConnectionClass.isConnected(getActivity())){
                    Toast.makeText(getActivity(), "Please connect to the internet!", Toast.LENGTH_LONG).show();
                    return;
                }
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                String hour=String.valueOf(hourOfDay);
                                if(hour.length()<2){
                                 hour="0"+hour;
                                }
                                timeET.setText(hour + ":" + minute);
                            }
                        }, mHour, mMinute, false);

                timePickerDialog.show();

            }
        });
        return v;
    }




}

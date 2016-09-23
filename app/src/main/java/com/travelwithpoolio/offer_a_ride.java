package com.travelwithpoolio;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TimePicker;
import android.widget.Toast;


import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class offer_a_ride extends Fragment {

    //public final String REGISTER_URL="http://192.168.1.101/poolio/register.php"; //Siddharth's pc
    public final String OFFER_URL="http://www.poolio.in/pooqwerty123lio/offer.php";// Sumit's pc
    String dateforsql,timeforsql;
    String[] locations ={"SRM Arch Gate","Abode Valley","Estancia","SRM Backgate","Potheri Station/Main Campus","Green Pearl","Safa", "Akshaya","Airport","Central Station","Egmore Station"};//need to make it dynamic
    List<String> vehicleType = new ArrayList<String>(); //No need for dynamic i suppose
    //public static Spinner spinner;
    String[] vehicless={"Bike","Car","Auto","Cab"};
    AutoCompleteTextView actv,actv2,spinner;
    String source, destination, type,mobile,date,time,vname,vnumber,msg;
    int availableSeats, amount=0;
    public int chargeable=1; //false for free ride
    SharedPreferences pref;
    EditText sourceET,destinationET, dateET, timeET,vnameET,vnumberET,availableET,messagev;
    static RadioGroup chargeableRG;
    Button offer_button;
    static int dayCheck;
    LinearLayout chargeLayout;
    static boolean timeCheck;// if timeCheck is false -> don't go to next screen
    ImageView Calenderiv;
    private int mYear, mMonth, mDay, mHour, mMinute,position;
    AVLoadingIndicatorView avi;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.activity_offer_a_ride, container, false);
        if(!InternetConnectionClass.isConnected(getActivity())){
            Toast.makeText(getActivity(), "Please connect to the internet!", Toast.LENGTH_LONG).show();
        }
        spinner = (AutoCompleteTextView) v.findViewById(R.id.spin);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.select_dialog_item,locations);
        actv= (AutoCompleteTextView)v.findViewById(R.id.from);
        actv.setThreshold(1);
        actv.setAdapter(adapter);
        actv.setTextColor(Color.RED);
        actv2= (AutoCompleteTextView)v.findViewById(R.id.to);
        actv2.setThreshold(0);
        actv2.setAdapter(adapter);
        actv2.setTextColor(Color.RED);
        messagev=(EditText) v.findViewById(R.id.messageET);
        avi=(AVLoadingIndicatorView) v.findViewById(R.id.avi_offerride);
        avi.setVisibility(View.GONE);
        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(),android.R.layout.select_dialog_item,vehicless);
        spinner.setThreshold(1);
        spinner.setAdapter(adapter2);

        sourceET = (EditText)v.findViewById(R.id.from);
        destinationET=(EditText)v.findViewById(R.id.to);
        dateET=(EditText)v.findViewById(R.id.date);
        timeET=(EditText)v.findViewById(R.id.time);
        vnameET=(EditText)v.findViewById(R.id.vname);
        vnumberET=(EditText)v.findViewById(R.id.vnumber);
        availableET=(EditText)v.findViewById(R.id.passengers);
//        amountET=(EditText)v.findViewById(R.id.money);
        Calenderiv = (ImageView)v.findViewById(R.id.calender);
        chargeLayout = (LinearLayout)v.findViewById(R.id.layer_charge);
        chargeableRG=(RadioGroup)v.findViewById(R.id.radioGrp);
        SharedPreferences offerSp=getActivity().getSharedPreferences("offer",Context.MODE_PRIVATE);
        sourceET.setText(offerSp.getString("source",""));
        destinationET.setText(offerSp.getString("destination",""));
        vnameET.setText(offerSp.getString("vname",""));
        vnumberET.setText(offerSp.getString("vnumber",""));
        availableET.setText(offerSp.getString("availableseats",""));
        spinner.setText(offerSp.getString("type",""));
        if("auto".equalsIgnoreCase(spinner.getText().toString()))
        {
            vnameET.setVisibility(View.GONE);
            vnumberET.setVisibility(View.GONE);
        }
        if("cab".equalsIgnoreCase(spinner.getText().toString()))
        {
            vnameET.setHint("Cab company");
            vnumberET.setVisibility(View.GONE);
        }



        spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if(spinner.getText().toString().equalsIgnoreCase("auto")||spinner.getText().toString().equalsIgnoreCase("cab"))
                {

                    if(spinner.getText().toString().equalsIgnoreCase("auto"))
                        vnameET.setVisibility(View.GONE);
                        vnumberET.setVisibility(View.GONE);
                        availableET.setText("2");
                    if(spinner.getText().toString().equalsIgnoreCase("cab"))
                        vnameET.setVisibility(View.VISIBLE);

                        vnumberET.setVisibility(View.GONE);
                        availableET.setText("3");

                }
                else
                {
                    vnameET.setVisibility(View.VISIBLE);
                    vnumberET.setVisibility(View.VISIBLE);
                    if(spinner.getText().toString().equalsIgnoreCase("car"))
                        availableET.setText("4");
                    if(spinner.getText().toString().equalsIgnoreCase("bike"))
                        availableET.setText("1");
                }


            }
        });

        availableET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Integer count = Integer.parseInt((availableET.getText().toString().equalsIgnoreCase(""))?"0":availableET.getText().toString());
                if(spinner.getText().toString().equalsIgnoreCase("bike"))
                {
                    if(count>1)
                        availableET.setText("1");
                }
                if(spinner.getText().toString().equalsIgnoreCase("car"))
                {
                    if(count>5)
                        availableET.setText("4");
                }
                if(spinner.getText().toString().equalsIgnoreCase("cab"))
                {
                    if(count>6)
                        availableET.setText("6");
                }
                if(spinner.getText().toString().equalsIgnoreCase("auto"))
                {
                    if(count>2)
                        availableET.setText("2");
                }
            }
        });






        Calenderiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!InternetConnectionClass.isConnected(getActivity())){
                    Snackbar snackbar = Snackbar.make(getView(),"Please connect to the internet!",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    return;
                }
                final Calendar c = Calendar.getInstance();

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                timeforsql= hourOfDay + ":" + minute;
                                String hour=String.valueOf(hourOfDay);
                                if(hour.length()<2){
                                    hour="0"+hour;
                                }
                                timeET.setText(hour + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {


                        dateforsql= year + "-" + (monthOfYear + 1)+"-"+dayOfMonth;
                        dateET.setText(dayOfMonth+ "-" + (monthOfYear + 1)+"-"+year);

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);




            }
        });



        timeET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!InternetConnectionClass.isConnected(getActivity())){
                    Snackbar snackbar = Snackbar.make(getView(),"Please connect to the internet!",Snackbar.LENGTH_SHORT);
                    snackbar.show();
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
                                timeforsql= (hourOfDay + ":" + minute);
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
//        dateET.setText(date);





        offer_button = (Button)v.findViewById(R.id.btn_offer);
        offer_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(drop.equalsIgnoreCase(pickup)){
//                    Snackbar snackbar = Snackbar.make(getView(),"drop and pickup locations should be different.",Snackbar.LENGTH_SHORT);
//                    snackbar.show();
//                    return;
//                }
                offerRides();

            }
        });



        return v;

    }

    void offerRides() {
        if (!InternetConnectionClass.isConnected(getActivity())) {
            Snackbar snackbar = Snackbar.make(getView(),"Please connect to the internet!",Snackbar.LENGTH_SHORT);
            snackbar.show();
            return;
        }
        msg=messagev.getText().toString();
        pref = getActivity().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        mobile = pref.getString("mobile", null);
        source = sourceET.getText().toString().trim();
        destination = destinationET.getText().toString().trim();

        type = spinner.getText().toString();
        if ("".equalsIgnoreCase(dateforsql) || "".equalsIgnoreCase(timeforsql)) {

            Snackbar snackbar = Snackbar.make(getView(),"please enter date or time",Snackbar.LENGTH_SHORT);
            snackbar.show();
            return;
        }
        if(source.equalsIgnoreCase(destination)){
            Snackbar snackbar = Snackbar.make(getView(),"source and destination locations should be different.",Snackbar.LENGTH_SHORT);
            snackbar.show();
            return;
        }
        date = dateforsql;
        time=timeET.getText().toString();
        time = timeforsql;
        vname = vnameET.getText().toString();
        vnumber = vnumberET.getText().toString();
        if(!availableET.getText().toString().equalsIgnoreCase("")) {
            availableSeats = Integer.parseInt(availableET.getText().toString());
        }
        //int chargeable will be set automatically depending on radio button selected
//        if(chargeableRG.getCheckedRadioButtonId()==R.id.radio_charge)
//        {
//            amount=Integer.parseInt(amountET.getText().toString());
//        }

        //Toast.makeText(getContext(),"Mobile :"+mobile+"\n Source:"+source+"  Destination:"+destination+"\ntype:"+type
        //  +"\nSeats:"+availableSeats+"\n Amount:"+amount,Toast.LENGTH_LONG).show();
        if (!InternetConnectionClass.isConnected(getActivity())) {

            Snackbar snackbar = Snackbar.make(getView(),"Please connect to the internet!",Snackbar.LENGTH_SHORT);
            snackbar.show();

            return;
        }
        SharedPreferences offerSp = getActivity().getSharedPreferences("offer", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = offerSp.edit();
        editor.putString("source", source);
        editor.putString("destination", destination);
        editor.putString("type", type);
        editor.putString("vname", vname);
        editor.putString("vnumber", vnumber);
        editor.putString("availableseats", availableSeats + "");
        editor.apply();
//        if (("bike".equalsIgnoreCase(type) || "car".equalsIgnoreCase(type)) && (( "".equalsIgnoreCase(vname) ||"".equalsIgnoreCase(vnumber))||"".equalsIgnoreCase(source)||"".equalsIgnoreCase(destination)||"".equalsIgnoreCase(dateET.getText().toString())||"".equalsIgnoreCase(timeET.getText().toString())))
//        {
//            Snackbar snackbar = Snackbar.make(getView(),"Please fill all values",Snackbar.LENGTH_SHORT);
//            snackbar.show();
//        }
//        else if("auto".equalsIgnoreCase(type)) && (( "".equalsIgnoreCase(source)||"".equalsIgnoreCase(destination)||"".equalsIgnoreCase(dateET.getText().toString())||"".equalsIgnoreCase(timeET.getText().toString())))
//        {
//            Snackbar snackbar = Snackbar.make(getView(),"Please fill all values",Snackbar.LENGTH_SHORT);
//            snackbar.show();
//        }
//        else
//        {
//            //Log.d("**offer**",mobile+" "+source+" "+destination+" "+type+" "+date+" "+time+" "+vname+" "+vnumber+" "+availableSeats+" "+chargeable+" "+amount+" "+msg);
//            offer(mobile, source, destination, type, date, time, vname, vnumber, availableSeats, chargeable, amount,msg);
//        }
        if(("bike".equalsIgnoreCase(type) || "car".equalsIgnoreCase(type)) && ( "".equalsIgnoreCase(vname) ||"".equalsIgnoreCase(vnumber)||"".equalsIgnoreCase(source)||"".equalsIgnoreCase(destination)||"".equalsIgnoreCase(dateET.getText().toString())||"".equalsIgnoreCase(timeET.getText().toString())))
        {
            Snackbar snackbar = Snackbar.make(getView(),"Please fill all values",Snackbar.LENGTH_SHORT);
            snackbar.show();

        }
        else if(("auto".equalsIgnoreCase(type)) && ( "".equalsIgnoreCase(source)||"".equalsIgnoreCase(destination)||"".equalsIgnoreCase(dateET.getText().toString())||"".equalsIgnoreCase(timeET.getText().toString())))
        {
            Snackbar snackbar = Snackbar.make(getView(),"Please fill all values",Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
        else if(("cab".equalsIgnoreCase(type)) && ( "".equalsIgnoreCase(vname)||"".equalsIgnoreCase(source)||"".equalsIgnoreCase(destination)||"".equalsIgnoreCase(dateET.getText().toString())||"".equalsIgnoreCase(timeET.getText().toString())))
        {
            Snackbar snackbar = Snackbar.make(getView(),"Please fill all values",Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
        else
        {
            offer(mobile, source, destination, type, date, time, vname, vnumber, availableSeats, chargeable, amount,msg);
        }

    }

    //11 parameters
    public void offer(String mobile, String source, String destination, String type, String date, String time, String vname, String vnumber, final int availableSeats, int chargeable, int amount, String msg){
        class OfferTheRide extends AsyncTask<String, Void, String> {
//            ProgressDialog loading;
            RegisterUserClass ruc=new RegisterUserClass();


            protected void onPreExecute() {
                avi.setVisibility(View.VISIBLE);
                avi.show();
                super.onPreExecute();
//                loading = ProgressDialog.show(getContext(), "Saving Your Details","Thanks for offering ride", true, true);
            }
            protected void onPostExecute(String s){
                super.onPostExecute(s);
//                loading.dismiss();
                avi.hide();

                if("".equals(s))
                {
                    s="Server error, Please try again after some time!";
                }
                else if("successfully registered".equalsIgnoreCase(s)){
                    Intent myIntent = new Intent(getContext(), Confirmation.class);//Need to make an activity saying your ride has been offered succesfully and will be displayed soon
                    startActivity(myIntent);
                    //getActivity().overridePendingTransition(R.anim.next_slide_in, R.anim.next_slide_out);
                   // Toast.makeText(getContext(),"Ride offered",Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getContext(),s,Toast.LENGTH_LONG).show();




            }


            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<String,String>();
                data.put("mobile",params[0]);
                data.put("source",params[1]);
                data.put("destination",params[2]);
                data.put("type",params[3]);
                data.put("date",params[4]);
                data.put("time",params[5]);
                data.put("vname",params[6]);
                data.put("vnumber",params[7]);
                data.put("availableSeats",params[8]);
                data.put("chargeable",params[9]);
                data.put("amount",params[10]);
                data.put("msg",params[11]);
                String result = ruc.sendPostRequest(OFFER_URL,data);
                //Log.i("@doinBackground:", result);
                return  result;
            }
        }
        OfferTheRide otr = new OfferTheRide();
        //Log.d("**offer**",mobile+" "+source+" "+destination+" "+type+" "+date+" "+time+" "+vname+" "+vnumber+" "+availableSeats+" "+chargeable+" "+amount+" "+msg);
        otr.execute(mobile,source,destination,type,date,time,vname,vnumber,Integer.toString(availableSeats),Integer.toString(chargeable),Integer.toString(amount),msg);
    }


}

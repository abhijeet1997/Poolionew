package com.travelwithpoolio;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class Data {

    public String id; //rideid
    public String first_name;
    public String last_name;
    public String gender;
    public String mobile;
    public String source;
    public String destination;
    public String type;
    public String date;//date when ride will begin
    public String time;//time when ride will begin
    public String vehicle_name;
    public String vehicle_number;
    public String seats;
    public String timestamp;//timestamp when ride was offered by the user
    public String device_id;
    public String status;
    public String message;
    public String mobile_book;
    public String msg;







    Data(String id , String first_name, String last_name, String mobile, String gender, String source , String destination, String type,
         String date, String time, String vehicle_name, String vehicle_number, String seats,String device_id,String msg)//constructor for findrides
    {
        this.id = id;
        this.first_name=first_name;
        this.last_name=last_name;
        this.gender=gender;
        this.mobile = mobile;
        this.source = source;
        this.destination = destination;
        this.type = type;
        final String oldDateFormat="yyyy-MM-dd";
        final String newDateFormat="EEE, d MMM";
         String oldDateString=date;
        String newDateString="";
        SimpleDateFormat sdf=new SimpleDateFormat(oldDateFormat);
       try {
           Date d = sdf.parse(oldDateString);
           SimpleDateFormat sdf_output=new SimpleDateFormat(newDateFormat);
           newDateString=sdf_output.format(d);
       }
       catch (ParseException e){
           Log.d("Date conversion error",e.getMessage());
       }
        this.date=newDateString;
        if(newDateString == "" || newDateString == " " || newDateString == null){
            this.date = date;
        }
//        this.date=date;
        this.time = time;
        this.vehicle_name = vehicle_name;
        this.vehicle_number = vehicle_number;
        this.seats = seats;
        this.device_id=device_id;
        this.msg=msg;

    }

    public Data(String id, String source, String destination, String date, String time,String timestamp,String status) //constructor for myrides
    {
        this.id = id;
        this.source = source;
        this.destination = destination;
        String oldDateString=date;
        String newDateString="";
        final String oldDateFormat="yyyy-MM-dd";
        final String newDateFormat="EEE, d MMM";
        SimpleDateFormat sdf=new SimpleDateFormat(oldDateFormat);
        try {
            Date d = sdf.parse(oldDateString);
            SimpleDateFormat sdf_output=new SimpleDateFormat(newDateFormat);
            newDateString=sdf_output.format(d);
        }
        catch (ParseException e){
            Log.d("Date conversion error",e.getMessage());
        }
        this.date=newDateString;
        if(newDateString == "" || newDateString == " " || newDateString == null){
            this.date = date;
        }
        this.time = time;
        StringTokenizer strToken=new StringTokenizer(timestamp," ");
        String date1=strToken.nextToken();
        Log.d("date:",date1);
        String time1=strToken.nextToken();
        time1=time1.substring(0,5);
        Log.d("time:",time1);
        final String oldDateFormat2="yyyy-MM-dd";
        final String newDateFormat2="EEE, d MMM yyyy";
        SimpleDateFormat sdf2=new SimpleDateFormat(oldDateFormat2);
        try {
            Date d2 = sdf2.parse(date1);
//            Log.d("date timestamp::",d2.toString());
            SimpleDateFormat sdf_output=new SimpleDateFormat(newDateFormat2);
            date1=sdf_output.format(d2);
        }
        catch(ParseException e){
            Log.d("Date conversion error",e.getMessage());
        }
        this.timestamp=date1+" "+time1;
        Log.d("timestamp",timestamp);
        if(timestamp == "" || timestamp == " " || timestamp == null){
            this.timestamp = "Cant be displayed";
        }
        this.status=status;
    }
    public Data(String message , String mobile_book , String timestamp ){  //constructor for messages

        this.message = message;
        this.mobile_book = mobile_book;
        StringTokenizer strToken=new StringTokenizer(timestamp," ");
        String date1=strToken.nextToken();
        Log.d("date:",date1);
        String time1=strToken.nextToken();
        time1=time1.substring(0,5);
        Log.d("time:",time1);
        final String oldDateFormat2="yyyy-MM-dd";
        final String newDateFormat2="EEE, d MMM yyyy";
        SimpleDateFormat sdf2=new SimpleDateFormat(oldDateFormat2);
        try {
            Date d2 = sdf2.parse(date1);
//            Log.d("date timestamp::",d2.toString());
            SimpleDateFormat sdf_output=new SimpleDateFormat(newDateFormat2);
            date1=sdf_output.format(d2);
        }
        catch(ParseException e){
            Log.d("Date conversion error",e.getMessage());
        }
        this.timestamp=date1+" "+time1;
        Log.d("timestamp",timestamp);
        if(timestamp == "" || timestamp == " " || timestamp == null){
            this.timestamp = "Cant be displayed";
        }

    }

    public String getid(){
        return  id;
    }
    public String getFirst_name(){
        return  first_name;
    }
    public String getLast_name(){
        return  last_name;
    }
    public String getGender(){
        return  gender;
    }
    public String getMobile(){
        return  mobile;
    }
    public String getSource(){
        return source;
    }
    public String getDestination(){
        return destination;
    }
    public String getType(){
        return type;
    }
    public String getDate(){
        return date;
    }
    public String getTime(){
        return time;
    }
    public  String getMessage(){
        return message;
    }
    public String getMobile_book(){
        return mobile_book;
    }
    public String getMsg(){return msg;}
    public String getVehicle_name(){
        return vehicle_name;
    }
    public String getVehicle_number(){
        return vehicle_number;
    }
    public String getSeats(){
        return seats;
    }
    public String getDevice_id(){
        return device_id;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public String getStatus() {
        return status;
    }



}

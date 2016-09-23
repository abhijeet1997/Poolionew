package com.travelwithpoolio;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;


public class Feedback extends Fragment {

SharedPreferences mSharedPreferences;
    String feedback,name,mobile,email;
    EditText nameTV, mobileTV, emailTV, feedbackTV;
    Button submit_button;
    public final String FEEDBACK_URL ="http://www.poolio.in/pooqwerty123lio/feedback.php";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_feedback, container, false);
        mSharedPreferences = v.getContext().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        nameTV=(EditText) v.findViewById(R.id.nameTV);
        mobileTV=(EditText) v.findViewById(R.id.phoneTV);
        emailTV=(EditText) v.findViewById(R.id.emailTV);
        feedbackTV=(EditText) v.findViewById(R.id.feedbackTV);
        submit_button=(Button)v.findViewById(R.id.submit_btn);
        name=mSharedPreferences.getString("name","");
        email=mSharedPreferences.getString("email","");
        mobile=mSharedPreferences.getString("mobile","");


        nameTV.setText(name);
        mobileTV.setText(mobile);
        emailTV.setText(email);

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedback=feedbackTV.getText().toString();
                saveMessage(v,feedback,mobile);

            }
        });




        return v;
    }

    void saveMessage( final View view,String feedback, String mobile)
    {
        //Toast.makeText(view.getContext(),"Message saved in db",Toast.LENGTH_SHORT).show();
        class saveMessageClass extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc=new RegisterUserClass();


            protected void onPreExecute() {

                super.onPreExecute();
                loading = ProgressDialog.show(view.getContext(), "Saving Your Details","Thanks for offering ride", true, true);
            }
            protected void onPostExecute(String s){
                super.onPostExecute(s);
                loading.dismiss();
                if("".equals(s))
                {
                    s="Server error, Please try again after some time!";
                }
                else if("successfully saved".equalsIgnoreCase(s)){

                    Snackbar snackbar = Snackbar.make(getView(),"Feedback Sent",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    Intent intent = new Intent(view.getContext(),Home.class);
                    startActivity(intent);
                }

                Toast.makeText(view.getContext(),s,Toast.LENGTH_LONG).show();


            }


            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<String,String>();
                data.put("feedback",params[0]);
                data.put("mobile",params[1]);
                String result = ruc.sendPostRequest(FEEDBACK_URL,data);
                //Log.i("@doinBackground:", result);
                return  result;

            }
        }
        saveMessageClass smc = new saveMessageClass();
        smc.execute(feedback,mobile);
    }







}

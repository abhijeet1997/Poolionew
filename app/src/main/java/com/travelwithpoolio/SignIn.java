package com.travelwithpoolio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.onesignal.OneSignal;
import com.wang.avi.AVLoadingIndicatorView;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class SignIn extends AppCompatActivity {
    TextView goToSignUp,forgotPass;
    Button btn_signin;
    public final String SIGNIN_URL="http://www.poolio.in/pooqwerty123lio/signin.php";//Sumit's pc
    public final String DEVICE_URL="http://www.poolio.in/pooqwerty123lio/deviceregister.php";// Sumit's pc
    EditText input_mob,input_pass;
    String mob="12345",pass,device_id;
    SharedPreferences mSharedPreferences;
    private String password = null;
    AVLoadingIndicatorView avi;
    View parentview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        parentview=findViewById(R.id.avi_signin);
        goToSignUp = (TextView)findViewById(R.id.link_signup);
        forgotPass=(TextView)findViewById(R.id.forgotPassword);

        input_mob=(EditText)findViewById(R.id.input_number);
        input_pass=(EditText)findViewById(R.id.input_password);
        avi=(AVLoadingIndicatorView)findViewById(R.id.avi_signin);
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable( String userId, String registrationId) {
                Log.d("******ID******", "User:" + userId);
                device_id = userId ;
                if (registrationId != null)
                    Log.d("debug", "registrationId:" + registrationId);
            }
        });


        goToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignIn.this, OTP.class);
                startActivity(intent);
                overridePendingTransition(R.anim.next_slide_in, R.anim.next_slide_out);
            }
        });
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SignIn.this,ForgotPassword.class);
                startActivity(intent);
                overridePendingTransition(R.anim.next_slide_in,R.anim.next_slide_out);
            }
        });

        btn_signin = (Button)findViewById(R.id.btn_login);
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignInButtonClick();

            }
        });
    }

    void onSignInButtonClick()
    {
        mSharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        mob= input_mob.getText().toString().trim();
        editor.putString("mobile",mob);
        editor.putString("device id",device_id);
        editor.commit();
        pass=input_pass.getText().toString().trim();
        password=md5(pass);
        if("".equalsIgnoreCase(mob) || "".equalsIgnoreCase(pass)){
            Snackbar snackbar = Snackbar.make(parentview,"One or more fields are empty.",Snackbar.LENGTH_SHORT);
            snackbar.show();
            return;
        }
        userLogin(mob, password);
        saveDeviceID(mob,device_id);
    }
    private void userLogin(final String mobile, final String password){
        class UserLoginClass extends AsyncTask<String,Void,String> {
//            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                loading = ProgressDialog.show(SignIn.this,"Signing in","Please wait while we connect to server",true,true);
                  avi.setVisibility(View.VISIBLE);
                   avi.show();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                loading.dismiss();
                avi.hide();
                if("success".equalsIgnoreCase(s)){

                    Intent intent = new Intent(SignIn.this,Home.class);
                    intent.putExtra("mobile",mobile);
                    intent.putExtra("pass",password);
                    startActivity(intent);
                    overridePendingTransition(R.anim.next_slide_in, R.anim.next_slide_out);



                }else{
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("mobile",params[0]);
                data.put("password",params[1]);

                RegisterUserClass ruc = new RegisterUserClass();

                String result = ruc.sendPostRequest(SIGNIN_URL,data);

                return result;
            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(mobile,password);
    }

    private void saveDeviceID(final String mobile, final String device_id){
        class saveDeviceIdClass extends AsyncTask<String, Void, String>{
            
            //ProgressDialog loading;
            RegisterUserClass ruc=new RegisterUserClass();

            protected void onPreExecute() {
                super.onPreExecute();
                
                //loading = ProgressDialog.show(getApplicationContext(), "Wait","Please wait while we connect to server", true, true);
            }
            protected void onPostExecute(String s){
                super.onPostExecute(s);
               // loading.dismiss();
                if("".equals(s))
                {
                    s="Server error, Please try again after some time!";
                }
                else if("device is successfully registered".equalsIgnoreCase(s)){
                    SharedPreferences sp = getSharedPreferences("device_id",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("device_id",device_id);
                    editor.apply();
                }
                //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }


            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<String,String>();
                data.put("mobile",params[0]);
                data.put("device_id",params[1]);
                String result = ruc.sendPostRequest(DEVICE_URL,data);
               // Log.i("@doinBackground:",result);
                return  result;

            }}
        saveDeviceIdClass abc = new saveDeviceIdClass();
        abc.execute(mobile,device_id);

    }
    public String md5(String s){
        MessageDigest digest ;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes(Charset.forName("US-ASCII")),0,s.length());
            byte[] magnitude = digest.digest();
            BigInteger bi  = new BigInteger(1,magnitude);
            String hash = String.format("%0" + (magnitude.length << 1) + "X" , bi);
            password = hash;
            return hash;
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }

        return "";
    }


}

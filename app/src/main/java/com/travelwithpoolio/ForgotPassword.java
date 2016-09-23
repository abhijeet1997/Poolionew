package com.travelwithpoolio;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.msg91.sendotp.library.SendOtpVerification;
import com.msg91.sendotp.library.Verification;
import com.msg91.sendotp.library.VerificationListener;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class ForgotPassword extends AppCompatActivity implements VerificationListener {
    EditText mPhone,otpet,pass_et,retypepass_et;
    String phoneNo,otp,password;
    RelativeLayout relativeLayoutOtp,relativeLayoutMobInput,relativeLayoutPassword;
    CountDownTimer countDownTimer;
    private Verification mVerification;
      private final String MOBVER_URL = "http://www.poolio.in/pooqwerty123lio/mobforgot.php";
      private final String MOBUPDATE_URL="http://www.poolio.in/pooqwerty123lio/mobupdate.php";
    View parentview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        parentview = findViewById(R.id.root_view);
        mPhone = (EditText) findViewById(R.id.input_number);
        relativeLayoutOtp=(RelativeLayout)findViewById(R.id.relative_layout_otp);
        relativeLayoutMobInput=(RelativeLayout)findViewById(R.id.mob_input_rl);
        relativeLayoutPassword=(RelativeLayout)findViewById(R.id.password_rl);
        otpet=(EditText)findViewById(R.id.input_otp);
        pass_et=(EditText)findViewById(R.id.input_pass);
        retypepass_et=(EditText)findViewById(R.id.input_retypepass);
        Log.i("Secret key :", getSecretKey());
    }
    private String getSecretKey() {
        MessageDigest md = null;
        try {        Context context=getApplicationContext();
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        return Base64.encodeToString(md.digest(), Base64.DEFAULT);}
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.previous_slide_in, R.anim.previous_slide_out);
    }
    public void check(View view)
    {   phoneNo = mPhone.getText().toString().trim();
        if(!InternetConnectionClass.isConnected(getApplicationContext())){
            Snackbar snackbar = Snackbar.make(parentview,"One or more fields are empty",Snackbar.LENGTH_SHORT);
            snackbar.show();

            return;
        }
        if(checkLength()) {
            char c = phoneNo.charAt(0);
            if ((c != '9') && (c != '8') && (c != '7')) {

                Snackbar snackbar = Snackbar.make(parentview,"Invalid mobile number",Snackbar.LENGTH_SHORT);
                snackbar.show();

                mPhone.setText("");
                return;
            }
            alreadyExists(phoneNo);

        }
        else {
            mPhone.setText("");
        }
    }

    public boolean checkLength() {
        if (mPhone.getText().toString().length() == 10) {

            return true;
        } else {
            Snackbar snackbar = Snackbar.make(parentview,"Not a valid phone number",Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }
    }
    public void alreadyExists(String mobile){


        class VerifyMobile extends AsyncTask<String, Void, String> {
            //ProgressDialog loading;

            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //loading = ProgressDialog.show(RegisterActivity.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //  loading.dismiss();
                if ("".equals(s)) {
                    s = "server error Please try again after some time!";
                } else if ("proceed".equalsIgnoreCase(s)) {
                   //if mobile is verified then,
                    relativeLayoutOtp.setVisibility(View.VISIBLE);
                    relativeLayoutMobInput.setVisibility(View.GONE);
                    Log.i("Secret key:", getSecretKey());
                    mVerification = SendOtpVerification.createSmsVerification(getApplicationContext(), phoneNo, ForgotPassword.this, "91");
                    mVerification.initiate();

                } else if("Mobile Number is not registered".equalsIgnoreCase(s)) {
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                }
                // Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                /*if SEND OTP Button becomes unresponsive here is the problem*/
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("mobile",params[0]);
                String result = ruc.sendPostRequest(MOBVER_URL,data);
                return  result;                               }
        }


        VerifyMobile ru = new VerifyMobile();
        ru.execute(mobile);
    }
    public void submitOtp(View v){
        otp = otpet.getText().toString();
        if (!otp.isEmpty()) {

            if (mVerification != null) {
                Snackbar snackbar = Snackbar.make(parentview,"Verification in progress",Snackbar.LENGTH_SHORT);
                snackbar.show();
                mVerification.verify(otp);

            }
            else {
                Snackbar snackbar = Snackbar.make(parentview,"Enter OTP",Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        }

    }



    @Override
    public void onInitiated(String response) {
        Log.i("Forgot password otp::","initiated "+response);
        countDownTimer=new CountDownTimer(120000,20000){
            @Override
            public void onTick(long millisUntilFinished) {
                if(!InternetConnectionClass.isConnected(getApplicationContext())){
                    Snackbar snackbar = Snackbar.make(parentview,"Connect to Internet!",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    return;
                }

            }

            public void onFinish(){

                Snackbar snackbar = Snackbar.make(parentview,"Wrong number or poor connection with server!",Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        }.start();
    }

    @Override
    public void onInitiationFailed(Exception exception) {
        Log.e("ForgotPassword otp::", "Verification initialization failed: " + exception.getMessage());
        //  countDownTimer.cancel();

        Snackbar snackbar = Snackbar.make(parentview,"Verification initialization failed! Please try again later!",Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public void onVerified(String response) {
        Log.d("Forgot password OTp::", "Verified!\n" + response);
        Snackbar snackbar = Snackbar.make(parentview,"Verified",Snackbar.LENGTH_SHORT);
        snackbar.show();
        relativeLayoutMobInput.setVisibility(View.GONE);
        countDownTimer.cancel();
        relativeLayoutOtp.setVisibility(View.GONE);
        relativeLayoutPassword.setVisibility(View.VISIBLE);
    }

    @Override
    public void onVerificationFailed(Exception exception) {
        Log.e("ForgotPassword otp::", "Verification failed: " + exception.getMessage());
        countDownTimer.cancel();
        Snackbar snackbar = Snackbar.make(parentview,"Verification failed",Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
    public void submitPassword(View v){
        password=pass_et.getText().toString().trim();
        if(!password.equalsIgnoreCase("")){
        if(retypepass_et.getText().toString().trim().toString().equals(password)){
         register(phoneNo,md5(password));
        }
            else {

            Snackbar snackbar = Snackbar.make(parentview,"Passwords do not match!",Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
        }
        else {
            Snackbar snackbar = Snackbar.make(parentview,"Please fill all values!",Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }
    private void register(String mobile, String password) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            RegisterUserClass ruc = new RegisterUserClass();


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ForgotPassword.this, "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                if("".equals(s))
                {
                    s="server error Please try again after some time!";
                }
                else if("successfully registered".equalsIgnoreCase(s)){
                    s="password changed successfully!";
                    Intent myIntent = new Intent(ForgotPassword.this, MainActivity.class);
                    startActivity(myIntent);
                    overridePendingTransition(R.anim.next_slide_in, R.anim.next_slide_out);
                }
                else{
                    Log.d("forgotpasspassact str",s);
                    s="Server error Please contact our customer care support!";
                }

                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> data = new HashMap<String,String>();
                data.put("mobile",params[0]);
                data.put("password",params[1]);
                String result = ruc.sendPostRequest(MOBUPDATE_URL,data);
                return  result;
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(mobile, password);
    }
    public String md5(String s){
        MessageDigest digest ;
        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes(Charset.forName("US-ASCII")),0,s.length());
            byte[] magnitude = digest.digest();
            BigInteger bi  = new BigInteger(1,magnitude);
            String hash = String.format("%0" + (magnitude.length << 1) + "X" , bi);
            return hash;
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }

        return "";
    }
}

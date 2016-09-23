package com.travelwithpoolio;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.msg91.sendotp.library.Config;
import com.msg91.sendotp.library.SendOtpVerification;
import com.msg91.sendotp.library.Verification;
import com.msg91.sendotp.library.VerificationListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class OTP extends AppCompatActivity implements VerificationListener {
    Button button_sendOTP,verify_btn;
    EditText mphone,verify_text;
    String phoneNo;
    LinearLayout otp_layout;
    private Verification mVerification;
    int flag=0;
    private  String TAG ="OTP state:";
    CountDownTimer countDownTimer;
    String otp="";
    private final String MOBVER_URL = "http://www.poolio.in/pooqwerty123lio/mobverify.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        button_sendOTP = (Button) findViewById(R.id.btn_sendotp);
        mphone = (EditText) findViewById(R.id.input_number);
        otp_layout=(LinearLayout)findViewById(R.id.linear_otp);
        verify_text=(EditText)findViewById(R.id.input_otp);
        verify_btn=(Button)findViewById(R.id.btn_verify);


//hello
        button_sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!InternetConnectionClass.isConnected(getApplicationContext())){

                    Toast.makeText(OTP.this, "Please connect to the internet!", Toast.LENGTH_LONG).show();
                    return;
                }
                sendOTP();
            }
        });
        Log.i("Secret key:", getSecretKey());
        //Toast.makeText(OTP.this, getSecretKey(), Toast.LENGTH_LONG).show();
        otp_layout.setVisibility(View.INVISIBLE);
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

    void sendOTP()
    {   phoneNo = mphone.getText().toString().trim();
        if(!InternetConnectionClass.isConnected(getApplicationContext())){
            Toast.makeText(getApplicationContext(), "No Internet Connection!", Toast.LENGTH_LONG).show();
            return;
        }
        if(checkLength()) {
            char c = phoneNo.charAt(0);
            if ((c != '9') && (c != '8') && (c != '7')) {
                Toast.makeText(getApplicationContext(), "Invalid Mobile Number", Toast.LENGTH_LONG).show();
                mphone.setText("");
                return;
            }
            if(!InternetConnectionClass.isConnected(getApplicationContext())){
                Toast.makeText(OTP.this, "Please connect to the internet!", Toast.LENGTH_LONG).show();
                return;
            }
            alreadyExists(phoneNo);

        }
        else {
            mphone.setText("");
        }
    }
    public boolean checkLength() {
        if (mphone.getText().toString().length() == 10) {

            return true;
        } else {
            Toast.makeText(OTP.this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
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
                    //here code
                    Log.d("Message from server:"," proceed");
                    otp_layout.setVisibility(View.VISIBLE);
                    Config config = SendOtpVerification.config().context(getApplicationContext())
                            .build();
                    mVerification = SendOtpVerification.createSmsVerification(getApplicationContext(), phoneNo, OTP.this, "91");
                    mVerification.initiate();

                } else if("Mobile Number already exist".equalsIgnoreCase(s)) {
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                }
                else {
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
    public void submitOtp(View view) {

        if(flag==0){
            otp = verify_text.getText().toString();
            if (!otp.isEmpty()) {

                if (mVerification != null) {
                    Toast.makeText(OTP.this, "Verification in progress!", Toast.LENGTH_SHORT).show();
                    mVerification.verify(otp);

                }
                else {
                    Toast.makeText(OTP.this, "Please Enter Otp!", Toast.LENGTH_SHORT).show();
                }
            }}
        else if(flag==1){

            navigateToPasswordActivity();
        }
    }
    void navigateToPasswordActivity(){
        Intent myIntent = new Intent(this,SignUp.class);
        myIntent.putExtra("mobile", phoneNo);
        startActivity(myIntent);
        overridePendingTransition(R.anim.next_slide_in, R.anim.next_slide_out);
    }

    @Override
    public void onInitiated(String response) {
        Log.d(TAG, "Initialized!");
        //  showProgress();
        countDownTimer=new CountDownTimer(120000,20000){
            @Override
            public void onTick(long millisUntilFinished) {
                if(!InternetConnectionClass.isConnected(getApplicationContext())){
                    Toast.makeText(getApplicationContext(),"Connect to internet!",Toast.LENGTH_SHORT).show();
                    return;
                }

            }

            public void onFinish(){
                Toast.makeText(getApplicationContext(),"Wrong number or poor connection with server!",Toast.LENGTH_LONG).show();
            }
        }.start();

    }

    @Override
    public void onInitiationFailed(Exception exception) {
        Log.e(TAG, "Verification initialization failed: " + exception.getMessage());
        //  countDownTimer.cancel();
        Toast.makeText(getApplicationContext(),"Verification initialization failed! Please Try Again Later!",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onVerified(String response) {
        Log.d(TAG, "Verified!\n" + response);
        Toast.makeText(getApplicationContext(),"Verified",Toast.LENGTH_LONG).show();
        verify_btn.setText("Next!");
        countDownTimer.cancel();
        button_sendOTP.setVisibility(View.INVISIBLE);
        mphone.setVisibility(View.INVISIBLE);
        flag=1;
    }

    @Override
    public void onVerificationFailed(Exception exception) {
        Log.e(TAG, "Verification failed: " + exception.getMessage());
        countDownTimer.cancel();
        Toast.makeText(getApplicationContext(),"Verification failed!",Toast.LENGTH_LONG).show();
    }
}

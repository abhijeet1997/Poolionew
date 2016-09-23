package com.travelwithpoolio;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.onesignal.OneSignal;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;


public class SplashScreen extends Activity {
    MyCountdownTimer myCountdownTimer;
    Animation fadeIn;
//    private ImageView logo;
    public String NO_VAL="empty";
     //public final String SIGNIN_URL="http://192.168.1.6/poolio/signin.php";//Siddharth's pc
     public final String SIGNIN_URL="http://www.poolio.in/pooqwerty123lio/signin.php";//Sumit's pc
    public final String CONDITION_URL="http://www.poolio.in/pooqwerty123lio/conditions.php";//Sumit's pc
    private String password = null;
    int flag;
    ImageView logo;
    String heading, description;
    String mob,pass;
    Bundle temp;
    int version_get;
    int version_now=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        temp=savedInstanceState;
        logo=(ImageView) findViewById(R.id.logo_splash);
        OneSignal.startInit(this).init();
               getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
//        StartAnimations();
//        avi=(AVLoadingIndicatorView)findViewById(R.id.avi_splash);
        SharedPreferences session = getSharedPreferences("session", MODE_PRIVATE);
        mob = session.getString("mobile", NO_VAL);
        pass = session.getString("password", NO_VAL);
        Log.d("session::",mob+" "+pass);
        getCondition(CONDITION_URL);

        myCountdownTimer = new MyCountdownTimer(3000, 1000);

        myCountdownTimer.start();

//        logo = (ImageView)findViewById(R.id.logo);
//        fadeIn = new AlphaAnimation(0.0f , 1.0f ) ;
//        fadeIn.setDuration(2100);
//        fadeIn.setFillAfter(true);
//        logo.startAnimation(fadeIn);

    }

//    private void StartAnimations() {
//        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
//        anim.reset();
//        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
//        anim.reset();
//        ImageView iv = (ImageView) findViewById(R.id.logo);
//        iv.clearAnimation();
//        iv.startAnimation(anim);
//    }

    public class MyCountdownTimer extends CountDownTimer
    {

        public MyCountdownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onTick(long millisUntilFinished){
//         Log.d("Splash",String.valueOf(logo.getAlpha()));
//           if(logo.getAlpha()==0.3){
//               logo.animate().alpha((float) 1).setDuration(200).start();
//           }
//            else {
//               logo.animate().alpha((float)0.3).setDuration(200).start();
//           }
//            logo.animate().alpha(1.0f).setDuration(3000).start();

        }
        @Override

        public void onFinish() {

            myCountdownTimer.cancel();
            if (flag == 0) {

                goFurther();
            }
            else if(flag==1) {

                 Dialog dialog = new Dialog(SplashScreen.this);
                dialog.setTitle("UPDATE");
                dialog.setContentView(R.layout.customdialog);
                TextView headingtv = (TextView)dialog.findViewById(R.id.heading_tv);
                TextView descriptiontv = (TextView)dialog.findViewById(R.id.description_tv);
                headingtv.setText(heading);
                descriptiontv.setText(description);
                Button button_continue =(Button)dialog.findViewById(R.id.button_continue);
                Button button_download =(Button)dialog.findViewById(R.id.button_download);
                button_continue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        goFurther();
                    }
                });
                button_download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "https://play.google.com/store/apps/details?id=androarmy.torque&hl=en";
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        //pass the url to intent data
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }
                });
                dialog.show();

            }
            else if(flag==2 && version_get > version_now){
                Dialog dialog = new Dialog(SplashScreen.this);
                dialog.setTitle("UPDATE");
                dialog.setContentView(R.layout.customdialog2);
                TextView headingtv = (TextView)dialog.findViewById(R.id.heading_tv);
                TextView descriptiontv = (TextView)dialog.findViewById(R.id.description_tv);
                headingtv.setText(heading);
                descriptiontv.setText(description);
                Button button_close = (Button)dialog.findViewById(R.id.button_exit);
                button_close.setText("Update");
                button_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = "https://play.google.com/store/apps/details?id=com.zipyrides&hl=en";

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.addCategory(Intent.CATEGORY_BROWSABLE);

                        //pass the url to intent data
                        intent.setData(Uri.parse(url));

                        startActivity(intent);
                        finish();
                    }
                });
                dialog.show();


            }
            else
                goFurther();
        }

    }
    private void userLogin(final String mobile, final String password){
        class UserLoginClass extends AsyncTask<String,Void,String> {
//            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

//                loading = ProgressDialog.show(SplashScreen.this,"Signing in","Please wait while we connect to server",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                loading.dismiss();
                if("success".equalsIgnoreCase(s)){

                    Intent intent = new Intent(SplashScreen.this,Home.class);
                    intent.putExtra("mobile",mobile);
                    intent.putExtra("pass",pass);
                    startActivity(intent);
                    overridePendingTransition(R.anim.next_slide_in, R.anim.next_slide_out);
                }else{
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(),SignIn.class);
                    startActivity(intent);
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


    public void getCondition(String url) {

        class GetJSON extends AsyncTask<String, Void, String> {



            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //loading = ProgressDialog.show(getContext(),"Please Wait",null,true,true);
//                 avi.show();
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setReadTimeout(15000);
                    con.setConnectTimeout(15000);
                    con.setDoInput(true);
                    con.setDoOutput(true);

                    String response;

                    int responseCode = con.getResponseCode();

                    if (responseCode == HttpsURLConnection.HTTP_OK) {

                        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        response = br.readLine();
                        return response;
                    } else {

                        response = "Error";

                        return response;

                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                    return "Exception";
                }


            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //loading.dismiss();
//                avi.hide();


                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray result = jsonObject.getJSONArray("result");
                    JSONObject c = result.getJSONObject(0);
                    version_get=c.getInt("version");
                    flag = c.getInt("id");//flag=0 normal operation flag=1 update flag=2 force update
                    if(flag!=0)
                    {
                        heading=c.getString("heading");
                        description=c.getString("description");
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }
        GetJSON gj = new GetJSON();
        gj.execute(url);
    }

    void goFurther(){


        if (mob.equalsIgnoreCase(NO_VAL) || pass.equalsIgnoreCase(NO_VAL) || mob.equalsIgnoreCase("") || pass.equalsIgnoreCase("")) {
            Intent myIntent = new Intent(SplashScreen.this, WelcomeSlider.class);
            SplashScreen.this.finish();
            startActivity(myIntent);
            overridePendingTransition(R.anim.next_slide_in, R.anim.next_slide_out);
        } else {
            if(!InternetConnectionClass.isConnected(getApplicationContext())){
            Toast.makeText(SplashScreen.this, "Please connect to internet!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
            userLogin(mob, pass);
        }
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
    public void onResume(){
        super.onResume();
        onCreate(temp);
    }



}


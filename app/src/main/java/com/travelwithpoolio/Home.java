package com.travelwithpoolio;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    String password;
    String mobile;
    String first_name,last_name,gender,email,vehicle_name,vehicle_number,driving_license;
    String places,estancia,abode,archgate,maincampus,safaa,akshaya,airport,centralstation,egmorestation,backgate,greenpearl;
    SharedPreferences mSharedPreferences;
    TextView usernameheaderTV;
    TextView emailheaderTV;
    Fragment fragment = null;
    Class fragmentClass = null;
    String lon,lat;
    LocationManager locationManager;
    LocationListener ll;
    int flag;
    private int P_GRANTED=1;
    private boolean permission_OK=false;
    int i=0;
    int j=0;
    com.github.clans.fab.FloatingActionMenu fab;
    public String[][] rate;
    com.github.clans.fab.FloatingActionButton fab2;

    public final String PROFILE_URL ="http://www.poolio.in/pooqwerty123lio/profile.php";
    public final String RATE_URL ="http://www.poolio.in/pooqwerty123lio/rate_fetch.php";//Sumit's pc
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fab = (com.github.clans.fab.FloatingActionMenu) findViewById(R.id.fab_home);
        fab2=(com.github.clans.fab.FloatingActionButton)findViewById(R.id.material_design_floating_action_menu_item2);
        fab.showMenu(true);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        permissionCheck();
        Intent intent=getIntent();
        mobile =intent.getStringExtra("mobile");
        password= intent.getStringExtra("pass");
        SharedPreferences mSharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        String mob = mSharedPreferences.getString("mobile", "null");
        fetchDetails(mobile);
        fetchRates();
        SharedPreferences session = getSharedPreferences("session", MODE_PRIVATE);
        SharedPreferences.Editor editor=session.edit();
        editor.putString("mobile", mobile);
        editor.putString("password", password);
        editor.apply();
        mSharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        //Toast.makeText(Home.this, mSharedPreferences.getString("device id","0")+"", Toast.LENGTH_SHORT).show();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        if(!InternetConnectionClass.isConnected(getApplicationContext())){
            Toast.makeText(Home.this, "Please connect to the internet!", Toast.LENGTH_LONG).show();
        }
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //navigationView.getMenu().performIdentifierAction(R.id.flContent, 0);
        //navigationView.getMenu().getItem(0).setChecked(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.containerView, new TabFragment()).commit();//change
        toolbar.setTitle("Rides");

        View header=navigationView.getHeaderView(0);
/*View view=navigationView.inflateHeaderView(R.layout.nav_header_main);*/
        usernameheaderTV = (TextView)header.findViewById(R.id.txt_user);
        emailheaderTV = (TextView)header.findViewById(R.id.txt_email);

        Intent in = getIntent();
        String text = "a";
        text="a"+in.getStringExtra("switch");
        if(!"a".equals(text))
        {
            if(text.equalsIgnoreCase("aride"))//intent coming after adding vehicle number, vehile name and DL from profile.java
            {
                fragmentClass=TabFragment.class;
                try {

                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }if (fragment!=null){

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.containerView, fragment).commit();

            }
            }
            if(text.equalsIgnoreCase("amessage"))//intent coming after booking ride from Recylcer_view_adapter.java
            {
                fragmentClass=Messages.class;
                try {

                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }if (fragment!=null){

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.containerView, fragment).commit();

            }
            }

        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {


            SweetAlertDialog pDialog = new SweetAlertDialog(Home.this, SweetAlertDialog.WARNING_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#3F51B5"));
            pDialog .setTitleText("Do you want to Exit ?")
                    .setCancelText("No")
                    .setConfirmText("Yes")
                    .showCancelButton(true)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            intent.addCategory(Intent.CATEGORY_HOME);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.cancel();
                        }
                    });


            pDialog.show();
//            new AlertDialog.Builder(this).setIcon(R.drawable.dialog_alert_icon).setTitle("Exit")
//                    .setMessage("Are you sure?")
//                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                            Intent intent = new Intent(Intent.ACTION_MAIN);
//                            intent.addCategory(Intent.CATEGORY_HOME);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                            finish();
//
//                        }
//                    }).setNegativeButton("no", null).show();

        }
        overridePendingTransition(R.anim.previous_slide_in, R.anim.previous_slide_out);
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if(!InternetConnectionClass.isConnected(getApplicationContext())){
//            Toast.makeText(Home.this, "Please connect to the internet!", Toast.LENGTH_LONG).show();
//            return false;
//        }
        switch (id){
            case R.id.nav_find:
                fab.setVisibility(View.VISIBLE);
                fragmentClass= TabFragment.class;
                toolbar.setTitle("Find");
                break;
            case R.id.nav_myrides:
                fragmentClass= myRides.class;
                toolbar.setTitle("My Rides");
                break;
            case R.id.nav_profile:
                fragmentClass=profile.class;
                toolbar.setTitle("Profile");
                break;
            case R.id.nav_messages:
                fragmentClass = Messages.class;
                toolbar.setTitle("Messages");
                break;
            case R.id.rate_card:
                fragmentClass=RateCalculator.class;
                toolbar.setTitle("Rate Calculator");
                break;
            case R.id.nav_share:
                ShareIt();
                break;
            case R.id.nav_contact:
                fragmentClass = Contact_Us.class;
                toolbar.setTitle("Contact Us");

                break;

            case R.id.nav_feedback:
                fragmentClass = Feedback.class;
                toolbar.setTitle("Feedback");
                break;


            case R.id.sign_out:
                signout();
                break;
        }

        try {

            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }if (fragment!=null){

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.containerView, fragment).commit();

        }
        item.setChecked(true);
        setTitle(item.getTitle());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void signout(){
        SharedPreferences session= getSharedPreferences("session",MODE_PRIVATE);
        session.edit().clear().commit();
        SharedPreferences userdetails = getSharedPreferences("UserDetails",MODE_PRIVATE);
        userdetails.edit().clear().commit();
        Intent in= new Intent(this,MainActivity.class);
        startActivity(in);
        overridePendingTransition(R.anim.previous_slide_in, R.anim.previous_slide_out);
    }
    private void ShareIt(){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "POOLIO");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Do you know you can save and earn through POOLIO? download poolio's app now:"+" http://poolio.in/app");
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }


    private void fetchDetails(final String mobile){
        class fetchDetailsClass extends AsyncTask<String,Void,String> {
            //ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
              //  loading = ProgressDialog.show(getApplicationContext(),"Profile","Please wait while we connect to our server",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //loading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray result = jsonObject.getJSONArray("result");
                    JSONObject c = result.getJSONObject(0);

                    first_name=c.getString("first_name");
                    last_name=c.getString("last_name");
                    gender=c.getString("gender");
                    email=c.getString("email");
                    vehicle_name=c.getString("vehicle_name");
                    vehicle_number=c.getString("vehicle_number");
                    driving_license=c.getString("driving_license");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mSharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString("name",first_name+" "+last_name);
                editor.putString("gender",gender);
                editor.putString("mobile",mobile);
                editor.putString("email",email);
                editor.putString("vehicle_name",vehicle_name);
                editor.putString("vehicle_number",vehicle_number);
                editor.putString("driving_license",driving_license);
                editor.commit();

                usernameheaderTV.setText(first_name+" "+last_name);
                emailheaderTV.setText(email);


            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("mobile",params[0]);
                RegisterUserClass ruc = new RegisterUserClass();
                String result = ruc.sendPostRequest(PROFILE_URL,data);
                return result;
            }
        }
        fetchDetailsClass fdc = new fetchDetailsClass();
        fdc.execute(mobile);
    }
    private void fetchRates(){
        class fetchRatesClass extends AsyncTask<String,Void,String> {
            //ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //  loading = ProgressDialog.show(getApplicationContext(),"Profile","Please wait while we connect to our server",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.d("rates:",s);
                //loading.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray result = jsonObject.getJSONArray("result");
                    rate= new String[result.length()][result.length()+1];
                  //  Log.d("result.length()",""+result.length());
                  for(int i=0;i<result.length();i++) {
                      j=0;
                      JSONObject c = result.getJSONObject(i);
                      rate[i][j++]=c.getString("places");
                      rate[i][j++] = c.getString("estancia");
                      rate[i][j++] = c.getString("abode");
                      rate[i][j++] = c.getString("archgate");
                      rate[i][j++] = c.getString("backgate");
                      rate[i][j++] = c.getString("maincampus/station");
                      rate[i][j++] = c.getString("greenpearl");
                      rate[i][j++] = c.getString("safaa");
                      rate[i][j++] = c.getString("akshaya");
                      rate[i][j++] = c.getString("airport");
                      rate[i][j++] = c.getString("egmorestation");
                      rate[i][j++] = c.getString("centralstation");
                     // Log.d("j=",""+j);
                  }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                RateCalculator.rateChart=rate;
//                for(int i=0;i<rate.length;i++)
//                    for(int j=0;j<rate[i].length;j++){
//                        Log.d("rate"+i+"*"+j,rate[i][j]);
//                    }
        }

            @Override
            protected String doInBackground(String... params) {
                RegisterUserClass ruc = new RegisterUserClass();
                String result = ruc.sendPostRequestWithoutparams(RATE_URL);
                return result;
            }
        }
        fetchRatesClass frc = new fetchRatesClass();
        frc.execute();
    }
    public void cancel(View v) {
        fab.hideMenu(true);
    }

    public void policeSupport(View v) {
        String number = "tel:" + "7708519676";
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(number));
        intent.putExtra(Intent.EXTRA_PHONE_NUMBER, number);
        Intent chosenIntent = Intent.createChooser(intent, "Call to Police Station!");
        chosenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(chosenIntent);

    }

    public void customerCare(View v) {
        String number = "tel:" + "7708519676";
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(number));
        intent.putExtra(Intent.EXTRA_PHONE_NUMBER, number);
        Intent chosenIntent = Intent.createChooser(intent, "Call To Customer Care!");
        chosenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(chosenIntent);

    }


    public void shareYourLocation(View v) {
        try {
            lon="";
            lat="";
            Log.d("Permission tag",String.valueOf(permission_OK));
            if (permission_OK) if (isLocationServiceEnabled()) {
                locationManager = (LocationManager) getApplicationContext()
                        .getSystemService(LOCATION_SERVICE);
                ll = new LocationListener() {

                    @Override
                    public void onLocationChanged(Location location) {
                        lon = String.valueOf(location.getLongitude());
                        lat = String.valueOf(location.getLatitude());
                        shareloc(lon,lat);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                };
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    permissionCheck();
                    return;
                }
                Criteria criteria = new Criteria();
                // Getting the name of the provider that meets the criteria
                String provider = locationManager.getBestProvider(criteria, false);
                Location location = locationManager.getLastKnownLocation(provider);
                if (location != null) {
                    lon = String.valueOf(location.getLongitude());
                    lat = String.valueOf(location.getLatitude());
                    shareloc(lon,lat);
                }
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
                if ("".equalsIgnoreCase(lon) || "".equalsIgnoreCase("len")) {
                    Log.d("either lat or long", "::are null");
                    Toast.makeText(Home.this, "Please wait while we fetch your location.", Toast.LENGTH_SHORT).show();
                    return;
                }

            } else {
                Toast.makeText(Home.this, "Please turn on location services.", Toast.LENGTH_SHORT).show();
                buildAlertMessageNoGps();

            }
            else {
                Toast.makeText(Home.this, "Please give location permissions.", Toast.LENGTH_SHORT).show();
                permissionCheck();
            }
        } catch (Exception e) {
            Toast.makeText(Home.this, "failed", Toast.LENGTH_SHORT).show();
            Log.d("share your loc", "failed" + e.getMessage() + "Cause :" + e.getCause());
        }
    }
    public boolean isLocationServiceEnabled() {
        LocationManager locationManager = null;
        boolean gps_enabled = false, network_enabled = false;

        if (locationManager == null)
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            Toast.makeText(Home.this, "Location Service check failed.", Toast.LENGTH_SHORT).show();
        }

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            Toast.makeText(Home.this, "Location Service check failed.", Toast.LENGTH_SHORT).show();
        }

        return gps_enabled || network_enabled;

    }

    public void permissionCheck() {
        int hasLocationPermission = -1;
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

            if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
                Log.d("locationPermission", "added in list");
                listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);

            } else if(hasLocationPermission == PackageManager.PERMISSION_GRANTED) {
                permission_OK = true;
                Log.d("Permission tag","true");
            }
            if (!listPermissionsNeeded.isEmpty()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), P_GRANTED);
                }
                return;
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0) {
            if (permissions[i] == Manifest.permission.ACCESS_FINE_LOCATION) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("permissiongranted", "location");
                    permission_OK = true;
                } else {
                    finish();
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                        Toast.makeText(this, "This application needs permission to access location for security purpose!"
                                + "please allow it.", Toast.LENGTH_LONG).show();
                    }
                }
            }


        }
    }

    void shareloc(String lon, String lat) {
        locationManager.removeUpdates(ll);
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "poolio");
        String loc=String.format(Locale.ENGLISH, "http://maps.google.com/maps?daddr=%s,%s ", lon, lat);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "I am in DANGER. Please locate me at :" +loc+"\n"+ "(lon=" + lon + "" +
                ") (lat=" + lat+")\n");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog,  final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();

    }

}

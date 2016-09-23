package com.travelwithpoolio;

import android.*;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button button_signup, button_signin;
    com.github.clans.fab.FloatingActionMenu fab;
    String lon;
    String lat;
    CoordinatorLayout parentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (!InternetConnectionClass.isConnected(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "Please connect to the internet!", Toast.LENGTH_LONG).show();
        }
        button_signin = (Button) findViewById(R.id.btn_signin);
        button_signup = (Button) findViewById(R.id.btn_signup);
        fab = (com.github.clans.fab.FloatingActionMenu) findViewById(R.id.fab_actionmenu);
        fab.setVisibility(View.VISIBLE);
        parentView=(CoordinatorLayout)findViewById(R.id.mainact_layout);
        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignUpClick();
            }
        });

        button_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignInClick();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void onSignUpClick() {
        Intent intent = new Intent(MainActivity.this, OTP.class);
        startActivity(intent);
        overridePendingTransition(R.anim.next_slide_in, R.anim.next_slide_out);
    }

    void onSignInClick() {
        Intent intent = new Intent(MainActivity.this, SignIn.class);
        startActivity(intent);
        overridePendingTransition(R.anim.next_slide_in, R.anim.next_slide_out);
    }

    public void cancel(View v) {
        fab.setVisibility(View.GONE);
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

//        final LocationManager locationManager = (LocationManager) getApplicationContext()
//                .getSystemService(LOCATION_SERVICE);
//        String provider;
//        provider = locationManager.getBestProvider(new Criteria(), false);
//        Location location = locationManager.getLastKnownLocation(provider);
//        if (location != null) {
//            Log.i("location::", "achieved");
//        } else {
//            Log.i("location::", " not achieved");
//        }
//        LocationListener ll = new LocationListener() {
//
//            @Override
//            public void onLocationChanged(Location location) {
//                lon = String.valueOf(location.getLongitude());
//                lat = String.valueOf(location.getLatitude());
//
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//
//            }
//        };
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Snackbar.make(parentView,"Please give permissions",Snackbar.LENGTH_SHORT).show();
//            return;
//        }
//        locationManager.requestLocationUpdates(provider, 1000, 5, ll);
//        if("".equalsIgnoreCase(lon)||"".equalsIgnoreCase("len")){
//            Log.d("either lat or long","::are null");
//            return;
//        }
//        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//        sharingIntent.setType("text/plain");
//        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "poolio");
//        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "i am in DANGER. Please locate me at :" +
//                " lon="+lon+" lat="+lat);
//        locationManager.removeUpdates(ll);
//        startActivity(Intent.createChooser(sharingIntent, "Share via"));

    }

    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this).setIcon(R.drawable.dialog_alert_icon).setTitle("Exit")
                .setMessage("Are you sure?")
                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    }
                }).setNegativeButton("no", null).show();


        overridePendingTransition(R.anim.previous_slide_in, R.anim.previous_slide_out);
    }
    public void onResume(){
        super.onResume();

    }

}

package com.travelwithpoolio;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
    Button button_signup, button_signin;
    com.github.clans.fab.FloatingActionMenu fab;
    String lon="";
    String lat="";
    CoordinatorLayout parentView;
    private int P_GRANTED = 1;
    private boolean permission_OK = false;
    int i = 0;
    LocationListener ll;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        permissionCheck();
        if (!InternetConnectionClass.isConnected(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "Please connect to the internet!", Toast.LENGTH_LONG).show();
        }
        button_signin = (Button) findViewById(R.id.btn_signin);
        button_signup = (Button) findViewById(R.id.btn_signup);
        fab = (com.github.clans.fab.FloatingActionMenu) findViewById(R.id.fab_actionmenu);
        fab.showMenu(true);
        parentView = (CoordinatorLayout) findViewById(R.id.mainact_layout);
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
        // automatically handle clicks on the MainActivity/Up button, so long
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
                    Toast.makeText(MainActivity.this, "Please wait while we fetch your location.", Toast.LENGTH_SHORT).show();
                    return;
                }

            } else {
                Toast.makeText(MainActivity.this, "Please turn on location services.", Toast.LENGTH_SHORT).show();
                buildAlertMessageNoGps();

            }
            else {
                Toast.makeText(MainActivity.this, "Please give location permissions.", Toast.LENGTH_SHORT).show();
                permissionCheck();
            }
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();
            Log.d("share your loc", "failed" + e.getMessage() + "Cause :" + e.getCause());
        }
    }

    @Override
    public void onBackPressed() {


        SweetAlertDialog pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#3F51B5"));
        pDialog.setTitleText("Do you want to Exit ?")
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

//        new AlertDialog.Builder(this).setIcon(R.drawable.dialog_alert_icon).setTitle("Exit")
//                .setMessage("Are you sure?")
//                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        Intent intent = new Intent(Intent.ACTION_MAIN);
//                        intent.addCategory(Intent.CATEGORY_MainActivity);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        finish();
//
//                    }
//                }).setNegativeButton("no", null).show();


        overridePendingTransition(R.anim.previous_slide_in, R.anim.previous_slide_out);
    }

    public void onResume() {
        super.onResume();

    }

    public boolean isLocationServiceEnabled() {
        LocationManager locationManager = null;
        boolean gps_enabled = false, network_enabled = false;

        if (locationManager == null)
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            Toast.makeText(MainActivity.this, "Location Service check failed.", Toast.LENGTH_SHORT).show();
        }

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            Toast.makeText(MainActivity.this, "Location Service check failed.", Toast.LENGTH_SHORT).show();
        }

        return gps_enabled || network_enabled;

    }

    public void permissionCheck() {
        int hasLocationPermission = -1;
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hasLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

            if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
                Log.d("location", "permission granted");
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

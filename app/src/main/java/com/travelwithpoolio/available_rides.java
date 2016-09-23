package com.travelwithpoolio;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public class available_rides extends AppCompatActivity {
    String [] id,mobile, source, destination, type, date, time, vehicle_name,vehicle_number, seats, first_name, last_name, gender,device_id,msg;
    RecyclerView recyclerView;
    String drop;
//    SwipeRefreshLayout mSwipeRefreshLayout;
    WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    public final String FIND_URL="http://www.poolio.in/pooqwerty123lio/find.php";//Sumit's pc
    int refreshing=0;
    AVLoadingIndicatorView  avi;
    TextView sorryTV,infoTV;
    ImageView sorryIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_rides);
        Intent intent = getIntent();
        final String pickup= intent.getStringExtra("pickup");
         drop= intent.getStringExtra("drop");
        final String date= intent.getStringExtra("date");
        final String time= intent.getStringExtra("time");
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        sorryTV=(TextView)findViewById(R.id.sorrytv);
        infoTV=(TextView)findViewById(R.id.infotv);
        sorryIV=(ImageView)findViewById(R.id.sadimage);
       // Log.i("**PREVIOUS ACTIVITY**",pickup+" "+drop);
         avi=(AVLoadingIndicatorView)findViewById(R.id.avi);
        avi.setVisibility(View.GONE);
        findRide(pickup,drop,date,time);
        //Toast.makeText(getApplicationContext(),json,Toast.LENGTH_LONG).show();

      //  mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                // Do work to refresh the list here.
                //Log.d("**REFRESHING**","reffreshing");
                refreshing=1;
                findRide(pickup,drop,date,time);



            }
        });
    }
    public List<Data>fill_with_data(){

        List<Data> data  = new ArrayList<>();
        for (int i = 0 ; i<id.length ; i++){
            if (id[i]!=null) {
                //Log.e("**CHECKING**",source[0]+" "+ destination[0]+vehicle_name[0]);
                data.add(new Data(id[i],first_name[i] ,last_name[i],mobile[i],gender[i],source[i], destination[i],type[i],date[i],time[i],vehicle_name[i],vehicle_number[i],seats[i],device_id[i],msg[i]));
            }


        }
        return data;

    }
    void findRide(String pickup,String drop,String date, String time)
    {
        if(!InternetConnectionClass.isConnected(getApplicationContext())){
            Toast.makeText(getApplicationContext(), "Please connect to the internet!", Toast.LENGTH_LONG).show();
            return;
        }
        else
            fetchRides(pickup,drop,date,time);



    }

    private void fetchRides(final String pickup,final String drop,final String date, final String time){
        class fetchRideClass extends AsyncTask<String,Void,String> {
//            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if(refreshing!=1)
                {
//                    loading = ProgressDialog.show(available_rides.this,"Finding Your Rides" ,"Please wait while we connect to server",true,true);
                  avi.setVisibility(View.VISIBLE);
                  avi.show();
                }

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(refreshing!=1)
                {
                      avi.hide();
//                    loading.dismiss();

                }

                mWaveSwipeRefreshLayout.setRefreshing(false);

                //Toast.makeText(getContext(),s,Toast.LENGTH_LONG).show();
                //Log.i("***JSON***",s);
                showRides(s);
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("pickup",params[0]);
                data.put("drop",params[1]);
                data.put("date",params[2]);
                data.put("time",params[3]);

                RegisterUserClass ruc = new RegisterUserClass();
                String result = ruc.sendPostRequest(FIND_URL,data);
                return result;
            }
        }
        fetchRideClass frc = new fetchRideClass();
        frc.execute(pickup,drop,date,time);
    }

    private void showRides(String json){

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray("result");
            definearray(result.length());
            for (int i = 0 ; i<result.length() ; i++) {
                JSONObject c = result.getJSONObject(i);

                id[i]= c.getString("id");
                first_name[i]= c.getString("first_name");
                last_name[i]= c.getString("last_name");
                gender[i]= c.getString("gender");
                mobile[i] = c.getString("mobile");
                source [i]= c.getString("source");
                destination [i] = c.getString("destination");
                type [i]= c.getString("type");
                date  [i]= c.getString("date");
                time [i] = c.getString("time");
                vehicle_name [i] = c.getString("vehicle_name");
                vehicle_number [i] = c.getString("vehicle_number");
                seats [i]= c.getString("seats");
                device_id[i]=c.getString("device_id");
                msg[i]=c.getString("msg");
                //Toast.makeText(getApplicationContext(),id[i]+mobile[i],Toast.LENGTH_SHORT).show();
            }
            if(id.length==0)
                {
                    recyclerView.setVisibility(View.GONE);
                    sorryIV.setVisibility(View.VISIBLE);
                    sorryTV.setVisibility(View.VISIBLE);
                    infoTV.setVisibility(View.VISIBLE);
                }
            List<Data> data = fill_with_data();

            final Recycler_View_Adapter adapter  = new Recycler_View_Adapter(data , getApplication());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager( new LinearLayoutManager(getApplicationContext()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void definearray(int len)
    {
        id= new String[len];
        mobile= new String[len];
        source= new String[len];
        destination= new String[len];
        type= new String[len];
        date= new String[len];
        time= new String[len];
        vehicle_name= new String[len];
        vehicle_number= new String[len];
        seats= new String[len];
        first_name= new String[len];
        last_name= new String[len];
        gender= new String[len];
        device_id=new String[len];
        msg=new String[len];
    }
}

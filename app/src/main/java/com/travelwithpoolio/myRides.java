package com.travelwithpoolio;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;


public class myRides extends android.support.v4.app.Fragment {

    public final String FIND_URL="http://www.poolio.in/pooqwerty123lio/myrides.php";//Sumit's pc
    SharedPreferences mSharedPreferences;
    String mobile;
    String [] id,source, destination, type, date, time, vehicle_name,vehicle_number, seats,timestamp,status;
    RecyclerView recyclerView;
    public static AVLoadingIndicatorView avi;
    int refreshing=0;
    WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    TextView sorryTV;
    ImageView sorryIV;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_my_rides, container, false);


        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        sorryTV=(TextView)view.findViewById(R.id.sorrytv);
        sorryIV=(ImageView)view.findViewById(R.id.proud);
        mSharedPreferences = getActivity().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        mobile = mSharedPreferences.getString("mobile", "null");
        //Toast.makeText(getContext(),mobile,Toast.LENGTH_LONG).show();
        if(!InternetConnectionClass.isConnected(getActivity())){
            Snackbar snackbar = Snackbar.make(getView(),"Please connect to the internet!",Snackbar.LENGTH_SHORT);
            snackbar.show();
        }

        avi=(AVLoadingIndicatorView) view.findViewById(R.id.avi_myrides2);
        avi.setVisibility(View.GONE);
        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) view.findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                // Do work to refresh the list here.
                //Log.d("**REFRESHING**","reffreshing");
                refreshing=1;
                fetchMyRides(mobile);
            }
        });
        fetchMyRides(mobile);
        return view;
    }

    public List<Data>fill_with_data(){

        List<Data> data  = new ArrayList<>();
        for (int i = 0 ; i<id.length ; i++){
            if (id[i]!=null) {
                //Log.e("**CHECKING**",source[0]+" "+ destination[0]);
                data.add(new Data(id[i],source[i], destination[i],date[i],time[i],timestamp[i],status[i]));
            }

        }
        return data;

    }

    private void fetchMyRides(final String mobile){
        class fetchRideClass extends AsyncTask<String,Void,String> {
//            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                loading = ProgressDialog.show(getContext(),"Fetching Your Rides","Please wait while we connect to our server",true,true);
                if(refreshing!=1)
                {
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
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray result = jsonObject.getJSONArray("result");
                    definearray(result.length());
                    for (int i = 0 ; i<result.length() ; i++) {
                        JSONObject c = result.getJSONObject(i);

                        id[i]= c.getString("id");
                        source [i]= c.getString("source");
                        destination [i] = c.getString("destination");
                        type [i]= c.getString("type");
                        date  [i]= c.getString("date");
                        time [i] = c.getString("time");
                        vehicle_name [i] = c.getString("vehicle_name");
                        vehicle_number [i] = c.getString("vehicle_number");
                        seats [i]= c.getString("seats");
                        timestamp[i]=c.getString("offer_time");
                        status[i]=c.getString("status");
                        //Log.i("**STATUS**",id[i]+" "+status[i]);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(id.length==0)
                {
                    recyclerView.setVisibility(View.GONE);
                    sorryIV.setVisibility(View.VISIBLE);
                    sorryTV.setVisibility(View.VISIBLE);

                }
                List<Data> data = fill_with_data();

                final Recycler_View_Adapter2 adapter  = new Recycler_View_Adapter2(data , getActivity());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager( new LinearLayoutManager(getContext()));

            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("mobile",params[0]);

                RegisterUserClass ruc = new RegisterUserClass();
                String result = ruc.sendPostRequest(FIND_URL,data);
                return result;
            }
        }
        fetchRideClass frc = new fetchRideClass();
        frc.execute(mobile);
    }

    void definearray(int len)
    {
        id= new String[len];
        source= new String[len];
        destination= new String[len];
        type= new String[len];
        date= new String[len];
        time= new String[len];
        vehicle_name= new String[len];
        vehicle_number= new String[len];
        seats= new String[len];
        timestamp=new String[len];
        status=new String[len];
    }





}

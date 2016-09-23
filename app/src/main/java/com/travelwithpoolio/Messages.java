package com.travelwithpoolio;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


public class Messages extends android.support.v4.app.Fragment {
    View view;

    private String MESSAGE_URL = "http://www.poolio.in/pooqwerty123lio/messagefetch.php";
    SharedPreferences mSharedPreferences;
    RecyclerView recyclerView;
    private static String TAG = Messages.class.getSimpleName();
    private TextView message;
    ProgressDialog loading;
    String[] id ,messages , timestamp ,mobile_book; // booked mobile no.
    String mobile;
    AVLoadingIndicatorView avi;
    int refreshing=0;
    WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    TextView sorryTV;
    ImageView sorryIV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.fragment_messages, container, false);
        // Inflate the layout for this fragment

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        sorryTV=(TextView)view.findViewById(R.id.sorrytv);
        sorryIV=(ImageView)view.findViewById(R.id.proud);
        mSharedPreferences = getActivity().getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        mobile = mSharedPreferences.getString("mobile", "null");
        avi=(AVLoadingIndicatorView)view.findViewById(R.id.avi_msg);
        avi.setVisibility(View.GONE);
        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) view.findViewById(R.id.main_swipe);

        fetchMessages(mobile);
        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener() {
            @Override public void onRefresh() {
                // Do work to refresh the list here.
                //Log.d("**REFRESHING**","reffreshing");
                refreshing=1;
                fetchMessages(mobile);
            }
        });
        return view;
    }



    public List<Data> fill_with_data(){

        List<Data> data  = new ArrayList<>();
        for (int i = 0 ; i<messages.length ; i++){
            if (messages[i]!=null) {
                data.add(new Data(messages[i],mobile_book[i],timestamp[i]));
            }

        }
        return data;

    }

    private void fetchMessages(final String mobile){
        class fetchMessageClass extends AsyncTask<String,Void,String> {
//            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
//                loading = ProgressDialog.show(getContext(),"Fetching Your messages","Please wait while we connect to our server",true,true);
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
//                loading.dismiss();
                if(refreshing!=1)
                {
                    avi.hide();
//                    loading.dismiss();

                }

                mWaveSwipeRefreshLayout.setRefreshing(false);
                try {



                    JSONObject jsonObject =  new JSONObject(s);
                    JSONArray result = jsonObject.getJSONArray("result");
                    definearray(result.length());
                    for (int i = 0; i < result.length(); i++)
                    {

                        JSONObject c = result.getJSONObject(i);

                        messages[i] = c.getString("message");
                        mobile_book[i] = c.getString("mobile_book");
                        timestamp[i] = c.getString("timestamp");
                        //Log.d("***JSON***",messages[i]+" "+mobile_book[i]+" "+timestamp[i]);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                }
                List<Data> data = fill_with_data();
                if(messages.length==0)
                {
                    recyclerView.setVisibility(View.GONE);
                    sorryIV.setVisibility(View.VISIBLE);
                    sorryTV.setVisibility(View.VISIBLE);
                }
                final Recycler_View_Adapter_Message adapter  = new Recycler_View_Adapter_Message(data , getActivity());
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager( new LinearLayoutManager(getContext()));

            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> data = new HashMap<>();
                data.put("mobile",params[0]);
                RegisterUserClass ruc = new RegisterUserClass();
                String result = ruc.sendPostRequest(MESSAGE_URL,data);
                return result;
            }
        }
        fetchMessageClass fmc = new fetchMessageClass();
        fmc.execute(mobile);
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    void definearray(int len)
    {
        messages= new String[len];
        mobile_book = new String[len];
        timestamp=new String[len];
    }



}

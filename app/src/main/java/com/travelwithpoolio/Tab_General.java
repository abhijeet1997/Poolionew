package com.travelwithpoolio;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kjaganmohan on 18/09/16.
 */
public class Tab_General extends Fragment {

    TextView generalTv;


    public Tab_General() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View myInflatedView = inflater.inflate(R.layout.tab_general, container,false);

        Bundle data = getArguments();
        if (data!=null){
            List<String> dataList = data.getStringArrayList("general");
            Log.d(dataList.toString(),"data");
        }

        generalTv = (TextView) myInflatedView.findViewById(R.id.generaltv);
        generalTv.setText(data.toString());


        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return myInflatedView;
    }
}

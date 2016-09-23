package com.travelwithpoolio;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;


public class RateCalculator extends Fragment {
    public static String rateChart[][];

    public RateCalculator() {

    }

    String[] locations = {"SRM Arch Gate", "Abode Valley", "Estancia", "SRM Backgate", "Potheri Station/Main Campus", "Green Pearl", "Safa", "Akshaya", "Airport", "Central Station", "Egmore Station"};//need to make it dynamic
    AutoCompleteTextView sourceTV, destnationTV;
    String source, destination;
    TextView rateTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_rate_calculator, container, false);
        sourceTV = (AutoCompleteTextView) v.findViewById(R.id.sourcetv);
        destnationTV = (AutoCompleteTextView) v.findViewById(R.id.destinationtv);
        rateTV=(TextView)v.findViewById(R.id.ratetv);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item, locations);
        sourceTV.setThreshold(1);
        sourceTV.setAdapter(adapter);
        sourceTV.setTextColor(Color.RED);
        destnationTV.setThreshold(1);
        destnationTV.setAdapter(adapter);
        destnationTV.setTextColor(Color.RED);



        destnationTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                source = sourceTV.getText().toString();
                destination = destnationTV.getText().toString();
                if(source.equalsIgnoreCase(destination)){
                    Snackbar snackbar = Snackbar.make(getView(),"drop and pickup locations should be different.",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    return;
                }
                if ("".equals(source))
                    return;
                Log.d("rate:",""+getRate(source, destination));
                rateTV.setText("₹"+getRate(source,destination));

            }
        });
        sourceTV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                source = sourceTV.getText().toString();
                destination = destnationTV.getText().toString();
                if(source.equalsIgnoreCase(destination)){
                    Snackbar snackbar = Snackbar.make(getView(),"source and destination locations should be different.",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                    return;
                }
                if ("".equals(destination))
                    return;
                Log.d("rate:",getRate(source, destination));
                rateTV.setText("₹"+getRate(source,destination));
            }
        });

        return v;
    }

    public String getRate(String source, String destination)
    {
        source = source.toLowerCase().replaceAll("\\s", "");
        if(source.equalsIgnoreCase("potheristation/maincampus")){
            source="station/maincampus";
        }
        destination = destination.toLowerCase().replaceAll("\\s", "");
        Log.d("*SOURCE**DESTINATION*",""+source+"*"+destination);
        int index = 0;
        if (rateChart != null) {
            Log.d("in switch","true");
            switch (destination) {
                case "estancia":
                    index = 1;
                    break;
                case "abodevalley":
                    index = 2;
                    break;
                case "srmarchgate":
                    index = 3;
                    break;
                case "srmbackgate":
                    index = 4;
                    break;
                case "potheristation/maincampus":
                    index = 5;
                    break;
                case "greenpearl":
                    index = 6;
                    break;
                case "safa":
                    index = 7;
                    break;
                case "akshaya":
                    index = 8;
                    break;
                case "airport":
                    index = 9;
                    break;
                case "egmorestation":
                    index = 10;
                    break;
                case "centralstation":
                    index = 11;
                    break;
            }
            int postindex = 0;
            for (int i = 0; i < 11; i++) {
                if (source.equalsIgnoreCase(rateChart[i][0])) {
                    postindex = i;
                    break;
                }
            }
            Log.d("loc::",index+","+postindex);
            if (rateChart[postindex][index].equalsIgnoreCase("estancia")) {
                return "rate calculator failed";
            } else {
                return rateChart[postindex][index];
            }
        }
        return null;
    }


}

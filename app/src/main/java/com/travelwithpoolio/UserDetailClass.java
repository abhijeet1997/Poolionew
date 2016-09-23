package com.travelwithpoolio;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lenovo on 13-02-2016.
 */
public class UserDetailClass {
    public String sendGetRequestParam(String requestURL, String mobile){
        StringBuilder sb =new StringBuilder();
        try {
            URL url = new URL(requestURL+mobile);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String s;
            while((s=bufferedReader.readLine())!=null){
                sb.append(s+"\n");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }
}

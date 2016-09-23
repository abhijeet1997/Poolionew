package com.travelwithpoolio;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;




public class Faq extends AppCompatActivity {


    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.general,
            R.drawable.security,
            R.drawable.code
    };

    public String General_faq , Security_faq ,Technical_faq;
    public String[] faqQue =null , faqAns=null;
    public String[] SecurityfaqQue=null , SecurityfaqAns = null;
    public  String[] TechnicalfaqQue =null, TechnicalfaqAns = null;
    ArrayList<String> quest = new ArrayList<String>();
    ArrayList<String> ans = new ArrayList<String>();

    ArrayList<String> securityquest = new ArrayList<String>();
    ArrayList<String> securityans = new ArrayList<String>();


    ArrayList<String> techquest = new ArrayList<String>();
    ArrayList<String> techans = new ArrayList<String>();










    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_faq);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("FAQ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        new Faqs().execute("http://www.poolio.in/pooqwerty123lio/FAQ.json");


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();






    }

    private void setupTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("      General");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.general, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("       Security");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.security, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("    Technical");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.code, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag( new Tab_General(), "General");
        adapter.addFrag(new Tab_Security(), "Security");
        adapter.addFrag(new Tab_Technical(), "Technical");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(getApplicationContext(),Home.class);
            startActivity(intent);
            overridePendingTransition(R.anim.next_slide_in, R.anim.next_slide_out);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),Home.class);
        startActivity(intent);
        overridePendingTransition(R.anim.next_slide_in, R.anim.next_slide_out);

    }


    public class Faqs extends AsyncTask<String , Void ,String> {
        String server_response;

        @Override
        protected String doInBackground(String... strings) {

            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                int responseCode = urlConnection.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK){
                    server_response = readStream(urlConnection.getInputStream());
                    Log.v("CatalogClient", server_response);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.e("Response", "" + server_response);

            if (server_response!=null){
                try {

                    JSONObject jsonObj = new JSONObject(server_response);
                    JSONArray categories = jsonObj.getJSONArray("categories");
                    for (int i = 0; i < categories.length(); i++) {

                        JSONObject c = categories.getJSONObject(i);
                        String title = c.getString("title");




                        JSONArray posts = c.getJSONArray("posts");
                        if (title.equals("general")) {
                            General_faq = title;

                            for (int j = 0; j < posts.length(); j++) {

                                JSONObject foo = posts.getJSONObject(j);

                                String question = foo.getString("question");
                                String answer = foo.getString("answer");
                                Log.d("question",question);
//
//                                faqQue[j] = question;
//                                faqAns[j] = answer;
                                quest.add(question);
                                ans.add(answer);


                            }
                        }

                            else if(title.equals("security")){

                                Security_faq = title;

                            for (int j = 0; j < posts.length(); j++) {

                                JSONObject foo = posts.getJSONObject(j);

                                String question = foo.getString("question");
                                String answer = foo.getString("answer");

//                                SecurityfaqQue[j] = question;
//                                SecurityfaqAns[j] = answer;
                                securityquest.add(question);
                                securityans.add(answer);


                            }

                            }
                            else if (title.equals("technical")){
                                Technical_faq = title;

                            for (int j = 0; j < posts.length(); j++) {

                                JSONObject foo = posts.getJSONObject(j);

                                String question = foo.getString("question");
                                String answer = foo.getString("answer");

//                                TechnicalfaqQue[j] = question;
//                                TechnicalfaqAns[j] = answer;

                                techquest.add(question);
                                techans.add(answer);


                            }


                        }




                    }

                    Bundle data = new Bundle();
                    data.putStringArrayList("general",quest);
                    Log.d("general array",quest.toString());
                    Fragment f  = new Fragment();
                    f.setArguments(data);




                }
                catch (JSONException e){

                }
            }


        }
    }

// Converting InputStream to String

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }



}

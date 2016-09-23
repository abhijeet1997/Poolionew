package com.travelwithpoolio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EnterOTP extends AppCompatActivity {
    Button buttn_next;
    EditText otp;
    String mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);
        otp = (EditText)findViewById(R.id.input_otp);
        buttn_next = (Button) findViewById(R.id.btn_next);
        buttn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!InternetConnectionClass.isConnected(getApplicationContext())){
                    Toast.makeText(EnterOTP.this, "Please connect to the internet!", Toast.LENGTH_LONG).show();
                    return;
                }
                onNextClick();

            }
        });
        Intent in = getIntent();
        mobile= in.getStringExtra("mobile");
    }

    void onNextClick()
    {
        Intent intent = new Intent(EnterOTP.this,SignUp.class);
        intent.putExtra("mobile",mobile);
        startActivity(intent);
        overridePendingTransition(R.anim.next_slide_in,R.anim.next_slide_out);
    }
}

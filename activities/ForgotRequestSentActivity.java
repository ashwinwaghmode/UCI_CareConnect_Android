package com.devool.ucicareconnect.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.devool.ucicareconnect.R;

public class ForgotRequestSentActivity extends AppCompatActivity {

    TextView tvheading, tvRequestSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_request_sent);
        tvheading = findViewById(R.id.tv_heading);
        tvRequestSubject = findViewById(R.id.tv_request_subject);

        if(getIntent().hasExtra("flag_lost_activation")){
            tvheading.setText("Retrieve Activation Code");
            tvRequestSubject.setText("Your activation code has been sent to your email");
        }else if(getIntent().hasExtra("flag_forgot_username")){
            tvheading.setText("Username Retrieved");
            tvRequestSubject.setText("Your username has been sent to your email");
        }else if(getIntent().hasExtra("flag_forgot_password")){
            tvheading.setText("Password Retrieved");
            tvRequestSubject.setText("Your request has been sent to your email");
        }
    }
}

package com.devool.ucicareconnect.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.devool.ucicareconnect.R;

import org.w3c.dom.Text;

public class WelcomeActivityAfterLogin extends AppCompatActivity implements View.OnClickListener{

    SharedPreferences sharedpreferences;
    Button btnNext;
    public static final String USER_INFO= "user_info" ;
    TextView tvUserName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_after_login);
        btnNext = findViewById(R.id.btn_next);
        tvUserName = findViewById(R.id.tv_user_name);
        btnNext.setOnClickListener(this);

        sharedpreferences = getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();

        tvUserName.setText("Hi " + sharedpreferences.getString("USER_NAME", "")+ ",");
        editor.putString("USER_NAME_FROM_ACTIVATION_CODE", sharedpreferences.getString("USER_NAME", ""));
        editor.commit();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                startActivity(new Intent(WelcomeActivityAfterLogin.this, UserNameActivity.class));
                break;


        }
    }
}

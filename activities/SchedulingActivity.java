package com.devool.ucicareconnect.activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.devool.ucicareconnect.R;
import com.devool.ucicareconnect.fragments.AppoinmentTypeFragment;
import com.devool.ucicareconnect.fragments.NewAppointmentFragment;
import com.devool.ucicareconnect.fragments.ReferralNameFragment;
import com.devool.ucicareconnect.fragments.ReferralRelationship;

public class SchedulingActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnNewppointment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduling);

        if(getIntent().getExtras().getString("Interaction_Type_ID").equalsIgnoreCase("1")){

        }else if(getIntent().getExtras().getString("Interaction_Type_ID").equalsIgnoreCase("2")){
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            AppoinmentTypeFragment fragment = new AppoinmentTypeFragment();
            fragmentTransaction.add(R.id.myContainer, fragment);
            fragmentTransaction.commit();
        }else if(getIntent().getExtras().getString("Interaction_Type_ID").equalsIgnoreCase("3")){

        }else if(getIntent().getExtras().getString("Interaction_Type_ID").equalsIgnoreCase("4")){
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            ReferralRelationship fragment = new ReferralRelationship();
            fragmentTransaction.add(R.id.myContainer, fragment);
            fragmentTransaction.commit();
        }




        //btnNewppointment = (Button)findViewById(R.id.btn_new_appointment);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //case R.id.btn_new_appointment:

              //  break;
        }

    }
}

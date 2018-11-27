package com.devool.ucicareconnect.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.devool.ucicareconnect.R;

public class CancelFollowUpActivity extends AppCompatActivity implements View.OnClickListener{

    TextView tvAppointmentType, tvAppointmentName, tvAvailability, tvTImeofDay,  tvSpecificRequest, tvSpecificRequestTitle;
    ImageView imgCloseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_follow_up);
        tvAppointmentType = findViewById(R.id.tv_appointment_type);
        tvAppointmentName = findViewById(R.id.tv_followup_name);
        tvAvailability = findViewById(R.id.tv_availability);
        tvTImeofDay = findViewById(R.id.tv_time_of_day);
        tvSpecificRequest = findViewById(R.id.tv_specific_request);
        tvSpecificRequestTitle = findViewById(R.id.tv_specific_request_title);
        imgCloseButton = findViewById(R.id.img_close_button);

        imgCloseButton.setOnClickListener(this);

        tvAppointmentType.setText(getIntent().getExtras().getString("appointment_type"));
        tvAppointmentName.setText(getIntent().getExtras().getString("Physicians_Name"));
        tvAvailability.setText(getIntent().getExtras().getString("Availability"));
        tvTImeofDay.setText(getIntent().getExtras().getString("Time_of_day"));

        if (getIntent().getExtras().getString("Any_Specific_Request") != null && !getIntent().getExtras().getString("Any_Specific_Request").isEmpty()) {
            tvSpecificRequest.setText(getIntent().getExtras().getString("Any_Specific_Request"));
            tvSpecificRequest.setVisibility(View.VISIBLE);
            tvSpecificRequestTitle.setVisibility(View.VISIBLE);
        } else {
            tvSpecificRequest.setVisibility(View.GONE);
            tvSpecificRequestTitle.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_close_button:
                Intent intent = new Intent(this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
        }
    }
}

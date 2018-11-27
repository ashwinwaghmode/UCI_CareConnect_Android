package com.devool.ucicareconnect.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.devool.ucicareconnect.R;
import com.devool.ucicareconnect.utils.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class CancelAppointmentActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvAppointmentType, tvAppointmentName, tvExmaType, tvRequestBy, tvPhysicianName, tvPhysicianType, tvAvailability, tvTImeofDay, tvSpecificRequest, tvSpecificRequestTitle;
    TextView tvExamTypeHeading, tvQuestedByHeading, tvPhysicianNameHeading, tvPhysicianTypeHeading;
    String strRequest;
    Intent i;
    ImageView imgCloseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_appointment);

        tvAppointmentType = findViewById(R.id.tv_appointment_type);
        tvAppointmentName = findViewById(R.id.tv_appointment_name);
        tvExmaType = findViewById(R.id.tv_exam_type);
        tvRequestBy = findViewById(R.id.tv_request_by);
        tvPhysicianName = findViewById(R.id.tv_type_name);
        tvPhysicianType = findViewById(R.id.tv_physician_type);
        tvAvailability = findViewById(R.id.tv_availability);
        tvTImeofDay = findViewById(R.id.tv_time_of_day);
        tvExamTypeHeading = findViewById(R.id.tv_examm_type_heading);
        tvQuestedByHeading = findViewById(R.id.tv_requested_by_heading);
        tvPhysicianNameHeading = findViewById(R.id.tv_physician_name_heading);
        tvPhysicianTypeHeading = findViewById(R.id.tv_physician_type_heading);
        tvSpecificRequest = findViewById(R.id.tv_specific_request);
        tvSpecificRequestTitle = findViewById(R.id.tv_specific_request_title);
        imgCloseButton = findViewById(R.id.img_close_button);

        imgCloseButton.setOnClickListener(this);

        i = getIntent();
        strRequest = i.getStringExtra("request");

       /* if (strRequest.equalsIgnoreCase("Lab")) {
            tvAppointmentType.setText(getIntent().getExtras().getString("appointment_type"));
            tvAppointmentName.setText(getIntent().getExtras().getString("Speciality"));
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

            tvPhysicianNameHeading.setVisibility(View.GONE);
            tvPhysicianName.setVisibility(View.GONE);
            tvPhysicianType.setVisibility(View.GONE);
            tvPhysicianTypeHeading.setVisibility(View.GONE);
            tvExmaType.setVisibility(View.GONE);
            tvExamTypeHeading.setVisibility(View.GONE);
            tvRequestBy.setVisibility(View.GONE);
            tvQuestedByHeading.setVisibility(View.GONE);
        } else if (strRequest.equalsIgnoreCase("Physician")) {
            tvAppointmentType.setText(getIntent().getExtras().getString("appointment_type"));
            tvAppointmentName.setText(getIntent().getExtras().getString("Speciality"));
            tvAvailability.setText(getIntent().getExtras().getString("Availability"));
            tvTImeofDay.setText(getIntent().getExtras().getString("Time_of_day"));
            tvPhysicianName.setText(getIntent().getExtras().getString("Physicians_Name"));
            tvPhysicianType.setText(getIntent().getExtras().getString("Physicians_Specialty"));
            tvPhysicianNameHeading.setVisibility(View.VISIBLE);
            tvPhysicianName.setVisibility(View.VISIBLE);
            tvPhysicianType.setVisibility(View.VISIBLE);
            tvPhysicianTypeHeading.setVisibility(View.VISIBLE);

            if (getIntent().getExtras().getString("Any_Specific_Request") != null && !getIntent().getExtras().getString("Any_Specific_Request").isEmpty()) {
                tvSpecificRequest.setText(getIntent().getExtras().getString("Any_Specific_Request"));
                tvSpecificRequest.setVisibility(View.VISIBLE);
                tvSpecificRequestTitle.setVisibility(View.VISIBLE);
            } else {
                tvSpecificRequest.setVisibility(View.GONE);
                tvSpecificRequestTitle.setVisibility(View.GONE);
            }
        } else if (strRequest.equalsIgnoreCase("Radiology / Diagnostics")) {
            tvAppointmentType.setText(getIntent().getExtras().getString("appointment_type"));
            tvAppointmentName.setText(getIntent().getExtras().getString("Speciality"));
            tvAvailability.setText(getIntent().getExtras().getString("Availability"));
            tvTImeofDay.setText(getIntent().getExtras().getString("Time_of_day"));
            tvExmaType.setText(getIntent().getExtras().getString("Radiology_Type"));
            tvRequestBy.setText(getIntent().getExtras().getString("Requested_By"));
            tvExmaType.setVisibility(View.VISIBLE);
            tvExamTypeHeading.setVisibility(View.VISIBLE);
            tvRequestBy.setVisibility(View.VISIBLE);
            tvQuestedByHeading.setVisibility(View.VISIBLE);
            tvPhysicianNameHeading.setVisibility(View.GONE);
            tvPhysicianName.setVisibility(View.GONE);
            tvPhysicianType.setVisibility(View.GONE);
            tvPhysicianTypeHeading.setVisibility(View.GONE);

            if (getIntent().getExtras().getString("Any_Specific_Request") != null && !getIntent().getExtras().getString("Any_Specific_Request").isEmpty()) {
                tvSpecificRequest.setText(getIntent().getExtras().getString("Any_Specific_Request"));
                tvSpecificRequest.setVisibility(View.VISIBLE);
                tvSpecificRequestTitle.setVisibility(View.VISIBLE);
            } else {
                tvSpecificRequest.setVisibility(View.GONE);
                tvSpecificRequestTitle.setVisibility(View.GONE);
            }
        }*/

        getAppointmentRequestDetail();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close_button:
                Intent intent = new Intent(this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
        }
    }

    public void getAppointmentRequestDetail() {
        RequestQueue requestQueue = Volley.newRequestQueue(CancelAppointmentActivity.this);
        String URL = AppConfig.BASE_URL + AppConfig.GET_APPOINTMENT_REQUESTED + "/" + getIntent().getExtras().getString("event_id");
        JSONObject jsonBody = new JSONObject();

        final String requestBody = jsonBody.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("getUserInfo", response);
                    JSONObject object = new JSONObject(response);
                    tvAppointmentType.setText(object.getString("appointment_Type"));
                    if (object.getString("speciality").equals("1")) {
                        tvAppointmentName.setText("Physician");
                    } else if (object.getString("speciality").equals("2")) {
                        tvAppointmentName.setText("Radiology / Diagnostics");
                    } else if (object.getString("speciality").equals("3")) {
                        tvAppointmentName.setText("Lab");
                    }
                    tvAvailability.setText(object.getString("availability"));
                    tvTImeofDay.setText(object.getString("time_of_day"));
                    if (object.isNull("any_Specific_Request") || object.getString("any_Specific_Request").equals("")) {
                        tvSpecificRequest.setVisibility(View.GONE);
                        tvSpecificRequestTitle.setVisibility(View.GONE);
                    } else {
                        tvSpecificRequest.setText(object.getString("any_Specific_Request"));
                        tvSpecificRequest.setVisibility(View.VISIBLE);
                        tvSpecificRequestTitle.setVisibility(View.VISIBLE);
                    }
                    if (object.isNull("physicians_Name") || object.getString("physicians_Name").equals("")) {
                        tvPhysicianName.setVisibility(View.GONE);
                        tvPhysicianNameHeading.setVisibility(View.GONE);
                    } else {
                        tvPhysicianName.setText(object.getString("physicians_Name"));
                        tvPhysicianName.setVisibility(View.VISIBLE);
                        tvPhysicianNameHeading.setVisibility(View.VISIBLE);
                    }
                    if (object.isNull("physicians_Specialty") || object.getString("physicians_Specialty").equals("")) {
                        tvPhysicianType.setVisibility(View.GONE);
                        tvPhysicianTypeHeading.setVisibility(View.GONE);
                    } else {
                        tvPhysicianType.setText(object.getString("physicians_Specialty"));
                        tvPhysicianType.setVisibility(View.VISIBLE);
                        tvPhysicianTypeHeading.setVisibility(View.VISIBLE);
                    }
                    if (object.isNull("radiology_Type") || object.getString("radiology_Type").equals("")) {
                        tvExmaType.setVisibility(View.GONE);
                        tvExamTypeHeading.setVisibility(View.GONE);
                    } else {
                        tvExmaType.setText(object.getString("radiology_Type"));
                        tvExmaType.setVisibility(View.VISIBLE);
                        tvExamTypeHeading.setVisibility(View.VISIBLE);
                    }
                    if (object.isNull("requested_By") || object.getString("requested_By").equals("")) {
                        tvRequestBy.setVisibility(View.GONE);
                        tvQuestedByHeading.setVisibility(View.GONE);
                    } else {
                        tvRequestBy.setText(object.getString("requested_By"));
                        tvRequestBy.setVisibility(View.VISIBLE);
                        tvQuestedByHeading.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VOLLEY", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);

                }
                return super.parseNetworkResponse(response);
            }
        };

        requestQueue.add(stringRequest);
    }
}

package com.devool.ucicareconnect.fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.devool.ucicareconnect.activities.DashboardActivity;
import com.devool.ucicareconnect.utils.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class FollowupAppointmentRequest extends Fragment implements View.OnClickListener {

    public static final String USER_INFO = "user_info";
    String StrAppointmentType, strDoctorName, strFollowupAvailability, strFollowupTimeofDay, strAnySpecificRequest, strInteractionId, strInteractionDetailId, strUserId;
    TextView tvAppointmentType, tvDoctorName, tvFollowupAvailability, tvFollowupTimeOfDay, tvSpecificRequest, tvSpecificRequestTitle;
    Button btnSendRequest, btnAnySpecificRequest;
    ImageView imgCloseButton, imgBtnCall, imgBtnChat;
    SharedPreferences sharedpreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            StrAppointmentType = getArguments().getString("appointment_type");
            strDoctorName = getArguments().getString("physician_name");
            strFollowupAvailability = getArguments().getString("followup_availability");
            strFollowupTimeofDay = getArguments().getString("followup_time_of_day");
            strAnySpecificRequest = getArguments().getString("Any_Specific_Request");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View row = inflater.inflate(R.layout.fragment_followup_appointment_request, container, false);
        tvAppointmentType = row.findViewById(R.id.tv_appointment_type);
        tvDoctorName = row.findViewById(R.id.tv_doctor_name);
        tvFollowupAvailability = row.findViewById(R.id.tv_availability);
        tvFollowupTimeOfDay = row.findViewById(R.id.tv_time_of_day);
        tvSpecificRequest = row.findViewById(R.id.tv_specific_request);
        tvSpecificRequestTitle = row.findViewById(R.id.tv_specific_request_title);
        btnAnySpecificRequest = row.findViewById(R.id.btn_any_specific_request);
        btnSendRequest = row.findViewById(R.id.btn_send_request);
        imgCloseButton = row.findViewById(R.id.img_close_button);
        imgBtnCall = row.findViewById(R.id.img_btn_call);
        imgBtnChat = row.findViewById(R.id.img_btn_chat);

        tvAppointmentType.setText(StrAppointmentType);
        tvDoctorName.setText(strDoctorName);
        tvFollowupAvailability.setText(strFollowupAvailability);
        tvFollowupTimeOfDay.setText(strFollowupTimeofDay);


        if (strAnySpecificRequest != null && !strAnySpecificRequest.isEmpty()) {
            tvSpecificRequest.setVisibility(View.VISIBLE);
            tvSpecificRequestTitle.setVisibility(View.VISIBLE);
            tvSpecificRequest.setText(strAnySpecificRequest);
        } else {
            tvSpecificRequest.setVisibility(View.GONE);
            tvSpecificRequestTitle.setVisibility(View.GONE);
        }

        sharedpreferences = getActivity().getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        strInteractionDetailId = sharedpreferences.getString("interaction_DTL_ID", "");
        strInteractionId = sharedpreferences.getString("interaction_ID", "");
        strUserId = sharedpreferences.getString("USER_ID", "");

        btnSendRequest.setOnClickListener(this);
        imgCloseButton.setOnClickListener(this);
        btnAnySpecificRequest.setOnClickListener(this);
        imgBtnChat.setOnClickListener(this);
        imgBtnCall.setOnClickListener(this);
        return row;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_any_specific_request:
                btnAnySpecificRequest.setBackground(getResources().getDrawable(R.drawable.fill_appointment_button_corner));
                btnAnySpecificRequest.setTextColor(getResources().getColor(R.color.btn_text_color));
                btnAnySpecificRequest.setBackground(getResources().getDrawable(R.drawable.fill_appointment_button_corner));
                btnAnySpecificRequest.setTextColor(getResources().getColor(R.color.btn_text_color));
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                AnySpecificRequestFragmrent fragment = new AnySpecificRequestFragmrent();
                Bundle args = new Bundle();
                args.putString("appointment_type", StrAppointmentType);
                args.putString("physician_name", strDoctorName);
                args.putString("followup_availability", strFollowupAvailability);
                args.putString("followup_time_of_day", strFollowupTimeofDay);
                args.putString("followup_flag", "followup");
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.myContainer, fragment);
                fragmentTransaction.commit();
                fragmentTransaction.addToBackStack(null);
                break;
            case R.id.btn_send_request:
                submitLabRequest();
                btnSendRequest.setBackground(getResources().getDrawable(R.drawable.fill_appointment_button_corner));
                btnSendRequest.setTextColor(getResources().getColor(R.color.btn_text_color));
                break;
            case R.id.img_close_button:
                Intent intent = new Intent(getActivity(), DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.img_btn_call:
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:(866) 698-2422"));
                startActivity(i);
                break;
        }
    }

    public void submitLabRequest() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            String URL = AppConfig.BASE_URL + AppConfig.CREATE_REQUEST_HISTORY;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("Interaction_DTL_ID", strInteractionDetailId);
            jsonBody.put("UserID", strUserId);
            jsonBody.put("status_Id", "Y");
            jsonBody.put("Appointment_Type", StrAppointmentType);
            jsonBody.put("Interaction_ID", strInteractionId);
            jsonBody.put("Physicians_Name", strDoctorName);
            jsonBody.put("Availability", strFollowupAvailability);
            jsonBody.put("Time_of_day", strFollowupTimeofDay);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("VOLLEY_PHYSICIAN_RESONS", response);
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(0);
                            if (object.getString("inMsg").equalsIgnoreCase("Request Saved!!!")) {
                                Intent intent = new Intent(getActivity(), DashboardActivity.class);
                                intent.putExtra("followup_request_flag", "followup_request");
                                intent.putExtra("appointment_type", StrAppointmentType);
                                intent.putExtra("Physicians_Name", strDoctorName);
                                intent.putExtra("Availability", strFollowupAvailability);
                                intent.putExtra("Time_of_day", strFollowupTimeofDay);
                                intent.putExtra("Any_Specific_Request", strAnySpecificRequest);
                                startActivity(intent);
                            }
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
                        // can get more details such as response.headers
                    }
                    return super.parseNetworkResponse(response);
                }
            };

            requestQueue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

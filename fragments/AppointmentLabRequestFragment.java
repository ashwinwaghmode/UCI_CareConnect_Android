package com.devool.ucicareconnect.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

public class AppointmentLabRequestFragment extends Fragment implements View.OnClickListener {

    TextView tvAppointmentType, tvNewAppointName, tvAvailability, tvTimeOfDay, tvSpecificRequest, tvSpecificRequestTitle;
    String strAppointmentType, strMeetPurpose, strAvailability, strDay, strInteractionId, strInteractionDetailId, strUserId, strAnySpecificRequest;
    Button btnSendRequest, btnAnySpecificRequest;
    SharedPreferences sharedpreferences;
    public static final String USER_INFO = "user_info";
    ImageView imgCloseButton, imgBtnCall, imgBtnChat;

    public static AppointmentLabRequestFragment newInstance(String param1, String param2) {
        AppointmentLabRequestFragment fragment = new AppointmentLabRequestFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            strAppointmentType = getArguments().getString("appointment_type");
            strMeetPurpose = getArguments().getString("meet_purpose");
            strAvailability = getArguments().getString("availability");
            strDay = getArguments().getString("day");
            strAnySpecificRequest = getArguments().getString("Any_Specific_Request");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View row = inflater.inflate(R.layout.fragment_appointment_lab_request, container, false);
        tvAppointmentType = row.findViewById(R.id.tv_appointment_type);
        tvNewAppointName = row.findViewById(R.id.tv_appointment_name);
        tvAvailability = row.findViewById(R.id.tv_availability);
        tvTimeOfDay = row.findViewById(R.id.tv_time_of_day);
        tvSpecificRequest = row.findViewById(R.id.tv_specific_request);
        tvSpecificRequestTitle = row.findViewById(R.id.tv_specific_request_title);
        btnSendRequest = row.findViewById(R.id.btn_send_request);
        imgCloseButton = row.findViewById(R.id.img_close_button);
        imgBtnCall = row.findViewById(R.id.img_btn_call);
        imgBtnChat = row.findViewById(R.id.img_btn_chat);
        btnAnySpecificRequest = row.findViewById(R.id.btn_any_specific_request);

        tvAppointmentType.setText(strAppointmentType);
        tvNewAppointName.setText(strMeetPurpose);
        tvAvailability.setText(strAvailability);
        tvTimeOfDay.setText(strDay);

        if(strAnySpecificRequest!=null && !strAnySpecificRequest.isEmpty()){
            tvSpecificRequest.setVisibility(View.VISIBLE);
            tvSpecificRequestTitle.setVisibility(View.VISIBLE);
            tvSpecificRequest.setText(strAnySpecificRequest);
        }else {
            tvSpecificRequest.setVisibility(View.GONE);
            tvSpecificRequestTitle.setVisibility(View.GONE);
        }

        btnSendRequest.setOnClickListener(this);
        imgCloseButton.setOnClickListener(this);
        imgBtnChat.setOnClickListener(this);
        imgBtnCall.setOnClickListener(this);
        btnAnySpecificRequest.setOnClickListener(this);

        sharedpreferences = getActivity().getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        strInteractionDetailId = sharedpreferences.getString("interaction_DTL_ID", "");
        strInteractionId = sharedpreferences.getString("interaction_ID", "");
        strUserId = sharedpreferences.getString("USER_ID", "");

        return row;
    }

    public void submitLabRequest() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            String URL = AppConfig.BASE_URL + AppConfig.CREATE_REQUEST_HISTORY;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("Interaction_DTL_ID", strInteractionDetailId);
            jsonBody.put("UserID", strUserId);
            jsonBody.put("status_Id", "Y");
            jsonBody.put("Appointment_Type", strAppointmentType);
            jsonBody.put("Interaction_ID", strInteractionId);
            jsonBody.put("Speciality", "3");
            jsonBody.put("Physicians_Name", "");
            jsonBody.put("Physicians_Specialty", "");
            jsonBody.put("Availability", strAvailability);
            jsonBody.put("Time_of_day", strDay);
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("VOLLEY_PHYSICIAN_RESONS", response);
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i =0; i<array.length();i++){
                            JSONObject object = array.getJSONObject(0);
                            if(object.getString("inMsg").equalsIgnoreCase("Request Saved!!!")) {
                                Intent intent = new Intent(getActivity(), DashboardActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("is_lab_requested_completed", "Lab");
                                intent.putExtra("request", "Lab");
                                intent.putExtra("appointment_type", strAppointmentType);
                                intent.putExtra("Speciality", "Lab");
                                intent.putExtra("Availability", strAvailability);
                                intent.putExtra("Time_of_day", strDay);
                                intent.putExtra("Any_Specific_Request", strAnySpecificRequest);
                                startActivity(intent);
                                getActivity().finish();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_request:
                btnSendRequest.setBackground(getResources().getDrawable(R.drawable.fill_appointment_button_corner));
                btnSendRequest.setTextColor(getResources().getColor(R.color.btn_text_color));
                submitLabRequest();
                break;
            case R.id.img_close_button:
                Intent intent = new Intent(getActivity(), DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.btn_any_specific_request:
                btnAnySpecificRequest.setBackground(getResources().getDrawable(R.drawable.fill_appointment_button_corner));
                btnAnySpecificRequest.setTextColor(getResources().getColor(R.color.btn_text_color));
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                AnySpecificRequestFragmrent fragment = new AnySpecificRequestFragmrent();
                Bundle args = new Bundle();
                args.putString("appointment_type", strAppointmentType);
                args.putString("Speciality", strMeetPurpose);
                args.putString("Availability", strAvailability);
                args.putString("Time_of_day", strDay);
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.myContainer, fragment);
                fragmentTransaction.commit();
                fragmentTransaction.addToBackStack(null);
                break;
            case R.id.img_btn_call:
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:(866) 698-2422"));
                startActivity(i);
                break;
        }

    }
}

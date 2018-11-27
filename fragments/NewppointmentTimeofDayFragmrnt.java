package com.devool.ucicareconnect.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.devool.ucicareconnect.R;
import com.devool.ucicareconnect.activities.DashboardActivity;
import com.devool.ucicareconnect.utils.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class NewppointmentTimeofDayFragmrnt extends Fragment implements View.OnClickListener {

    public static final String USER_INFO = "user_info";
    Button btnMorning, btnAfternoon, btnNxt, btnAnyTime;
    boolean flagMorning, flagAfternoon;
    String strAppointmentType, strMeetPurpose, strExamType, strTestOrder, strAvailability, strDay, strPhysicianName, strPhysicianType, strOutsideProvidrName, strProviderPhoneNumber, strInteractionId, strInteractionDetailId, strUserId;
    boolean buttonClicked = false;
    SharedPreferences sharedpreferences;
    ImageView imgCloseButton, imgBtnCall, imgBtnChat;
    boolean isNewUser;

    public static NewppointmentTimeofDayFragmrnt newInstance(String param1, String param2) {
        NewppointmentTimeofDayFragmrnt fragment = new NewppointmentTimeofDayFragmrnt();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            strAppointmentType = getArguments().getString("appointment_type");
            strMeetPurpose = getArguments().getString("meet_purpose");
            strExamType = getArguments().getString("exam_type");
            strTestOrder = getArguments().getString("test_order");
            strAvailability = getArguments().getString("availability");
            strPhysicianName = getArguments().getString("physician_name");
            strPhysicianType = getArguments().getString("physician_type");
            strOutsideProvidrName = getArguments().getString("outside_provider_name");
            strProviderPhoneNumber = getArguments().getString("provider_Phone_number");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View row = inflater.inflate(R.layout.fragment_newppointment_timeof_day_fragmrnt, container, false);

        btnMorning = (Button) row.findViewById(R.id.btn_morning);
        btnAfternoon = (Button) row.findViewById(R.id.btn_afternoon);
        btnNxt = (Button) row.findViewById(R.id.btn_next);
        btnAnyTime = row.findViewById(R.id.btn_any_time);
        imgCloseButton = row.findViewById(R.id.img_close_button);
        imgBtnCall = row.findViewById(R.id.img_btn_call);
        imgBtnChat = row.findViewById(R.id.img_btn_chat);

        btnMorning.setOnClickListener(this);
        btnAfternoon.setOnClickListener(this);
//        btnNxt.setOnClickListener(this);
        btnAnyTime.setOnClickListener(this);
        imgCloseButton.setOnClickListener(this);
        imgBtnChat.setOnClickListener(this);
        imgBtnCall.setOnClickListener(this);

        sharedpreferences = getActivity().getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        strInteractionDetailId = sharedpreferences.getString("interaction_DTL_ID", "");
        strInteractionId = sharedpreferences.getString("interaction_ID", "");
        strUserId = sharedpreferences.getString("USER_ID", "");
        isNewUser = sharedpreferences.getBoolean("IS_NEW_USER", false);
        //isNewUser = false;

        return row;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_morning:
                //buttonClicked = true;
                strDay = btnMorning.getText().toString();
                btnMorning.setBackground(getResources().getDrawable(R.drawable.fill_appointment_button_corner));
                btnMorning.setTextColor(getResources().getColor(R.color.btn_text_color));
                flagMorning = true;
                if (flagAfternoon) {
                    btnAfternoon.setBackground(getResources().getDrawable(R.drawable.appointment_btn_corner));
                    btnAfternoon.setTextColor(getResources().getColor(R.color.bacgroun_color));
                }

                if(!isNewUser) {
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FirstTimePhysicianNewPatientFragment fragment = new FirstTimePhysicianNewPatientFragment();
                    Bundle args = new Bundle();
                    args.putString("appointment_type", strAppointmentType);
                    args.putString("meet_purpose", strMeetPurpose);
                    args.putString("availability", strAvailability);
                    args.putString("day", strDay);
                    args.putString("physician_name", strPhysicianName);
                    args.putString("physician_type", strPhysicianType);
                    fragment.setArguments(args);
                    fragmentTransaction.replace(R.id.myContainer, fragment);
                    fragmentTransaction.commit();
                    fragmentTransaction.addToBackStack(null);
                }else {
                    if (strMeetPurpose.equals("Physician")) {
                        submit();
                    } else if (strMeetPurpose.equals("Lab")) {
                        submitLabInfo();
                    } else {
                        submitInfoForUCI();
                    }
                }
                break;

            case R.id.btn_afternoon:
                //buttonClicked = true;
                strDay = btnAfternoon.getText().toString();
                if (flagMorning) {
                    btnMorning.setBackground(getResources().getDrawable(R.drawable.appointment_btn_corner));
                    btnMorning.setTextColor(getResources().getColor(R.color.bacgroun_color));
                }
                btnAfternoon.setBackground(getResources().getDrawable(R.drawable.fill_appointment_button_corner));
                btnAfternoon.setTextColor(getResources().getColor(R.color.btn_text_color));
                flagAfternoon = true;

                if(!isNewUser) {
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FirstTimePhysicianNewPatientFragment fragment = new FirstTimePhysicianNewPatientFragment();
                    Bundle args = new Bundle();
                    args.putString("appointment_type", strAppointmentType);
                    args.putString("meet_purpose", strMeetPurpose);
                    args.putString("availability", strAvailability);
                    args.putString("day", strDay);
                    args.putString("physician_name", strPhysicianName);
                    args.putString("physician_type", strPhysicianType);
                    fragment.setArguments(args);
                    fragmentTransaction.replace(R.id.myContainer, fragment);
                    fragmentTransaction.commit();
                    fragmentTransaction.addToBackStack(null);
                }else {
                    if (strMeetPurpose.equals("Physician")) {
                        submit();
                    } else if (strMeetPurpose.equals("Lab")) {
                        submitLabInfo();
                    } else {
                        submitInfoForUCI();
                    }
                }
                break;

            case R.id.btn_any_time:
                //buttonClicked = true;
                strDay = btnAnyTime.getText().toString();
                btnAnyTime.setBackground(getResources().getDrawable(R.drawable.fill_appointment_button_corner));
                btnAnyTime.setTextColor(getResources().getColor(R.color.btn_text_color));
                flagMorning = true;
                if (flagAfternoon) {
                    btnAfternoon.setBackground(getResources().getDrawable(R.drawable.appointment_btn_corner));
                    btnAfternoon.setTextColor(getResources().getColor(R.color.bacgroun_color));
                }

                if(!isNewUser) {
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FirstTimePhysicianNewPatientFragment fragment = new FirstTimePhysicianNewPatientFragment();
                    Bundle args = new Bundle();
                    args.putString("appointment_type", strAppointmentType);
                    args.putString("meet_purpose", strMeetPurpose);
                    args.putString("availability", strAvailability);
                    args.putString("day", strDay);
                    args.putString("physician_name", strPhysicianName);
                    args.putString("physician_type", strPhysicianType);
                    fragment.setArguments(args);
                    fragmentTransaction.replace(R.id.myContainer, fragment);
                    fragmentTransaction.commit();
                    fragmentTransaction.addToBackStack(null);
                }else {
                    if (strMeetPurpose.equals("Physician")) {
                        submit();
                    } else if (strMeetPurpose.equals("Lab")) {
                        submitLabInfo();
                    } else {
                        submitInfoForUCI();
                    }
                }
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

            /*case R.id.btn_next:
                if(buttonClicked) {
                    btnNxt.setBackground(getResources().getDrawable(R.drawable.fill_appointment_button_corner));
                    btnNxt.setTextColor(getResources().getColor(R.color.btn_text_color));

                    Bundle args = new Bundle();
                    args.putString("appointment_type", strAppointmentType);
                    args.putString("meet_purpose", strMeetPurpose);
                    args.putString("exam_type", strExamType);
                    args.putString("test_order", strTestOrder);
                    args.putString("availability", strAvailability);
                    args.putString("day", strDay);
                    args.putString("physician_name", strPhysicianName);
                    args.putString("physician_type", strPhysicianType);
                    args.putString("outside_provider_name", strOutsideProvidrName);
                    args.putString("provider_Phone_number", strProviderPhoneNumber);

                    if (strMeetPurpose.equals("Physician")) {
                        submit();
                        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        AppointmentPhysicianRequestFragment fragment = new AppointmentPhysicianRequestFragment();
                        fragment.setArguments(args);
                        fragmentTransaction.replace(R.id.myContainer, fragment);
                        fragmentTransaction.commit();
                        fragmentTransaction.addToBackStack(null);
                    } else if (strMeetPurpose.equals("Lab")) {
                        submitLabInfo();
                        //Toast.makeText(getActivity(), "Lab Called", Toast.LENGTH_SHORT).show();
                        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        AppointmentLabRequestFragment fragment = new AppointmentLabRequestFragment();
                        fragment.setArguments(args);
                        fragmentTransaction.replace(R.id.myContainer, fragment);
                        fragmentTransaction.commit();
                        fragmentTransaction.addToBackStack(null);
                    } else {
                        submitInfoForUCI();
                        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        AppointmentRadiologyRequestFragment fragment = new AppointmentRadiologyRequestFragment();
                        fragment.setArguments(args);
                        fragmentTransaction.replace(R.id.myContainer, fragment);
                        fragmentTransaction.commit();
                        fragmentTransaction.addToBackStack(null);
                    }
                }else {
                    Toast.makeText(getActivity(), "Please select time of day", Toast.LENGTH_SHORT).show();
                }

                break;*/
        }
    }

    public void submit() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            String URL = AppConfig.BASE_URL + AppConfig.CREATE_REQUEST_HISTORY;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("Interaction_DTL_ID", strInteractionDetailId);
            jsonBody.put("UserID", strUserId);
            jsonBody.put("status_Id", "N");
            jsonBody.put("Interaction_ID", strInteractionId);
            jsonBody.put("Requested_By", "");
            jsonBody.put("OutsideProvider_number", "");
            if (strDay.equalsIgnoreCase("Morning")) {
                jsonBody.put("Time_of_day", strDay);
            } else {
                jsonBody.put("Time_of_day", strDay);
            }
            jsonBody.put("Radiology_Type", "");
            jsonBody.put("Care_Facility_Name", "");
            jsonBody.put("Care_Facility_Location", "");
            jsonBody.put("Around_Dates", "");
            jsonBody.put("Additional_Info", "");
            jsonBody.put("Created_By_User_ID", "");
            jsonBody.put("Modified_By_User_ID", "");
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("VOLLEY", response);
                    try {
                        JSONArray array = new JSONArray(response);

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(0);
                            if (object.getString("inMsg").equalsIgnoreCase("Request Saved!!!")) {
                                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                AppointmentPhysicianRequestFragment fragment = new AppointmentPhysicianRequestFragment();
                                Bundle args = new Bundle();
                                args.putString("appointment_type", strAppointmentType);
                                args.putString("meet_purpose", strMeetPurpose);
                                args.putString("exam_type", strExamType);
                                args.putString("test_order", strTestOrder);
                                args.putString("availability", strAvailability);
                                args.putString("day", strDay);
                                args.putString("physician_name", strPhysicianName);
                                args.putString("physician_type", strPhysicianType);
                                args.putString("outside_provider_name", strOutsideProvidrName);
                                args.putString("provider_Phone_number", strProviderPhoneNumber);
                                fragment.setArguments(args);
                                fragmentTransaction.replace(R.id.myContainer, fragment);
                                fragmentTransaction.commit();
                                fragmentTransaction.addToBackStack(null);
                            }
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
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

    public void submitInfoForUCI() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            String URL = AppConfig.BASE_URL + AppConfig.CREATE_REQUEST_HISTORY;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("Interaction_DTL_ID", strInteractionDetailId);
            jsonBody.put("UserID", strUserId);
            jsonBody.put("status_Id", "N");
            jsonBody.put("Interaction_ID", strInteractionId);
            if (strDay.equalsIgnoreCase("Morning")) {
                jsonBody.put("Time_of_day", strDay);
            } else {
                jsonBody.put("Time_of_day", strDay);
            }
            jsonBody.put("Care_Facility_Name", "");
            jsonBody.put("Care_Facility_Location", "");
            jsonBody.put("Around_Dates", "");
            jsonBody.put("Additional_Info", "");
            jsonBody.put("Created_By_User_ID", "");
            jsonBody.put("Modified_By_User_ID", "");
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("VOLLEY", response);
                    try {
                        JSONArray array = new JSONArray(response);

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(0);
                            if (object.getString("inMsg").equalsIgnoreCase("Request Saved!!!")) {
                                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                AppointmentRadiologyRequestFragment fragment = new AppointmentRadiologyRequestFragment();
                                Bundle args = new Bundle();
                                args.putString("appointment_type", strAppointmentType);
                                args.putString("meet_purpose", strMeetPurpose);
                                args.putString("exam_type", strExamType);
                                args.putString("test_order", strTestOrder);
                                args.putString("availability", strAvailability);
                                args.putString("day", strDay);
                                args.putString("physician_name", strPhysicianName);
                                args.putString("physician_type", strPhysicianType);
                                args.putString("outside_provider_name", strOutsideProvidrName);
                                args.putString("provider_Phone_number", strProviderPhoneNumber);
                                fragment.setArguments(args);
                                fragmentTransaction.replace(R.id.myContainer, fragment);
                                fragmentTransaction.commit();
                                fragmentTransaction.addToBackStack(null);
                            }
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
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

    public void submitLabInfo() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            String URL = AppConfig.BASE_URL + AppConfig.CREATE_REQUEST_HISTORY;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("Interaction_DTL_ID", strInteractionDetailId);
            jsonBody.put("UserID", strUserId);
            jsonBody.put("Interaction_ID", strInteractionId);
            jsonBody.put("status_Id", "N");
            if (strDay.equalsIgnoreCase("Morning")) {
                jsonBody.put("Time_of_day", strDay);
            } else {
                jsonBody.put("Time_of_day", strDay);
            }
            jsonBody.put("Physicians_Name", "");
            jsonBody.put("Physicians_Specialty ", "");
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("VOLLEY", response);
                    try {
                        JSONArray array = new JSONArray(response);

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(0);
                            if (object.getString("inMsg").equalsIgnoreCase("Request Saved!!!")) {
                                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                AppointmentLabRequestFragment fragment = new AppointmentLabRequestFragment();
                                Bundle args = new Bundle();
                                args.putString("appointment_type", strAppointmentType);
                                args.putString("meet_purpose", strMeetPurpose);
                                args.putString("exam_type", strExamType);
                                args.putString("test_order", strTestOrder);
                                args.putString("availability", strAvailability);
                                args.putString("day", strDay);
                                args.putString("physician_name", strPhysicianName);
                                args.putString("physician_type", strPhysicianType);
                                args.putString("outside_provider_name", strOutsideProvidrName);
                                args.putString("provider_Phone_number", strProviderPhoneNumber);
                                fragment.setArguments(args);
                                fragmentTransaction.replace(R.id.myContainer, fragment);
                                fragmentTransaction.commit();
                                fragmentTransaction.addToBackStack(null);
                            }
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
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


   /* @Override
    public void onPause() {
        super.onPause();
        buttonClicked = false;
    }*/
}

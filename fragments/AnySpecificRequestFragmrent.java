package com.devool.ucicareconnect.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

public class AnySpecificRequestFragmrent extends Fragment implements View.OnClickListener {

    ImageView imgCloseButton, imgBtnCall, imgBtnChat;
    Button btnSave;

    String strAppointmentType, strSpecialty, strAvailability, strTimeofDay, strPhysicianName, strPhysicianType, strRequestedBy, strOutsideProviderNumber, strRadiologyType, strInteractionId, strInteractionDetailId, strUserId, strTestOrder, strExamType;
    public static final String USER_INFO = "user_info";
    SharedPreferences sharedpreferences;

    EditText edtAnySpecificRequest;

    String StrFolloowupAppointmentType, strFollowupDoctorName, strFollowupAvailability, strFollowupTimeofDay, strFolloupFlag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            strAppointmentType = getArguments().getString("appointment_type");
            strSpecialty = getArguments().getString("Speciality");
            strAvailability = getArguments().getString("Availability");
            strTimeofDay = getArguments().getString("Time_of_day");
            strPhysicianName = getArguments().getString("Physicians_Name");
            strPhysicianType = getArguments().getString("Physicians_Specialty");
            strRequestedBy = getArguments().getString("Requested_By");
            strOutsideProviderNumber = getArguments().getString("OutsideProvider_number");
            strRadiologyType = getArguments().getString("Radiology_Type");
            strTestOrder = getArguments().getString("test_order");
            strExamType = getArguments().getString("exam_type");


            StrFolloowupAppointmentType = getArguments().getString("appointment_type");
            strFollowupDoctorName = getArguments().getString("physician_name");
            strFollowupAvailability = getArguments().getString("followup_availability");
            strFollowupTimeofDay = getArguments().getString("followup_time_of_day");
            strFolloupFlag= getArguments().getString("followup_flag");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View row = inflater.inflate(R.layout.fragment_your_appointment_request_fragmrent, container, false);
        imgCloseButton = row.findViewById(R.id.img_close_button);
        imgBtnCall = row.findViewById(R.id.img_btn_call);
        imgBtnChat = row.findViewById(R.id.img_btn_chat);
        btnSave = row.findViewById(R.id.btn_save);
        edtAnySpecificRequest = row.findViewById(R.id.edit_specific_request);

        imgCloseButton.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        imgBtnChat.setOnClickListener(this);
        imgBtnCall.setOnClickListener(this);

        edtAnySpecificRequest.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edtAnySpecificRequest.setRawInputType(InputType.TYPE_CLASS_TEXT);

        sharedpreferences = getActivity().getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        strInteractionDetailId = sharedpreferences.getString("interaction_DTL_ID", "");
        strInteractionId = sharedpreferences.getString("interaction_ID", "");
        strUserId = sharedpreferences.getString("USER_ID", "");

        return row;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close_button:
                Intent intent = new Intent(getActivity(), DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.btn_save:
                btnSave.setBackground(getResources().getDrawable(R.drawable.fill_appointment_button_corner));
                btnSave.setTextColor(getResources().getColor(R.color.btn_text_color));
                if(strFolloupFlag!=null){
                    submitFollowupRequest();
                    break;
                }else {
                    submitAnySpecificRequest();
                    break;
                }
            case R.id.img_btn_call:
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:(866) 698-2422"));
                startActivity(i);
                break;
        }

    }

    private void submitFollowupRequest() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            String URL = AppConfig.BASE_URL + AppConfig.CREATE_REQUEST_HISTORY;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("Interaction_DTL_ID", strInteractionDetailId);
            jsonBody.put("UserID", strUserId);
            jsonBody.put("status_Id", "N");
            jsonBody.put("Interaction_ID", strInteractionId);
            jsonBody.put("Any_Specific_Request", edtAnySpecificRequest.getText().toString());
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
                                FollowupAppointmentRequest fragment = new FollowupAppointmentRequest();
                                Bundle args = new Bundle();
                                args.putString("appointment_type", StrFolloowupAppointmentType);
                                args.putString("physician_name", strFollowupDoctorName);
                                args.putString("followup_availability", strFollowupAvailability);
                                args.putString("followup_time_of_day", strFollowupTimeofDay);
                                args.putString("Any_Specific_Request", edtAnySpecificRequest.getText().toString());
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


    public void submitAnySpecificRequest() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            String URL = AppConfig.BASE_URL + AppConfig.CREATE_REQUEST_HISTORY;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("Interaction_DTL_ID", strInteractionDetailId);
            jsonBody.put("UserID", strUserId);
            jsonBody.put("status_Id", "N");
            jsonBody.put("Interaction_ID", strInteractionId);
            jsonBody.put("Any_Specific_Request", edtAnySpecificRequest.getText().toString());
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
                                if (strSpecialty.equals("Physician") && strSpecialty != null) {
                                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    AppointmentPhysicianRequestFragment fragment = new AppointmentPhysicianRequestFragment();
                                    Bundle args = new Bundle();
                                    args.putString("appointment_type", strAppointmentType);
                                    args.putString("meet_purpose", strSpecialty);
                                    args.putString("exam_type", strRadiologyType);
                                    args.putString("test_order", strRequestedBy);
                                    args.putString("availability", strAvailability);
                                    args.putString("day", strTimeofDay);
                                    args.putString("physician_name", strPhysicianName);
                                    args.putString("physician_type", strPhysicianType);
                                    args.putString("outside_provider_name", strRadiologyType);
                                    args.putString("provider_Phone_number", strOutsideProviderNumber);
                                    args.putString("Any_Specific_Request", edtAnySpecificRequest.getText().toString());
                                    fragment.setArguments(args);
                                    fragmentTransaction.replace(R.id.myContainer, fragment);
                                    fragmentTransaction.commit();
                                    fragmentTransaction.addToBackStack(null);
                                } else if (strSpecialty.equals("Radiology / Diagnostics") && strSpecialty != null) {
                                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    AppointmentRadiologyRequestFragment fragment = new AppointmentRadiologyRequestFragment();
                                    Bundle args = new Bundle();
                                    args.putString("appointment_type", strAppointmentType);
                                    args.putString("meet_purpose", strSpecialty);
                                    args.putString("exam_type", strExamType);
                                    args.putString("test_order", strTestOrder);
                                    args.putString("availability", strAvailability);
                                    args.putString("day", strTimeofDay);
                                    args.putString("physician_name", strPhysicianName);
                                    args.putString("physician_type", strPhysicianType);
                                    args.putString("outside_provider_name", strRadiologyType);
                                    args.putString("provider_Phone_number", strOutsideProviderNumber);
                                    args.putString("Any_Specific_Request", edtAnySpecificRequest.getText().toString());
                                    fragment.setArguments(args);
                                    fragmentTransaction.replace(R.id.myContainer, fragment);
                                    fragmentTransaction.commit();
                                    fragmentTransaction.addToBackStack(null);
                                }else if(strSpecialty.equals("Lab") && strSpecialty != null){
                                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    AppointmentLabRequestFragment fragment = new AppointmentLabRequestFragment();
                                    Bundle args = new Bundle();
                                    args.putString("appointment_type", strAppointmentType);
                                    args.putString("meet_purpose", strSpecialty);
                                    args.putString("exam_type", strRadiologyType);
                                    args.putString("test_order", strRequestedBy);
                                    args.putString("availability", strAvailability);
                                    args.putString("day", strTimeofDay);
                                    args.putString("physician_name", strPhysicianName);
                                    args.putString("physician_type", strPhysicianType);
                                    args.putString("outside_provider_name", strRadiologyType);
                                    args.putString("provider_Phone_number", strOutsideProviderNumber);
                                    args.putString("Any_Specific_Request", edtAnySpecificRequest.getText().toString());
                                    fragment.setArguments(args);
                                    fragmentTransaction.replace(R.id.myContainer, fragment);
                                    fragmentTransaction.commit();
                                    fragmentTransaction.addToBackStack(null);
                                }
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
}

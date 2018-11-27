package com.devool.ucicareconnect.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

public class NewAppointmentFragment extends Fragment implements View.OnClickListener {

    public static final String USER_INFO = "user_info";
    Button btnPhycian, btnRadiology, btnLab, btnNext;
    boolean flagPhycian = false, flagRadiology = false, flagLab = false;
    String strAppointmentTypeFlag, strInteractionId, strInteractionDetailId, strUserId;
    boolean flagBtnClicked = false;
    boolean isNewUser;
    String strAAppointmentType, strMeetPurpose;
    SharedPreferences sharedpreferences;
    ImageView imgCloseButton, imgBtnCall, imgBtnChat;

    public static NewAppointmentFragment newInstance(String param1, String param2) {
        NewAppointmentFragment fragment = new NewAppointmentFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        strAAppointmentType = getArguments().getString("appointment_type");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View row = inflater.inflate(R.layout.fragment_new_appointment, container, false);

        btnPhycian = (Button) row.findViewById(R.id.btn_physician);
        btnRadiology = (Button) row.findViewById(R.id.btn_radiology);
        btnLab = (Button) row.findViewById(R.id.btn_lab);
        imgCloseButton = row.findViewById(R.id.img_close_button);
        imgBtnCall = row.findViewById(R.id.img_btn_call);
        imgBtnChat = row.findViewById(R.id.img_btn_chat);
        //btnNext = (Button)row.findViewById(R.id.btn_next);

        btnPhycian.setOnClickListener(this);
        btnRadiology.setOnClickListener(this);
        btnLab.setOnClickListener(this);
        imgCloseButton.setOnClickListener(this);
        imgBtnChat.setOnClickListener(this);
        imgBtnCall.setOnClickListener(this);
        //btnNext.setOnClickListener(this);

        sharedpreferences = getActivity().getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        strInteractionDetailId = sharedpreferences.getString("interaction_DTL_ID", "");
        strInteractionId = sharedpreferences.getString("interaction_ID", "");
        isNewUser = sharedpreferences.getBoolean("IS_NEW_USER", false);
        //isNewUser = false;
        //Toast.makeText(getActivity(), ""+isNewUser, Toast.LENGTH_SHORT).show();
        strUserId = sharedpreferences.getString("USER_ID", "");

        return row;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_physician:
                strMeetPurpose = btnPhycian.getText().toString();
                if (isNewUser) {
                    submit();
                } else {
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    NewAppointmentPhysicianTypeFragment fragment = new NewAppointmentPhysicianTypeFragment();
                    Bundle args = new Bundle();
                    args.putString("appointment_type", strAAppointmentType);
                    args.putString("meet_purpose", strMeetPurpose);
                    fragment.setArguments(args);
                    fragmentTransaction.replace(R.id.myContainer, fragment);
                    fragmentTransaction.commit();
                    fragmentTransaction.addToBackStack(null);
                }

                btnPhycian.setBackground(getResources().getDrawable(R.drawable.fill_appointment_button_corner));
                btnPhycian.setTextColor(getResources().getColor(R.color.btn_text_color));
                flagPhycian = true;
                //flagBtnClicked=true;

                if (flagRadiology) {
                    btnRadiology.setBackground(getResources().getDrawable(R.drawable.appointment_btn_corner));
                    btnRadiology.setTextColor(getResources().getColor(R.color.bacgroun_color));
                }

                if (flagLab) {
                    btnLab.setBackground(getResources().getDrawable(R.drawable.appointment_btn_corner));
                    btnLab.setTextColor(getResources().getColor(R.color.bacgroun_color));
                }
                break;
            case R.id.btn_radiology:
                strMeetPurpose = btnRadiology.getText().toString();
                submit();
                btnRadiology.setBackground(getResources().getDrawable(R.drawable.fill_appointment_button_corner));
                btnRadiology.setTextColor(getResources().getColor(R.color.btn_text_color));
                flagRadiology = true;
                //flagBtnClicked=true;
                if (flagPhycian) {
                    btnPhycian.setBackground(getResources().getDrawable(R.drawable.appointment_btn_corner));
                    btnPhycian.setTextColor(getResources().getColor(R.color.bacgroun_color));
                }
                if (flagLab) {
                    btnLab.setBackground(getResources().getDrawable(R.drawable.appointment_btn_corner));
                    btnLab.setTextColor(getResources().getColor(R.color.bacgroun_color));
                }
                break;
            case R.id.btn_lab:
                strMeetPurpose = btnLab.getText().toString();
                submit();
                btnLab.setBackground(getResources().getDrawable(R.drawable.fill_appointment_button_corner));
                btnLab.setTextColor(getResources().getColor(R.color.btn_text_color));
                flagLab = true;
                //flagBtnClicked=true;
                if (flagPhycian) {
                    btnPhycian.setBackground(getResources().getDrawable(R.drawable.appointment_btn_corner));
                    btnPhycian.setTextColor(getResources().getColor(R.color.bacgroun_color));
                }
                if (flagRadiology) {
                    btnRadiology.setBackground(getResources().getDrawable(R.drawable.appointment_btn_corner));
                    btnRadiology.setTextColor(getResources().getColor(R.color.bacgroun_color));
                }
                break;
            /*case R.id.btn_next:
                btnNext.setBackground(getResources().getDrawable(R.drawable.fill_appointment_button_corner));
                btnNext.setTextColor(getResources().getColor(R.color.btn_text_color));
                Bundle args = new Bundle();
                args.putString("appointment_type", strAAppointmentType);
                args.putString("meet_purpose", strMeetPurpose);
                if(flagBtnClicked) {
                    if(strMeetPurpose.equals("Physician") && strMeetPurpose!=null) {
                        strAppointmentTypeFlag = "1";
                        submit();
                        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        NewAppointmentPhysicianNameFragment fragment = new NewAppointmentPhysicianNameFragment();
                        fragment.setArguments(args);
                        fragmentTransaction.replace(R.id.myContainer, fragment);
                        fragmentTransaction.commit();
                        fragmentTransaction.addToBackStack(null);
                        break;
                    }else if(strMeetPurpose.equals("Lab") && strMeetPurpose!=null) {
                        strAppointmentTypeFlag = "3";
                        submit();
                        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        NewAppointmentAvailabiltyFragmenet fragment = new NewAppointmentAvailabiltyFragmenet();
                        fragment.setArguments(args);
                        fragmentTransaction.replace(R.id.myContainer, fragment);
                        fragmentTransaction.commit();
                        fragmentTransaction.addToBackStack(null);
                        break;
                    }else{
                        strAppointmentTypeFlag = "2";
                        submit();
                        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        NewAppoRadiologyExamTypetFragment fragment = new NewAppoRadiologyExamTypetFragment();
                        fragment.setArguments(args);
                        fragmentTransaction.replace(R.id.myContainer, fragment);
                        fragmentTransaction.commit();
                        fragmentTransaction.addToBackStack(null);
                        break;
                    }
                }else{
                    btnNext.setBackground(getResources().getDrawable(R.drawable.appointment_btn_corner));
                    btnNext.setTextColor(getResources().getColor(R.color.bacgroun_color));
                    Toast.makeText(getActivity(), "Please select meet purpose", Toast.LENGTH_SHORT).show();
                    break;
                }*/
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

   /* @Override
    public void onPause() {
        super.onPause();
        flagBtnClicked = false;
    }*/

    public void submit() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            String URL = AppConfig.BASE_URL + AppConfig.CREATE_REQUEST_HISTORY;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("Interaction_DTL_ID", strInteractionDetailId);
            jsonBody.put("UserID", strUserId);
            jsonBody.put("status_Id", "N");
            jsonBody.put("Interaction_ID", strInteractionId);
            if (strMeetPurpose.equals("Physician")) {
                jsonBody.put("Speciality", "1");
            } else if (strMeetPurpose.equalsIgnoreCase("Radiology / Diagnostics")) {
                jsonBody.put("Speciality", "2");
            } else {
                jsonBody.put("Speciality", "3");
            }
            jsonBody.put("Requested_By", "");
            jsonBody.put("OutsideProvider_number", "");
            jsonBody.put("Availability", "");
            jsonBody.put("Time_of_day", "");
            jsonBody.put("Physicians_Name", "");
            jsonBody.put("Physicians_Specialty", "");
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
                                Bundle args = new Bundle();
                                args.putString("appointment_type", strAAppointmentType);
                                args.putString("meet_purpose", strMeetPurpose);
                                if (strMeetPurpose.equals("Physician") && strMeetPurpose != null) {
                                    strAppointmentTypeFlag = "1";
                                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    NewAppointmentPhysicianTypeFragment fragment = new NewAppointmentPhysicianTypeFragment();
                                    fragment.setArguments(args);
                                    fragmentTransaction.replace(R.id.myContainer, fragment);
                                    fragmentTransaction.commit();
                                    fragmentTransaction.addToBackStack(null);
                                    break;
                                } else if (strMeetPurpose.equals("Lab") && strMeetPurpose != null) {
                                    strAppointmentTypeFlag = "3";
                                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    NewAppointmentAvailabiltyFragmenet fragment = new NewAppointmentAvailabiltyFragmenet();
                                    fragment.setArguments(args);
                                    fragmentTransaction.replace(R.id.myContainer, fragment);
                                    fragmentTransaction.commit();
                                    fragmentTransaction.addToBackStack(null);
                                    break;
                                } else {
                                    strAppointmentTypeFlag = "2";
                                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    NewAppoRadiologyExamTypetFragment fragment = new NewAppoRadiologyExamTypetFragment();
                                    fragment.setArguments(args);
                                    fragmentTransaction.replace(R.id.myContainer, fragment);
                                    fragmentTransaction.commit();
                                    fragmentTransaction.addToBackStack(null);
                                    break;
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


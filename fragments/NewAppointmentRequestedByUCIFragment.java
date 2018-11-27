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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.devool.ucicareconnect.R;
import com.devool.ucicareconnect.activities.DashboardActivity;
import com.devool.ucicareconnect.utils.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class NewAppointmentRequestedByUCIFragment extends Fragment implements View.OnClickListener {

    public static final String USER_INFO = "user_info";
    Button btnUci, btnOutsideProvider, btnNext;
    boolean flagUCI = false, flagOutsideProvider = false;
    String strAppointmentType, strMeetPurpose, strExamType, strTestOrder, strInteractionId, strInteractionDetailId, strUserId;
    boolean buttonClicked = false;
    SharedPreferences sharedpreferences;
    ImageView imgCloseButton, imgBtnCall, imgBtnChat;

    public static NewAppointmentRequestedByUCIFragment newInstance(String param1, String param2) {
        NewAppointmentRequestedByUCIFragment fragment = new NewAppointmentRequestedByUCIFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            strAppointmentType = getArguments().getString("appointment_type");
            strMeetPurpose = getArguments().getString("meet_purpose");
            strExamType = getArguments().getString("exam_type");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View row = inflater.inflate(R.layout.fragment_new_appointment_requested_by, container, false);

        btnUci = (Button) row.findViewById(R.id.btn_uci);
        btnOutsideProvider = (Button) row.findViewById(R.id.btn_outside_provider);
        btnNext = (Button) row.findViewById(R.id.btn_next);
        imgCloseButton = row.findViewById(R.id.img_close_button);
        imgBtnCall = row.findViewById(R.id.img_btn_call);
        imgBtnChat = row.findViewById(R.id.img_btn_chat);

        btnUci.setOnClickListener(this);
        btnOutsideProvider.setOnClickListener(this);
        imgCloseButton.setOnClickListener(this);
        imgBtnChat.setOnClickListener(this);
        imgBtnCall.setOnClickListener(this);
        //btnNext.setOnClickListener(this);

        sharedpreferences = getActivity().getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        strInteractionDetailId = sharedpreferences.getString("interaction_DTL_ID", "");
        strInteractionId = sharedpreferences.getString("interaction_ID", "");
        strUserId = sharedpreferences.getString("USER_ID", "");

        return row;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /*case R.id.btn_next:
                if (buttonClicked) {
                    btnNext.setBackground(getResources().getDrawable(R.drawable.fill_appointment_button_corner));
                    btnNext.setTextColor(getResources().getColor(R.color.btn_text_color));

                    Bundle args = new Bundle();
                    args.putString("appointment_type", strAppointmentType);
                    args.putString("meet_purpose", strMeetPurpose);
                    args.putString("exam_type", strExamType);
                    args.putString("test_order", strTestOrder);
                    if (strTestOrder.equals("Outside Provider")) {
                        submit();
                        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        NewAppointmentRequestedByOutProviderFragment fragment = new NewAppointmentRequestedByOutProviderFragment();
                        fragment.setArguments(args);
                        fragmentTransaction.replace(R.id.myContainer, fragment);
                        fragmentTransaction.commit();
                        fragmentTransaction.addToBackStack(null);
                        break;
                    } else {
                        submit();
                        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        NewAppointmentAvailabiltyFragmenet fragment = new NewAppointmentAvailabiltyFragmenet();
                        fragment.setArguments(args);
                        fragmentTransaction.replace(R.id.myContainer, fragment);
                        fragmentTransaction.commit();
                        fragmentTransaction.addToBackStack(null);
                        break;
                    }
                } else {
                    Toast.makeText(getActivity(), "Please select provider name", Toast.LENGTH_SHORT).show();
                    break;
                }*/
            case R.id.btn_uci:
                //buttonClicked = true;
                strTestOrder = btnUci.getText().toString();
                btnUci.setBackground(getResources().getDrawable(R.drawable.fill_appointment_button_corner));
                btnUci.setTextColor(getResources().getColor(R.color.btn_text_color));
                flagUCI = true;
                if (flagOutsideProvider) {
                    btnOutsideProvider.setBackground(getResources().getDrawable(R.drawable.appointment_btn_corner));
                    btnOutsideProvider.setTextColor(getResources().getColor(R.color.bacgroun_color));
                }
                submit();
                break;

            case R.id.btn_outside_provider:
                //buttonClicked= true;
                strTestOrder = btnOutsideProvider.getText().toString();
                if (flagUCI) {
                    btnUci.setBackground(getResources().getDrawable(R.drawable.appointment_btn_corner));
                    btnUci.setTextColor(getResources().getColor(R.color.bacgroun_color));
                }
                btnOutsideProvider.setBackground(getResources().getDrawable(R.drawable.fill_appointment_button_corner));
                btnOutsideProvider.setTextColor(getResources().getColor(R.color.btn_text_color));
                flagOutsideProvider = true;

                submit();
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

    public void submit() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            String URL = AppConfig.BASE_URL + AppConfig.CREATE_REQUEST_HISTORY;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("Interaction_DTL_ID", strInteractionDetailId);
            jsonBody.put("UserID", strUserId);
            jsonBody.put("status_Id", "N");
            jsonBody.put("Interaction_ID", strInteractionId);
            if (strTestOrder.equalsIgnoreCase("Outside Provider")) {
                jsonBody.put("Requested_By", strTestOrder);
            } else {
                jsonBody.put("Requested_By", strTestOrder);
            }
            jsonBody.put("OutsideProvider_number", "");
            jsonBody.put("Availability", "");
            jsonBody.put("Time_of_day", "");
            jsonBody.put("Physicians_Name", "");
            jsonBody.put("Physicians_Specialty ", "");
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
                                if (strTestOrder.equals("Outside Provider")) {
                                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    NewAppointmentRequestedByOutProviderFragment fragment = new NewAppointmentRequestedByOutProviderFragment();
                                    Bundle args = new Bundle();
                                    args.putString("appointment_type", strAppointmentType);
                                    args.putString("meet_purpose", strMeetPurpose);
                                    args.putString("exam_type", strExamType);
                                    args.putString("test_order", strTestOrder);
                                    fragment.setArguments(args);
                                    fragmentTransaction.replace(R.id.myContainer, fragment);
                                    fragmentTransaction.commit();
                                    fragmentTransaction.addToBackStack(null);
                                } else {
                                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    NewAppointmentAvailabiltyFragmenet fragment = new NewAppointmentAvailabiltyFragmenet();
                                    Bundle args = new Bundle();
                                    args.putString("appointment_type", strAppointmentType);
                                    args.putString("meet_purpose", strMeetPurpose);
                                    args.putString("exam_type", strExamType);
                                    args.putString("test_order", strTestOrder);
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


    /*@Override
    public void onPause() {
        super.onPause();
        buttonClicked = false;
    }*/
}

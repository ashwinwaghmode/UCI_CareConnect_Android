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
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.devool.ucicareconnect.R;
import com.devool.ucicareconnect.activities.DashboardActivity;
import com.devool.ucicareconnect.utils.AppConfig;
import com.devool.ucicareconnect.utils.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class AppoinmentTypeFragment extends Fragment implements View.OnClickListener {

    public static final String USER_INFO = "user_info";
    Button btnNewAppointment, brnAppointmentFollowup, btnNext;
    boolean flagnewAppointment = false, flagFollowAppointment = false;
    String StrAppointmentType, strInteractionId, strInteractionDetailId, strUserId;
    boolean buttonPressed = false;
    SharedPreferences sharedpreferences;
    ImageView imgCloseButton, imgBtnCall, imgBtnChat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View row = inflater.inflate(R.layout.fragment_appoinment_type, container, false);

        btnNewAppointment = (Button) row.findViewById(R.id.btn_new_appointment);
        brnAppointmentFollowup = (Button) row.findViewById(R.id.btn_followp_aappointment);
        imgCloseButton = row.findViewById(R.id.img_close_button);
        imgBtnCall = row.findViewById(R.id.img_btn_call);
        imgBtnChat = row.findViewById(R.id.img_btn_chat);
        //btnNext = (Button) row.findViewById(R.id.btn_next);

        btnNewAppointment.setOnClickListener(this);
        brnAppointmentFollowup.setOnClickListener(this);
        imgCloseButton.setOnClickListener(this);
        imgBtnChat.setOnClickListener(this);
        imgBtnCall.setOnClickListener(this);
        //btnNext.setOnClickListener(this);

        sharedpreferences = getActivity().getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        strInteractionDetailId = sharedpreferences.getString("interaction_DTL_ID", "");
        strInteractionId = sharedpreferences.getString("interaction_ID", "");
        strUserId = sharedpreferences.getString("USER_ID", "");

        // Toast.makeText(getActivity(), "User_Id ="+ strUserId + "\n" + "Interaction_id = " + strInteractionId + "\n" + "Interaction_detail_di = " + strInteractionDetailId, Toast.LENGTH_SHORT).show();

        return row;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_new_appointment:
                //buttonPressed = true;
                StrAppointmentType = btnNewAppointment.getText().toString();
                submit();
                btnNewAppointment.setBackground(getResources().getDrawable(R.drawable.fill_appointment_button_corner));
                btnNewAppointment.setTextColor(getResources().getColor(R.color.btn_text_color));
                flagnewAppointment = true;
                if (flagFollowAppointment) {
                    brnAppointmentFollowup.setBackground(getResources().getDrawable(R.drawable.appointment_btn_corner));
                    brnAppointmentFollowup.setTextColor(getResources().getColor(R.color.bacgroun_color));
                }
                break;

            case R.id.btn_followp_aappointment:
                //buttonPressed = true;
                StrAppointmentType = brnAppointmentFollowup.getText().toString();
                submit();
                if (flagnewAppointment) {
                    btnNewAppointment.setBackground(getResources().getDrawable(R.drawable.appointment_btn_corner));
                    btnNewAppointment.setTextColor(getResources().getColor(R.color.bacgroun_color));
                }
                brnAppointmentFollowup.setBackground(getResources().getDrawable(R.drawable.fill_appointment_button_corner));
                brnAppointmentFollowup.setTextColor(getResources().getColor(R.color.btn_text_color));
                flagFollowAppointment = true;
                break;

            /*case R.id.btn_next:
                if (!buttonPressed) {
                    Toast.makeText(getActivity(), "Please select Appointment", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    submit();
                    btnNext.setBackground(getResources().getDrawable(R.drawable.fill_appointment_button_corner));
                    btnNext.setTextColor(getResources().getColor(R.color.btn_text_color));

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

    public void submit() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            String URL = AppConfig.BASE_URL + AppConfig.CREATE_REQUEST_HISTORY;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("Interaction_DTL_ID", strInteractionDetailId);
            jsonBody.put("UserID", strUserId);
            jsonBody.put("status_Id", "N");
            jsonBody.put("Appointment_Type", StrAppointmentType);
            jsonBody.put("Interaction_ID", strInteractionId);
            jsonBody.put("Requested_By", "");
            jsonBody.put("OutsideProvider_number", "");
            jsonBody.put("Availability", "");
            jsonBody.put("Time_of_day", "");
            jsonBody.put("Physicians_Name", "");
            jsonBody.put("Physicians_Specialty ", "");
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
                                if(StrAppointmentType.equals("Follow-up Appointment")){
                                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    FollowupAppointmentFragment fragment = new FollowupAppointmentFragment();
                                    Bundle args = new Bundle();
                                    args.putString("appointment_type", StrAppointmentType);
                                    fragment.setArguments(args);
                                    fragmentTransaction.replace(R.id.myContainer, fragment);
                                    fragmentTransaction.commit();
                                    fragmentTransaction.addToBackStack(null);
                                }else {
                                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    NewAppointmentFragment fragment = new NewAppointmentFragment();
                                    Bundle args = new Bundle();
                                    args.putString("appointment_type", StrAppointmentType);
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

   /* @Override
    public void onPause() {
        super.onPause();
        buttonPressed = false;
    }*/
}

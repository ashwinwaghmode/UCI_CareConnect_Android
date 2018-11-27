package com.devool.ucicareconnect.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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


public class ReferralCompletedFragment extends Fragment implements View.OnClickListener{

    String strRelationship, strReferralname, strFamilyRelation, strAssociation, strContactInfo, strEmailAddress;
    LinearLayout llAssociateName, llFamilyRelation;

    TextView tvReferralName, tvRelationship, tvAssociateName, tvFamiyRelation, tvPhoneNumber, tvEmailAddress;

    SharedPreferences sharedpreferences;
    Button btnSendRequest, btnMakeAnotherReferral;
    ImageView imgCloseButton, imgBtnCall, imgBtnChat;

    public static final String USER_INFO = "user_info";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        strFamilyRelation = getArguments().getString("family_relation");
        strRelationship = getArguments().getString("relationship");
        strReferralname = getArguments().getString("Referral_name");
        strAssociation = getArguments().getString("Association");
        strContactInfo = getArguments().getString("contact_info");
        strEmailAddress = getArguments().getString("contact_email");

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View row =  inflater.inflate(R.layout.fragment_referral_completed, container, false);
        llAssociateName = row.findViewById(R.id.ll_associate_name);
        llFamilyRelation = row.findViewById(R.id.ll_family_relation);

        tvReferralName = row.findViewById(R.id.tv_referral_name);
        tvRelationship = row.findViewById(R.id.tv_relationship);
        tvAssociateName = row.findViewById(R.id.tv_associate_name);
        tvFamiyRelation = row.findViewById(R.id.tv_family_relation);
        tvPhoneNumber = row.findViewById(R.id.tv_phone_number);
        tvEmailAddress = row.findViewById(R.id.tv_email_address);
        btnSendRequest = row.findViewById(R.id.btn_send_request);
        imgCloseButton = row.findViewById(R.id.img_close_button);
        imgBtnCall = row.findViewById(R.id.img_btn_call);
        imgBtnChat = row.findViewById(R.id.img_btn_chat);
        btnMakeAnotherReferral = row.findViewById(R.id.btn_make_another_referral);

        btnSendRequest.setOnClickListener(this);
        imgCloseButton.setOnClickListener(this);
        imgBtnChat.setOnClickListener(this);
        imgBtnCall.setOnClickListener(this);
        btnMakeAnotherReferral.setOnClickListener(this);

        if(strRelationship.equalsIgnoreCase("Friend")){
            tvReferralName.setText(strReferralname);
            tvRelationship.setText(strRelationship);
            tvPhoneNumber.setText(strContactInfo);
            tvEmailAddress.setText(strEmailAddress);
            llFamilyRelation.setVisibility(View.GONE);
            llAssociateName.setVisibility(View.GONE);
        }else if(strRelationship.equalsIgnoreCase("Family")){
            tvReferralName.setText(strReferralname);
            tvRelationship.setText(strRelationship);
            llFamilyRelation.setVisibility(View.VISIBLE);
            tvFamiyRelation.setText(strFamilyRelation);
            tvPhoneNumber.setText(strContactInfo);
            tvEmailAddress.setText(strEmailAddress);
        }else if(strRelationship.equalsIgnoreCase("Other Associates")){
            tvReferralName.setText(strReferralname);
            tvRelationship.setText(strRelationship);
            tvPhoneNumber.setText(strContactInfo);
            tvEmailAddress.setText(strEmailAddress);
            llFamilyRelation.setVisibility(View.GONE);
            llAssociateName.setVisibility(View.VISIBLE);
            tvAssociateName.setText(strAssociation);
        }

        sharedpreferences = getActivity().getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        return row;
    }

    public void submitLabRequest() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            String URL = AppConfig.BASE_URL + AppConfig.POST_CREATE_REFERRAL;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("UserID", sharedpreferences.getString("USER_ID", ""));
            jsonBody.put("Interaction_ID", sharedpreferences.getString("interaction_ID", ""));
            jsonBody.put("Referral_name", strReferralname);
            if(strRelationship.equalsIgnoreCase("Family")){
                jsonBody.put("family_relation", strFamilyRelation);
                jsonBody.put("Association", "");
            }else if(strRelationship.equalsIgnoreCase("Other Associates")){
                jsonBody.put("Association", strAssociation);
                jsonBody.put("family_relation", "");
            }
            jsonBody.put("Relationship", strRelationship);
            jsonBody.put("referal_phone", strContactInfo);
            jsonBody.put("referal_email", strEmailAddress);
            jsonBody.put("Is_Confirm", "true");
            jsonBody.put("Status_Id", "Y");
            jsonBody.put("referral_ID", sharedpreferences.getString("referral_ID", ""));
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("VOLLEY_PHYSICIAN_RESONS", response);
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i =0; i<array.length();i++){
                            JSONObject object = array.getJSONObject(0);
                            if(object.getString("inMsg").equalsIgnoreCase("Referral Saved!!!")){
                                Intent intent = new Intent(getActivity(), DashboardActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("flag", "referral");
                                intent.putExtra("Referral_name", strReferralname);
                                intent.putExtra("family_relation", strFamilyRelation);
                                intent.putExtra("Association", strAssociation);
                                intent.putExtra("Relationship", strRelationship);
                                intent.putExtra("referal_phone", strContactInfo);
                                intent.putExtra("referal_email", strEmailAddress);
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
        switch (v.getId()){
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
            case R.id.btn_make_another_referral:
                btnMakeAnotherReferral.setBackground(getResources().getDrawable(R.drawable.fill_appointment_button_corner));
                btnMakeAnotherReferral.setTextColor(getResources().getColor(R.color.btn_text_color));
                submitAnotherReferral();
                break;
            case R.id.img_btn_call:
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:(866) 698-2422"));
                startActivity(i);
                break;
        }
    }

    public void submitAnotherReferral() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            String URL = AppConfig.BASE_URL + AppConfig.POST_CREATE_REFERRAL;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("UserID", sharedpreferences.getString("USER_ID", ""));
            jsonBody.put("Interaction_ID", sharedpreferences.getString("interaction_ID", ""));
            jsonBody.put("Referral_name", strReferralname);
            if(strRelationship.equalsIgnoreCase("Family")){
                jsonBody.put("family_relation", strFamilyRelation);
                jsonBody.put("Association", "");
            }else if(strRelationship.equalsIgnoreCase("Other Associates")){
                jsonBody.put("Association", strAssociation);
                jsonBody.put("family_relation", "");
            }
            jsonBody.put("Relationship", strRelationship);
            jsonBody.put("referal_phone", strContactInfo);
            jsonBody.put("referal_email", strEmailAddress);
            jsonBody.put("Is_Confirm", "true");
            jsonBody.put("Status_Id", "Y");
            jsonBody.put("referral_ID", sharedpreferences.getString("referral_ID", ""));
            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Submit_another_referral", response);
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i =0; i<array.length();i++){
                            JSONObject object = array.getJSONObject(0);
                            if(object.getString("inMsg").equalsIgnoreCase("Referral Saved!!!")){
                                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                ReferralRelationship fragment = new ReferralRelationship();
                                fragmentTransaction.replace(R.id.myContainer, fragment);
                                fragmentTransaction.commit();
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

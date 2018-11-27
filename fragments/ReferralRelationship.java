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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.devool.ucicareconnect.R;
import com.devool.ucicareconnect.activities.DashboardActivity;
import com.devool.ucicareconnect.utils.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class ReferralRelationship extends Fragment implements View.OnClickListener{

    Button btnFriend, btnFamily, btnOtherAssociates, btnNext;
    boolean flagFriend = false, flagFamily = false, flagOtherAssociates = false;

    String strRelationship, strReferralname;

    SharedPreferences sharedpreferences;
    public static final String USER_INFO = "user_info";
    ImageView imgCloseButton, imgBtnCall, imgBtnChat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//(        strReferralname = getArguments().getString("Referral_name");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View row = inflater.inflate(R.layout.fragment_referral_relationship, container, false);
        btnFriend = row.findViewById(R.id.btn_friend);
        btnFamily = row.findViewById(R.id.btn_family);
        btnOtherAssociates = row.findViewById(R.id.btn_other_associates);
        //btnNext = row.findViewById(R.id.btn_next);
        imgCloseButton = row.findViewById(R.id.img_close_button);
        imgBtnCall = row.findViewById(R.id.img_btn_call);
        imgBtnChat = row.findViewById(R.id.img_btn_chat);


        btnFriend.setOnClickListener(this);
        btnFamily.setOnClickListener(this);
        btnOtherAssociates.setOnClickListener(this);
        //btnNext.setOnClickListener(this);
        imgCloseButton.setOnClickListener(this);
        imgBtnChat.setOnClickListener(this);
        imgBtnCall.setOnClickListener(this);

        sharedpreferences = getActivity().getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);

        return row;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_friend:
                strRelationship = btnFriend.getText().toString();
                btnFriend.setBackground(getResources().getDrawable(R.drawable.fill_appointment_button_corner));
                btnFriend.setTextColor(getResources().getColor(R.color.btn_text_color));
                flagFriend=true;

                if(flagFamily) {
                    btnFamily.setBackground(getResources().getDrawable(R.drawable.appointment_btn_corner));
                    btnFamily.setTextColor(getResources().getColor(R.color.bacgroun_color));
                }

                if(flagOtherAssociates) {
                    btnOtherAssociates.setBackground(getResources().getDrawable(R.drawable.appointment_btn_corner));
                    btnOtherAssociates.setTextColor(getResources().getColor(R.color.bacgroun_color));
                }
                submitRelationShip();
                break;
            case R.id.btn_family:
                strRelationship = btnFamily.getText().toString();
                btnFamily.setBackground(getResources().getDrawable(R.drawable.fill_appointment_button_corner));
                btnFamily.setTextColor(getResources().getColor(R.color.btn_text_color));
                flagFamily = true;

                if(flagFriend) {
                    btnFriend.setBackground(getResources().getDrawable(R.drawable.appointment_btn_corner));
                    btnFriend.setTextColor(getResources().getColor(R.color.bacgroun_color));
                }
                if(flagOtherAssociates) {
                    btnOtherAssociates.setBackground(getResources().getDrawable(R.drawable.appointment_btn_corner));
                    btnOtherAssociates.setTextColor(getResources().getColor(R.color.bacgroun_color));
                }

                submitRelationShip();
                break;
            case R.id.btn_other_associates:
                strRelationship = btnOtherAssociates.getText().toString();
                btnOtherAssociates.setBackground(getResources().getDrawable(R.drawable.fill_appointment_button_corner));
                btnOtherAssociates.setTextColor(getResources().getColor(R.color.btn_text_color));
                flagOtherAssociates=true;

                if(flagFriend) {
                    btnFriend.setBackground(getResources().getDrawable(R.drawable.appointment_btn_corner));
                    btnFriend.setTextColor(getResources().getColor(R.color.bacgroun_color));
                }
                if(flagFamily) {
                    btnFamily.setBackground(getResources().getDrawable(R.drawable.appointment_btn_corner));
                    btnFamily.setTextColor(getResources().getColor(R.color.bacgroun_color));
                }

                submitRelationShip();
                break;
           /* case R.id.btn_next:
                btnNext.setBackground(getResources().getDrawable(R.drawable.fill_appointment_button_corner));
                btnNext.setTextColor(getResources().getColor(R.color.btn_text_color));
                submitRelationShip();
                break;*/
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

    private void submitRelationShip() {

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            String URL = AppConfig.BASE_URL + AppConfig.POST_CREATE_REFERRAL;
            JSONObject jsonBody = new JSONObject();
            //jsonBody.put("Referral_name", strReferralname);
            jsonBody.put("UserID", sharedpreferences.getString("USER_ID", ""));
            jsonBody.put("Interaction_ID", sharedpreferences.getString("interaction_ID", ""));
            jsonBody.put("Relationship", strRelationship);
            jsonBody.put("Is_Confirm", "true");
            jsonBody.put("Status_Id", "N");
            jsonBody.put("referral_ID", sharedpreferences.getString("referral_ID", ""));

            Log.e("UserID", sharedpreferences.getString("USER_ID", ""));
            Log.e("Interaction_ID", sharedpreferences.getString("interaction_ID", ""));
            Log.e("Relationship", strRelationship);
            Log.e("family_relation", "");
            Log.e("referal_phone", "");
            Log.e("referal_email", "");
            Log.e("Is_Confirm", "false");
            Log.e("Association", "");
            Log.e("Status_Id", "N");
            Log.e("referral_ID", sharedpreferences.getString("referral_ID", ""));

            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.e("REFERRAL_RESPONSE", response);
                        JSONArray array = new JSONArray(response);

                        for (int i =0; i<array.length();i++){
                            JSONObject object = array.getJSONObject(0);
                            if(object.getString("inMsg").equalsIgnoreCase("Referral Saved!!!")){
                                if(strRelationship.equalsIgnoreCase("Friend")){
                                    //Toast.makeText(getActivity(), "Friend", Toast.LENGTH_SHORT).show();
                                    android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    ReferralContactInfoFragment fragment = new ReferralContactInfoFragment();
                                    Bundle args = new Bundle();
                                    args.putString("relationship", strRelationship);
                                    //args.putString("Referral_name", strReferralname);
                                    fragment.setArguments(args);
                                    fragmentTransaction.replace(R.id.myContainer, fragment);
                                    fragmentTransaction.commit();
                                    fragmentTransaction.addToBackStack(null);
                                }else if(strRelationship.equalsIgnoreCase("Family")){
                                    //Toast.makeText(getActivity(), "Family", Toast.LENGTH_SHORT).show();
                                    android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    ReferralFamilyRelationFragment fragment = new ReferralFamilyRelationFragment();
                                    Bundle args = new Bundle();
                                    args.putString("relationship", strRelationship);
                                    //args.putString("Referral_name", strReferralname);
                                    fragment.setArguments(args);
                                    fragmentTransaction.replace(R.id.myContainer, fragment);
                                    fragmentTransaction.commit();
                                    fragmentTransaction.addToBackStack(null);
                                }else if(strRelationship.equalsIgnoreCase("Other Associates")){
                                    //Toast.makeText(getActivity(), "Other Associates", Toast.LENGTH_SHORT).show();
                                    android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    ReferralAssociateName fragment = new ReferralAssociateName();
                                    Bundle args = new Bundle();
                                    args.putString("relationship", strRelationship);
                                    //args.putString("Referral_name", strReferralname);
                                    fragment.setArguments(args);
                                    fragmentTransaction.replace(R.id.myContainer, fragment);
                                    fragmentTransaction.commit();
                                    fragmentTransaction.addToBackStack(null);
                                }
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

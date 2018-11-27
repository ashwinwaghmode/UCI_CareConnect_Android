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
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ReferralContactInfoFragment extends Fragment implements View.OnClickListener {

    public static final String USER_INFO = "user_info";
    EditText edtRelativePhoneNumber, edtRelativeEmailAddress, edtReferralName;
    Button btnNext;
    SharedPreferences sharedpreferences;
    String strRelationship, strReferralname, strFamilyRelation, strAssociation;
    ImageView imgCloseButton, imgBtnCall, imgBtnChat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        strRelationship = getArguments().getString("relationship");
        //strReferralname = getArguments().getString("Referral_name");
        strFamilyRelation = getArguments().getString("family_relation");
        strAssociation = getArguments().getString("Association");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View row = inflater.inflate(R.layout.fragment_referral_contact_info, container, false);
        edtRelativePhoneNumber = row.findViewById(R.id.edit_relaative_contact_name);
        edtRelativeEmailAddress = row.findViewById(R.id.edit_referral_email);
        edtReferralName = row.findViewById(R.id.edit_referral_name);
        imgCloseButton = row.findViewById(R.id.img_close_button);
        imgBtnCall = row.findViewById(R.id.img_btn_call);
        imgBtnChat = row.findViewById(R.id.img_btn_chat);

        btnNext = row.findViewById(R.id.btn_next);
        imgCloseButton.setOnClickListener(this);
        imgBtnChat.setOnClickListener(this);
        imgBtnCall.setOnClickListener(this);

        btnNext.setOnClickListener(this);
        sharedpreferences = getActivity().getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);


        edtRelativePhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        edtReferralName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtReferralName.setBackgroundResource(R.drawable.edit_text_background);
                if (edtReferralName.getText().toString().equalsIgnoreCase("")) {
                    edtReferralName.setBackgroundResource(R.drawable.edit_text_background);
                    edtRelativePhoneNumber.setBackgroundResource(R.drawable.edit_text_background);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtRelativePhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtRelativePhoneNumber.setBackgroundResource(R.drawable.edit_text_background);
                if (edtRelativePhoneNumber.getText().toString().equalsIgnoreCase("")) {
                    edtRelativePhoneNumber.setBackgroundResource(R.drawable.edit_text_background);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return row;


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (edtReferralName.getText().toString().equals("") && edtRelativePhoneNumber.getText().toString().equals("")) {
                    edtReferralName.setBackgroundResource(R.drawable.activation_error_color_background);
                    edtRelativePhoneNumber.setBackgroundResource(R.drawable.activation_error_color_background);
                    break;
                } else if (edtReferralName.getText().toString().equals("")) {
                    edtReferralName.setBackgroundResource(R.drawable.activation_error_color_background);
                    break;
                } else if (edtRelativePhoneNumber.getText().toString().equals("")) {
                    edtRelativePhoneNumber.setBackgroundResource(R.drawable.activation_error_color_background);
                    break;
                } else if(edtRelativePhoneNumber.getText().toString().length()<14){
                    edtRelativePhoneNumber.setBackgroundResource(R.drawable.activation_error_color_background);
                    break;
                } else {
                    edtRelativePhoneNumber.setBackgroundResource(R.drawable.edit_text_background);
                    edtReferralName.setBackgroundResource(R.drawable.edit_text_background);
                    btnNext.setBackground(getResources().getDrawable(R.drawable.fill_appointment_button_corner));
                    btnNext.setTextColor(getResources().getColor(R.color.btn_text_color));
                    submitContactInfo();
                    break;
                }

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

    private void submitContactInfo() {

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            String URL = AppConfig.BASE_URL + AppConfig.POST_CREATE_REFERRAL;
            JSONObject jsonBody = new JSONObject();
            if (strRelationship.equalsIgnoreCase("Friend")) {
                //Toast.makeText(getActivity(), "COntact_Friend", Toast.LENGTH_SHORT).show();
                jsonBody.put("Referral_name", edtReferralName.getText().toString());
                jsonBody.put("family_relation", "");
                jsonBody.put("Association", "");
                jsonBody.put("referal_phone", edtRelativePhoneNumber.getText().toString());
                jsonBody.put("referal_email", edtRelativeEmailAddress.getText().toString());
            } else if (strRelationship.equalsIgnoreCase("Family")) {
                jsonBody.put("family_relation", strFamilyRelation);
                jsonBody.put("Referral_name", edtReferralName.getText().toString());
                // Toast.makeText(getActivity(), "COntact_Family", Toast.LENGTH_SHORT).show();
                jsonBody.put("Association", "");
                jsonBody.put("referal_phone", edtRelativePhoneNumber.getText().toString());
                jsonBody.put("referal_email", edtRelativeEmailAddress.getText().toString());
            } else if (strRelationship.equalsIgnoreCase("Other Associates")) {
                jsonBody.put("Referral_name", edtReferralName.getText().toString());
                // Toast.makeText(getActivity(), "COntact_Other_ass", Toast.LENGTH_SHORT).show();
                jsonBody.put("family_relation", "");
                jsonBody.put("Association", strAssociation);
                jsonBody.put("referal_phone", edtRelativePhoneNumber.getText().toString());
                jsonBody.put("referal_email", edtRelativeEmailAddress.getText().toString());
            }
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
            Log.e("Association", "associate");
            Log.e("Status_Id", "N");
            Log.e("referral_ID", sharedpreferences.getString("referral_ID", ""));

            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.e("REFERRAL_RESPONSE", response);
                        JSONArray array = new JSONArray(response);

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(0);
                            if (object.getString("inMsg").equalsIgnoreCase("Referral Saved!!!")) {
                                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                ReferralConfirmationFragment fragment = new ReferralConfirmationFragment();
                                Bundle args = new Bundle();
                                if (strRelationship.equalsIgnoreCase("Family")) {
                                    args.putString("family_relation", strFamilyRelation);
                                } else if (strRelationship.equalsIgnoreCase("Friend")) {

                                } else if (strRelationship.equalsIgnoreCase("Other Associates")) {
                                    args.putString("Association", strAssociation);
                                }
                                args.putString("relationship", strRelationship);
                                args.putString("Referral_name", edtReferralName.getText().toString());
                                args.putString("contact_info", edtRelativePhoneNumber.getText().toString());
                                args.putString("contact_email", edtRelativeEmailAddress.getText().toString());

                                /*Log.e("relationship", strRelationship);
                                Log.e("Association", strAssociation);
                                Log.e("Referral_name", strReferralname);
                                Log.e("family_relation", strFamilyRelation);
                                Log.e("contact_info", edtRelativePhoneNumber.getText().toString());
                                Log.e("contact_email", edtRelativeEmailAddress.getText().toString());*/
                                fragment.setArguments(args);
                                fragmentTransaction.replace(R.id.myContainer, fragment);
                                fragmentTransaction.commit();
                                fragmentTransaction.addToBackStack(null);
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

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
import android.widget.EditText;
import android.widget.ImageView;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class NewAppoRadiologyExamTypetFragment extends Fragment implements View.OnClickListener{

    Button btnNxt;
    String strAppoimentType, strMeetPurpose, strEdtExamType, strInteractionId, strInteractionDetailId, strUserId;
    EditText edtExamType;
    ImageView imgCloseButton, imgBtnCall, imgBtnChat;

    SharedPreferences sharedpreferences;
    public static final String USER_INFO= "user_info";

    public static NewAppoRadiologyExamTypetFragment newInstance(String param1, String param2) {
        NewAppoRadiologyExamTypetFragment fragment = new NewAppoRadiologyExamTypetFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        strAppoimentType = getArguments().getString("appointment_type");
        strMeetPurpose = getArguments().getString("meet_purpose");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View row = inflater.inflate(R.layout.fragment_new_appo_radio_test, container, false);
        edtExamType = (EditText)row.findViewById(R.id.edit_text_exam_type);
        btnNxt = (Button)row.findViewById(R.id.btn_next);
        imgCloseButton = row.findViewById(R.id.img_close_button);
        imgBtnCall = row.findViewById(R.id.img_btn_call);
        imgBtnChat = row.findViewById(R.id.img_btn_chat);

        btnNxt.setOnClickListener(this);
        imgCloseButton.setOnClickListener(this);
        imgBtnChat.setOnClickListener(this);
        imgBtnCall.setOnClickListener(this);

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
            case R.id.btn_next:
                strEdtExamType = edtExamType.getText().toString();
                btnNxt.setBackground(getResources().getDrawable(R.drawable.fill_appointment_button_corner));
                btnNxt.setTextColor(getResources().getColor(R.color.btn_text_color));
                submit();
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                NewAppointmentRequestedByUCIFragment fragment = new NewAppointmentRequestedByUCIFragment();
                fragmentTransaction.replace(R.id.myContainer, fragment);
                Bundle args = new Bundle();
                args.putString("appointment_type", strAppoimentType);
                args.putString("meet_purpose", strMeetPurpose);
                args.putString("exam_type", strEdtExamType);
                fragment.setArguments(args);
                fragmentTransaction.commit();
                fragmentTransaction.addToBackStack(null);
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

    public void submit()
    {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            String URL = AppConfig.BASE_URL+AppConfig.CREATE_REQUEST_HISTORY;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("Interaction_DTL_ID", strInteractionDetailId);
            jsonBody.put("UserID", strUserId);
            jsonBody.put("status_Id", "N");
            jsonBody.put("Interaction_ID", strInteractionId);
            jsonBody.put("Requested_By", "");
            jsonBody.put("OutsideProvider_number", "");
            jsonBody.put("Availability", "");
            jsonBody.put("Time_of_day", "");
            jsonBody.put("Physicians_Name", "");
            jsonBody.put("Physicians_Specialty ", "");
            jsonBody.put("Radiology_Type", strEdtExamType);
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

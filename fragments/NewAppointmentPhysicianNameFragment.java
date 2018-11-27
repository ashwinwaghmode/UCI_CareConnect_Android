package com.devool.ucicareconnect.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.devool.ucicareconnect.helper.DoctorNameHelper;
import com.devool.ucicareconnect.helper.DoctorSpecialtyHelper;
import com.devool.ucicareconnect.utils.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class NewAppointmentPhysicianNameFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {


    public static final String USER_INFO = "user_info";
    Button btnNext;
    AutoCompleteTextView edtPhysicianName;
    String strMeetPurpose, strAAppointmentType, strInteractionId, strInteractionDetailId, strUserId, strPhysicianType, strSpecialtyId, strPhysicianName;
    ArrayAdapter<String> adapter;
    ArrayList<String> doctorSpecialtyList = new ArrayList<>();
    ArrayList<DoctorNameHelper> doctorSpecialtyItemList = new ArrayList<>();
    HashMap<String, String> customerListDynamic = new HashMap<>();


    SharedPreferences sharedpreferences;
    ImageView imgCloseButton;
    private ListView lv;

    public static NewAppointmentPhysicianNameFragment newInstance(String param1, String param2) {
        NewAppointmentPhysicianNameFragment fragment = new NewAppointmentPhysicianNameFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            strMeetPurpose = getArguments().getString("meet_purpose");
            strAAppointmentType = getArguments().getString("appointment_type");
            strPhysicianType = getArguments().getString("physician_type");
            strSpecialtyId = getArguments().getString("specialty_id");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View row = inflater.inflate(R.layout.fragment_new_appointment_physician_name, container, false);
        btnNext = (Button) row.findViewById(R.id.btn_next);
        edtPhysicianName = (AutoCompleteTextView) row.findViewById(R.id.edit_physician_name);
        imgCloseButton = row.findViewById(R.id.img_close_button);

        btnNext.setOnClickListener(this);
        imgCloseButton.setOnClickListener(this);

        sharedpreferences = getActivity().getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        strInteractionDetailId = sharedpreferences.getString("interaction_DTL_ID", "");
        strInteractionId = sharedpreferences.getString("interaction_ID", "");
        strUserId = sharedpreferences.getString("USER_ID", "");

        doctorSpecialtyList.clear();
        doctorSpecialtyItemList.clear();
        edtPhysicianName.setOnItemClickListener(this);
        getDoctorsName();
        return row;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                submit();
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                NewAppointmentAvailabiltyFragmenet fragment = new NewAppointmentAvailabiltyFragmenet();
                Bundle args = new Bundle();
                args.putString("meet_purpose", strMeetPurpose);
                args.putString("appointment_type", strAAppointmentType);
                args.putString("physician_name", edtPhysicianName.getText().toString());
                args.putString("physician_type", strPhysicianType);
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.myContainer, fragment);
                fragmentTransaction.commit();
                fragmentTransaction.addToBackStack(null);
                break;
            case R.id.img_close_button:
                Intent intent = new Intent(getActivity(), DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
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
            jsonBody.put("Requested_By", "");
            jsonBody.put("OutsideProvider_number", "");
            jsonBody.put("Availability", "");
            jsonBody.put("Time_of_day", "");
            jsonBody.put("Physicians_Name", strPhysicianName);
            //jsonBody.put("Physicians_Specialty", "");
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        strPhysicianName = (String) parent.getItemAtPosition(position);
        Toast.makeText(getActivity(), strPhysicianName, Toast.LENGTH_SHORT).show();
    }


    public void getDoctorsName() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String URL = AppConfig.BASE_URL + AppConfig.GET_DOCTOR_BY_SPECILALTY + "/" + strSpecialtyId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("getDoctorsName", response);
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        DoctorNameHelper helper = new DoctorNameHelper();
                        helper.setStrDoctorId(object.getString("doctor_ID"));
                        helper.setStrDoctorName(object.getString("doctor_Name"));
                        helper.setStrDoctorSpecialtyId(object.getString("doctor_Specialty_ID"));
                        doctorSpecialtyItemList.add(helper);
                        doctorSpecialtyList.add(object.getString("doctor_Name"));
                    }
                    adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.product_name, doctorSpecialtyList);
                    adapter.notifyDataSetChanged();
                    edtPhysicianName.setAdapter(adapter);
                    edtPhysicianName.setThreshold(1);

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

    }
}

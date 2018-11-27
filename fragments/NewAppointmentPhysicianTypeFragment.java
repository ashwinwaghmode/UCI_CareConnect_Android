package com.devool.ucicareconnect.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.TextView;
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
import com.devool.ucicareconnect.adapter.DoctorSpecialtyAdapter;
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

public class NewAppointmentPhysicianTypeFragment extends Fragment implements View.OnClickListener {

    public static final String USER_INFO = "user_info";
    AutoCompleteTextView edtPhysicianType, edtPhysicianame;
    Button btnNext;
    String strMeetPurpose, strPhysicianName, strAAppointmentType, strInteractionId, strInteractionDetailId, strUserId, strSpecialtyId, strPhysicianType, strPhysiciaanName;
    ImageView imgCloseButton, imgBtnCall, imgBtnChat;
    SharedPreferences sharedpreferences;
    ArrayAdapter adapter, doctorAdapter;
    ArrayList<DoctorSpecialtyHelper> doctorSpecialtyHelperList = new ArrayList<>();
    ArrayList<String> doctorSpecialtyList = new ArrayList<>();
    HashMap<String, String> customerListDynamic = new HashMap<>();
    ArrayList<String> getdoctorSpecialtyList = new ArrayList<>();
    ArrayList<DoctorNameHelper> doctorSpecialtyItemList = new ArrayList<>();
    TextView tvPhysicianHeading, tvPhysicianSubheading;
    private ListView lv;
    boolean isNewUser;

    public static NewAppointmentPhysicianTypeFragment newInstance(String param1, String param2) {
        NewAppointmentPhysicianTypeFragment fragment = new NewAppointmentPhysicianTypeFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    public static Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            strMeetPurpose = getArguments().getString("meet_purpose");
            strPhysicianName = getArguments().getString("physician_name");
            strAAppointmentType = getArguments().getString("appointment_type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View row = inflater.inflate(R.layout.fragment_new_appointment_physician_type, container, false);
        edtPhysicianType = (AutoCompleteTextView) row.findViewById(R.id.edit_physician_type);
        edtPhysicianame = (AutoCompleteTextView) row.findViewById(R.id.edit_physician_name);
        tvPhysicianHeading = row.findViewById(R.id.tv_request_heading);
        tvPhysicianSubheading = row.findViewById(R.id.tv_request_subheading);
        btnNext = (Button) row.findViewById(R.id.btn_next);
        imgCloseButton = row.findViewById(R.id.img_close_button);
        imgBtnCall = row.findViewById(R.id.img_btn_call);
        imgBtnChat = row.findViewById(R.id.img_btn_chat);

        edtPhysicianType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                strSpecialtyId = (String) getKeyFromValue(customerListDynamic, edtPhysicianType.getText().toString());
                //Toast.makeText(getActivity(), strSpecialtyId, Toast.LENGTH_SHORT).show();
                doctorSpecialtyItemList.clear();
                getdoctorSpecialtyList.clear();
                getDoctorsName(strSpecialtyId);
                tvPhysicianHeading.setText("Physician's Name");
                tvPhysicianSubheading.setText("Do you know the physician's name?");
                strPhysicianType = (String) parent.getItemAtPosition(position);
                //Toast.makeText(getActivity(), strPhysicianType, Toast.LENGTH_SHORT).show();
            }
        });
        edtPhysicianame.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                strPhysiciaanName = (String) parent.getItemAtPosition(position);
            }
        });

        btnNext.setOnClickListener(this);
        imgCloseButton.setOnClickListener(this);
        imgBtnChat.setOnClickListener(this);
        imgBtnCall.setOnClickListener(this);

        sharedpreferences = getActivity().getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        strInteractionDetailId = sharedpreferences.getString("interaction_DTL_ID", "");
        strInteractionId = sharedpreferences.getString("interaction_ID", "");
        strUserId = sharedpreferences.getString("USER_ID", "");

        doctorSpecialtyList.clear();
        doctorSpecialtyHelperList.clear();

        getDoctorSpecialty();

        isNewUser = sharedpreferences.getBoolean("IS_NEW_USER", false);
        //isNewUser = false;

        return row;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:

                if (isNewUser) {
                    submit();
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    NewAppointmentAvailabiltyFragmenet fragment = new NewAppointmentAvailabiltyFragmenet();
                    Bundle args = new Bundle();
                    args.putString("meet_purpose", strMeetPurpose);
                    args.putString("appointment_type", strAAppointmentType);
                    args.putString("physician_name", strPhysiciaanName);
                    args.putString("physician_type", strPhysicianType);
                    fragment.setArguments(args);
                    fragmentTransaction.replace(R.id.myContainer, fragment);
                    fragmentTransaction.commit();
                    fragmentTransaction.addToBackStack(null);
                }else {
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    NewAppointmentAvailabiltyFragmenet fragment = new NewAppointmentAvailabiltyFragmenet();
                    Bundle args = new Bundle();
                    args.putString("meet_purpose", strMeetPurpose);
                    args.putString("appointment_type", strAAppointmentType);
                    args.putString("physician_name", strPhysiciaanName);
                    args.putString("physician_type", strPhysicianType);
                    fragment.setArguments(args);
                    fragmentTransaction.replace(R.id.myContainer, fragment);
                    fragmentTransaction.commit();
                    fragmentTransaction.addToBackStack(null);
                }
               /* android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                NewAppointmentPhysicianNameFragment fragment = new NewAppointmentPhysicianNameFragment();
                Bundle args = new Bundle();
                args.putString("meet_purpose", strMeetPurpose);
                args.putString("physician_name", strPhysicianName);
                args.putString("appointment_type", strAAppointmentType);
                args.putString("physician_type", edtPhysicianType.getText().toString());
                args.putString("specialty_id", strSpecialtyId);
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.myContainer, fragment);
                fragmentTransaction.commit();
                fragmentTransaction.addToBackStack(null);*/


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
            jsonBody.put("Requested_By", "");
            jsonBody.put("OutsideProvider_number", "");
            jsonBody.put("Availability", "");
            jsonBody.put("Time_of_day", "");
            jsonBody.put("Physicians_Name", strPhysiciaanName);
            jsonBody.put("Physicians_Specialty", strPhysicianType);
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

    /*@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.edit_physician_type:
                strSpecialtyId = (String) getKeyFromValue(customerListDynamic, edtPhysicianType.getText().toString());
                Toast.makeText(getActivity(), strSpecialtyId, Toast.LENGTH_SHORT).show();
                doctorSpecialtyItemList.clear();
                getdoctorSpecialtyList.clear();
                getDoctorsName(strSpecialtyId);
                strPhysicianType = (String) parent.getItemAtPosition(position);
                break;
            case R.id.edit_physician_name:

                break;
        }
    }*/

    public void getDoctorSpecialty() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String URL = AppConfig.BASE_URL + AppConfig.GET_DOCTOR_SPECIALTY;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("getDoctorsSpecialty", response);
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        DoctorSpecialtyHelper helper = new DoctorSpecialtyHelper();
                        helper.setStrDoctorSpecialtyId(object.getString("doctor_Specialty_ID"));
                        helper.setStrDoctorSpecialty(object.getString("doctor_Specialty"));
                        helper.setStrIsActive(object.getString("is_Active"));
                        doctorSpecialtyHelperList.add(helper);
                        customerListDynamic.put(object.getString("doctor_Specialty_ID"), object.getString("doctor_Specialty"));
                    }
                    for (DoctorSpecialtyHelper doctorSpecialtyHelper : doctorSpecialtyHelperList) {
                        doctorSpecialtyList.add(doctorSpecialtyHelper.getStrDoctorSpecialty());
                    }
                    adapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.product_name, doctorSpecialtyList);
                    adapter.notifyDataSetChanged();
                    edtPhysicianType.setAdapter(adapter);
                    edtPhysicianType.setThreshold(1);

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

    public void getDoctorsName(String specialtyId) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        String URL = AppConfig.BASE_URL + AppConfig.GET_DOCTOR_BY_SPECILALTY + "/" + specialtyId;

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
                        getdoctorSpecialtyList.add(object.getString("doctor_Name"));
                    }
                    doctorAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, R.id.product_name, getdoctorSpecialtyList);
                    doctorAdapter.notifyDataSetChanged();
                    edtPhysicianame.setAdapter(doctorAdapter);
                    edtPhysicianame.setThreshold(1);

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

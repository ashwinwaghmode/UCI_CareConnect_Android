package com.devool.ucicareconnect.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.devool.ucicareconnect.adapter.MultiViewTypeAdapter;
import com.devool.ucicareconnect.helper.ContextualModelItems;
import com.devool.ucicareconnect.pubnub.LoginActivity;
import com.devool.ucicareconnect.pubnub.MainActivity;
import com.devool.ucicareconnect.utils.AppConfig;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DashboardActivity extends Activity implements View.OnClickListener {

    public static final String USER_INFO = "user_info";
    RelativeLayout relScheduing, relQuickAnswers, relMyChart, relRefrrals, llSlideDrawer;
    LinearLayout relPopup;
    SharedPreferences sharedpreferences;
    String currentDateandTime, strInterActionTypeID;
    Button btnEventDetails, btnViewReferralDetails;

    String strRequest, strReltionship;

    TextView tvHeading, tvSubHeading, tvWelcomeTitle, tvHeadingSlider;
    Intent i;
    ImageView imgUserInfo, imgClosebtn, imgNotch;
    ArrayList<ContextualModelItems> contextualModelItems = new ArrayList<ContextualModelItems>();
    MultiViewTypeAdapter adapter;
    RecyclerView contextualView;
    Activity activity;
    TextView tvChatus, tvScheduling, tvMyChart, tvReferral;
    //SlidingUpPanelLayout layout;
    NestedScrollView myView;
    BottomSheetBehavior bottomSheetBehavior;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        activity = DashboardActivity.this;
        myView = findViewById(R.id.sliding_layout);
        relScheduing = (RelativeLayout) findViewById(R.id.rel_scheduling);
        relQuickAnswers = findViewById(R.id.rel_quick_answer);
        relMyChart = findViewById(R.id.rel_my_chart);
        relRefrrals = findViewById(R.id.rel_referrals);
        contextualView = findViewById(R.id.recycler_view_request);
        relPopup = findViewById(R.id.rel_pop_up);
        tvHeadingSlider = findViewById(R.id.tv_heading);
        //btnEventDetails = findViewById(R.id.btn_view_event_details);
        //btnViewReferralDetails = findViewById(R.id.btn_view_referral_details);
        //tvHeading = findViewById(R.id.tv_heading);
        //tvSubHeading = findViewById(R.id.tv_subheading);
        tvWelcomeTitle = findViewById(R.id.tv_welcome_tittle);
        imgUserInfo = findViewById(R.id.img_user_info);
        imgClosebtn = (ImageView) findViewById(R.id.img_close_button);
        imgNotch = findViewById(R.id.img_notch);
        llSlideDrawer = findViewById(R.id.ll_slide_drawer);
        tvChatus = findViewById(R.id.tv_sub_heading_chat);
        tvScheduling = findViewById(R.id.tv_sub_heading_scheduling);
        tvMyChart = findViewById(R.id.tv_sub_my_chart);
        tvReferral = findViewById(R.id.tv_sub_my_referral);

        relScheduing.setOnClickListener(this);
        relQuickAnswers.setOnClickListener(this);
        relMyChart.setOnClickListener(this);
        relRefrrals.setOnClickListener(this);
        //btnEventDetails.setOnClickListener(this);
        // btnViewReferralDetails.setOnClickListener(this);
        imgUserInfo.setOnClickListener(this);
        imgClosebtn.setOnClickListener(this);
        //layout.setScrollableViewHelper(new NestedScrollableViewHelper());
        /*layout.setPanelSlideListener(onSlideListener());
        llSlideDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                imgNotch.setVisibility(View.GONE);
                tvCloseButton.setVisibility(View.VISIBLE);
            }
        });*/


        //layout.setDragView(findViewById(R.id.recycler_view_request));
       /* layout.addPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelOpened(@NonNull View view) {

            }

            @Override
            public void onPanelClosed(@NonNull View view) {

            }
        });*/

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentDateandTime = sdf.format(new Date());

        sharedpreferences = getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        submitFCMforNotification();

        i = getIntent();
        strRequest = i.getStringExtra("request");
        strReltionship = i.getStringExtra("Relationship");

        relPopup.setVisibility(View.VISIBLE);
        getContextualList();
        //relPopup.setVisibility(View.VISIBLE);

        if (getIntent().hasExtra("followup_request_flag")) {
            //getContextualList();
            relPopup.setVisibility(View.VISIBLE);
            /*btnViewReferralDetails.setVisibility(View.GONE);
            btnEventDetails.setVisibility(View.VISIBLE);*/
        }

        if (getIntent().hasExtra("flag")) {
            // getContextualList();
            relPopup.setVisibility(View.VISIBLE);
            /*btnViewReferralDetails.setVisibility(View.VISIBLE);
            btnEventDetails.setVisibility(View.GONE);
            tvHeading.setText("Referral Submitted");
            tvSubHeading.setText("Thank you for the referral. We will contact them shortly.");
            btnEventDetails.setText("View Referral Details");*/
        }

        if (getIntent().hasExtra("signup_flag")) {
            tvWelcomeTitle.setText("We're happy to assist you with anything at UCI.");
        }

        if (getIntent().hasExtra("sigin_flag")) {
            tvWelcomeTitle.setText("How can we help?");
        }

        if (getIntent().hasExtra("is_lab_requested_completed")) {
            relPopup.setVisibility(View.VISIBLE);
            // getContextualList();
            /*relPopup.setVisibility(View.VISIBLE);
            btnViewReferralDetails.setVisibility(View.GONE);
            btnEventDetails.setVisibility(View.VISIBLE);*/
        }
        if (getIntent().hasExtra("is_physician_requested_completed")) {
            relPopup.setVisibility(View.VISIBLE);
            //  getContextualList();
            /*relPopup.setVisibility(View.VISIBLE);
            btnViewReferralDetails.setVisibility(View.GONE);
            btnEventDetails.setVisibility(View.VISIBLE);*/
        }
        if (getIntent().hasExtra("is_radiology_requested_completed")) {
            relPopup.setVisibility(View.VISIBLE);
            // getContextualList();
            /*relPopup.setVisibility(View.VISIBLE);
            btnViewReferralDetails.setVisibility(View.GONE);
            btnEventDetails.setVisibility(View.VISIBLE);*/
        }

        bottomSheetBehavior = BottomSheetBehavior.from(myView);
        bottomSheetBehavior.setPeekHeight(250);


        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        imgNotch.setVisibility(View.GONE);
                        imgClosebtn.setVisibility(View.VISIBLE);
                        tvHeadingSlider.setVisibility(View.VISIBLE);
                        llSlideDrawer.setBackground(getResources().getDrawable(R.drawable.account_textview_header_shadow));
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        imgNotch.setVisibility(View.VISIBLE);
                        imgClosebtn.setVisibility(View.GONE);
                        tvHeadingSlider.setVisibility(View.GONE);
                        llSlideDrawer.setBackground(getResources().getDrawable(R.drawable.slider_bakgoun_toolbar_white));
                    }
                    break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

    }

   /* private SlidingUpPanelLayout.PanelSlideListener onSlideListener() {
        return new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float v) {
                Log.e("", "panel is sliding");
            }

            @Override
            public void onPanelCollapsed(View view) {
                Log.e("", "panel Collapse");
            }

            @Override
            public void onPanelExpanded(View view) {
                Log.e("", "panel expand");
            }

            @Override
            public void onPanelAnchored(View view) {
                Log.e("", "panel anchored");
            }

            @Override
            public void onPanelHidden(View view) {
                Log.e("", "panel is Hidden");
            }
        };

    }*/
   /* public class NestedScrollableViewHelper extends ScrollableViewHelper {
        public int getScrollableViewScrollPosition(View scrollableView, boolean isSlidingUp) {
            if (contextualView != null) {
                if(isSlidingUp){
                    return contextualView.getScrollY();
                } else {
                    RecyclerView nsv = ((RecyclerView) contextualView);
                    View child = nsv.getChildAt(0);
                    return (child.getBottom() - (nsv.getHeight() + nsv.getScrollY()));
                }
            } else {
                return 0;
            }
        }
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rel_scheduling:
                strInterActionTypeID = "2";
                relScheduing.setBackgroundResource(R.drawable.scheduling_btn_background);
                tvScheduling.setTextColor(getResources().getColor(R.color.btn_text_color));
                createDonorInteraction();
                break;
            case R.id.rel_quick_answer:
                strInterActionTypeID = "1";
               Intent i1 = new Intent(DashboardActivity.this, MainActivity.class);
               startActivity(i1);
                relQuickAnswers.setBackgroundResource(R.drawable.scheduling_btn_background);
                tvChatus.setTextColor(getResources().getColor(R.color.btn_text_color));
                break;
            case R.id.rel_my_chart:
                strInterActionTypeID = "3";
                Toast.makeText(this, "WIP", Toast.LENGTH_SHORT).show();
                //relMyChart.setBackgroundResource(R.drawable.scheduling_btn_background);
                break;
            case R.id.rel_referrals:
                strInterActionTypeID = "4";
                relRefrrals.setBackgroundResource(R.drawable.scheduling_btn_background);
                tvReferral.setTextColor(getResources().getColor(R.color.btn_text_color));
                //createReferral();
                /*Intent intent = new Intent(DashboardActivity.this, SchedulingActivity.class);
                intent.putExtra("Interaction_Type_ID", strInterActionTypeID);
                startActivity(intent);*/
                createDonorInteraction();
                break;
            case R.id.img_close_button:
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                break;
            /*case R.id.rel_pop_up:
                contextualView.setVisibility(View.VISIBLE);
                break;*/
            /*case R.id.btn_view_event_details:
                if(getIntent().hasExtra("followup_request_flag")){
                    Intent intent = new Intent(DashboardActivity.this, CancelFollowUpActivity.class);
                    intent.putExtra("followup_request_flag", "followup_request");
                    intent.putExtra("appointment_type", getIntent().getExtras().getString("appointment_type"));
                    intent.putExtra("Physicians_Name", getIntent().getExtras().getString("Physicians_Name"));
                    intent.putExtra("Availability", getIntent().getExtras().getString("Availability"));
                    intent.putExtra("Time_of_day", getIntent().getExtras().getString("Time_of_day"));
                    intent.putExtra("Any_Specific_Request", getIntent().getExtras().getString("Any_Specific_Request"));
                    startActivity(intent);
                    break;
                }else {
                    if (strRequest.equalsIgnoreCase("Lab")) {
                        Intent i = new Intent(DashboardActivity.this, CancelAppointmentActivity.class);
                        i.putExtra("request", "Lab");
                        i.putExtra("appointment_type", getIntent().getExtras().getString("appointment_type"));
                        i.putExtra("Speciality", getIntent().getExtras().getString("Speciality"));
                        i.putExtra("Availability", getIntent().getExtras().getString("Availability"));
                        i.putExtra("Time_of_day", getIntent().getExtras().getString("Time_of_day"));
                        i.putExtra("Any_Specific_Request", getIntent().getExtras().getString("Any_Specific_Request"));
                        startActivity(i);
                        break;
                    } else if (strRequest.equalsIgnoreCase("Physician")) {
                        Intent i = new Intent(DashboardActivity.this, CancelAppointmentActivity.class);
                        i.putExtra("request", "Physician");
                        i.putExtra("appointment_type", getIntent().getExtras().getString("appointment_type"));
                        i.putExtra("Speciality", "Physician");
                        i.putExtra("Physicians_Name", getIntent().getExtras().getString("Physicians_Name"));
                        i.putExtra("Physicians_Specialty", getIntent().getExtras().getString("Physicians_Specialty"));
                        i.putExtra("Availability", getIntent().getExtras().getString("Availability"));
                        i.putExtra("Time_of_day", getIntent().getExtras().getString("Time_of_day"));
                        i.putExtra("Any_Specific_Request", getIntent().getExtras().getString("Any_Specific_Request"));
                        startActivity(i);
                        break;
                    } else if (strRequest.equalsIgnoreCase("Radiology / Diagnostics")) {
                        Intent i = new Intent(DashboardActivity.this, CancelAppointmentActivity.class);
                        i.putExtra("request", "Radiology / Diagnostics");
                        i.putExtra("appointment_type", getIntent().getExtras().getString("appointment_type"));
                        i.putExtra("Speciality", "Radiology / Diagnostics");
                        i.putExtra("Availability", getIntent().getExtras().getString("Availability"));
                        i.putExtra("Time_of_day", getIntent().getExtras().getString("Time_of_day"));
                        i.putExtra("Requested_By", getIntent().getExtras().getString("Requested_By"));
                        i.putExtra("OutsideProvider_number", getIntent().getExtras().getString("OutsideProvider_number"));
                        i.putExtra("Radiology_Type", getIntent().getExtras().getString("Radiology_Type"));
                        i.putExtra("Any_Specific_Request", getIntent().getExtras().getString("Any_Specific_Request"));
                        startActivity(i);
                        break;
                    }
                }
                break;

            case R.id.btn_view_referral_details:
                if(strReltionship.equalsIgnoreCase("Friend")){
                Intent intent = new Intent(DashboardActivity.this, CancelReferralActivity.class);
                intent.putExtra("Referral_name", getIntent().getExtras().getString("Referral_name"));
                intent.putExtra("Relationship", getIntent().getExtras().getString("Relationship"));
                intent.putExtra("referal_phone", getIntent().getExtras().getString("referal_phone"));
                intent.putExtra("referal_email", getIntent().getExtras().getString("referal_email"));
                startActivity(intent);
                break;
            }else if(strReltionship.equalsIgnoreCase("Family")){
                Intent intent = new Intent(DashboardActivity.this, CancelReferralActivity.class);
                intent.putExtra("family_relation", getIntent().getExtras().getString("family_relation"));
                intent.putExtra("Referral_name", getIntent().getExtras().getString("Referral_name"));
                intent.putExtra("Relationship", getIntent().getExtras().getString("Relationship"));
                intent.putExtra("referal_phone", getIntent().getExtras().getString("referal_phone"));
                intent.putExtra("referal_email", getIntent().getExtras().getString("referal_email"));
                startActivity(intent);
                break;
            }else if(strReltionship.equalsIgnoreCase("Other Associates")){
                Intent intent = new Intent(DashboardActivity.this, CancelReferralActivity.class);
                intent.putExtra("Association", getIntent().getExtras().getString("Association"));
                intent.putExtra("Referral_name", getIntent().getExtras().getString("Referral_name"));
                intent.putExtra("Relationship", getIntent().getExtras().getString("Relationship"));
                intent.putExtra("referal_phone", getIntent().getExtras().getString("referal_phone"));
                intent.putExtra("referal_email", getIntent().getExtras().getString("referal_email"));
                startActivity(intent);
                break;
            }
            break;*/
            case R.id.img_user_info:
                Intent i = new Intent(DashboardActivity.this, UsersAccountActivity.class);
                startActivity(i);
                break;
        }

    }

    public void createDonorInteraction() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(DashboardActivity.this);
            String URL = AppConfig.BASE_URL + AppConfig.CREATE_SCHEDULING;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("UserID", sharedpreferences.getString("USER_ID", ""));
            jsonBody.put("Interaction_ID", "0");
            if (strInterActionTypeID.equalsIgnoreCase("4")) {
                jsonBody.put("Interaction_Type_ID", "4");
                Log.e("Interaction_Type_ID", "4");
            } else {
                jsonBody.put("Interaction_Type_ID", "2");
                Log.e("Interaction_Type_ID", "2");
            }
            jsonBody.put("Status_ID", "0");
            jsonBody.put("Chat_ID", "0");
            jsonBody.put("Interaction_Start_Time", currentDateandTime);
            jsonBody.put("Interaction_DTL_ID", "0");
            jsonBody.put("Created_By_User_ID", sharedpreferences.getString("USER_ID", ""));

            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.e("Dashboaard_response", response);
                        JSONArray array = new JSONArray(response);

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(0);
                            if (object.getString("inMsg").equalsIgnoreCase("Interaction Saved!!!")) {
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("interaction_DTL_ID", object.getString("interaction_DTL_ID"));
                                editor.putString("interaction_ID", object.getString("interaction_ID"));
                                editor.putString("referral_ID", object.getString("referral_ID"));
                                editor.commit();
                                Intent intent = new Intent(DashboardActivity.this, SchedulingActivity.class);
                                intent.putExtra("Interaction_Type_ID", strInterActionTypeID);
                                startActivity(intent);
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

    public void getContextualList() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(DashboardActivity.this);
            String URL = AppConfig.BASE_URL + AppConfig.GET_CONTEXTUAL_LIST;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("UserID", sharedpreferences.getString("USER_ID", ""));
            jsonBody.put("MobileDate", currentDateandTime);
            Log.e("UserID", sharedpreferences.getString("USER_ID", ""));
            Log.e("MobileDate", currentDateandTime);


            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.e("getContextualResponse(", response);
                        JSONArray array = new JSONArray(response);

                        for (int i = 0; i < array.length(); i++) {
                            ContextualModelItems items = new ContextualModelItems();
                            JSONObject object = array.getJSONObject(i);
                            items.setUserId(object.getString("userId"));
                            items.setEventTypeId(object.getString("eventType_ID"));
                            items.setEventId(object.getString("event_ID"));
                            if(object.isNull("eventDate")) {
                                items.setEventDate("");
                            }else {
                                items.setEventDate(object.getString("eventDate"));
                            }
                            items.setEventDescription(object.getString("eventDescription"));
                            items.setPhysiciaanName(object.getString("physicianName"));

                            if(object.isNull("address")){
                               items.setAddress("");
                            }else {
                                items.setAddress(object.getString("address"));
                            }
                            items.setMobileDate(object.getString("mobileDate"));
                            items.setReqquestString(object.getString("requestString"));
                            items.setTimeRemaining(object.getString("timeRemaining"));

                            if(object.isNull("location")) {
                                items.setStrLocation("");
                            }else {
                                items.setStrLocation(object.getString("location"));
                            }
                            items.setStrImage(object.getString("photo1"));

                            contextualModelItems.add(items);
                            Log.e("Listahvf", "" + contextualModelItems.size());

                        }

                        if (contextualModelItems.size() > 0) {
                            relPopup.setVisibility(View.VISIBLE);
                            //contextualView.setVisibility(View.VISIBLE);
                        } else {
                            relPopup.setVisibility(View.GONE);
                            //contextualView.setVisibility(View.GONE);
                        }
                        contextualView.setHasFixedSize(true);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(DashboardActivity.this);
                        contextualView.setLayoutManager(mLayoutManager);
                        //Log.e("ListSize", ""+achevementDataItems.size());
                        adapter = new MultiViewTypeAdapter(DashboardActivity.this, contextualModelItems, activity);
                        contextualView.setAdapter(adapter);
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

    public void submitFCMforNotification() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(DashboardActivity.this);
            String URL = AppConfig.BASE_URL + AppConfig.UPDATE_DONOR_DEVICES;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("Token", sharedpreferences.getString("device_token", ""));
            jsonBody.put("Device_ID", sharedpreferences.getString("device_id", ""));
            jsonBody.put("Device_Type", "Android");
            jsonBody.put("Device_Status", "");
            jsonBody.put("App_Build_Version", "");
            jsonBody.put("Platform_Version", "");
            jsonBody.put("Phone_No", "");

            Log.e("FCM", sharedpreferences.getString("device_token", "null"));
            Log.e("Device_id", sharedpreferences.getString("device_id", ""));

            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                   Log.e("Notification_response", response);
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

    @Override
    protected void onResume() {
        super.onResume();
        relScheduing.setBackgroundResource(R.drawable.referral_button_background);
        tvScheduling.setTextColor(getResources().getColor(R.color.bacgroun_color));

        relRefrrals.setBackgroundResource(R.drawable.referral_button_background);
        tvReferral.setTextColor(getResources().getColor(R.color.bacgroun_color));

        relQuickAnswers.setBackgroundResource(R.drawable.referral_button_background);
        tvChatus.setTextColor(getResources().getColor(R.color.bacgroun_color));
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }
}

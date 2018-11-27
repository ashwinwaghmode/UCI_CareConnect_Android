package com.devool.ucicareconnect.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.devool.ucicareconnect.utils.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class CancelReferralActivity extends AppCompatActivity implements View.OnClickListener{

    TextView tvReferralName, tvRelationship, tvAssociateName, tvFamiyRelation, tvPhoneNumber, tvEmailAddress;
    LinearLayout llAssociateName, llFamilyRelation, llEmailAddress;
    ImageView imgCloseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_another_refferal);

        llAssociateName = findViewById(R.id.ll_associate_name);
        llFamilyRelation = findViewById(R.id.ll_family_relation);
        llEmailAddress = findViewById(R.id.ll_email_address);

        tvReferralName = findViewById(R.id.tv_referral_name);
        tvRelationship = findViewById(R.id.tv_relationship);
        tvAssociateName = findViewById(R.id.tv_associate_name);
        tvFamiyRelation = findViewById(R.id.tv_family_relation);
        tvPhoneNumber = findViewById(R.id.tv_phone_number);
        tvEmailAddress = findViewById(R.id.tv_email_address);
        imgCloseButton = findViewById(R.id.img_close_button);

        imgCloseButton.setOnClickListener(this);

        getReferralRequestedDetail();

     /*   if (getIntent().getExtras().getString("Relationship").equalsIgnoreCase("Friend")) {
            tvReferralName.setText(getIntent().getExtras().getString("Referral_name"));
            tvRelationship.setText(getIntent().getExtras().getString("Relationship"));
            tvPhoneNumber.setText(getIntent().getExtras().getString("referal_phone"));
            tvEmailAddress.setText(getIntent().getExtras().getString("referal_email"));
            llFamilyRelation.setVisibility(View.GONE);
            llAssociateName.setVisibility(View.GONE);
        } else if (getIntent().getExtras().getString("Relationship").equalsIgnoreCase("Family")) {
            tvReferralName.setText(getIntent().getExtras().getString("Referral_name"));
            tvRelationship.setText(getIntent().getExtras().getString("Relationship"));
            llFamilyRelation.setVisibility(View.VISIBLE);
            tvFamiyRelation.setText(getIntent().getExtras().getString("family_relation"));
            tvPhoneNumber.setText(getIntent().getExtras().getString("referal_phone"));
            tvEmailAddress.setText(getIntent().getExtras().getString("referal_email"));
        } else if (getIntent().getExtras().getString("Relationship").equalsIgnoreCase("Other Associates")) {
            tvReferralName.setText(getIntent().getExtras().getString("Referral_name"));
            tvRelationship.setText(getIntent().getExtras().getString("Relationship"));
            tvPhoneNumber.setText(getIntent().getExtras().getString("referal_phone"));
            tvEmailAddress.setText(getIntent().getExtras().getString("referal_email"));
            llFamilyRelation.setVisibility(View.GONE);
            llAssociateName.setVisibility(View.VISIBLE);
            tvAssociateName.setText(getIntent().getExtras().getString("Association"));
        }*/

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_close_button:
                Intent intent = new Intent(this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;
        }
    }

    public void getReferralRequestedDetail() {
        RequestQueue requestQueue = Volley.newRequestQueue(CancelReferralActivity.this);
        String URL = AppConfig.BASE_URL + AppConfig.GET_REFERRAL_REQUESTED + "/" + getIntent().getExtras().getString("event_id");
        JSONObject jsonBody = new JSONObject();

        final String requestBody = jsonBody.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("getUserInfo", response);
                    JSONObject object = new JSONObject(response);
                    tvReferralName.setText(object.getString("referral_name"));
                    tvRelationship.setText(object.getString("relationship"));
                    tvPhoneNumber.setText(object.getString("referal_phone"));
                    if (object.isNull("referal_email") || object.getString("referal_email").equals("")) {
                        llEmailAddress.setVisibility(View.GONE);
                    } else {
                        tvEmailAddress.setText(object.getString("referal_email"));
                        llEmailAddress.setVisibility(View.VISIBLE);
                    }
                   if(object.isNull("association") || object.getString("association").equals("")){
                        llAssociateName.setVisibility(View.GONE);
                   }else {
                        tvAssociateName.setText(object.getString("association"));
                       llAssociateName.setVisibility(View.GONE);
                   }
                   if(object.isNull("family_relation") || object.getString("family_relation").equals("")){
                        llFamilyRelation.setVisibility(View.GONE);
                   }else {
                       llFamilyRelation.setVisibility(View.VISIBLE);
                       tvFamiyRelation.setText(object.getString("family_relation"));
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
    }
}

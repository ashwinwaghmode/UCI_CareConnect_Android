package com.devool.ucicareconnect.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.devool.ucicareconnect.utils.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class EmailValidationActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnNext;
    TextView emailAddressText, tvEmailAddressMsg, tvHeading;
    EditText edtEmailAddress;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Typeface tf;
    ImageView imgBackArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_validation);
        btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);

        edtEmailAddress = findViewById(R.id.edit_email_address);
        emailAddressText = findViewById(R.id.tv_email_address_text);
        tvEmailAddressMsg = findViewById(R.id.tv_email_address_msg);
        tvHeading = findViewById(R.id.tv_heading);
        imgBackArrow = findViewById(R.id.img_back_arrow);
        imgBackArrow.setOnClickListener(this);

        edtEmailAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        if (!edtEmailAddress.getText().toString().equalsIgnoreCase("") && getIntent().getExtras().getString("strEmail").equalsIgnoreCase(edtEmailAddress.getText().toString())) {
                            emailAddressText.setVisibility(View.VISIBLE);
                            emailAddressText.setText("Email Address");
                            //btnNext.setVisibility(View.VISIBLE);
                            btnNext.setEnabled(true);
                            btnNext.setTextColor(getResources().getColor(R.color.btn_text_color));
                            submitUserEmail();
                        } else {
                            emailAddressText.setVisibility(View.VISIBLE);
                            edtEmailAddress.setBackgroundResource(R.drawable.activation_error_color_background);
                            emailAddressText.setText("Incorrect Email Address");
                            //btnNext.setVisibility(View.GONE);
                            btnNext.setEnabled(false);
                            btnNext.setTextColor(getResources().getColor(R.color.btn_grey_color));
                            break;
                        }
                        break;
                }
                return false;
            }
        });

        edtEmailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtEmailAddress.setBackgroundResource(R.drawable.edit_text_background);
                emailAddressText.setText("Email Address");
                if (edtEmailAddress.getText().toString().trim().matches(emailPattern) && s.length() > 0) {
                    emailAddressText.setVisibility(View.GONE);
                } else {
                    emailAddressText.setVisibility(View.VISIBLE);
                    emailAddressText.setText("Email Address");
                }
                if (edtEmailAddress.getText().toString().equalsIgnoreCase("")) {
                    emailAddressText.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (edtEmailAddress.getText().toString().trim().matches(emailPattern) && s.length() > 0) {
                    emailAddressText.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void appyfontForAllViews() {
        tf = Typeface.createFromAsset(getAssets(), "KievitSlabOT-Bold.otf");
        tvHeading.setTypeface(tf, Typeface.BOLD);

        tf = Typeface.createFromAsset(getAssets(), "KievitSlabOT-Bold.otf");
        edtEmailAddress.setTypeface(tf);

        tf = Typeface.createFromAsset(getAssets(), "KievitSlabOT-Medium.otf");
        btnNext.setTypeface(tf);

        tf = Typeface.createFromAsset(getAssets(), "HelveticaNeueLight.ttf");
        tvEmailAddressMsg.setTypeface(tf);

        tf = Typeface.createFromAsset(getAssets(), "HelveticaNeueLight.ttf");
        emailAddressText.setTypeface(tf);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                startActivity(new Intent(EmailValidationActivity.this, WelcomeActivityAfterLogin.class));
                break;
            //submitUserEmail();
            case R.id.img_back_arrow:
                onBackPressed();
                break;
        }
    }

    private void submitUserEmail() {
        {
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(EmailValidationActivity.this);
                String URL = AppConfig.BASE_URL + AppConfig.GET_USER_FROM_EMAIl;
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("UserID", getIntent().getExtras().getString("user_id"));
                jsonBody.put("Email", edtEmailAddress.getText().toString());

                final String requestBody = jsonBody.toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("EMAIl_response", response);
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(0);
                                //Toast.makeText(LoginActivity.this, object.getString("inMsg"), Toast.LENGTH_SHORT).show();
                                if (object.getString("email").equalsIgnoreCase("")) {
                                    //btnNext.setVisibility(View.GONE);
                                    //tvActivationCodeMsg.setText("Please double-check your code and try agaain. if it's still not working, just tap the link above to reset");
                                    edtEmailAddress.setBackgroundResource(R.drawable.activation_error_color_background);
                                    emailAddressText.setText("Incorrect Email Address");
                                    btnNext.setTextColor(getResources().getColor(R.color.btn_grey_color));
                                    btnNext.setEnabled(false);
                                } else {
                                    btnNext.setTextColor(getResources().getColor(R.color.btn_text_color));
                                    btnNext.setEnabled(true);
                                    //btnNext.setVisibility(View.VISIBLE);
                                    // tvActivationCodeMsg.setText("");
                                    edtEmailAddress.setBackgroundResource(R.drawable.edit_text_background);
                                    //emailAddressText.setText("Email Address");
                                }
                                /*if(!object.getString("email").trim().matches(emailPattern)){
                                    emailAddressText.setText("Invalid Email Address");
                                }*/
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
}

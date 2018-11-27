package com.devool.ucicareconnect.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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

public class ConfirmPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnNext;
    EditText edtConfirmPassword;
    public static final String USER_INFO = "user_info";
    SharedPreferences sharedpreferences;
    TextView tvHeading, tvConfirPasswordText;
    Typeface tf;
    ImageView imgBackArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_password);
        btnNext = findViewById(R.id.btn_next);
        edtConfirmPassword = findViewById(R.id.edit_confirm_password);
        tvHeading = findViewById(R.id.tv_heading);
        tvConfirPasswordText = findViewById(R.id.tv_confirm_password_text);
        imgBackArrow = findViewById(R.id.img_back_arrow);

        sharedpreferences = getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);

        btnNext.setOnClickListener(this);
        imgBackArrow.setOnClickListener(this);

        appyfontForAllViews();

        edtConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtConfirmPassword.setBackgroundResource(R.drawable.edit_text_background);
                tvConfirPasswordText.setText("Please confirm you password");
                if(edtConfirmPassword.getText().toString().equalsIgnoreCase("")) {
                    tvConfirPasswordText.setVisibility(View.GONE);
                }else {
                    tvConfirPasswordText.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void appyfontForAllViews() {
        tf = Typeface.createFromAsset(getAssets(), "KievitSlabOT-Bold.otf");
        tvHeading.setTypeface(tf,Typeface.BOLD);

        tf = Typeface.createFromAsset(getAssets(), "KievitSlabOT-Bold.otf");
        edtConfirmPassword.setTypeface(tf,Typeface.BOLD);

        tf = Typeface.createFromAsset(getAssets(), "KievitSlabOT-Medium.otf");
        btnNext.setTypeface(tf);

        tf = Typeface.createFromAsset(getAssets(), "HelveticaNeueLight.ttf");
        tvConfirPasswordText.setTypeface(tf);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (getIntent().getExtras().getString("password").equalsIgnoreCase(edtConfirmPassword.getText().toString())) {
                        submitUpdatePassword();
                        tvConfirPasswordText.setVisibility(View.VISIBLE);
                        break;
                } else {
                    tvConfirPasswordText.setVisibility(View.VISIBLE);
                    edtConfirmPassword.setBackgroundResource(R.drawable.activation_error_color_background);
                    tvConfirPasswordText.setText("Please confirm you password");
                    break;
                }
            case R.id.img_back_arrow:
                onBackPressed();
                break;
        }
    }


    private void submitUpdatePassword() {
        {
            final SharedPreferences.Editor editor = sharedpreferences.edit();
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(ConfirmPasswordActivity.this);
                String URL = AppConfig.BASE_URL + AppConfig.UPDATE_PASSWORD;
                JSONObject jsonBody = new JSONObject();
                if(getIntent().hasExtra("flag")){
                    jsonBody.put("userID", getIntent().getExtras().getString("userId"));
                }else {
                    jsonBody.put("userID", sharedpreferences.getString("USER_ID", ""));
                }
                jsonBody.put("Password", edtConfirmPassword.getText().toString());

                Log.e("USER_ID", sharedpreferences.getString("USER_ID", ""));
                Log.e("USER_NAME", edtConfirmPassword.getText().toString());

                final String requestBody = jsonBody.toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("Update_Password_respon", response);
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(0);
                                editor.putString("USER_ID", object.getString("userID"));
                                editor.commit();
                                if(getIntent().hasExtra("flag")){
                                    Intent intent = new Intent(ConfirmPasswordActivity.this,LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    //intent.putExtra("password", edtCreatePassword.getText().toString());
                                    intent.putExtra("user_name", getIntent().getExtras().getString("user_name"));
                                    startActivity(intent);
                                }else {
                                    Intent intent = new Intent(ConfirmPasswordActivity.this,ProfileActivity.class);
                                    //intent.putExtra("password", edtCreatePassword.getText().toString());
                                    intent.putExtra("user_name", getIntent().getExtras().getString("user_name"));
                                    startActivity(intent);
                                    //startActivity(new Intent(ConfirmPasswordActivity.this,ProfileActivity.class));
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

}

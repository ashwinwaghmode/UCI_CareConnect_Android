package com.devool.ucicareconnect.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
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

public class SignUpPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnSignIn;
    EditText edtSignupPassword;

    SharedPreferences sharedpreferences;
    public static final String USER_INFO= "user_info";

    TextView tvSignUpPasswordText,tvForgotPasswordMsg, tvTextheading;
    ImageView imgMike, imgBackArrow;
    Typeface tf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_password);
        btnSignIn = findViewById(R.id.btn_sign_in);
        edtSignupPassword = findViewById(R.id.edit_sign_up_password);
        tvSignUpPasswordText = findViewById(R.id.tv_sign_up_password_text);
        tvForgotPasswordMsg = findViewById(R.id.tv_forgot_password_msg);
        tvTextheading = findViewById(R.id.tv_heading);
        imgMike = findViewById(R.id.img_mike);
        imgBackArrow = findViewById(R.id.img_back_arrow);

        sharedpreferences = getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        btnSignIn.setOnClickListener(this);
        imgBackArrow.setOnClickListener(this);
        tvForgotPasswordMsg.setOnClickListener(this);

        appyfontForAllViews();

        tvForgotPasswordMsg.setPaintFlags(tvForgotPasswordMsg.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        edtSignupPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtSignupPassword.setBackgroundResource(R.drawable.edit_text_background);
                tvSignUpPasswordText.setText("Enter Your Password");

                if(edtSignupPassword.getText().toString().equalsIgnoreCase("")){
                    tvForgotPasswordMsg.setText("Forgot Your Username?");
                    imgMike.setVisibility(View.VISIBLE);
                }else {
                    tvForgotPasswordMsg.setText("");
                    imgMike.setVisibility(View.GONE);
                }

                if (edtSignupPassword.getText().toString().equalsIgnoreCase("")) {
                    tvSignUpPasswordText.setVisibility(View.GONE);
                } else {
                    tvSignUpPasswordText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtSignupPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        getLoginUserDetails();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_sign_in:
                Intent intent = new Intent(SignUpPasswordActivity.this, DashboardActivity.class);
                intent.putExtra("sigin_flag", "SignUp");
                startActivity(intent);
                break;
            case R.id.img_back_arrow:
                onBackPressed();
                break;
            case R.id.tv_forgot_password_msg:
                startActivity(new Intent(SignUpPasswordActivity.this, ForgotPasswordActivity.class));
                break;
        }
    }

    private void appyfontForAllViews() {
        tf = Typeface.createFromAsset(getAssets(), "KievitSlabOT-Bold.otf");
        tvTextheading.setTypeface(tf,Typeface.BOLD);

        tf = Typeface.createFromAsset(getAssets(), "KievitSlabOT-Bold.otf");
        edtSignupPassword.setTypeface(tf,Typeface.BOLD);

        tf = Typeface.createFromAsset(getAssets(), "KievitSlabOT-Medium.otf");
        btnSignIn.setTypeface(tf);

        tf = Typeface.createFromAsset(getAssets(), "HelveticaNeueLight.ttf");
        tvSignUpPasswordText.setTypeface(tf);

        tf = Typeface.createFromAsset(getAssets(), "HelveticaNeueLight.ttf");
        tvForgotPasswordMsg.setTypeface(tf);

    }

    public void getLoginUserDetails(){
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(SignUpPasswordActivity.this);
            String URL = AppConfig.BASE_URL + AppConfig.GET_USER_LOGIN_INFO;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("UserID", sharedpreferences.getString("USER_ID", ""));
            jsonBody.put("Password", edtSignupPassword.getText().toString());
            //jsonBody.put("Email", edtEmailAddress.getText().toString());

            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.e("signUp_password", response);
                        JSONArray array = new JSONArray(response);

                        for (int i =0; i<array.length();i++){
                            JSONObject object = array.getJSONObject(0);
                            if(object.getString("inMsg").equalsIgnoreCase("Invalid User credentials!.")){
                                edtSignupPassword.setBackgroundResource(R.drawable.activation_error_color_background);
                                tvSignUpPasswordText.setText("Invalid User credentials!.");
                                tvForgotPasswordMsg.setText("Forgot Your Password?");
                                btnSignIn.setTextColor(getResources().getColor(R.color.btn_grey_color));
                                btnSignIn.setEnabled(false);
                            }else {
                                editor.putString("USER_ID", object.getString("userID"));
                                editor.commit();

                                edtSignupPassword.setBackgroundResource(R.drawable.edit_text_background);
                                tvForgotPasswordMsg.setText("");
                                tvSignUpPasswordText.setText("Enter Your Password");
                                btnSignIn.setTextColor(getResources().getColor(R.color.btn_text_color));
                                btnSignIn.setEnabled(true);
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

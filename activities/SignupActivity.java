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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String USER_INFO = "user_info";
    Button btnSignIn;
    EditText edtUserName;
    String strUserId, strUserName = "";
    TextView tvRememberMe, tvForgotUserName, tvUserNameText;
    Typeface tf;
    SharedPreferences sharedpreferences;

    CheckBox checkBoxUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        btnSignIn = findViewById(R.id.btn_sign_in);
        tvRememberMe = findViewById(R.id.tv_remember_me);
        tvForgotUserName = findViewById(R.id.tv_forgot_user_name);
        tvUserNameText = findViewById(R.id.tv_username_text);
        edtUserName = findViewById(R.id.edit_user_name);
        checkBoxUserName = findViewById(R.id.check_bx_user_name);
        btnSignIn.setOnClickListener(this);
        tvForgotUserName.setOnClickListener(this);

        edtUserName.requestFocus();
        /*InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);*/
        InputMethodManager imgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.showSoftInput(edtUserName, InputMethodManager.SHOW_IMPLICIT);

        sharedpreferences = getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);

        applyfontForAllViews();

        tvForgotUserName.setPaintFlags(tvForgotUserName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        edtUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtUserName.setBackgroundResource(R.drawable.edit_text_background);
                tvUserNameText.setText("Username");
                tvForgotUserName.setText("Forgot Username?");
                if (checkBoxUserName.isChecked()) {
                    checkBoxUserName.setChecked(false);
                }
                if (edtUserName.getText().toString().equalsIgnoreCase("")) {
                    tvForgotUserName.setText("Forgot Username?");
                } else {
                    tvForgotUserName.setText("Forgot Username?");
                }
                if (edtUserName.getText().toString().equalsIgnoreCase("")) {
                    tvUserNameText.setVisibility(View.GONE);
                } else {
                    tvUserNameText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /*if (strUserName != null || !strUserName.equalsIgnoreCase("")) {*/
        if (!sharedpreferences.contains(null)) {
            if (strUserName == null) {
                edtUserName.setText("");
                checkBoxUserName.setChecked(false);
            } else {
                edtUserName.setText(sharedpreferences.getString("USER_NAME", ""));
                checkBoxUserName.setChecked(true);
            }
        } else {
            edtUserName.setText("");
            checkBoxUserName.setChecked(false);
        }

        edtUserName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        getUserLogin();
                        break;
                }
                return false;
            }
        });



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                startActivity(new Intent(SignupActivity.this, SignUpPasswordActivity.class));
                break;
            case R.id.tv_forgot_user_name:
                startActivity(new Intent(SignupActivity.this, ForgotUserNameActivity.class));
                break;

        }
    }

    public void getUserLogin() {
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(SignupActivity.this);
            String URL = AppConfig.BASE_URL + AppConfig.GET_USER_LOGIN;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("UserName", edtUserName.getText().toString());
            //jsonBody.put("Email", edtEmailAddress.getText().toString());

            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.e("get_user_login", response);
                        JSONArray array = new JSONArray(response);

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(0);
                            if (!object.getString("inMsg").equalsIgnoreCase("Invalid User!.")) {
                                strUserId = object.getString("userID");
                                editor.putString("USER_ID", strUserId);
                                editor.putBoolean("IS_NEW_USER", object.getBoolean("is_NewUser"));
                                editor.commit();

                                if (checkBoxUserName.isChecked()) {
                                    editor.putString("USER_NAME", edtUserName.getText().toString());
                                    editor.commit();
                                    strUserName = edtUserName.getText().toString();
                                    checkBoxUserName.setChecked(true);
                                    // Toast.makeText(SignupActivity.this, "Checm", Toast.LENGTH_SHORT).show();
                                } else {
                                    editor.remove("USER_NAME");
                                    checkBoxUserName.setChecked(false);
                                    editor.commit();
                                }

                                tvForgotUserName.setText("");
                                edtUserName.setBackgroundResource(R.drawable.edit_text_background);
                                tvUserNameText.setText("Username");
                                tvForgotUserName.setText("");
                                btnSignIn.setTextColor(getResources().getColor(R.color.btn_text_color));
                                btnSignIn.setEnabled(true);
                            } else {
                                btnSignIn.setTextColor(getResources().getColor(R.color.btn_grey_color));
                                btnSignIn.setEnabled(false);
                                edtUserName.setBackgroundResource(R.drawable.activation_error_color_background);
                                tvForgotUserName.setText("Please select another username.");
                                tvUserNameText.setText("Invalid username");
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

    private void applyfontForAllViews() {
        tf = Typeface.createFromAsset(getAssets(), "HelveticaNeueLight.ttf");
        tvUserNameText.setTypeface(tf, Typeface.BOLD);

        tf = Typeface.createFromAsset(getAssets(), "KievitSlabOT-Bold.otf");
        edtUserName.setTypeface(tf, Typeface.BOLD);

        tf = Typeface.createFromAsset(getAssets(), "KievitSlabOT-Medium.otf");
        btnSignIn.setTypeface(tf);

        tf = Typeface.createFromAsset(getAssets(), "HelveticaNeueLight.ttf");
        tvRememberMe.setTypeface(tf);

        tf = Typeface.createFromAsset(getAssets(), "HelveticaNeueLight.ttf");
        tvForgotUserName.setTypeface(tf);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(SignupActivity.this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}

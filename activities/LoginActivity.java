package com.devool.ucicareconnect.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    public static final String USER_INFO = "user_info";
    public static final String UserId = "user_id";
    private static final int REQUEST_PHONE_STATE = 1;
    TextView tvActivationCode, tvActivationCodeText;
    Button btnNext, btnLogin;
    EditText edtActivationCode;
    TextView tvActivationCodeMsg;
    String strUserId;
    SharedPreferences sharedpreferences;
    Typeface tf;
    int keyDel = 0;
    String strOriginalString;
    String device_id, device_token, strEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);

        sharedpreferences = getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);

        tvActivationCode = findViewById(R.id.tv_lost_activation_code);
        tvActivationCode.setPaintFlags(tvActivationCode.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        btnNext = findViewById(R.id.btn_next);
        btnLogin = findViewById(R.id.btn_login);
        tvActivationCodeText = findViewById(R.id.tv_activation_code_text);
        tvActivationCodeMsg = findViewById(R.id.tv_activation_code_msg);
        edtActivationCode = findViewById(R.id.edit_activation_code);

        btnNext.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        tvActivationCode.setOnClickListener(this);

        edtActivationCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        checkPasscode();
                        break;
                }
                return false;
            }
        });

        appyfontForAllViews();

        edtActivationCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtActivationCode.setBackgroundResource(R.drawable.edit_text_background);
                tvActivationCodeText.setText("Activation Code");

                if (edtActivationCode.getText().toString().equalsIgnoreCase("")) {
                    tvActivationCodeText.setVisibility(View.GONE);
                    tvActivationCodeMsg.setText("Check your email for your activation code.");
                } else {
                    tvActivationCodeText.setVisibility(View.VISIBLE);
                }
                addDashtoActivationCode();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

      /*  String android_id = Settings.Secure.getString(getApplication().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Log.e("Devic_d", android_id);*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                getDeviceId();
            } else {
                ActivityCompat.requestPermissions(LoginActivity.this,
                        new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE},
                        REQUEST_PHONE_STATE);
            }
        } else {
            getDeviceId();
        }

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.w("getInstanceId failed", task.getException());
                    return;
                }
                device_token = task.getResult().getToken();
                Log.d("device_token: ", device_token);
                final SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("device_token", device_token);
                editor.commit();
            }
        });
    }

    void addDashtoActivationCode() {
        edtActivationCode.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_DEL)
                    keyDel = 1;
                return false;
            }
        });
        if (keyDel == 0) {
            int len = edtActivationCode.getText().length();
            if (len == 5) {
                edtActivationCode.setText(edtActivationCode.getText() + "-");
                edtActivationCode.setSelection(edtActivationCode.getText().length());
            }
        } else {
            keyDel = 0;
        }
    }

    void removeDashToActivationCode() {
        strOriginalString = edtActivationCode.getText().toString().replace("-", "");
    }

    private void appyfontForAllViews() {
        tf = Typeface.createFromAsset(getAssets(), "KievitSlabOT-Bold.otf");
        edtActivationCode.setTypeface(tf, Typeface.BOLD);

        tf = Typeface.createFromAsset(getAssets(), "HelveticaNeueLight.ttf");
        tvActivationCode.setTypeface(tf);

        tf = Typeface.createFromAsset(getAssets(), "HelveticaNeueLight.ttf");
        tvActivationCodeMsg.setTypeface(tf);

        tf = Typeface.createFromAsset(getAssets(), "KievitSlabOT-Medium.otf");
        btnNext.setTypeface(tf);

        tf = Typeface.createFromAsset(getAssets(), "KievitSlabOT-Medium.otf");
        btnLogin.setTypeface(tf);

        tf = Typeface.createFromAsset(getAssets(), "HelveticaNeueLight.ttf");
        tvActivationCodeText.setTypeface(tf);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                Intent i = new Intent(LoginActivity.this, EmailValidationActivity.class);
                i.putExtra("user_id", strUserId);
                i.putExtra("strEmail", strEmail);
                startActivity(i);
                break;
            case R.id.btn_login:
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                break;
            case R.id.tv_lost_activation_code:
                startActivity(new Intent(LoginActivity.this, LostActivationCodeActivity.class));
                break;
        }
    }

    public void checkPasscode() {
        sharedpreferences = getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        removeDashToActivationCode();
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
            String URL = AppConfig.BASE_URL + AppConfig.GET_USER_PASSCODE;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("Passcode", strOriginalString);
            Log.e("passcode", strOriginalString);

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        Log.e("Passcode_response", response);
                        JSONArray array = new JSONArray(response);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(0);
                            strUserId = object.getString("userID");
                            editor.putString("USER_ID", strUserId);
                            editor.putBoolean("IS_NEW_USER", object.getBoolean("is_NewUser"));
                            if (!edtActivationCode.getText().toString().equals("")) {
                                if (object.getString("userName") != null && !object.getString("inMsg").equalsIgnoreCase("Invalid Passcode!.")) {
                                    editor.putString("USER_NAME", object.getString("firstName"));
                                } else {
                                    editor.putString("USER_NAME", sharedpreferences.getString("USER_NAME", ""));
                                }
                            } else {
                                editor.putString("USER_NAME", sharedpreferences.getString("USER_NAME", ""));
                            }

                            editor.commit();
                            //Toast.makeText(LoginActivity.this, object.getString("inMsg"), Toast.LENGTH_SHORT).show();
                            if (object.getString("inMsg").equalsIgnoreCase("Invalid Passcode!.")) {
                                //btnNext.setVisibility(View.GONE);
                                //btnNext.setBackground(getResources().getDrawable(R.drawable.grey_btn_background));
                                btnNext.setTextColor(getResources().getColor(R.color.btn_grey_color));
                                btnNext.setEnabled(false);
                                tvActivationCodeMsg.setText("Please double-check your code and try again. if it's still not working, just tap the link above to reset");
                                edtActivationCode.setBackgroundResource(R.drawable.activation_error_color_background);
                                tvActivationCodeText.setText("Incorrect Activation Code");
                                tf = Typeface.createFromAsset(getAssets(), "HelveticaNeueLight.ttf");
                                tvActivationCodeText.setTypeface(tf, Typeface.BOLD);
                            } else {
                                strEmail = object.getString("email");
                                //btnNext.setVisibility(View.VISIBLE);
                                btnNext.setTextColor(getResources().getColor(R.color.btn_text_color));
                                //btnNext.setBackground(getResources().getDrawable(R.drawable.referral_button_background));
                                btnNext.setEnabled(true);
                                tvActivationCodeMsg.setText("");
                                edtActivationCode.setBackgroundResource(R.drawable.edit_text_background);
                                tvActivationCodeMsg.setText("Check your email for your activation code.");
                                tvActivationCodeText.setText("Activation Code");
                                tf = Typeface.createFromAsset(getAssets(), "HelveticaNeueLight.ttf");
                                tvActivationCodeText.setTypeface(tf);
                            }
                          /*  if (object.getString("is_AdminCreated").equalsIgnoreCase("N")) {
                                //btnNext.setVisibility(View.GONE);
                                //btnNext.setBackground(getResources().getDrawable(R.drawable.grey_btn_background));
                                btnNext.setEnabled(false);
                                btnNext.setTextColor(getResources().getColor(R.color.btn_grey_color));
                                tvActivationCodeMsg.setText("Activation code already used");
                                edtActivationCode.setBackgroundResource(R.drawable.activation_error_color_background);
                                tvActivationCodeText.setText("Activation code already used");
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PHONE_STATE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    getDeviceId();
                } else {
                    Toast.makeText(LoginActivity.this, "Unable to continue without granting permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void getDeviceId() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        device_id = telephonyManager.getDeviceId();
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("device_id", device_id);
        editor.commit();
        Log.d("device_id: ", device_id);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.edit_activation_code:
                checkPasscode();
        }
    }
}

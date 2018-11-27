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

public class UserNameActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnNext;
    SharedPreferences sharedpreferences;
    public static final String USER_INFO= "user_info" ;
    EditText edtCreateUserName;
    TextView tvHeading, tvUserNameText, tvUserNameMsg;
    Typeface tf;
    ImageView imgBackArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);
        btnNext = findViewById(R.id.btn_next);
        tvHeading = findViewById(R.id.tv_heading);
        tvUserNameText = findViewById(R.id.tv_username_text);
        tvUserNameMsg = findViewById(R.id.tv_user_name_msg);
        imgBackArrow = findViewById(R.id.img_back_arrow);

        btnNext.setOnClickListener(this);
        imgBackArrow.setOnClickListener(this);

        sharedpreferences = getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        edtCreateUserName = findViewById(R.id.edit_create_user_name);

        edtCreateUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtCreateUserName.setBackgroundResource(R.drawable.edit_text_background);
                tvUserNameText.setText("Username");
                tvUserNameMsg.setVisibility(View.GONE);
                if(edtCreateUserName.getText().toString().equalsIgnoreCase("")) {
                    tvUserNameText.setVisibility(View.GONE);
                }else {
                    tvUserNameText.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        appyfontForAllViews();

        edtCreateUserName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        if(!edtCreateUserName.getText().toString().equals("")){
                            updateUserInfo();
                        }else {
                            btnNext.setTextColor(getResources().getColor(R.color.btn_grey_color));
                            btnNext.setEnabled(false);
                        }

                        break;
                }
                return false;
            }
        });

    }

    private void appyfontForAllViews() {
        tf = Typeface.createFromAsset(getAssets(), "KievitSlabOT-Bold.otf");
        tvHeading.setTypeface(tf,Typeface.BOLD);

        tf = Typeface.createFromAsset(getAssets(), "KievitSlabOT-Bold.otf");
        edtCreateUserName.setTypeface(tf,Typeface.BOLD);

        tf = Typeface.createFromAsset(getAssets(), "KievitSlabOT-Medium.otf");
        btnNext.setTypeface(tf);

        tf = Typeface.createFromAsset(getAssets(), "HelveticaNeueLight.ttf");
        tvUserNameText.setTypeface(tf);

        tf = Typeface.createFromAsset(getAssets(), "HelveticaNeueLight.ttf");
        tvUserNameMsg.setTypeface(tf);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next:
                Intent intent = new Intent(UserNameActivity.this, CreatePasswordActivity.class);
                intent.putExtra("user_name", edtCreateUserName.getText().toString());
                startActivity(intent);
                break;
            case R.id.img_back_arrow:
                onBackPressed();
                break;
        }
    }

    private void updateUserInfo() {
        {
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(UserNameActivity.this);
                String URL = AppConfig.BASE_URL + AppConfig.UPDATE_LOGIN_USER;
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("UserID", sharedpreferences.getString("USER_ID", ""));
                jsonBody.put("UserName", edtCreateUserName.getText().toString());
                final SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("USER_NAME", edtCreateUserName.getText().toString());
                editor.commit();
                //jsonBody.put("Email", edtEmailAddress.getText().toString());

                final String requestBody = jsonBody.toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("Update_User_response", response);
                            JSONArray array = new JSONArray(response);

                            for (int i =0; i<array.length();i++){
                                JSONObject object = array.getJSONObject(0);
                                //Toast.makeText(LoginActivity.this, object.getString("inMsg"), Toast.LENGTH_SHORT).show();
                                if(object.getString("inMsg").equalsIgnoreCase("UserName Updated!!")) {
                                    btnNext.setTextColor(getResources().getColor(R.color.btn_text_color));
                                    btnNext.setEnabled(true);
                                }
                                if(object.getString("inMsg").equalsIgnoreCase("UserName Already exists!")){
                                    tvUserNameText.setText("Looks like this username is taken.");
                                    edtCreateUserName.setBackgroundResource(R.drawable.activation_error_color_background);
                                    tvUserNameMsg.setVisibility(View.VISIBLE);
                                    tvUserNameMsg.setText("Please select another username.");
                                    btnNext.setTextColor(getResources().getColor(R.color.btn_grey_color));
                                    btnNext.setEnabled(false);
                                }else {
                                    tvUserNameMsg.setVisibility(View.GONE);
                                    btnNext.setTextColor(getResources().getColor(R.color.btn_text_color));
                                    btnNext.setEnabled(true);
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

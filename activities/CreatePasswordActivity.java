package com.devool.ucicareconnect.activities;

import android.app.KeyguardManager;
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
import android.widget.LinearLayout;
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
import com.devool.ucicareconnect.utils.PasswordValidator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreatePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String USER_INFO = "user_info";
    Button btnNext;
    EditText edtCreatePassword, edtConfirmPassword;
    TextView tvUpLowChar, tvMinimumChar, tvNumber, tvSpecialCharacters, tvCharacterPair, tvCreatePassswordText, tvHeading, tvPasswoedRequirementHeading, tvConfirPasswordText;
    Typeface tf;
    ImageView imgBackArrow;
    LinearLayout llpasswoedReq;
    SharedPreferences sharedpreferences;

    public static boolean isValidcharLimitPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = ("^.{8,20}$");
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean isValidcaseSensPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = ("^(?=.*[a-z])(?=.*[A-Z]).{0,20}");
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();

    }

    public static boolean isValidateAtleastOneNumber(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = ("^(?=.*\\d).{0,20}$");
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();

    }

    public static boolean isValidateSpecialCharacters(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = ("^(?=.*[@#$%^&+=*]).{1,20}$");
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();

    }

    public static boolean iswhole(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = ("@\"^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{1,20}");
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();

    }

    public static boolean hasConsecutiveCharacters(String pwd) {
        String[] letter = pwd.split(""); // here you get each letter in to a string array

        for (int i = 0; i < letter.length - 2; i++) {
            if (letter[i].equals(letter[i + 1]) && letter[i + 1].equals(letter[i + 2])) {
                return true; //return true as it has 3 consecutive same character
            }
        }
        return false;
    }

    public static boolean hasConsecutiveCharacter(String pwd) {
        String[] letter = pwd.split(""); // here you get each letter in to a string array

        for (int i = 0; i < letter.length - 2; i++) {
            if (letter[i].equals(letter[i + 2])) {
                return true; //return true as it has 3 consecutive same character
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);
        btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);

        edtCreatePassword = findViewById(R.id.edit_create_password);
        edtConfirmPassword = findViewById(R.id.edit_confirm_password);
        tvUpLowChar = findViewById(R.id.tv_upplow_char);
        tvMinimumChar = findViewById(R.id.tv_minimum_characters);
        tvNumber = findViewById(R.id.tv_one_number);
        tvSpecialCharacters = findViewById(R.id.tv_special_characters);
        tvCharacterPair = findViewById(R.id.tv_chaaractrs_pair);
        tvCreatePassswordText = findViewById(R.id.tv_create_password_text);
        tvHeading = findViewById(R.id.tv_heading);
        tvPasswoedRequirementHeading = findViewById(R.id.tv_password_requirement_heading);
        imgBackArrow = findViewById(R.id.img_back_arrow);
        llpasswoedReq = findViewById(R.id.ll_password_condition);
        tvConfirPasswordText = findViewById(R.id.tv_confirm_password_text);

        imgBackArrow.setOnClickListener(this);
        sharedpreferences = getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);

        edtCreatePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtCreatePassword.setBackgroundResource(R.drawable.edit_text_background);
                tvCreatePassswordText.setText("Enter Your Password");
                if (edtCreatePassword.getText().toString().equalsIgnoreCase("")) {
                    tvPasswoedRequirementHeading.setTextColor(getResources().getColor(R.color.bacgroun_color));
                    tvCreatePassswordText.setVisibility(View.GONE);
                } else {
                    tvCreatePassswordText.setVisibility(View.VISIBLE);
                }

                if (isValidcaseSensPassword(edtCreatePassword.getText().toString().trim())) {
                    tvUpLowChar.setTextColor(getResources().getColor(R.color.error_color));
                } else {
                    tvUpLowChar.setTextColor(getResources().getColor(R.color.btn_text_color));
                }

                if (isValidcharLimitPassword(edtCreatePassword.getText().toString())) {
                    tvMinimumChar.setTextColor(getResources().getColor(R.color.error_color));
                } else {
                    tvMinimumChar.setTextColor(getResources().getColor(R.color.btn_text_color));
                }

                if (isValidateAtleastOneNumber(edtCreatePassword.getText().toString())) {
                    tvNumber.setTextColor(getResources().getColor(R.color.error_color));
                } else {
                    tvNumber.setTextColor(getResources().getColor(R.color.btn_text_color));
                }

                if (isValidateSpecialCharacters(edtCreatePassword.getText().toString())) {
                    tvSpecialCharacters.setTextColor(getResources().getColor(R.color.error_color));
                } else {
                    tvSpecialCharacters.setTextColor(getResources().getColor(R.color.btn_text_color));
                }

                if (hasConsecutiveCharacters(edtCreatePassword.getText().toString())) {
                    tvCharacterPair.setTextColor(getResources().getColor(R.color.error_color));
                } else {
                    tvCharacterPair.setTextColor(getResources().getColor(R.color.btn_text_color));
                }

                if (hasConsecutiveCharacter(edtCreatePassword.getText().toString())) {
                    tvCharacterPair.setTextColor(getResources().getColor(R.color.btn_text_color));
                } else {
                   /* if (hasConsecutiveCharacters(edtCreatePassword.getText().toString())) {
                        tvCharacterPair.setTextColor(getResources().getColor(R.color.error_color));
                    } else {
                        tvCharacterPair.setTextColor(getResources().getColor(R.color.btn_text_color));
                    }*/
                    tvCharacterPair.setTextColor(getResources().getColor(R.color.error_color));
                    if (edtCreatePassword.getText().toString().equalsIgnoreCase("")) {
                        tvCharacterPair.setTextColor(getResources().getColor(R.color.btn_text_color));
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        edtCreatePassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        if (!edtCreatePassword.getText().toString().equalsIgnoreCase("") &&
                                isValidcaseSensPassword(edtCreatePassword.getText().toString().trim())&&
                                isValidateAtleastOneNumber(edtCreatePassword.getText().toString())&&
                                isValidateSpecialCharacters(edtCreatePassword.getText().toString())) {
                            llpasswoedReq.setVisibility(View.GONE);
                            //tvConfirPasswordText.setVisibility(View.VISIBLE);
                            edtConfirmPassword.setVisibility(View.VISIBLE);
                        } else {
                            llpasswoedReq.setVisibility(View.VISIBLE);
                            //tvConfirPasswordText.setVisibility(View.VISIBLE);
                            edtConfirmPassword.setVisibility(View.GONE);
                        }
                        break;
                }
                return false;
            }
        });

        edtConfirmPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        if (edtCreatePassword.getText().toString().equals(edtConfirmPassword.getText().toString())) {
                            btnNext.setTextColor(getResources().getColor(R.color.btn_text_color));
                            btnNext.setEnabled(true);
                        } else {
                            btnNext.setTextColor(getResources().getColor(R.color.btn_grey_color));
                            btnNext.setEnabled(false);
                        }
                        break;
                }
                return false;
            }
        });

        edtConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtConfirmPassword.setBackgroundResource(R.drawable.edit_text_background);
                tvConfirPasswordText.setText("Re-enter Password");
                if (edtConfirmPassword.getText().toString().equalsIgnoreCase("")) {
                    tvConfirPasswordText.setVisibility(View.GONE);
                } else {
                    tvConfirPasswordText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        appyfontForAllViews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                // PasswordValidator passwordValidator = new PasswordValidator();
                if (edtCreatePassword.getText().toString().equalsIgnoreCase("")) {
                    tvCreatePassswordText.setVisibility(View.VISIBLE);
                    edtCreatePassword.setBackgroundResource(R.drawable.activation_error_color_background);
                    tvCreatePassswordText.setText("Something's not right. Please check below.");
                    tvPasswoedRequirementHeading.setTextColor(getResources().getColor(R.color.text_password_error));
                    break;
                } else if (!isValidcaseSensPassword(edtCreatePassword.getText().toString().trim())) {
                    //tvCreatePassswordText.setVisibility(View.VISIBLE);
                    edtCreatePassword.setBackgroundResource(R.drawable.activation_error_color_background);
                    tvCreatePassswordText.setText("Something's not right. Please check below.");
                    tvPasswoedRequirementHeading.setTextColor(getResources().getColor(R.color.text_password_error));
                    break;
                } else if (!isValidcharLimitPassword(edtCreatePassword.getText().toString())) {
                    //tvCreatePassswordText.setVisibility(View.VISIBLE);
                    edtCreatePassword.setBackgroundResource(R.drawable.activation_error_color_background);
                    tvCreatePassswordText.setText("Something's not right. Please check below.");
                    tvPasswoedRequirementHeading.setTextColor(getResources().getColor(R.color.text_password_error));
                    break;
                } else if (!isValidateAtleastOneNumber(edtCreatePassword.getText().toString())) {
                    //tvCreatePassswordText.setVisibility(View.VISIBLE);
                    edtCreatePassword.setBackgroundResource(R.drawable.activation_error_color_background);
                    tvCreatePassswordText.setText("Something's not right. Please check below.");
                    tvPasswoedRequirementHeading.setTextColor(getResources().getColor(R.color.text_password_error));
                    break;
                } else if (!isValidateSpecialCharacters(edtCreatePassword.getText().toString())) {
                    //tvCreatePassswordText.setVisibility(View.VISIBLE);
                    edtCreatePassword.setBackgroundResource(R.drawable.activation_error_color_background);
                    tvCreatePassswordText.setText("Something's not right. Please check below.");
                    tvPasswoedRequirementHeading.setTextColor(getResources().getColor(R.color.text_password_error));
                    break;
                }/*else if(!hasConsecutiveCharacters(edtCreatePassword.getText().toString())){
                    //tvCreatePassswordText.setVisibility(View.VISIBLE);
                    edtCreatePassword.setBackgroundResource(R.drawable.activation_error_color_background);
                    tvCreatePassswordText.setText("Something's not right. Please check below.");
                    break;
                }*//*else if(hasConsecutiveCharacter(edtCreatePassword.getText().toString())){
                    //tvCreatePassswordText.setVisibility(View.VISIBLE);
                    edtCreatePassword.setBackgroundResource(R.drawable.activation_error_color_background);
                    tvCreatePassswordText.setText("Something's not right. Please check below.");
                    break;
                }*/ else {
                    tvPasswoedRequirementHeading.setTextColor(getResources().getColor(R.color.bacgroun_color));
                    edtCreatePassword.setBackgroundResource(R.drawable.edit_text_background);
                    tvCreatePassswordText.setText("Enter Your Password");

                    if (edtCreatePassword.getText().toString().equals(edtConfirmPassword.getText().toString())) {
                        submitUpdatePassword();
                        tvConfirPasswordText.setVisibility(View.VISIBLE);
                        edtConfirmPassword.setVisibility(View.VISIBLE);
                        llpasswoedReq.setVisibility(View.GONE);
                    } else {
                        tvConfirPasswordText.setVisibility(View.VISIBLE);
                        edtConfirmPassword.setBackgroundResource(R.drawable.activation_error_color_background);
                        tvConfirPasswordText.setText("Your passwords do not match. Please try again.");
                    }

                    /*Intent i = new Intent(CreatePasswordActivity.this, ConfirmPasswordActivity.class);
                    if(getIntent().hasExtra("flag")){
                        i.putExtra("flag",getIntent().getExtras().getString("flag"));
                        i.putExtra("userId", getIntent().getExtras().getString("userId"));
                        i.putExtra("password", edtCreatePassword.getText().toString());
                        //Toast.makeText(this, "WIP", Toast.LENGTH_SHORT).show();
                    }else {
                        i.putExtra("password", edtCreatePassword.getText().toString());
                        i.putExtra("user_name", getIntent().getExtras().getString("user_name"));
                    }
                    startActivity(i);*/
                    break;
                }

            case R.id.img_back_arrow:
                onBackPressed();
                break;
        }
    }

    private void appyfontForAllViews() {
        tf = Typeface.createFromAsset(getAssets(), "KievitSlabOT-Bold.otf");
        tvHeading.setTypeface(tf, Typeface.BOLD);

        tf = Typeface.createFromAsset(getAssets(), "KievitSlabOT-Bold.otf");
        edtCreatePassword.setTypeface(tf, Typeface.BOLD);

        tf = Typeface.createFromAsset(getAssets(), "KievitSlabOT-Medium.otf");
        btnNext.setTypeface(tf);

        tf = Typeface.createFromAsset(getAssets(), "HelveticaNeueLight.ttf");
        tvCreatePassswordText.setTypeface(tf);

        tf = Typeface.createFromAsset(getAssets(), "HelveticaNeueLight.ttf");
        tvMinimumChar.setTypeface(tf);

        tf = Typeface.createFromAsset(getAssets(), "HelveticaNeueLight.ttf");
        tvSpecialCharacters.setTypeface(tf);

        tf = Typeface.createFromAsset(getAssets(), "HelveticaNeueLight.ttf");
        tvNumber.setTypeface(tf);

        tf = Typeface.createFromAsset(getAssets(), "HelveticaNeueLight.ttf");
        tvUpLowChar.setTypeface(tf);

        tf = Typeface.createFromAsset(getAssets(), "HelveticaNeueLight.ttf");
        tvCharacterPair.setTypeface(tf);

        tf = Typeface.createFromAsset(getAssets(), "HelveticaNeueLight.ttf");
        tvPasswoedRequirementHeading.setTypeface(tf);

        tf = Typeface.createFromAsset(getAssets(), "KievitSlabOT-Bold.otf");
        edtConfirmPassword.setTypeface(tf, Typeface.BOLD);

        tf = Typeface.createFromAsset(getAssets(), "HelveticaNeueLight.ttf");
        tvConfirPasswordText.setTypeface(tf);

    }

    private void submitUpdatePassword() {
        {
            final SharedPreferences.Editor editor = sharedpreferences.edit();
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(CreatePasswordActivity.this);
                String URL = AppConfig.BASE_URL + AppConfig.UPDATE_PASSWORD;
                JSONObject jsonBody = new JSONObject();
                if (getIntent().hasExtra("flag")) {
                    jsonBody.put("userID", getIntent().getExtras().getString("userId"));
                } else {
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
                                if (getIntent().hasExtra("flag")) {
                                    Intent intent = new Intent(CreatePasswordActivity.this, SignupActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    //intent.putExtra("password", edtCreatePassword.getText().toString());
                                    intent.putExtra("user_name", getIntent().getExtras().getString("user_name"));
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Intent intent = new Intent(CreatePasswordActivity.this, ProfileActivity.class);
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

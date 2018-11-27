package com.devool.ucicareconnect.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.devool.ucicareconnect.utils.AppConfig;
import com.devool.ucicareconnect.utils.PermissionUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAccountActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener, View.OnFocusChangeListener {

    public static final String USER_INFO = "user_info";
    SharedPreferences sharedpreferences;
    LinearLayout llName, llPhoneNumber;
    SwitchCompat switchCompat;
    TextView tvName, tvEmailAddress, tvAddress, tvPhoneNumber;
    String strUserId;
    Button btnSignOut, btnChangePassword;
    ImageView imgCloseButton;
    EditText edtAssistantName, edtAssistantPhoneNumber;
    CircleImageView imgProfileImg;
    String userChoosenTask, ImageToString;
    Bitmap bm;
    RelativeLayout relImglayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_account);
        llName = findViewById(R.id.ll_switcher_name);
        llPhoneNumber = findViewById(R.id.ll_switcher_phone_number);
        switchCompat = findViewById(R.id.switcher_assistant_name);
        tvName = findViewById(R.id.tv_user_name);
        tvAddress = findViewById(R.id.tv_address);
        tvEmailAddress = findViewById(R.id.tv_email_address);
        tvPhoneNumber = findViewById(R.id.tv_phone_number);
        btnSignOut = findViewById(R.id.btn_sign_out);
        imgCloseButton = findViewById(R.id.img_close_button);
        btnChangePassword = findViewById(R.id.btn_change_password);
        edtAssistantName = findViewById(R.id.tv_assistant_name);
        edtAssistantPhoneNumber = findViewById(R.id.tv_assistant_phone_number);
        imgProfileImg = findViewById(R.id.img_upload_profile_photo);
        relImglayout = findViewById(R.id.rel_image_layout);

        switchCompat.setOnCheckedChangeListener(this);
        btnSignOut.setOnClickListener(this);
        imgCloseButton.setOnClickListener(this);
        btnChangePassword.setOnClickListener(this);
        tvEmailAddress.setOnClickListener(this);
        imgProfileImg.setOnClickListener(this);
        relImglayout.setOnClickListener(this);

        sharedpreferences = getSharedPreferences(USER_INFO, Context.MODE_PRIVATE);
        strUserId = sharedpreferences.getString("USER_ID", "");

        edtAssistantPhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        getProfileInfo();

        edtAssistantName.setOnFocusChangeListener(this);
        edtAssistantPhoneNumber.setOnFocusChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switcher_assistant_name:
                if (isChecked) {
                    llName.setVisibility(View.VISIBLE);
                    llPhoneNumber.setVisibility(View.VISIBLE);
                    break;
                } else {
                    llName.setVisibility(View.GONE);
                    llPhoneNumber.setVisibility(View.GONE);
                    break;
                }
        }
    }

    public void getProfileInfo() {
        RequestQueue requestQueue = Volley.newRequestQueue(UsersAccountActivity.this);
        String URL = AppConfig.BASE_URL + AppConfig.GET_USER_PROFILE + "/" + strUserId;
        JSONObject jsonBody = new JSONObject();

        final String requestBody = jsonBody.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("getUserInfo", response);
                    JSONArray array = new JSONArray(response);

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(0);
                        tvName.setText(object.getString("firstName") + " " + object.getString("lastName"));
                        tvEmailAddress.setText(object.getString("email"));
                        tvPhoneNumber.setText(object.getString("phNo"));
                        tvAddress.setText(object.getString("addLn1"));
                        edtAssistantName.setText(object.getString("assistant_Name"));
                        edtAssistantPhoneNumber.setText(object.getString("assistant_PhNo"));
                        if (object.getString("is_Assistant").equals("false")) {
                            switchCompat.setChecked(false);
                        } else if (object.getString("is_Assistant").equals("true")) {
                            switchCompat.setChecked(true);
                        }
                        //decode base64 string to image
                        //byte[] imageBytes;
                        String base64Image = object.getString("profileImage");
                        /*byte[] decodedString = Base64.decode(base64Image, Base64.URL_SAFE);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        imgProfileImg.setImageBitmap(decodedByte);*/

                        Bitmap b = base64ToBitmap(base64Image);
                        imgProfileImg.setImageBitmap(b);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_out:
                Intent i = new Intent(UsersAccountActivity.this, SignupActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
                break;
            case R.id.img_close_button:
                Intent intent = new Intent(UsersAccountActivity.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                onBackPressed();
                break;
            case R.id.btn_change_password:
                Intent intent1 = new Intent(UsersAccountActivity.this, CreatePasswordActivity.class);
                intent1.putExtra("flag", "from_account_screen");
                intent1.putExtra("userId", strUserId);
                startActivity(intent1);
                break;
            case R.id.tv_email_address:
                Toast.makeText(this, "WIP", Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_upload_profile_photo:
                selectImage();
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.tv_assistant_name:
                if (!hasFocus) {
                    if (edtAssistantName.getText().toString().equals("")) {
                        updateAssistant("", "", "0");
                    } else {
                        updateAssistant(edtAssistantName.getText().toString(), "", "1");
                    }
                }
                break;
            case R.id.tv_assistant_phone_number:
                if (!hasFocus) {
                    if (edtAssistantPhoneNumber.getText().toString().equals("") && edtAssistantName.getText().toString().equals("")) {
                        updateAssistant("", "", "0");
                    } else {
                        if (!edtAssistantName.getText().toString().equals("")) {
                            updateAssistant(edtAssistantName.getText().toString(), edtAssistantPhoneNumber.getText().toString(), "1");
                        } else {
                            updateAssistant("", edtAssistantPhoneNumber.getText().toString(), "1");
                        }
                    }
                }
                break;
        }
    }

    public void updateAssistant(String strAssistantName, String strAssistantPhoneNumber, String isAssistant) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(UsersAccountActivity.this);
            String URL = AppConfig.BASE_URL + AppConfig.UPDATE_AASSISTANT_PROFILE;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("UserID", strUserId);
            jsonBody.put("Assistant_Name", strAssistantName);
            jsonBody.put("Assistant_PhNo", strAssistantPhoneNumber);
            jsonBody.put("Is_Assistant", isAssistant);

            Log.e("UserID", strUserId);
            Log.e("Assistant_Name", strAssistantName);
            Log.e("Assistant_PhNo", strAssistantPhoneNumber);
            Log.e("Is_Assistant", isAssistant);

            final String requestBody = jsonBody.toString();

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.e("Assistant_response", response);
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

    private Bitmap base64ToBitmap(String b64) {
        try {
        byte[] imageAsBytes = Base64.decode(b64.getBytes(), Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
        } catch (Exception e) {
           e.printStackTrace();
        }
        return null;
    }

    private void selectImage() {
        final CharSequence[] items = {"Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(UsersAccountActivity.this);
        builder.setTitle("Select Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = PermissionUtils.checkPermission(UsersAccountActivity.this);
                if (items[item].equals("Choose from Gallery")) {
                    userChoosenTask = "Choose from Gallery";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), 5);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionUtils.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Choose from Gallery"))
                        galleryIntent();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 5)
                onSelectFromGalleryResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                int nh = (int) (bm.getHeight() * (512.0 / bm.getWidth()));
                Bitmap scaled = Bitmap.createScaledBitmap(bm, 512, nh, true);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                imgProfileImg.setImageBitmap(scaled);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ImageToString = convertString(bm);
        submitImage();
    }

    private String convertString(Bitmap bm) {
        //Bitmap bm = BitmapFactory.decodeFile(docFilePath);
        Bitmap imageScaled = Bitmap.createScaledBitmap(bm, 700, 1080, true);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        imageScaled.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] data = bos.toByteArray();
        return android.util.Base64.encodeToString(data, android.util.Base64.DEFAULT);
    }


    public void submitImage() {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(UsersAccountActivity.this);
            String URL = AppConfig.BASE_URL + AppConfig.UPDATE_PROLFILE_IMG;
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("UserID", strUserId);
            if (ImageToString != null) {
                jsonBody.put("ProfileImage", ImageToString);
            } else {
                jsonBody.put("ProfileImage", "");
            }

            Log.e("useid", sharedpreferences.getString("USER_ID", ""));
            if (ImageToString != null) {
                Log.e("ProfileImage", ImageToString);
            } else {
                Log.e("ProfileImage", "");
            }


            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(UsersAccountActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
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

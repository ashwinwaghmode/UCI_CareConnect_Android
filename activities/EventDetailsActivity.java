package com.devool.ucicareconnect.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.devool.ucicareconnect.R;
import com.devool.ucicareconnect.adapter.MultiViewTypeAdapter;
import com.devool.ucicareconnect.utils.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class EventDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tvEventname, tvEventDetails, tvEventDateaAndTime, tvLink, tvEventDescDetails;
    ImageView imageView;
    ProgressBar progressBar;
    ImageView imgCloseButton;
    String strLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        tvEventname = findViewById(R.id.tv_event_name);
        tvEventDetails = findViewById(R.id.tv_event_details);
        tvEventDateaAndTime = findViewById(R.id.tv_event_date_time);
        tvLink = findViewById(R.id.tv_link);
        tvEventDescDetails = findViewById(R.id.tv_event_detail_desc);
        imageView = findViewById(R.id.img_event);
        progressBar = findViewById(R.id.progress);
        imgCloseButton = findViewById(R.id.img_close_button);

        imgCloseButton.setOnClickListener(this);
        tvLink.setOnClickListener(this);
        tvLink.setPaintFlags(tvLink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        getEventDescDetails();


    }

    public void getEventDescDetails() {
        RequestQueue requestQueue = Volley.newRequestQueue(EventDetailsActivity.this);
        String URL = AppConfig.BASE_URL + AppConfig.GET_EVENT_DETAILS + "/" + getIntent().getExtras().getString("event_id");
        JSONObject jsonBody = new JSONObject();

        final String requestBody = jsonBody.toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    Log.e("event_response", response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(0);
                        tvEventname.setText(object.getString("header"));
                        tvEventDetails.setText(object.getString("details"));
                        tvEventDateaAndTime.setText(object.getString("event_Date"));

                        if (object.getString("rsvpLink").equalsIgnoreCase("") || object.isNull("rsvpLink")) {
                            tvLink.setText(object.getString("add_link"));
                            strLink = object.getString("add_link");
                        } else {
                            tvLink.setText(object.getString("rsvpLink"));
                            strLink = object.getString("rsvpLink");
                        }
                        if (object.getString("details").equalsIgnoreCase("") || object.isNull("details")) {
                            tvEventDescDetails.setText(object.getString("announcement"));
                        } else {
                            tvEventDescDetails.setText(object.getString("details"));
                        }

                        Glide.with(EventDetailsActivity.this).
                                load(object.getString("url1")).
                                placeholder(R.drawable.loading)
                                .listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                        progressBar.setVisibility(View.GONE);
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        progressBar.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(imageView);
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
            case R.id.img_close_button:
                Intent intent = new Intent(EventDetailsActivity.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                onBackPressed();
                break;
            case R.id.tv_link:
                if (strLink != null) {
                    try {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                        browserIntent.setData(Uri.parse(strLink));
                        startActivity(browserIntent);
                    } catch (ActivityNotFoundException e) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                        browserIntent.setData(Uri.parse("http://" + strLink));
                        startActivity(browserIntent);
                    }
                    break;
                }
        }
    }
}

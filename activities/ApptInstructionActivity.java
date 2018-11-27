package com.devool.ucicareconnect.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
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
import com.devool.ucicareconnect.adapter.InstructionAdapter;
import com.devool.ucicareconnect.adapter.MultiViewTypeAdapter;
import com.devool.ucicareconnect.helper.ContextualModelItems;
import com.devool.ucicareconnect.helper.InstructionAttachmenthelper;
import com.devool.ucicareconnect.utils.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ApptInstructionActivity extends AppCompatActivity {

    TextView tvEventName, tvEventDateAndTime, tvDoctorName, tvDoctorAddress, tvDoctorDept, tvDocumentDetail;
    ArrayList<InstructionAttachmenthelper> list = new ArrayList<>();
    RecyclerView recyclerViewAttachment;
    InstructionAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appt_instruction);

        tvEventName = findViewById(R.id.tv_event_name);
        tvEventDateAndTime = findViewById(R.id.tv_event_date_time);
        tvDoctorName = findViewById(R.id.tv_doctor_name);
        tvDoctorAddress = findViewById(R.id.tv_event_address);
        tvDoctorDept = findViewById(R.id.tv_doctor_dept);
        tvDocumentDetail = findViewById(R.id.tv_document_detail);
        recyclerViewAttachment = findViewById(R.id.recycler_view_attachement);

        getInstructionDetails();
    }

    public void getInstructionDetails() {
        RequestQueue requestQueue = Volley.newRequestQueue(ApptInstructionActivity.this);
        String URL = AppConfig.BASE_URL + AppConfig.GET_INSTRUCTION_DETAIL + "/" + getIntent().getExtras().getString("event_id");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e("getInstructionDetails", response);
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        tvEventName.setText(object.getString("appointment_type"));
                        tvEventDateAndTime.setText(object.getString("appointment_Date"));
                        tvDoctorName.setText(object.getString("doctor"));
                        tvDoctorAddress.setText(object.getString("address"));
                        tvDoctorDept.setText(object.getString("physician_Department"));
                        //tvDocumentDetail.setText(Html.fromHtml(object.getString("instructions")));
                        Spanned html = Html.fromHtml(object.getString("instructions"));
                        tvDocumentDetail.setText(noTrailingwhiteLines(html));
                        //noTrailingwhiteLines(object.getString("instructions"));
                       /* String str = object.getString("instructions").replaceAll("\\r\\n", "");
                        tvDocumentDetail.setText(Html.fromHtml(str));*/

                        JSONArray array1 = new JSONArray(array.getJSONObject(i).getString("fileattach"));
                        for(int j = 0; j < array1.length(); j++){
                            InstructionAttachmenthelper attachmenthelper = new InstructionAttachmenthelper();
                            attachmenthelper.setStrFileName(array1.getJSONObject(j).getString("fileName"));
                            attachmenthelper.setStrExtension(array1.getJSONObject(j).getString("extension"));
                            attachmenthelper.setStrDescription(array1.getJSONObject(j).getString("description"));
                            attachmenthelper.setStrAttachmentURL(array1.getJSONObject(j).getString("attachment_URI"));
                            list.add(attachmenthelper);
                        }

                        recyclerViewAttachment.setHasFixedSize(true);
                        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(ApptInstructionActivity.this,2);
                        recyclerViewAttachment.setLayoutManager(mLayoutManager);
                        //Log.e("ListSize", ""+achevementDataItems.size());
                        adapter = new InstructionAdapter(ApptInstructionActivity.this, list);
                        recyclerViewAttachment.setAdapter(adapter);
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

    private CharSequence noTrailingwhiteLines(CharSequence text) {

        while (text.charAt(text.length() - 1) == '\n') {
            text = text.subSequence(0, text.length() - 1);
        }
        return text;
    }
}

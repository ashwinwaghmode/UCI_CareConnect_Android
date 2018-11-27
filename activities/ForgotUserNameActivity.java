package com.devool.ucicareconnect.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.devool.ucicareconnect.R;

public class ForgotUserNameActivity extends AppCompatActivity implements View.OnClickListener{

    Button btnNext;
    EditText edtEmailAddress;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Typeface tf;
    ImageView imgBackArrow;
    TextView tvActivationCode, tvHeading, tvActivationCodeMsg, tvUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_user_name);

        btnNext = findViewById(R.id.btn_next);
        edtEmailAddress = findViewById(R.id.edt_email_address);
        tvActivationCode = findViewById(R.id.tv_lost_activation_code);
        tvActivationCodeMsg = findViewById(R.id.tv_activation_code_msg);
        tvUserName = findViewById(R.id.tv_username_text);
        imgBackArrow = findViewById(R.id.img_back_arrow);
        tvHeading = findViewById(R.id.tv_heading);

        btnNext.setOnClickListener(this);
        imgBackArrow.setOnClickListener(this);

        edtEmailAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                int result = actionId & EditorInfo.IME_MASK_ACTION;
                switch (result) {
                    case EditorInfo.IME_ACTION_DONE:
                        if (edtEmailAddress.getText().toString().trim().matches(emailPattern) && edtEmailAddress.length() > 0) {
                            btnNext.setTextColor(getResources().getColor(R.color.btn_text_color));
                            btnNext.setEnabled(true);
                        } else {
                            edtEmailAddress.setBackgroundResource(R.drawable.activation_error_color_background);
                            btnNext.setTextColor(getResources().getColor(R.color.btn_grey_color));
                            btnNext.setEnabled(false);
                            tvUserName.setText("Invalid email address");
                        }
                        break;
                }
                return false;
            }
        });
        appyfontForAllViews();

        edtEmailAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtEmailAddress.setBackgroundResource(R.drawable.edit_text_background);
                tvUserName.setText("Email Address");

                if (edtEmailAddress.getText().toString().equalsIgnoreCase("")) {
                    tvUserName.setVisibility(View.GONE);
                    //tvActivationCodeMsg.setText("Check your email for your activation code.");
                } else {
                    tvUserName.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void appyfontForAllViews() {
        tf = Typeface.createFromAsset(getAssets(), "KievitSlabOT-Bold.otf");
        edtEmailAddress.setTypeface(tf, Typeface.BOLD);

        tf = Typeface.createFromAsset(getAssets(), "HelveticaNeueLight.ttf");
        tvActivationCode.setTypeface(tf);

        tf = Typeface.createFromAsset(getAssets(), "HelveticaNeueLight.ttf");
        tvActivationCodeMsg.setTypeface(tf);

        tf = Typeface.createFromAsset(getAssets(), "KievitSlabOT-Medium.otf");
        btnNext.setTypeface(tf);

        tf = Typeface.createFromAsset(getAssets(), "HelveticaNeueLight.ttf");
        tvUserName.setTypeface(tf);

        tf = Typeface.createFromAsset(getAssets(), "KievitSlabOT-Bold.otf");
        tvHeading.setTypeface(tf,Typeface.BOLD);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                //oast.makeText(this, "WIP", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(ForgotUserNameActivity.this, ForgotRequestSentActivity.class);
                i.putExtra("flag_forgot_username", "username");
                startActivity(i);
                break;
            case R.id.img_back_arrow:
                onBackPressed();
                break;
        }
    }
}

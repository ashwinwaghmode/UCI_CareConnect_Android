package com.devool.ucicareconnect.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.devool.ucicareconnect.R;
import com.devool.ucicareconnect.activities.DashboardActivity;

public class FirstTimePhysicianPatientPreviousFacilityFragment extends Fragment implements View.OnClickListener {

    EditText edtPhysicianName, edtFacility, edtPhoneNumber, edtFacilityAddress;
    Button btnNext;
    ImageView imgCloseButton, imgBtnCall, imgBtnChat;
    String strAssistance, strpatient, strAppointmentType, strMeetPurpose, strAvailability, strDay, strPhysicianName, strPhysicianType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            strAppointmentType = getArguments().getString("appointment_type");
            strMeetPurpose = getArguments().getString("meet_purpose");
            strAvailability = getArguments().getString("availability");
            strPhysicianName = getArguments().getString("physician_name");
            strPhysicianType = getArguments().getString("physician_type");
            strpatient = getArguments().getString("strPatient");
            strDay = getArguments().getString("day");
            strAssistance = getArguments().getString("strAssistance");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View row = inflater.inflate(R.layout.fragment_first_time_physician_patient_previous_facility, container, false);
        edtPhysicianName = row.findViewById(R.id.edit_physician_name);
        edtFacility = row.findViewById(R.id.edit_facility);
        edtPhoneNumber = row.findViewById(R.id.edit_phone_number);
        edtFacilityAddress = row.findViewById(R.id.edit_facility_address);
        btnNext = row.findViewById(R.id.btn_next);
        imgCloseButton = row.findViewById(R.id.img_close_button);
        imgBtnCall = row.findViewById(R.id.img_btn_call);
        imgBtnChat = row.findViewById(R.id.img_btn_chat);

        edtPhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        edtFacilityAddress.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edtFacilityAddress.setRawInputType(InputType.TYPE_CLASS_TEXT);

        btnNext.setOnClickListener(this);
        imgCloseButton.setOnClickListener(this);
        imgBtnChat.setOnClickListener(this);
        imgBtnCall.setOnClickListener(this);

        edtPhysicianName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtPhysicianName.setBackgroundResource(R.drawable.edit_text_background);
                if (edtPhysicianName.getText().toString().equalsIgnoreCase("")) {
                    edtPhysicianName.setBackgroundResource(R.drawable.edit_text_background);
                    edtFacility.setBackgroundResource(R.drawable.edit_text_background);
                    edtPhoneNumber.setBackgroundResource(R.drawable.edit_text_background);
                    edtFacilityAddress.setBackgroundResource(R.drawable.edit_text_background);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtFacility.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtFacility.setBackgroundResource(R.drawable.edit_text_background);
                if (edtFacility.getText().toString().equalsIgnoreCase("")) {
                    edtPhysicianName.setBackgroundResource(R.drawable.edit_text_background);
                    edtFacility.setBackgroundResource(R.drawable.edit_text_background);
                    edtPhoneNumber.setBackgroundResource(R.drawable.edit_text_background);
                    edtFacilityAddress.setBackgroundResource(R.drawable.edit_text_background);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtPhoneNumber.setBackgroundResource(R.drawable.edit_text_background);
                if (edtPhoneNumber.getText().toString().equalsIgnoreCase("")) {
                    edtPhysicianName.setBackgroundResource(R.drawable.edit_text_background);
                    edtFacility.setBackgroundResource(R.drawable.edit_text_background);
                    edtPhoneNumber.setBackgroundResource(R.drawable.edit_text_background);
                    edtFacilityAddress.setBackgroundResource(R.drawable.edit_text_background);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtFacilityAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                edtFacilityAddress.setBackgroundResource(R.drawable.edit_text_background);
                if (edtFacilityAddress.getText().toString().equalsIgnoreCase("")) {
                    edtPhysicianName.setBackgroundResource(R.drawable.edit_text_background);
                    edtFacility.setBackgroundResource(R.drawable.edit_text_background);
                    edtPhoneNumber.setBackgroundResource(R.drawable.edit_text_background);
                    edtFacilityAddress.setBackgroundResource(R.drawable.edit_text_background);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return row;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (edtPhysicianName.getText().toString().equals("") &&
                        edtFacility.getText().toString().equals("") &&
                        edtPhoneNumber.getText().toString().equals("") &&
                        edtFacilityAddress.getText().toString().equals("")) {
                    edtPhysicianName.setBackgroundResource(R.drawable.activation_error_color_background);
                    edtFacility.setBackgroundResource(R.drawable.activation_error_color_background);
                    edtPhoneNumber.setBackgroundResource(R.drawable.activation_error_color_background);
                    edtFacilityAddress.setBackgroundResource(R.drawable.activation_error_color_background);
                    break;
                } else if (edtPhysicianName.getText().toString().equals("")) {
                    edtPhysicianName.setBackgroundResource(R.drawable.activation_error_color_background);
                    break;
                } else if (edtFacility.getText().toString().equals("")) {
                    edtFacility.setBackgroundResource(R.drawable.activation_error_color_background);
                    break;
                } else if (edtPhoneNumber.getText().toString().length() < 14) {
                    edtPhoneNumber.setBackgroundResource(R.drawable.activation_error_color_background);
                    break;
                } else if (edtFacilityAddress.getText().toString().equals("")) {
                    edtFacilityAddress.setBackgroundResource(R.drawable.activation_error_color_background);
                } else {
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    FirstTimePhysicianNewPtRequest fragment = new FirstTimePhysicianNewPtRequest();
                    Bundle args = new Bundle();
                    args.putString("appointment_type", strAppointmentType);
                    args.putString("meet_purpose", strMeetPurpose);
                    args.putString("availability", strAvailability);
                    args.putString("day", strDay);
                    args.putString("physician_name", strPhysicianName);
                    args.putString("physician_type", strPhysicianType);
                    args.putString("strPatient", strpatient);
                    args.putString("strAssistance", strAssistance);
                    args.putString("facility_physician_name", edtPhysicianName.getText().toString());
                    args.putString("facility_name", edtFacility.getText().toString());
                    args.putString("facility_phone_number", edtPhoneNumber.getText().toString());
                    args.putString("facility_address", edtFacilityAddress.getText().toString());
                    fragment.setArguments(args);
                    fragmentTransaction.replace(R.id.myContainer, fragment);
                    fragmentTransaction.commit();
                    fragmentTransaction.addToBackStack(null);
                }
                break;
            case R.id.img_close_button:
                Intent intent = new Intent(getActivity(), DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.img_btn_call:
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:(866) 698-2422"));
                startActivity(i);
                break;
        }
    }
}

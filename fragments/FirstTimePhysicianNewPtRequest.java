package com.devool.ucicareconnect.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devool.ucicareconnect.R;
import com.devool.ucicareconnect.activities.DashboardActivity;

public class FirstTimePhysicianNewPtRequest extends Fragment implements View.OnClickListener{
    ImageView imgCloseButton, imgBtnCall, imgBtnChat;
    LinearLayout llPhysicianName, llFacilityName, llFacilityPhoneNumber, llFacilityAddress, llFacilityAnySpecificRequest;
    TextView tvAppointmentType, tvNewAppointment, tvPhysicianName, tvPhysicianType, tvAvaliability,tvTimeOfDay, tvSpecificRequest, tvFacilityPhysicianName, tvFacilityname, tvFacilityPhoneNumber, tvFacilityAddress, tvNewPatientmdRecords, tvNewpatientAssistance;

    String strAssistance, strpatient, strAppointmentType, strMeetPurpose, strAvailability, strDay, strPhysicianName, strPhysicianType, strFacilityPhysicianName, strFacilityName, strFacilityphoneNumber, strFacilityAddress, strAnySpecificRequest;
    Button btnAnySpecificRequest, btnSendRequest;

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
            strFacilityPhysicianName = getArguments().getString("facility_physician_name");
            strFacilityName = getArguments().getString("facility_name");
            strFacilityphoneNumber = getArguments().getString("facility_phone_number");
            strFacilityAddress = getArguments().getString("facility_address");
            strAnySpecificRequest = getArguments().getString("Any_Specific_Request");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View row = inflater.inflate(R.layout.fragment_first_time_physician_new_pt_request, container, false);
        llPhysicianName = row.findViewById(R.id.ll_facility_physician_name);
        llFacilityName = row.findViewById(R.id.ll_facility_name);
        llFacilityPhoneNumber = row.findViewById(R.id.ll_facility_phone_number);
        llFacilityAddress = row.findViewById(R.id.ll_facility_address);
        llFacilityAnySpecificRequest = row.findViewById(R.id.ll_facility_any_specific_request);

        tvAppointmentType = row.findViewById(R.id.tv_appointment_type);
        tvNewAppointment = row.findViewById(R.id.tv_appointment_name);
        tvPhysicianName = row.findViewById(R.id.tv_type_name);
        tvPhysicianType = row.findViewById(R.id.tv_physician_type);
        tvAvaliability = row.findViewById(R.id.tv_availability);
        tvTimeOfDay = row.findViewById(R.id.tv_time_of_day);
        tvSpecificRequest = row.findViewById(R.id.tv_specific_request);
        tvFacilityPhysicianName = row.findViewById(R.id.tv_previous_facility_physician_name);
        tvFacilityname = row.findViewById(R.id.tv_previous_facility_name);
        tvFacilityPhoneNumber = row.findViewById(R.id.tv_previous_facility_phone_number);
        tvFacilityAddress = row.findViewById(R.id.tv_previous_facility_address);
        tvNewPatientmdRecords = row.findViewById(R.id.tv_new_patient_md_records);
        tvNewpatientAssistance = row.findViewById(R.id.tv_assistance);

        imgCloseButton = row.findViewById(R.id.img_close_button);
        imgBtnCall = row.findViewById(R.id.img_btn_call);
        imgBtnChat = row.findViewById(R.id.img_btn_chat);
        btnAnySpecificRequest =row.findViewById(R.id.btn_any_specific_request);
        btnSendRequest = row.findViewById(R.id.btn_send_request);


        tvAppointmentType.setText(strAppointmentType);
        tvNewAppointment.setText(strMeetPurpose);
        tvPhysicianName.setText(strPhysicianName);
        tvPhysicianType.setText(strPhysicianType);
        tvAvaliability.setText(strAvailability);
        tvTimeOfDay.setText(strDay);
        tvNewPatientmdRecords.setText(strpatient);
        tvNewpatientAssistance.setText(strAssistance);

        if(strAnySpecificRequest!=null && !strAnySpecificRequest.isEmpty()){
            tvSpecificRequest.setText(strAnySpecificRequest);
            llFacilityAnySpecificRequest.setVisibility(View.VISIBLE);
        }else {
            llFacilityAnySpecificRequest.setVisibility(View.GONE);
        }
        if(strFacilityPhysicianName!=null && !strFacilityPhysicianName.isEmpty()){
            llPhysicianName.setVisibility(View.VISIBLE);
            tvFacilityPhysicianName.setText(strFacilityPhysicianName);
        }else {
            llPhysicianName.setVisibility(View.GONE);
        }
        if(strFacilityName!=null && !strFacilityName.isEmpty()){
            llFacilityName.setVisibility(View.VISIBLE);
            tvFacilityname.setText(strFacilityName);
        }else {
            llFacilityName.setVisibility(View.GONE);
        }
        if(strFacilityphoneNumber!=null && !strFacilityphoneNumber.isEmpty()){
            tvFacilityPhoneNumber.setText(strFacilityphoneNumber);
            llFacilityPhoneNumber.setVisibility(View.VISIBLE);
        }else {
            llFacilityPhoneNumber.setVisibility(View.GONE);
        }
        if(strFacilityAddress!=null && !strFacilityAddress.isEmpty()){
            tvFacilityAddress.setText(strFacilityAddress);
            llFacilityAddress.setVisibility(View.VISIBLE);
        }else {
            llFacilityAddress.setVisibility(View.GONE);
        }
        if(strAnySpecificRequest!=null && !strAnySpecificRequest.isEmpty()){
            tvSpecificRequest.setText(strAnySpecificRequest);
            llFacilityAnySpecificRequest.setVisibility(View.VISIBLE);
        }else {
            llFacilityAnySpecificRequest.setVisibility(View.GONE);
        }

        imgCloseButton.setOnClickListener(this);
        btnAnySpecificRequest.setOnClickListener(this);
        btnSendRequest.setOnClickListener(this);
        imgBtnChat.setOnClickListener(this);
        imgBtnCall.setOnClickListener(this);

        return row;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_close_button:
                Intent intent = new Intent(getActivity(), DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.btn_send_request:
                btnSendRequest.setBackground(getResources().getDrawable(R.drawable.fill_appointment_button_corner));
                btnSendRequest.setTextColor(getResources().getColor(R.color.btn_text_color));
                Intent intent1 = new Intent(getActivity(), DashboardActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("is_physician_requested_completed", "Physician");
                intent1.putExtra("request", "Physician");
                intent1.putExtra("appointment_type", strAppointmentType);
                intent1.putExtra("Speciality", "Physician");
                intent1.putExtra("Physicians_Name", strPhysicianName);
                intent1.putExtra("Physicians_Specialty", strPhysicianType);
                intent1.putExtra("Availability", strAvailability);
                intent1.putExtra("Time_of_day", strDay);
                intent1.putExtra("Any_Specific_Request", strAnySpecificRequest);
                startActivity(intent1);
                getActivity().finish();
                break;
            case R.id.btn_any_specific_request:
                btnAnySpecificRequest.setBackground(getResources().getDrawable(R.drawable.fill_appointment_button_corner));
                btnAnySpecificRequest.setTextColor(getResources().getColor(R.color.btn_text_color));
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FirstTimePhysicianNewPtAnySpecificRequest fragment = new FirstTimePhysicianNewPtAnySpecificRequest();
                Bundle args = new Bundle();
                args.putString("appointment_type", strAppointmentType);
                args.putString("meet_purpose", strMeetPurpose);
                args.putString("availability", strAvailability);
                args.putString("day", strDay);
                args.putString("physician_name", strPhysicianName);
                args.putString("physician_type", strPhysicianType);
                args.putString("strPatient", strpatient);
                args.putString("strAssistance", strAssistance);
                args.putString("facility_physician_name", strFacilityPhysicianName);
                args.putString("facility_name", strFacilityName);
                args.putString("facility_phone_number", strFacilityphoneNumber);
                args.putString("facility_address", strFacilityAddress);
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.myContainer, fragment);
                fragmentTransaction.commit();
                fragmentTransaction.addToBackStack(null);
                break;
            case R.id.img_btn_call:
                Intent i = new Intent(Intent.ACTION_DIAL);
                i.setData(Uri.parse("tel:(866) 698-2422"));
                startActivity(i);
                break;
        }
    }
}

package com.devool.ucicareconnect.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.devool.ucicareconnect.R;
import com.devool.ucicareconnect.activities.DashboardActivity;

public class FirstTimePhysicianNewPatientFragment extends Fragment implements View.OnClickListener{

    Button btnyes, btnNo;
    ImageView imgCloseButton, imgBtnCall, imgBtnChat;
    String strpatient, strAppointmentType, strMeetPurpose, strAvailability, strDay, strPhysicianName, strPhysicianType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            strAppointmentType = getArguments().getString("appointment_type");
            strMeetPurpose = getArguments().getString("meet_purpose");
            strAvailability = getArguments().getString("availability");
            strPhysicianName = getArguments().getString("physician_name");
            strPhysicianType = getArguments().getString("physician_type");
            strDay = getArguments().getString("day");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View row = inflater.inflate(R.layout.fragment_first_time_physician_new_patient, container, false);
        btnyes = (Button) row.findViewById(R.id.btn_yes);
        btnNo = (Button) row.findViewById(R.id.btn_no);
        imgCloseButton = row.findViewById(R.id.img_close_button);
        imgBtnCall = row.findViewById(R.id.img_btn_call);
        imgBtnChat = row.findViewById(R.id.img_btn_chat);

        btnyes.setOnClickListener(this);
        btnNo.setOnClickListener(this);
        imgCloseButton.setOnClickListener(this);
        imgBtnChat.setOnClickListener(this);
        imgBtnCall.setOnClickListener(this);
        return row;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_yes:
                strpatient = btnyes.getText().toString();
                btnyes.setBackground(getResources().getDrawable(R.drawable.fill_appointment_button_corner));
                btnyes.setTextColor(getResources().getColor(R.color.btn_text_color));
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
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.myContainer, fragment);
                fragmentTransaction.commit();
                fragmentTransaction.addToBackStack(null);
                break;
            case R.id.btn_no:
                strpatient = btnNo.getText().toString();
                btnNo.setBackground(getResources().getDrawable(R.drawable.fill_appointment_button_corner));
                btnNo.setTextColor(getResources().getColor(R.color.btn_text_color));
                android.support.v4.app.FragmentManager fragmentManager1 = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                FirstTimePhysicianNewPatientAssistanceFragment fragment1 = new FirstTimePhysicianNewPatientAssistanceFragment();
                Bundle args1 = new Bundle();
                args1.putString("appointment_type", strAppointmentType);
                args1.putString("meet_purpose", strMeetPurpose);
                args1.putString("availability", strAvailability);
                args1.putString("day", strDay);
                args1.putString("physician_name", strPhysicianName);
                args1.putString("physician_type", strPhysicianType);
                args1.putString("strPatient", strpatient);
                fragment1.setArguments(args1);
                fragmentTransaction1.replace(R.id.myContainer, fragment1);
                fragmentTransaction1.commit();
                fragmentTransaction1.addToBackStack(null);
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

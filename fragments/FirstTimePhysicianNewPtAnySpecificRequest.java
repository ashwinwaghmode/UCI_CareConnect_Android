package com.devool.ucicareconnect.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.devool.ucicareconnect.R;
import com.devool.ucicareconnect.activities.DashboardActivity;

public class FirstTimePhysicianNewPtAnySpecificRequest extends Fragment implements View.OnClickListener{

    ImageView imgCloseButton, imgBtnCall, imgBtnChat;
    Button btnSave;

    String strMeetPurpose, strAppointmentType, strAvailability, strpatient, strDay, strPhysicianType, strPhysicianName, strAssistance, strFacilityPhysicianName, strFacilityName, strInteractionDetailId, strUserId, strFacilityphoneNumber, strFacilityAddress, strAnySpecificRequest;
    public static final String USER_INFO = "user_info";
    SharedPreferences sharedpreferences;

    EditText edtAnySpecificRequest;

    String StrFolloowupAppointmentType, strFollowupDoctorName, strFollowupAvailability, strFollowupTimeofDay, strFolloupFlag;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View row = inflater.inflate(R.layout.fragment_first_time_physician_new_pt_any_specific_request, container, false);
        edtAnySpecificRequest = row.findViewById(R.id.edit_specific_request);
        imgCloseButton = row.findViewById(R.id.img_close_button);
        btnSave = row.findViewById(R.id.btn_save);
        imgBtnCall = row.findViewById(R.id.img_btn_call);
        imgBtnChat = row.findViewById(R.id.img_btn_chat);

        imgCloseButton.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        imgBtnChat.setOnClickListener(this);
        imgBtnCall.setOnClickListener(this);

        edtAnySpecificRequest.setImeOptions(EditorInfo.IME_ACTION_DONE);
        edtAnySpecificRequest.setRawInputType(InputType.TYPE_CLASS_TEXT);
        return row;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close_button:
                Intent intent = new Intent(getActivity(), DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.btn_save:
                btnSave.setBackground(getResources().getDrawable(R.drawable.fill_appointment_button_corner));
                btnSave.setTextColor(getResources().getColor(R.color.btn_text_color));
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
                args.putString("facility_physician_name", strFacilityPhysicianName);
                args.putString("facility_name", strFacilityName);
                args.putString("facility_phone_number", strFacilityphoneNumber);
                args.putString("facility_address", strFacilityAddress);
                args.putString("Any_Specific_Request", edtAnySpecificRequest.getText().toString());
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

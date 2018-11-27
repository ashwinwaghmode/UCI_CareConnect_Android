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

public class ReferralConfirmationFragment extends Fragment implements View.OnClickListener{


    String strRelationship, strReferralname, strFamilyRelation, strAssociation, strContactInfo, strEmailAddress, strRelation;
    Button btnConfirm;
    ImageView imgCloseButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // if(strRelationship.equalsIgnoreCase("Family")){

        //}else if(strRelationship.equalsIgnoreCase("Friend")){

        //}else if(strRelationship.equalsIgnoreCase("Other Associates")){

        //}
        strRelationship = getArguments().getString("relationship");
        strReferralname = getArguments().getString("Referral_name");
        strFamilyRelation = getArguments().getString("family_relation");
        strAssociation = getArguments().getString("Association");
        strContactInfo = getArguments().getString("contact_info");
        strEmailAddress = getArguments().getString("contact_email");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View row = inflater.inflate(R.layout.fragment_referral_confirmation, container, false);
        btnConfirm = row.findViewById(R.id.btn_confirm);
        imgCloseButton = row.findViewById(R.id.img_close_button);

        btnConfirm.setOnClickListener(this);
        imgCloseButton.setOnClickListener(this);

        return row;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_confirm:
                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ReferralCompletedFragment fragment = new ReferralCompletedFragment();
                Bundle args = new Bundle();
                args.putString("relationship", strRelationship);
                args.putString("Association", strAssociation);
                args.putString("Referral_name", strReferralname);
                args.putString("family_relation", strFamilyRelation);
                args.putString("contact_info", strContactInfo);
                args.putString("contact_email", strEmailAddress);
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.myContainer, fragment);
                fragmentTransaction.commit();
                fragmentTransaction.addToBackStack(null);
                break;
            case R.id.img_close_button:
                Intent intent = new Intent(getActivity(), DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
                break;
        }
    }
}

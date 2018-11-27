package com.devool.ucicareconnect.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.devool.ucicareconnect.R;
import com.devool.ucicareconnect.activities.DashboardActivity;

public class ExclusivelyForYouFragment extends Fragment implements View.OnClickListener{

    Button btnContinue;
    TextView tvExclusively, tvForYou, tvExclusivelyForYouDes;
    Typeface tf;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View row = inflater.inflate(R.layout.fragment_exclusively_for_you, container, false);

        btnContinue = row.findViewById(R.id.btn_continue);
        tvExclusively = row.findViewById(R.id.tv_exclusively);
        tvForYou = row.findViewById(R.id.tv_for_you);
        tvExclusivelyForYouDes = row.findViewById(R.id.tv_exclusively_for_you_desc);

        btnContinue.setOnClickListener(this);

        appyfontForAllViews();
        return row;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_continue:
                Intent i = new Intent(getActivity(), DashboardActivity.class);
                i.putExtra("signup_flag", "SignUp");
                startActivity(i);
                break;
        }
    }

    private void appyfontForAllViews() {
        tf = Typeface.createFromAsset(getActivity().getAssets(), "RobotoSlab-Regular.ttf");
        tvExclusively.setTypeface(tf);

        tf = Typeface.createFromAsset(getActivity().getAssets(), "RobotoSlab-Regular.ttf");
        tvForYou.setTypeface(tf);

        tf = Typeface.createFromAsset(getActivity().getAssets(), "KievitSlabOT-Medium.otf");
        btnContinue.setTypeface(tf);

        tf = Typeface.createFromAsset(getActivity().getAssets(), "HelveticaNeueLight.ttf");
        tvExclusivelyForYouDes.setTypeface(tf);

    }
}

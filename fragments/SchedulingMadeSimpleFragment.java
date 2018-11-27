package com.devool.ucicareconnect.fragments;

import android.content.Context;
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

public class SchedulingMadeSimpleFragment extends Fragment implements View.OnClickListener{

    Button btnNext;
    TextView tvScheduling, tvMadeSimple, tvMadeSimpleDesc;
    Typeface tf;
    String strUserName;


    public static SchedulingMadeSimpleFragment newInstance(String param1, String param2) {
        SchedulingMadeSimpleFragment fragment = new SchedulingMadeSimpleFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            strUserName = getArguments().getString("user_name");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View row = inflater.inflate(R.layout.fragment_scheduling_made_simple, container, false);
        btnNext = row.findViewById(R.id.btn_next);
        tvScheduling = row.findViewById(R.id.tv_scheduling);
        tvMadeSimple = row.findViewById(R.id.tv_made_simple);
        tvMadeSimpleDesc = row.findViewById(R.id.scheduling_made_simple_desc);

        btnNext.setOnClickListener(this);

        appyfontForAllViews();
        return row;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next:
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                OnDemandServiceFragment fragment = new OnDemandServiceFragment();
                Bundle args = new Bundle();
                args.putString("user_name", strUserName);
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.myContainer, fragment);
                fragmentTransaction.commit();
                fragmentTransaction.addToBackStack(null);
                break;
        }
    }

    private void appyfontForAllViews() {
        tf = Typeface.createFromAsset(getActivity().getAssets(), "RobotoSlab-Regular.ttf");
        tvScheduling.setTypeface(tf);

        tf = Typeface.createFromAsset(getActivity().getAssets(), "RobotoSlab-Regular.ttf");
        tvMadeSimple.setTypeface(tf);

        tf = Typeface.createFromAsset(getActivity().getAssets(), "KievitSlabOT-Medium.otf");
        btnNext.setTypeface(tf);

        tf = Typeface.createFromAsset(getActivity().getAssets(), "HelveticaNeueLight.ttf");
        tvMadeSimpleDesc.setTypeface(tf);

    }
}

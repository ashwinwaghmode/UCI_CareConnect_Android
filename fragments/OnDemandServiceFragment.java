package com.devool.ucicareconnect.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.devool.ucicareconnect.R;

public class OnDemandServiceFragment extends Fragment implements View.OnClickListener{

    Button btnNext;
    TextView tvOnDemand, tvService, tvOnDemandDesc;
    Typeface tf;
    String strUserName;

    public static OnDemandServiceFragment newInstance(String param1, String param2) {
        OnDemandServiceFragment fragment = new OnDemandServiceFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View row = inflater.inflate(R.layout.fragment_on_demand, container, false);
        btnNext = row.findViewById(R.id.btn_next);
        tvOnDemand = row.findViewById(R.id.tv_on_demand);
        tvService = row.findViewById(R.id.tv_service);
        tvOnDemandDesc = row.findViewById(R.id.tv_on_demand_desc);

        btnNext.setOnClickListener(this);

        appyfontForAllViews();
        return row;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_next:
                /*android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                SchedulingMadeSimpleFragment fragment = new SchedulingMadeSimpleFragment();
                fragmentTransaction.replace(R.id.myContainer, fragment);
                fragmentTransaction.commit();
                fragmentTransaction.addToBackStack(null);*/

                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ExclusivelyForYouFragment fragment = new ExclusivelyForYouFragment();
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
        tvOnDemand.setTypeface(tf);

        tf = Typeface.createFromAsset(getActivity().getAssets(), "RobotoSlab-Regular.ttf");
        tvService.setTypeface(tf);

        tf = Typeface.createFromAsset(getActivity().getAssets(), "KievitSlabOT-Medium.otf");
        btnNext.setTypeface(tf);

        tf = Typeface.createFromAsset(getActivity().getAssets(), "HelveticaNeueLight.ttf");
        tvOnDemandDesc.setTypeface(tf);

    }
}

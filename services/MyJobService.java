package com.devool.ucicareconnect.services;


import android.util.Log;


public class MyJobService extends com.firebase.jobdispatcher.JobService {

    private static final String TAG = "MyJobService";

    @Override
    public boolean onStartJob(com.firebase.jobdispatcher.JobParameters jobParameters) {
        Log.d(TAG, "Performing long running task in scheduled job");
        // TODO(developer): add long running task here.
        return false;
    }

    @Override
    public boolean onStopJob(com.firebase.jobdispatcher.JobParameters jobParameters) {
        return false;
    }

}

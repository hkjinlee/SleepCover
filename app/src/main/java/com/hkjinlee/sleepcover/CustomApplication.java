package com.hkjinlee.sleepcover;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by hkjinlee on 15. 6. 28..
 */
public class CustomApplication extends Application {
    public static GoogleAnalytics analytics;
    public static Tracker tracker;
    private static final String UA = "UA-64550997-1";

    @Override
    public void onCreate() {
        super.onCreate();

        analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(1800);

        tracker = analytics.newTracker(UA);
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);
    }
}

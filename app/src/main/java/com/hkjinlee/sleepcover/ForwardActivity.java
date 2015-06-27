package com.hkjinlee.sleepcover;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * An activity that invokes actual Ebook reader.
 * Does not have any visible UI component.
 *
 */
public class ForwardActivity extends Activity implements Constants {
    private static final String TAG = "ForwardActivity";

    /**
     *
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            finish();
        }

        Intent i = getIntent();
        Preferences prefs = Preferences.getInstance(this);

        if (prefs.readChangeLockScreen() && !prefs.isCurrentEbook(i.getData())) {
            prefs.writeCurrentEbook(i.getData());
            changeSleepCover(i);
        } else {
            Log.d(TAG, "Cover change is skipped.");
        }
        launchReaderActivity(i);
    }

    /**
     * Changes lockscreen wallpaper before invoking Ebook reader
     *
     */
    protected void changeSleepCover(Intent launchIntent) {
        Intent serviceIntent = launchIntent.setClass(this, SleepCoverChangeService.class);
        startService(serviceIntent);
    }

    /**
     * launches reader application itself
     *
     */
    private void launchReaderActivity(Intent i) {
        Log.d(TAG, "launchReaderActivity()");
        Preferences prefs = Preferences.getInstance(this);

        AppInfo reader = prefs.readDefaultReader(i.getType());
        if (reader == null) {
            launchReaderChooseActivity(i.getType());
            return;
        }

        i.setComponent(reader.toComponentName());
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        Util.toast(this, "Launching " + reader.getName());
        Log.d(TAG, "Launching intent = " + i.toString());

        startActivity(i);
    }

    /**
     * Used when no ebook reader application is selected as default reader app
     */
    private void launchReaderChooseActivity(String mimeType) {
        Intent i = new Intent(ACTION_CHOOSE_READER).setType(mimeType);
        i.setClass(this, ReaderChooseActivity.class);
        startActivityForResult(i, NOTIFY_READER_CHOICE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult()");

        if (requestCode == NOTIFY_READER_CHOICE) {
            if (resultCode == RESULT_OK) {
                launchReaderActivity(getIntent());
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart()");

        // Needs to finish this activity because the stack comes back when user presses 'back'
        // from actual Ebook reader activity
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }
}

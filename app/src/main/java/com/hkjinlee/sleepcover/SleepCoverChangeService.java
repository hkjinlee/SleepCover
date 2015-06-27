package com.hkjinlee.sleepcover;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;

/**
 * Service that changes sleep cover of the device.
 * Made as a service to do the job in the background and reduce user's waiting time.
 *
 */
public class SleepCoverChangeService extends IntentService {
    private static final String SERVICE_NAME = "SleepCoverChangeService";
    private static final String TAG = SERVICE_NAME;

    /**
     * Constructor
     */
    public SleepCoverChangeService() {
        super(SERVICE_NAME);
    }

    /**
     *
     *
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Loading ebook " + intent.getDataString());
        long timestamp = System.currentTimeMillis();

        String mimeType = intent.getType();
        Uri ebookFileUri = intent.getData();
        try {
            CoverImageExtractor extractor = CoverImageExtractor.getInstance(mimeType);
            CoverImage cover = extractor.extract(ebookFileUri);

            DeviceProfile profile = DeviceProfile.findDevice();
            profile.writeSleepCover(cover);
        } catch (IOException e) {
            Log.d(TAG, "IOException", e);
        } catch (DeviceProfile.NoCompatibleDeviceException e) {
            Log.d(TAG, "NoCompatibleDeviceException", e);
        }

        Log.d(TAG, "The job took " + (System.currentTimeMillis() - timestamp) + " ms.");
    }
}

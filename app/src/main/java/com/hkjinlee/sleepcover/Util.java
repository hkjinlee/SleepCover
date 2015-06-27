package com.hkjinlee.sleepcover;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * Common utility class.
 *
 * Created by hkjinlee on 15. 6. 20..
 */
public class Util implements Constants {
    static final int DEFAULT_BUFFER_SIZE = 2048;

    /**
     * Shows toast message.
     *
     * @param context
     * @param message
     */
    public static void toast(Context context, String message) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean show_log = prefs.getBoolean(PREFS_SHOW_LOG, false);
        if (show_log) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Shows toast message by specifying resource id such as R.string.some_message.
     *
     * @param context
     * @param resourceId
     */
    public static void toast(Context context, int resourceId) {
        String msg = context.getResources().getString(resourceId);
        toast(context, msg);
    }

    /**
     * Copies inputstream to outputstream.
     * This method doesn't close the streams.
     *
     * @param in
     * @param out
     * @return
     * @throws IOException
     */
    public static int copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int n = 0;
        int count = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
     * Calculates HASH fingerprint of first 512 bytes of the stream.
     * Can be used to compare large files.
     *
     * @param in
     * @return
     * @throws IOException
     */
    public static int getInputStreamHash(InputStream in) throws IOException {
        byte[] buffer = new byte[512];
        in.read(buffer);
        return Arrays.hashCode(buffer);
    }
}

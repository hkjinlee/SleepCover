/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hkjinlee.sleepcover;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Handles general preferences by enapsulating SharedPreferences to ensure signature checking.
 * Adopts singleton pattern.
 *
 * Created by hkjinlee on 15. 6. 21..
 */
public class Preferences implements Constants {
    private static final String TAG = "Preferences";

    /**
     * Singleton instance of the class
     */
    static Preferences instance;

    /**
     * Actual SharedPreferences for getting and setting preferences.
     */
    private SharedPreferences prefs;

    /**
     * Initializes singleton instance
     *
     * @param context
     * @return
     */
    public static Preferences getInstance(Context context) {
        if (instance == null) {
            instance = new Preferences(context);
        }
        return instance;
    }

    /**
     * Default constructor used in getInstance().
     * Marked as private to avoid unexpcted access from the outside.
     *
     * @param context
     */
    private Preferences(Context context) {
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Retrieves default application for specific ebook file format
     *
     * @param mimeType
     * @return
     */
    public AppInfo readDefaultReader(String mimeType) {
        String pref_key = getPrefKey(mimeType);
        return AppInfo.fromString(prefs.getString(pref_key, NULL));
    }

    /**
     * Stores default application for specific ebook file format
     *
     * @param mimeType
     * @param reader
     */
    public void writeDefaultReader(String mimeType, AppInfo reader) {
        String pref_key = getPrefKey(mimeType);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(pref_key, reader.toString());
        editor.commit();
    }

    /**
     * Converts MIME type to the appropriate preference key.
     *
     * @param mimeType
     * @return
     */
    private String getPrefKey(String mimeType) {
        switch (mimeType) {
            case MIMETYPE_EPUB:
                return PREFS_DEFAULT_READER_EPUB;
            case MIMETYPE_PDF:
                return PREFS_DEFAULT_READER_PDF;
        }
        return null;
    }

    /**
     * true when the user turns on 'lockscreen wallpaper change' feature.
     *
     * @return
     */
    public boolean readChangeLockScreen() {
        return prefs.getBoolean(PREFS_CHANGE_LOCKSCREEN, true);
    }

    /**
     * Check whether given ebook is the one being displayed on sleep cover.
     *
     * @param ebookUri
     * @return
     */
    public boolean isCurrentEbook(Uri ebookUri) {
        String current = prefs.getString(PREFS_CURRENT_EBOOK_URI, null);
        return current != null && ebookUri.toString().equals(current);
    }

    public void writeCurrentEbook(Uri ebookUri) {
        Log.d(TAG, "Write current ebook as: " + ebookUri.toString());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PREFS_CURRENT_EBOOK_URI, ebookUri.toString());
        editor.commit();
    }
}

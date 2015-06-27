package com.hkjinlee.sleepcover;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.commonsware.cwac.anddown.AndDown;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Main activity for setting preferences such as default ebook reader application.
 *
 */
public class MainActivity extends ActionBarActivity implements Constants {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // uses PreferencesFragment instead
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content,
                        new SettingsFragment())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Fragment that constitutes the UI part.
     *
     */
    public static class SettingsFragment extends PreferenceFragment implements
            SharedPreferences.OnSharedPreferenceChangeListener, Constants {
        private static final String TAG = "SettingsFragment";

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_settings);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            LinearLayout layout = (LinearLayout) super.onCreateView(inflater, container, savedInstanceState);

            Button btn = new Button(getActivity());
            btn.setText(R.string.test_launch_button);

            layout.addView(btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchViewActivityForTest(getEpubUris().get(0));
                }
            });

            return layout;
        }

        /**
         * Registers preference change listener
         */
        @Override
        public void onResume() {
            super.onResume();
            Log.d(TAG, "onResume()");
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
            updatePreferenceSummaries();
        }

        /**
         * Unregisters preference change listener
         */
        @Override
        public void onPause() {
            super.onPause();
            Log.d(TAG, "onPause()");
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        /**
         * Event handler.
         * Modified to show warning dialog when the user touches 'Clear Preferences' or 'About'
         *
         * @param screen
         * @param pref
         * @return true when click event was consumed inside this method
         */
        @Override
        public boolean onPreferenceTreeClick(PreferenceScreen screen, Preference pref) {
            if (PREFS_CLEAR_PREFS.equals(pref.getKey())) {
                showClearPreferenceWarningDialog();
                return true;
            } else if (PREFS_ABOUT.equals(pref.getKey())) {
                showAboutDialog();
                return true;
            }

            return super.onPreferenceTreeClick(screen, pref);
        }

        /**
         * Shows a Clear Preference warning dialog
         */
        private void showClearPreferenceWarningDialog() {
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.title_warning)
                    .setMessage(R.string.message_warning_prefs_clear)
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.clear();
                                    editor.commit();
                                }
                            })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                    .create().show();
        }

        protected List<Uri> getEpubUris() {
            List<Uri> l;
            l = new ArrayList<Uri>();
            Log.d(TAG, "Start adding sample files");
            try {
                String[] files = getActivity().getAssets().list(SAMPLE_EPUB_PATH);
                for (String f : files) {
                    l.add(Uri.parse("content://" + CONTENT_URI_PREFIX + "/" + f));
                    Log.d(TAG, "file: " + f);
                }
            } catch (IOException e) {
                Log.d(getClass().getName(), getActivity().getResources().getString(R.string.error_no_sample_epub));
            }
            Log.d(TAG, "Finish adding sample files");
            return l;
        }

        private void launchViewActivityForTest(Uri uri) {
            Intent i = new Intent(getActivity(), ForwardActivity.class);
            i.setAction(Intent.ACTION_VIEW);
            i.setData(uri);
            i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            Log.d(TAG, i.toString());
            startActivity(i);
        }

        /**
         * Generic method for updating summary shown in the screen.
         * Used by updatePreferenceSummaries() and onSharedPreferenceChanged()
         *
         * @param key
         */
        private void updatePreferenceSummary(String key) {
            Preference pref = findPreference(key);

            switch (key) {
                case PREFS_DEFAULT_READER_EPUB:
                case PREFS_DEFAULT_READER_PDF:
                    AppInfo reader = AppInfo.fromString(pref.getSharedPreferences().getString(key, null));
                    pref.setSummary(reader == null ? getString(R.string.default_reader_summary_none) : reader.getName());
            }
        }

        /**
         *
         */
        private void updatePreferenceSummaries() {
            Log.d(TAG, "updatePreferenceSummaries()");
            updatePreferenceSummary(PREFS_DEFAULT_READER_EPUB);
            updatePreferenceSummary(PREFS_DEFAULT_READER_PDF);
        }

        /**
         * Callback for preference change.
         *
         * @param sharedPreferences
         * @param key
         */
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Log.d(TAG, "onSharedPreferenceChanged()");
            updatePreferenceSummary(key);
        }

        /**
         * Shows about dialog.
         *
         */
        private void showAboutDialog() {
            View dialog_view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_about, null, false);
            TextView textview_credit = (TextView) dialog_view.findViewById(R.id.textview_credit);
            String html = new AndDown().markdownToHtml(getString(R.string.message_credit));
            textview_credit.setText(Html.fromHtml(html));

            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.title_about)
                    .setIcon(R.mipmap.ic_launcher)
                    .setView(dialog_view)
                    .create()
                    .show();
        }
    }
}
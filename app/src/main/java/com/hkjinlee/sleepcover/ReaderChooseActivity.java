/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hkjinlee.sleepcover;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity shown as a small dialog for choosing default app for ebook files.
 * Uses intent's MIME type to determine ebook format handled by selected ebook reader.
 */
public class ReaderChooseActivity extends Activity implements Constants {
    static final String TAG = "ReaderChooseActivity";

    /**
     * ListAdapter for available ebook readers
     */
    ListAdapter readerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader_choose);

        List<AppInfo> readers = loadReaderAppList();
        Log.d(TAG, "readers = " + readers.toString());

        ListView readerListView = (ListView) findViewById(R.id.readerlistView);
        readerListAdapter = new ReaderAdapter(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, readers);
        readerListView.setAdapter(readerListAdapter);
        readerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppInfo reader = (AppInfo) readerListAdapter.getItem(position);
                onDefaultReaderChosen(reader);
            }
        });
    }

    /**
     * Called when an item is selected to specify default reader application.
     * Calls finish() after writing preference to dismiss the activity.
     *
     * @param reader
     */
    private void onDefaultReaderChosen(AppInfo reader) {
        Preferences prefs = Preferences.getInstance(this);
        prefs.writeDefaultReader(getIntent().getType(), reader);
        setResult(RESULT_OK);
        finish();
    }

    /**
     * Loads available ebook reader applications.
     * Uses intent's MIME type to properly load compatible applications to the format.
     *
     * @return
     */
    private List<AppInfo> loadReaderAppList() {
        PackageManager pm = getPackageManager();
        Intent i = new Intent(Intent.ACTION_VIEW).setType(getIntent().getType());

        List<AppInfo> readers = new ArrayList<AppInfo>();
        for (ResolveInfo ri : pm.queryIntentActivities(i, PackageManager.MATCH_DEFAULT_ONLY)) {
            // Excludes self
            if (ri.activityInfo.name.equals(ForwardActivity.class.getCanonicalName())) {
                continue;
            }

            AppInfo reader = new AppInfo(
                    ri.activityInfo.applicationInfo.loadLabel(pm).toString(),
                    ri.activityInfo.applicationInfo.packageName,
                    ri.activityInfo.name,
                    ri.activityInfo.applicationInfo.loadIcon(pm)
            );
            readers.add(reader);
        }

        return readers;
    }
}

/**
 * ListAdapter for displaying ebook reader application list.
 * Shows app icon and app name.
 *
 */
class ReaderAdapter extends ArrayAdapter<AppInfo> {
    /**
     * Used inside getView() for rendering screen properly.
     */
    int textViewResourceId;

    /**
     * Padding value for app icon.
     */
    int padding;

    /**
     * Default constructor
     *
     * @param context
     * @param resource
     * @param textViewResourceId
     * @param readers
     */
    public ReaderAdapter(Context context, int resource, int textViewResourceId, List<AppInfo> readers) {
        super(context, resource, textViewResourceId, readers);
        this.textViewResourceId = textViewResourceId;
        this.padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                12, context.getResources().getDisplayMetrics());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AppInfo reader = getItem(position);
        View view = super.getView(position, convertView, parent);

        TextView textView = (TextView) view.findViewById(textViewResourceId);
        textView.setText(reader.getName());
        textView.setCompoundDrawablesWithIntrinsicBounds(reader.icon, null, null, null);
        textView.setCompoundDrawablePadding(padding);

        return view;
    }
}


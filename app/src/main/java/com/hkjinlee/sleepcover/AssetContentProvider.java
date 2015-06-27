package com.hkjinlee.sleepcover;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AssetContentProvider extends ContentProvider implements Constants {
    private static final String TAG = "AssetContentProvider";

    public AssetContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        return MIMETYPE_EPUB;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public AssetFileDescriptor openAssetFile(Uri uri, String mode) throws FileNotFoundException {
        AssetManager am = getContext().getAssets();
        String file_name = uri.getLastPathSegment();

        if (file_name == null) {
            throw new FileNotFoundException();
        }
        AssetFileDescriptor afd = null;
        try {
            String file_path = new StringBuilder().append(SAMPLE_EPUB_PATH).append(File.separator).append(file_name).toString();
            Log.d(TAG, "asset file = " + file_path);
            File cache_file = copyFileToCache(file_path);
            Log.d(TAG, "cache file = " + cache_file.getPath());
            return new AssetFileDescriptor(ParcelFileDescriptor.open(cache_file, ParcelFileDescriptor.MODE_READ_ONLY), 0, -1);
        } catch (IOException e) {
            Log.d(TAG, "file read error", e);
            Util.toast(getContext(), getContext().getResources().getString(R.string.error_file_not_found));
        }
        return afd;
    }

    private File copyFileToCache(String assetPath) throws IOException {
        final File cache_file = new File(getContext().getCacheDir(), assetPath);
        if (!cache_file.exists()) {
            cache_file.getParentFile().mkdirs();

            InputStream is = null;
            OutputStream os = null;
            try {
                is = getContext().getAssets().open(assetPath, AssetManager.ACCESS_BUFFER);
                os = new FileOutputStream(cache_file, false);
                Util.copyStream(is, os);
            } finally {
                is.close();
                os.close();
            }
        }

        return cache_file;
    }
}

package com.hkjinlee.sleepcover;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Class holding actual cover image data.
 * Offers method to export cover image to JPG file.
 *
 * Created by hkjinlee on 15. 6. 21..
 */
public class CoverImage implements Constants {
    private static final String TAG = "CoverImage";

    /**
     * Image data
     */
    private byte[] data;
    /**
     * Image format extension
     */
    private String extension;

    public CoverImage(final byte[] data, String extension) {
        this.data = data;
        this.extension = extension;
    }

    public Bitmap getTargetBitmap(int width, int height, boolean rotate) throws IOException {
        Bitmap src = BitmapFactory.decodeByteArray(data, 0, data.length);
        Bitmap resized = Bitmap.createScaledBitmap(src, width, height, true);

        if (!rotate) {
            return resized;
        } else {
            Matrix matrix = new Matrix();
            matrix.postRotate(-90);
            return Bitmap.createBitmap(resized, 0, 0, width, height, matrix, true);
        }
    }
}

package com.hkjinlee.sleepcover;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Encapsulates compatible device profile.
 *
 * Created by hkjinlee on 15. 6. 27.
 */
abstract public class DeviceProfile implements Constants {
    abstract public boolean isCompatible();
    abstract public void writeSleepCover(CoverImage cover) throws IOException;

    /**
     * Find the first device profile which is compatible with this application.
     *
     * @return Profile of running device
     * @throws com.hkjinlee.sleepcover.DeviceProfile.NoCompatibleDeviceException
     */
    public static DeviceProfile findDevice() throws NoCompatibleDeviceException {
        for (DeviceProfile dp : profiles) {
            if (dp.isCompatible()) {
                return dp;
            }
        }
        throw new NoCompatibleDeviceException("no supported device");
    }

    /**
     * Device profile for Boyue T62+
     *
     */
    static DeviceProfile T62PLUS = new DeviceProfile() {
        private static final String PRODUCT = "T62D";
        private static final String TARGET = "/data/misc/standby.png";

        @Override
        public boolean isCompatible() {
            return Build.PRODUCT.equals(PRODUCT);
        }

        @Override
        public void writeSleepCover(CoverImage cover) throws IOException {
            Bitmap resized = cover.getTargetBitmap(758, 1024, true);

            FileOutputStream out = new FileOutputStream(TARGET);
            resized.compress(Bitmap.CompressFormat.PNG, 0, out);
            out.close();
        }
    };

    /**
     * Device profile for Tolino devices
     *
     */
    static DeviceProfile TOLINO = new DeviceProfile() {
        private static final String PRODUCT = "Tolino";
        private static final String TARGET = "/sdcard/suspend.jpg";

        @Override
        public boolean isCompatible() {
            return Build.PRODUCT.equals(PRODUCT);
        }

        @Override
        public void writeSleepCover(CoverImage cover) throws IOException {
            Bitmap resized = cover.getTargetBitmap(758, 1024, true);

            FileOutputStream out = new FileOutputStream(TARGET);
            resized.compress(Bitmap.CompressFormat.PNG, 0, out);
            out.close();
        }
    };

    private static DeviceProfile[] profiles = { T62PLUS, TOLINO };

    /**
     * An exception thrown when running device is not one of compatible devices.
     *
     */
    public static class NoCompatibleDeviceException extends Exception {
        public NoCompatibleDeviceException(String msg) {
            super(msg);
        }
    }
}

/*
 * Copyright (c) 2015. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package com.hkjinlee.sleepcover;

import android.content.ComponentName;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;

/**
 * A class that holds informations on eBook reader applications.
 *
 * Created by hkjinlee on 15. 6. 21..
 */
class AppInfo implements Constants {
    private String name;
    private String packageName;
    private String className;
    Drawable icon;
    static final String DELIMITER = ";";

    public AppInfo(String name, String packageName, String className, @Nullable Drawable icon) {
        this.name = name;
        this.packageName = packageName;
        this.className = className;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    /**
     * Needed to build eBook reader launch intent
     *
     * @return ComponentName of this application
     */
    public ComponentName toComponentName() {
        return new ComponentName(packageName, className);
    }

    /**
     * Serialize into String.
     * Needed to put the value inside SharedPreferences.
     *
     * Rule: [Application name];[Packagename];[Classname of Activity]
     *
     * @return Serialized string
     */
    public String toString() {
        return new StringBuilder()
                .append(name).append(DELIMITER)
                .append(packageName).append(DELIMITER)
                .append(className)
                .toString();
    }

    /**
     * Unserialize from String
     *
     * @param str
     * @return
     */
    public static AppInfo fromString(String str) {
        try {
            String[] tokens = str.split(DELIMITER);
            return new AppInfo(tokens[0], tokens[1], tokens[2], null);
        } catch (Exception e) {
            return null;
        }
    }
}

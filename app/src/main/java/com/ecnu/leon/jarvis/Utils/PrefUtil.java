package com.ecnu.leon.jarvis.Utils;

/**
 * Created by lugan on 08/08/2017.
 */

public class PrefUtil {

    private static final String TAG = "PrefUtil";

    public static String ControlPrefix = "control_";
    public static final String CONTROL_ENABLE = "YES";
    public static final String CONTROL_DISABLE = "NO";

    public static int getKeyInt(final String key, final int defaultVal) {
        return PrefUtilImpl.getKeyInt(key, defaultVal);
    }

    public static int getKeyIntRes(final String key, final int defaultValRes) {
        return PrefUtilImpl.getKeyIntRes(key, defaultValRes);
    }

    public static boolean getKeyBoolean(final String key, final boolean defaultVal) {
        return PrefUtilImpl.getKeyBoolean(key, defaultVal);
    }

    public static boolean getKeyBooleanRes(final String key, final int defaultValRes) {
        return PrefUtilImpl.getKeyBooleanRes(key, defaultValRes);
    }

    public static String getKeyString(final String key, final String defaultVal) {
        return PrefUtilImpl.getKeyString(key, defaultVal);
    }

    public static String getKeyStringRes(final String key, final int defaultValRes) {
        return PrefUtilImpl.getKeyStringRes(key, defaultValRes);
    }

    public static long getKeyLong(final String key, final long defaultVal) {
        return PrefUtilImpl.getKeyLong(key, defaultVal);
    }

    public static float getKeyFloat(final String key, final float defaultVal) {
        return PrefUtilImpl.getKeyFloat(key, defaultVal);
    }

    public static void setKey(final String key, final int value) {
        PrefUtilImpl.setKey(key, value);
    }

    public static void setKey(final String key, final boolean value) {
        PrefUtilImpl.setKey(key, value);
    }

    public static void setKey(final String key, final String value) {
        PrefUtilImpl.setKey(key, value);
    }

    public static void setKey(final String key, final long value) {
        PrefUtilImpl.setKey(key, value);
    }

    public static void setKey(final String key, final float value) {
        PrefUtilImpl.setKey(key, value);
    }

    public static void deleteKey(final String key) {
        PrefUtilImpl.deleteKey(key);
    }

    public static boolean containsKey(String key) {
        return PrefUtilImpl.containsKey(key);
    }

    public static int getNormalPhoneAdValue() {
        return 1;
    }
}

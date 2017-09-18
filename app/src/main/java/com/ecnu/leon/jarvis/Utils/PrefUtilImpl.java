package com.ecnu.leon.jarvis.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;

import com.ecnu.leon.jarvis.JarvisApplication;

/**
 * PrefUtil is used as a setting saver. All settings should save here.
 */
@SuppressLint("CommitPrefEdits")
public class PrefUtilImpl {

    private static final String TAG = "PrefUtil";

    private static final String QUERY_EXIST_SELECTION = PreferenceProvider.QUERY_TYPE + "=? AND " + PreferenceProvider.KEY + "=?";
    private static final String QUERY_VALUE_SELECTION = PreferenceProvider.QUERY_TYPE + "=? AND " + PreferenceProvider.KEY + "=? AND "
            + PreferenceProvider.TYPE + "=? AND " + PreferenceProvider.DEFAULT + "=?";

    private static final long INSERT_MIN_INTERVAL_NANOS = 1000 * 1000 * 1000; // 1s

    private static long sLastCheckTime;
    private static ContentValues sPendingValues = new ContentValues();
    private static final Object sDeletedValue = new Object();

    private static boolean shouldInsertNow() {
        return System.nanoTime() - sLastCheckTime >= INSERT_MIN_INTERVAL_NANOS;
    }

    public static void insertPendingValues() {
        if (sPendingValues.size() == 0 || !shouldInsertNow()) {
            return;
        }
        sLastCheckTime = System.nanoTime();

        BackgroundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                if (sPendingValues.size() > 0) {
                    doInsertPendingValues();
                }
            }
        }, BackgroundExecutor.ThreadType.IO);
    }

    private static void doInsertPendingValues() {
        ContentValues valuesToCommit;
        synchronized (sPendingValues) {
            valuesToCommit = new ContentValues(sPendingValues);
            sPendingValues.clear();
        }

        try {
            JarvisApplication.getContext().getContentResolver().insert(PreferenceProvider.BASE_URI, valuesToCommit);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        JarvisApplication.getContext().getSharedPreferences(JarvisApplication.getContext().getPackageName() + "_preferences", 0).registerOnSharedPreferenceChangeListener(listener);
    }

    public static void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        JarvisApplication.getContext().getSharedPreferences(JarvisApplication.getContext().getPackageName() + "_preferences", 0).unregisterOnSharedPreferenceChangeListener(listener);
    }

    public static int getKeyInt(final String key, final int defaultVal) {
        if (sPendingValues.size() > 0) {
            if (sPendingValues.containsKey(key)) {
                Object v = sPendingValues.get(key);
                if (v instanceof Integer) {
                    return (int) v;
                } else {
                    return defaultVal;
                }
            }
        }

        int value = defaultVal;
        try {
            String[] selectionArgs = new String[]{PreferenceProvider.QUERY_TYPE_VALUE, key, PreferenceProvider.INT_TYPE, String.valueOf(defaultVal)};
            Cursor cursor = JarvisApplication.getContext().getContentResolver().query(PreferenceProvider.BASE_URI, null, QUERY_VALUE_SELECTION, selectionArgs, null);
            if (cursor == null)
                return defaultVal;

            if (cursor.moveToFirst()) {
                value = cursor.getInt(0);
            }

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    public static int getKeyIntRes(final String key, final int defaultValRes) {
        return getKeyInt(key,
                JarvisApplication.getContext().getResources().getInteger(defaultValRes));
    }

    public static boolean getKeyBoolean(final String key,
                                        final boolean defaultVal) {
        if (sPendingValues.size() > 0) {
            if (sPendingValues.containsKey(key)) {
                Object v = sPendingValues.get(key);
                if (v instanceof Boolean) {
                    return (boolean) v;
                } else {
                    return defaultVal;
                }
            }
        }

        String[] selectionArgs = new String[]{PreferenceProvider.QUERY_TYPE_VALUE, key, PreferenceProvider.BOOLEAN_TYPE, String.valueOf(defaultVal)};
        boolean value = defaultVal;
        try {
            Cursor cursor = JarvisApplication.getContext().getContentResolver().query(PreferenceProvider.BASE_URI, null, QUERY_VALUE_SELECTION, selectionArgs, null);
            if (cursor == null)
                return defaultVal;
            if (cursor.moveToFirst()) {
                value = cursor.getInt(0) > 0;
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static boolean getKeyBooleanRes(final String key,
                                           final int defaultValRes) {
        return getKeyBoolean(key,
                JarvisApplication.getContext().getResources().getBoolean(defaultValRes));
    }

    public static String getKeyString(final String key, final String defaultVal) {
        if (sPendingValues.size() > 0) {
            if (sPendingValues.containsKey(key)) {
                Object v = sPendingValues.get(key);
                if (v instanceof String) {
                    return (String) v;
                } else {
                    return defaultVal;
                }
            }
        }

        String value = defaultVal;
        try {
            String[] selectionArgs = new String[]{PreferenceProvider.QUERY_TYPE_VALUE, key, PreferenceProvider.STRING_TYPE, defaultVal};
            Cursor cursor = JarvisApplication.getContext().getContentResolver().query(PreferenceProvider.BASE_URI, null, QUERY_VALUE_SELECTION, selectionArgs, null);
            if (cursor == null)
                return defaultVal;

            if (cursor.moveToFirst()) {
                value = cursor.getString(0);
            }

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }


    public static String getKeyStringRes(final String key,
                                         final int defaultValRes) {
        return getKeyString(key,
                JarvisApplication.getContext().getResources().getString(defaultValRes));
    }

    public static long getKeyLong(final String key, final long defaultVal) {
        if (sPendingValues.size() > 0) {
            if (sPendingValues.containsKey(key)) {
                Object v = sPendingValues.get(key);
                if (v instanceof Long) {
                    return (long) v;
                } else {
                    return defaultVal;
                }
            }
        }

        long value = defaultVal;
        try {
            String[] selectionArgs = new String[]{PreferenceProvider.QUERY_TYPE_VALUE, key, PreferenceProvider.LONG_TYPE, String.valueOf(defaultVal)};
            Cursor cursor = JarvisApplication.getContext().getContentResolver().query(PreferenceProvider.BASE_URI, null, QUERY_VALUE_SELECTION, selectionArgs, null);
            if (cursor == null)
                return defaultVal;

            if (cursor.moveToFirst()) {
                value = cursor.getLong(0);
            }

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static float getKeyFloat(final String key, final float defaultVal) {
        if (sPendingValues.size() > 0) {
            if (sPendingValues.containsKey(key)) {
                Object v = sPendingValues.get(key);
                if (v instanceof Float) {
                    return (float) v;
                } else {
                    return defaultVal;
                }
            }
        }

        float value = defaultVal;
        try {
            String[] selectionArgs = new String[]{PreferenceProvider.QUERY_TYPE_VALUE, key, PreferenceProvider.FLOAT_TYPE, String.valueOf(defaultVal)};
            Cursor cursor = JarvisApplication.getContext().getContentResolver().query(PreferenceProvider.BASE_URI, null, QUERY_VALUE_SELECTION, selectionArgs, null);
            if (cursor == null)
                return defaultVal;

            if (cursor.moveToFirst()) {
                value = cursor.getFloat(0);
            }

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }

    public static void setKey(final String key, final int value) {
        synchronized (sPendingValues) {
            sPendingValues.put(key, value);
        }

        insertPendingValues();
    }

    public static void setKey(final String key, final boolean value) {
        synchronized (sPendingValues) {
            sPendingValues.put(key, value);
        }

        insertPendingValues();
    }

    public static void setKey(final String key, final String value) {
        synchronized (sPendingValues) {
            sPendingValues.put(key, value);
        }

        insertPendingValues();
    }

    public static void setKey(final String key, final long value) {
        synchronized (sPendingValues) {
            sPendingValues.put(key, value);
        }

        insertPendingValues();
    }

    public static void setKey(final String key, final float value) {
        synchronized (sPendingValues) {
            sPendingValues.put(key, value);
        }

        insertPendingValues();
    }

    public static void deleteKey(final String key) {
        synchronized (sPendingValues) {
            sPendingValues.putNull(key);
        }

        insertPendingValues();
    }

    public static boolean containsKey(String key) {
        if (sPendingValues.size() > 0) {
            if (sPendingValues.containsKey(key)) {
                return sPendingValues.get(key) != null;
            }
        }

        boolean result = false;
        try {
            String[] selectionArgs = new String[]{PreferenceProvider.QUERY_TYPE_EXIST, key};
            Cursor cursor = JarvisApplication.getContext().getContentResolver().query(PreferenceProvider.BASE_URI, null, QUERY_EXIST_SELECTION, selectionArgs, null);

            if (cursor != null && cursor.getCount() > 0) {
                result = true;
            }

            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
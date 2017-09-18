package com.ecnu.leon.jarvis.Utils;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import java.util.Map;

/**
 * Created by hengfengtian on 8/28/15.
 */

public class PreferenceProvider extends ContentProvider {

    public static final String PREFERENCE_AUTHORITY = "com.cootek.smartdialer.model.provider.PreferenceProvider";
    public static Uri BASE_URI = Uri.parse("content://" + PREFERENCE_AUTHORITY);
    public static final String PREFERENCE_FILE_POSTFIX = "_preferences";


    public static final String QUERY_TYPE = "query_type";
    public static final String KEY = "key";
    public static final String TYPE = "type";
    public static final String DEFAULT = "default";

    public static final String QUERY_TYPE_VALUE = "value";
    public static final String QUERY_TYPE_EXIST = "exist";

    public static final String INT_TYPE = "integer";
    public static final String LONG_TYPE = "long";
    public static final String FLOAT_TYPE = "float";
    public static final String BOOLEAN_TYPE = "boolean";
    public static final String STRING_TYPE = "string";

    private SharedPreferences mSharedPreferences;

    private void init(Context context){
        mSharedPreferences = context.getSharedPreferences(context.getPackageName() + PREFERENCE_FILE_POSTFIX, 0);
    }

    @Override
    public boolean onCreate() {
        init(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd." + PREFERENCE_AUTHORITY + ".item";
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        mSharedPreferences
                .edit().clear().apply();
        return 0;
    }

    @SuppressLint("NewApi")
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        synchronized (PreferenceProvider.class) {
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            for (Map.Entry<String, Object> entry : values.valueSet()) {
                final Object value = entry.getValue();
                final String key = entry.getKey();
                if (value == null) {
                    editor.remove(key);
                } else if (value instanceof String) {
                    editor.putString(key, (String) value);
                } else if (value instanceof Boolean) {
                    editor.putBoolean(key, (Boolean) value);
                } else if (value instanceof Long) {
                    editor.putLong(key, (Long) value);
                } else if (value instanceof Integer) {
                    editor.putInt(key, (Integer) value);
                } else if (value instanceof Float) {
                    editor.putFloat(key, (Float) value);
                } else {
                    throw new IllegalArgumentException("Unsupported type " + uri);
                }
            }
            editor.apply();
            return null;
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        MatrixCursor cursor = null;
        String queryType = selectionArgs[0];
        if(QUERY_TYPE_EXIST.equals(queryType)){
            final String queryKey = selectionArgs[1];
            if (!mSharedPreferences.contains(queryKey)) {
                return cursor;
            }
            cursor = new MatrixCursor(new String[]{queryKey});
            MatrixCursor.RowBuilder existBuilder = cursor.newRow();
            existBuilder.add(new Object());
        } else if(QUERY_TYPE_VALUE.equals(queryType)) {
            final String key = selectionArgs[1];
            final String type = selectionArgs[2];
            final String defaultValue = selectionArgs[3];
            cursor = new MatrixCursor(new String[]{key});
            if (!mSharedPreferences.contains(key)) {
                return cursor;
            }
            MatrixCursor.RowBuilder rowBuilder = cursor.newRow();
            Object object = null;
            if (STRING_TYPE.equals(type)) {
                String keyValue = mSharedPreferences.getString(key, null);
                if (keyValue == null) {
                    keyValue = defaultValue;
                }
                object = keyValue;
            } else if (BOOLEAN_TYPE.equals(type)) {
                boolean value = Boolean.parseBoolean(defaultValue);
                object = mSharedPreferences.getBoolean(key, value) ? 1:0;
            } else if (LONG_TYPE.equals(type)) {
                long value = Long.parseLong(defaultValue);
                object = mSharedPreferences.getLong(key, value);
            } else if (INT_TYPE.equals(type)) {
                int value = Integer.parseInt(defaultValue);
                object = mSharedPreferences.getInt(key, value);
            } else if (FLOAT_TYPE.equals(type)) {
                float value = Float.parseFloat(defaultValue);
                object = mSharedPreferences.getFloat(key, value);
            } else {
                throw new IllegalArgumentException("Unsupported type " + type);
            }
            rowBuilder.add(object);
        } else {
            throw new IllegalArgumentException("Unsupported query type " + queryType);
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }
}
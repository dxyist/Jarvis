package com.ecnu.leon.jarvis.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;

import com.ecnu.leon.jarvis.tasks.item.DailyTask;

import java.util.Date;

/**
 * Created by Leon on 2017/8/10.
 * 数据库
 */

public class TaskDatabaseHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "Jarvis.db";
    public static final String TABLE_NAME = "Tasks";


    public TaskDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    public TaskDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table if not exists " + TABLE_NAME + " ( _id integer primary key, content text, WhiteDate date, taskValue integer, taskState integer)";
        db.execSQL(sql);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addTask(DailyTask dailyTask) {

        if (dailyTask != null) {
            String content = dailyTask.getContent();
            Date date = dailyTask.getWhiteDate();
            int taskValue = dailyTask.getTaskValue();
            int taskState = dailyTask.getTaskState();

            SQLiteDatabase db = this.getWritableDatabase(); // 取得数据库操作实例
            ContentValues values = new ContentValues();
            values.put("Content", dailyTask.getContent());
            values.put("WhiteDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dailyTask.getWhiteDate()));
            values.put("taskValue", dailyTask.getTaskValue());
            values.put("taskState", dailyTask.getTaskState());

            db.insert(TABLE_NAME, "Content", values);
            db.close();

        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }
}

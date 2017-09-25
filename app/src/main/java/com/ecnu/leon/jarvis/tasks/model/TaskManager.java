package com.ecnu.leon.jarvis.tasks.model;

import android.content.Context;
import android.widget.Toast;

import com.ecnu.leon.jarvis.JarvisApplication;
import com.ecnu.leon.jarvis.Utils.PrefKeys;
import com.ecnu.leon.jarvis.Utils.PrefUtil;
import com.ecnu.leon.jarvis.Utils.PrefUtils;
import com.ecnu.leon.jarvis.tasks.item.DailyTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Leon on 2017/7/31.
 */

public class TaskManager {

    private Boolean isLoadSuccess;

    private static TaskManager mTaskManager;

    public static final String TASK_TYPE_DAILY = "task_type_daily";

    private DailyTaskContainer dailyTaskContainer;

    private Context context;

    private TaskManager(Context context) {
        this.context = context;

        this.isLoadSuccess = true;
        dailyTaskContainer = new DailyTaskContainer(context);
    }

    public static TaskManager getInstance(Context context) {


        if (mTaskManager == null) {
            mTaskManager = new TaskManager(context.getApplicationContext());
        }
        return mTaskManager;
    }

    public void addNewDailyTask(String taskContent, int taskValue) {

        DailyTask task = new DailyTask(getNewTaskID(), taskContent, new Date(), taskValue);

        dailyTaskContainer.addDailyTask(task);
    }

    public ArrayList<DailyTask> getDailyTasks(Date currentDate) {
        return this.dailyTaskContainer.getDailyTaskList(currentDate);
    }

    public static int getNewTaskID() {
        int id = (int) PrefUtils.getKey(PrefKeys.TASK_ID, 0);
        PrefUtils.setKey(PrefKeys.TASK_ID, id + 1);
        return id;
    }


    public void saveContent() {

        if (isLoadSuccess) {
            if (dailyTaskContainer != null) {
                try {
                    dailyTaskContainer.save();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "数据存储失败！！！！！", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(context, "当前数据不可写", Toast.LENGTH_SHORT).show();

        }

    }

    public void loadContent() {
        if (dailyTaskContainer != null) {
            try {
                dailyTaskContainer.load();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                if (!(Boolean) PrefUtils.getKey(PrefKeys.DAILY_TASK_FIRST_TIME_LOAD, true)) {
                    isLoadSuccess = false;
                    Toast.makeText(context, "数据读取失败！！！！！", Toast.LENGTH_SHORT).show();
                } else {
                    PrefUtils.setKey(PrefKeys.DAILY_TASK_FIRST_TIME_LOAD, false);
                }

            }
        }
    }

}

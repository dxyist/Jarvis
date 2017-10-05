package com.ecnu.leon.jarvis.model.task.dailytask;

import android.content.Context;
import android.widget.Toast;

import com.ecnu.leon.jarvis.Utils.PrefKeys;
import com.ecnu.leon.jarvis.Utils.PrefUtils;
import com.ecnu.leon.jarvis.model.task.routinetask.RoutineTask;
import com.ecnu.leon.jarvis.model.task.routinetask.RoutineTaskContainer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Leon on 2017/7/31.
 */

public class TaskManager {

    private int initialValue = 10;

    // 存储当前全局时间
    public static Date currentTaskCalendar;
    private Boolean isLoadSuccess;

    private static TaskManager mTaskManager;

    public static final String TASK_TYPE_DAILY = "task_type_daily";

    private DailyTaskContainer dailyTaskContainer;
    private RoutineTaskContainer routineTaskContainer;

    private Context context;

    private TaskManager(Context context) {
        this.context = context;

        currentTaskCalendar = new Date();

        this.isLoadSuccess = true;
        dailyTaskContainer = new DailyTaskContainer(context);
        routineTaskContainer = new RoutineTaskContainer(context);
    }

    public static TaskManager getInstance(Context context) {


        if (mTaskManager == null) {
            mTaskManager = new TaskManager(context.getApplicationContext());
        }
        return mTaskManager;
    }

    public void addNewDailyTask(String taskContent, int taskValue) {
        // 取克隆日期
        DailyTask task = new DailyTask(getNewTaskID(), taskContent, (Date) TaskManager.currentTaskCalendar.clone(), taskValue);

        dailyTaskContainer.addDailyTask(task);
    }

    public void addNewRoutineTask(String taskContent, int taskValue, Boolean[] weeks) {
        RoutineTask task = new RoutineTask(getNewTaskID(), taskContent, taskValue, (Date) TaskManager.currentTaskCalendar.clone(), weeks);
        routineTaskContainer.addRoutineTask(task);
    }

    public ArrayList<DailyTask> getDailyTasks(Date currentDate) {
        return this.dailyTaskContainer.getDailyTaskList(currentDate);
    }

    public ArrayList<RoutineTask> getRoutineTasks(Date currentDate) {
        return this.routineTaskContainer.getRoutineTaskList(currentDate);
    }

    public static int getNewTaskID() {
        int id = (int) PrefUtils.getKey(PrefKeys.TASK_ID, 0);
        PrefUtils.setKey(PrefKeys.TASK_ID, id + 1);
        return id;
    }

    public int getOneDayValue(Date date) {
        int value = 0;

        value += dailyTaskContainer.getOneDayResultValue(date);
//        value += routineTaskContainer.get(date);

        return value;
    }

    public int getTotalValue() {
        int value = 0;

//        value += dailyTaskContainer.getTotalValue();
//        value += routineTaskContainer.get(date);

        return value;
    }


    public void saveContent() {
        if (isLoadSuccess) {
            // daily save
            if (dailyTaskContainer != null) {
                try {
                    dailyTaskContainer.save();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "数据存储失败！！！！！", Toast.LENGTH_SHORT).show();
                }
            }
            // routine save
            if (routineTaskContainer != null) {
                try {
                    routineTaskContainer.save();
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

        if (routineTaskContainer != null) {
            try {
                routineTaskContainer.load();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                if (!(Boolean) PrefUtils.getKey(PrefKeys.ROUTINE_TASK_FIRST_TIME_LOAD, true)) {
                    isLoadSuccess = false;
                    Toast.makeText(context, "数据读取失败！！！！！", Toast.LENGTH_SHORT).show();
                } else {
                    PrefUtils.setKey(PrefKeys.ROUTINE_TASK_FIRST_TIME_LOAD, false);
                }

            }
        }
    }

}

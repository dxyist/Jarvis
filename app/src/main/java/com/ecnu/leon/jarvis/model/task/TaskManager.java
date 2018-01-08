package com.ecnu.leon.jarvis.model.task;

import android.content.Context;
import android.widget.Toast;

import com.ecnu.leon.jarvis.Utils.PrefKeys;
import com.ecnu.leon.jarvis.Utils.PrefUtils;
import com.ecnu.leon.jarvis.model.task.consumable.Consumable;
import com.ecnu.leon.jarvis.model.task.consumable.ConsumableContainer;
import com.ecnu.leon.jarvis.model.task.dailytask.DailyTask;
import com.ecnu.leon.jarvis.model.task.dailytask.DailyTaskContainer;
import com.ecnu.leon.jarvis.model.task.routinetask.RoutineTask;
import com.ecnu.leon.jarvis.model.task.routinetask.RoutineTaskContainer;
import com.ecnu.leon.jarvis.model.task.targertask.TargetTask;
import com.ecnu.leon.jarvis.model.task.targertask.TargetTaskContainer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Leon on 2017/7/31.
 */

public class TaskManager {
    private static boolean isDataChanged = false;
    private int initialValue = 20;

    // 存储当前全局时间
    public static Date currentTaskCalendar = new Date();
    private Boolean isLoadSuccess;

    private static TaskManager mTaskManager;

    public static final String TASK_TYPE_DAILY = "task_type_daily";

    private DailyTaskContainer dailyTaskContainer;
    private RoutineTaskContainer routineTaskContainer;
    private ConsumableContainer consumableContainer;
    private TargetTaskContainer targetTaskContainer;


    private Context context;

    private int basicValue = 50;

    private int targetTaskTopLimit = 3;

    private TaskManager(Context context) {
        this.context = context;

        // 下面两个顺序不能弄反
        this.isLoadSuccess = true;


        dailyTaskContainer = new DailyTaskContainer(context);

        routineTaskContainer = new RoutineTaskContainer(context);


        consumableContainer = new ConsumableContainer(context);

        loadContent();

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
        Toast.makeText(context, dailyTaskContainer.getOneDayMaximumPossibleValue((Date) TaskManager.currentTaskCalendar.clone()) + "/" + dailyTaskContainer.getMaximumValueForOneDay(), Toast.LENGTH_SHORT).show();

    }

    public void addNewRoutineTask(String taskContent, int taskValue, Boolean[] weeks) {
        RoutineTask task = new RoutineTask(getNewTaskID(), taskContent, taskValue, (Date) TaskManager.currentTaskCalendar.clone(), weeks);

        routineTaskContainer.addRoutineTask(task);
    }

    public void addNewTargetTask(String taskContent, int taskValue, int priority, long deadlineTs) {
        TargetTask task = new TargetTask(getNewTaskID(), taskContent, taskValue, priority, deadlineTs);
        targetTaskContainer.addTargetTask(task);
    }

    public void addNewConsumable(String taskContent, int taskValue) {
        Consumable task = new Consumable(getNewTaskID(), taskContent, taskValue);

        consumableContainer.addConsumable(task);
    }

    public ArrayList<DailyTask> getDailyTasks(Date currentDate) {
        return this.dailyTaskContainer.getDailyTaskList(currentDate);
    }

    public ArrayList<RoutineTask> getRoutineTasks(Date currentDate) {
        return this.routineTaskContainer.getRoutineTaskList(currentDate);
    }

    public ArrayList<Consumable> getConsumables(Date currentDate) {
        return this.consumableContainer.getConsumableArrayList(currentDate);
    }

    public static int getNewTaskID() {
        int id = (int) PrefUtils.getKey(PrefKeys.TASK_ID, 0);
        PrefUtils.setKey(PrefKeys.TASK_ID, id + 1);
        return id;
    }

    public int getIncomingExchangeAmount() {
        return this.consumableContainer.getExchangeExpenses();
    }


    public int getOneDayValue(Date date) {
        int value = 0;

        value += dailyTaskContainer.getOneDayResultValue(date);
        value += routineTaskContainer.getOneDayResultValue(date);
        value -= consumableContainer.getOneDayConsumedValue(date);

        return value;
    }

    public int getTotalValue() {
        int value = 0;

        value += basicValue;
        value += dailyTaskContainer.getTotalValue();
        value += routineTaskContainer.getTotalValue();
        value -= consumableContainer.getTotalConsumedValue();

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

            // routine save
            if (consumableContainer != null) {
                try {
                    consumableContainer.save();
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
        try {
            dailyTaskContainer.load();
            routineTaskContainer.load();
            consumableContainer.load();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            if (!(Boolean) PrefUtils.getKey(PrefKeys.TASK_FIRST_TIME_LOAD, true)) {
                isLoadSuccess = false;
                Toast.makeText(context, "数据读取失败！！！！！", Toast.LENGTH_SHORT).show();
            } else {
                PrefUtils.setKey(PrefKeys.TASK_FIRST_TIME_LOAD, false);
            }

        }

    }

    public int getActiveTargetTaskQuantity() {

        return 0;
    }

    public int getTargetTaskLimit() {
        return targetTaskTopLimit;
    }

    public static boolean isDateChanged() {
        return isDataChanged;
    }

    public static void setDateChanged(boolean b) {
        isDataChanged = b;
    }
}

package com.ecnu.leon.jarvis.model.task.targertask;

import android.content.Context;

import com.ecnu.leon.jarvis.Utils.PrefKeys;
import com.ecnu.leon.jarvis.Utils.PrefUtils;
import com.ecnu.leon.jarvis.model.task.routinetask.RoutineTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Leon on 2017/11/4.
 */

public class TargetTaskContainer {
    private ArrayList<TargetTask> targetTasksArray = new ArrayList<TargetTask>();
    private static final String FILE_NAME = "TargetTask.dat";

    private Context context;

    // 重要性对应额度列表
    public final static int TASK_VALUE_CEILING_TRIVIA = 2;
    public final static int TASK_VALUE_CEILING_NORMAL = 10;
    public final static int TASK_VALUE_CEILING_IMPORTANT = 50;
    public final static int TASK_VALUE_CEILING_VERY_IMPORTANT = 100;

    // 重要性对应数量列表
    public final static int TASK_QUANTITY_CEILING_TRIVIA = 10;
    public final static int TASK_QUANTITY_CEILING_NORMAL = 5;
    public final static int TASK_QUANTITY_CEILING_IMPORTANT = 2;
    public final static int TASK_QUANTITY_CEILING_VERY_IMPORTANT = 1;


    public TargetTaskContainer(Context context) {
        this.context = context;
    }

    public void addTargetTask(TargetTask targetTask) {

        targetTasksArray.add(targetTask);
    }

    public ArrayList<TargetTask> getTargetTaskList() {

        ArrayList<TargetTask> list = new ArrayList<>();

        for (int i = 0; i < targetTasksArray.size(); i++) {
            // 过滤
            if (targetTasksArray.get(i).isFinished() && (Boolean) PrefUtils.getKey(PrefKeys.TARGET_TASK_HIDE_FINISHED, true)) {
                continue;
            }

            if (targetTasksArray.get(i).isRemoved()) {
                continue;
            }
            list.add(targetTasksArray.get(i));
        }
        return list;
    }

    public boolean save() throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE));
        out.writeObject(this.targetTasksArray);
        out.flush();
        out.close();
        return false;
    }

    @SuppressWarnings("unchecked")
    public boolean load() throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(context.openFileInput(FILE_NAME));
        this.targetTasksArray = (ArrayList<TargetTask>) in.readObject();
        in.close();
        return false;
    }


    public int getTotalValue() {
        // 日常任務的增加需要扣除一个月的数量额度
        int value = 0;
        // getRoutineAddConsume


        for (int i = 0; i < this.targetTasksArray.size(); i++) {
            value += targetTasksArray.get(i).getResultValue();
        }

        return value;
    }

    public static int getValueCeilingByPriority(int priority) {
        switch (priority) {
            case TargetTask.TASK_PRIORITY_TRIVIA: {
                return TASK_VALUE_CEILING_TRIVIA;
            }
            case TargetTask.TASK_PRIORITY_NORMAL: {
                return TASK_VALUE_CEILING_NORMAL;
            }
            case TargetTask.TASK_PRIORITY_IMPORTANT: {
                return TASK_VALUE_CEILING_IMPORTANT;
            }
            case TargetTask.TASK_PRIORITY_VERY_IMPORTANT: {
                return TASK_VALUE_CEILING_VERY_IMPORTANT;
            }
        }
        return 0;
    }

    public static int getQuantityCeilingByPriority(int priority) {
        switch (priority) {
            case TargetTask.TASK_PRIORITY_TRIVIA: {
                return TASK_QUANTITY_CEILING_TRIVIA;
            }
            case TargetTask.TASK_PRIORITY_NORMAL: {
                return TASK_QUANTITY_CEILING_NORMAL;
            }
            case TargetTask.TASK_PRIORITY_IMPORTANT: {
                return TASK_QUANTITY_CEILING_IMPORTANT;
            }
            case TargetTask.TASK_PRIORITY_VERY_IMPORTANT: {
                return TASK_QUANTITY_CEILING_VERY_IMPORTANT;
            }
        }
        return 0;
    }

    public int getCurrentTaskQuantityByPriority(int priority) {
        int quantity = 0;

        for (int i = 0; i < this.targetTasksArray.size(); i++) {
            if (this.targetTasksArray.get(i).isUnFinished() && this.targetTasksArray.get(i).getPriority() == priority) {
                quantity++;
            }
        }

        return quantity;
    }
}

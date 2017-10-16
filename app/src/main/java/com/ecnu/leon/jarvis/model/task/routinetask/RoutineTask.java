package com.ecnu.leon.jarvis.model.task.routinetask;

import android.widget.Toast;

import com.ecnu.leon.jarvis.model.task.Task;
import com.ecnu.leon.jarvis.model.task.dailytask.DailyTask;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leon on 2017/9/25.
 */

public class RoutineTask extends Task implements Serializable {

    private static final long serialVersionUID = 7523967370034938905L;
    public static int TASK_STATE_UNFINISHED = 0;
    public static int TASK_STATE_FINISHED = 1;
    // 表示任务失败
    public static int TASK_STATE_FAILED = -1;

    //数据域
    private String content;
    private int taskValue;

    private Boolean[] daysOfWeek = {};

    // 存储任务状态
    private HashMap<String, Integer> taskStatuses = new HashMap<>();


    /**
     * 必须传入一个名称和任务ID
     *
     * @param taskID
     * @param createCalendar
     */
    public RoutineTask(int taskID, String content, int value, Date createCalendar, Boolean[] daysOfWeek) {
        super(taskID, createCalendar);
        this.content = content;
        this.taskValue = value;
        this.daysOfWeek = daysOfWeek.clone();

    }


    @Override
    protected boolean setTaskType() {
        super.taskType = Task.ROUTINE_TASK_TYPE;
        return true;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTaskValue() {
        return taskValue;
    }

    public void setTaskValue(int taskValue) {
        this.taskValue = taskValue;
    }

    public boolean isFinished(Date currentDate) {

        String key = "";
        if (currentDate != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            key = format.format(currentDate);
        }

        if (taskStatuses.containsKey(key) && taskStatuses.get(key) == 1) {
            return true;
        } else {
            return false;
        }

    }

    public void setFinished(Date currentDate) {
        String key = "";
        if (currentDate != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            key = format.format(currentDate);
        }

        taskStatuses.put(key, 1);
    }

    public void setUnfinished(Date currentDate) {
        String key = "";
        if (currentDate != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            key = format.format(currentDate);
        }

        taskStatuses.put(key, 0);
    }

    public Boolean[] getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(Boolean[] daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    public int getOneDayValue(Date date) {
        int value = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(date);

        int taskStatus = 0;
        if (taskStatuses.containsKey(dateString)) {
            taskStatus = taskStatuses.get(dateString);
        }

        if (taskStatus == TASK_STATE_FINISHED) {
            return getTaskValue();
        }
        return value;
    }

    public int getTotalValue() {
        int totalValue = 0;
        for (Map.Entry<String, Integer> entry : taskStatuses.entrySet()) {
            int taskStatus = entry.getValue();

            if (taskStatus == TASK_STATE_FINISHED) {
                totalValue += getTaskValue();
            }
        }

        return totalValue;
    }
}

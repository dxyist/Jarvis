package com.ecnu.leon.jarvis.tasks.item;

import android.content.Intent;

import java.io.Serializable;
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

    private int[] daysOfWeek = {};

    // 存储任务状态
    private HashMap<String,Integer> taskStatuses;

    /**
     * 必须传入一个名称和任务ID
     *
     * @param taskID
     * @param createCalendar
     */
    public RoutineTask(int taskID, Date createCalendar,int[] daysOfWeek) {
        super(taskID, createCalendar);
        this.daysOfWeek = daysOfWeek.clone();
        this.taskStatuses = new HashMap<>();

    }


    @Override
    protected boolean setTaskType() {
        super.taskType = Task.ROUTINE_TASK_TYPE;
        return true;
    }
}

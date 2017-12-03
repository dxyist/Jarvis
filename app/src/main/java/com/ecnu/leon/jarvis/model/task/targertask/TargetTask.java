package com.ecnu.leon.jarvis.model.task.targertask;

import com.ecnu.leon.jarvis.model.task.Task;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Leon on 2017/10/17.
 */

public class TargetTask extends Task implements Serializable {

    private static final long serialVersionUID = 7523967370034438905L;


    public static int TASK_STATE_UNFINISHED = 0;
    public static int TASK_STATE_FINISHED = 1;
    // 表示任务失败
    public static int TASK_STATE_FAILED = -1;

    public static int TASK_PRIORITY_NORMAL = 1;
    public static int TASK_PRIORITY_IMPORTANT = 2;
    public static int TASK_PRIORITY_VERY_IMPORTANT = 3;


    private String content;
    // 任务截止日期(为空表示没有截止日期）
    private Date deadlineDate = null;

    private String whiteDateFormatString;

    private int priority;

    // 任务状态
    private int taskState;


    // 任务价值
    private int taskValue;


    // 任务奖励率（或者表示为任务重要程度，普通2倍，重要3倍，极其重要4倍）
    private int taskBonusRate = 2;

    /**
     * 必须传入一个名称和任务ID
     *
     * @param taskID
     * @param createCalendar
     */
    public TargetTask(int taskID, Date createCalendar) {
        super(taskID, createCalendar);
    }

    @Override
    protected boolean setTaskType() {
        super.taskType = Task.TARGET_TASK_TYPE;
        return true;
    }

}

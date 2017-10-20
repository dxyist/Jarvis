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


    private String content;
    // 写入日期，作为展示用
    private Date whiteDate;

    private String whiteDateFormatString;

    private int priority;

    // 任务状态
    private int taskState;


    // 任务价值
    private int taskValue;


    // 任务倒扣率
    private int taskPunchRate = 1;

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

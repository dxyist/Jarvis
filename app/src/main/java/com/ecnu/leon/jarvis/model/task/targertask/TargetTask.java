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
    // 剩余完成毫秒数
    private long deadlineTs = 0;

    private String whiteDateFormatString;

    private int priority;

    // 任务状态
    private int taskState = 1;

    // 任务价值
    private int taskValue;


    /**
     * 必须传入一个名称和任务ID
     *
     * @param taskID
     */
    public TargetTask(int taskID,String content,int taskValue,int priority,long deadlineTs) {
        super(taskID, new Date());
        this.content = content;
        this.taskValue = taskValue;
        this.priority = priority;
        this.deadlineTs = deadlineTs;
    }

    @Override
    protected boolean setTaskType() {
        super.taskType = Task.TARGET_TASK_TYPE;
        return true;
    }

}

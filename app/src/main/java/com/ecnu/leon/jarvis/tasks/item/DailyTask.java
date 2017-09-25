package com.ecnu.leon.jarvis.tasks.item;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Leon on 2017/8/10.
 */

public class DailyTask extends Task implements Serializable {

    private static final long serialVersionUID = 7523967370034938905L;


    public static int TASK_STATE_UNFINISHED = 0;
    public static int TASK_STATE_FINISHED = 1;
    // 表示任务失败
    public static int TASK_STATE_FAILED = -1;


    private String content;
    // 写入日期，作为展示用
    private Date whiteDate;

    private String whiteDateFormatString;

    // 任务状态
    private int taskState;


    // 任务价值
    private int taskValue;


    // 任务倒扣率
    private int taskPunchRate = 1;

    public int getTaskState() {
        return taskState;
    }

    public int getTaskValue() {
        return taskValue;
    }

    public DailyTask(int taskID, String content, Date whiteDate, int taskValue) {
        super(taskID, whiteDate);
        this.content = content;
        this.whiteDate = whiteDate;
        this.taskState = DailyTask.TASK_STATE_UNFINISHED;
        this.taskValue = taskValue;
        if (whiteDate != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            this.whiteDateFormatString = format.format(whiteDate);
        }
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getWhiteDate() {
        return whiteDate;
    }

    public void setTaskValue(int taskValue) {
        this.taskValue = taskValue;
    }

    public void setWhiteDate(Date whiteDate) {
        this.whiteDate = whiteDate;
    }

    public String getWhiteDateFormatString() {
        return whiteDateFormatString;
    }

    public boolean isFinished() {
        return taskState == TASK_STATE_FINISHED;
    }

    public boolean isFailed() {
        return taskState == TASK_STATE_FAILED;
    }

    public void setUnfinished() {
        taskState = TASK_STATE_UNFINISHED;
    }

    public void setFailed() {
        taskState = TASK_STATE_FAILED;
    }

    public void setFinished() {
        taskState = TASK_STATE_FINISHED;
    }

    @Override
    protected boolean setTaskType() {
        super.taskType = Task.DAILY_TASK_TYPE;
        return true;
    }

    public int getTaskPunchRate() {
        return taskPunchRate;
    }

    public void setTaskPunchRate(int taskPunchRate) {
        this.taskPunchRate = taskPunchRate;
    }
}

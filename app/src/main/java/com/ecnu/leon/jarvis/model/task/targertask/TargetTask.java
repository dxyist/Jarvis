package com.ecnu.leon.jarvis.model.task.targertask;

import android.widget.Toast;

import com.ecnu.leon.jarvis.model.task.Task;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Leon on 2017/10/17.
 */

public class TargetTask extends Task implements Serializable {

    private static final long serialVersionUID = 7523967370034438905L;


    public static final int TASK_STATE_UNFINISHED = 0;
    public static final int TASK_STATE_FINISHED = 1;
    // 表示任务失败
    public static final int TASK_STATE_FINISHED_AFTER_OVERDUE = 101;
    // 表示任务失败
    public static final int TASK_STATE_FAILED = 2;

    public static final int TASK_STATE_REMOVEED = 3;

    // 重要性列表
    public final static int TASK_PRIORITY_TRIVIA = 0;
    public final static int TASK_PRIORITY_NORMAL = 1;
    public final static int TASK_PRIORITY_IMPORTANT = 2;
    public final static int TASK_PRIORITY_VERY_IMPORTANT = 3;


    private String content;
    // 剩余完成毫秒数
    private long deadlineTs = 0;

    private String whiteDateFormatString;

    private int priority;

    // 任务状态
    private int taskState = 0;

    // 任务价值
    private int taskValue;

    // 任务倒扣率
    private int taskPunchRate = 1;

    private String comment = "";

    /**
     * 必须传入一个名称和任务ID
     *
     * @param taskID
     */
    public TargetTask(int taskID, String content, int taskValue, int priority, long deadlineTs) {
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public long getDeadlineTs() {
        return deadlineTs;
    }

    public void setDeadlineTs(long deadlineTs) {
        this.deadlineTs = deadlineTs;
    }

    public int getTaskState() {
        return taskState;
    }

    public void setTaskState(int taskState) {
        this.taskState = taskState;
    }

    public int getTaskValue() {
        return taskValue;
    }

    public void setTaskValue(int taskValue) {
        this.taskValue = taskValue;
    }

    public boolean isFinished() {
        return getTaskState() == TASK_STATE_FINISHED || getTaskState() == TASK_STATE_FINISHED_AFTER_OVERDUE;
    }

    public boolean isOverdueFinished() {
        return getTaskState() == TASK_STATE_FINISHED_AFTER_OVERDUE;
    }

    public boolean isUnFinished() {
        return getTaskState() == TASK_STATE_UNFINISHED;
    }

    public boolean isOverdue(Date date) {
        return (date.getTime() - getDeadlineTs()) >= getCreateDate().getTime();
    }


    public void setFinished() {
        this.setTaskState(TASK_STATE_FINISHED);
    }
    public void setOverdueFinished() {
        this.setTaskState(TASK_STATE_FINISHED_AFTER_OVERDUE);
    }

    public void setUnfinished() {
        this.setTaskState(TASK_STATE_UNFINISHED);
    }

    public void setRemoved() {
        this.setTaskState(TASK_STATE_REMOVEED);
    }

    public Boolean isRemoved() {
        return this.getTaskState() == TASK_STATE_REMOVEED;
    }

    public int getTaskPunchRate() {
        return taskPunchRate;
    }

    public void setTaskPunchRate(int taskPunchRate) {
        this.taskPunchRate = taskPunchRate;
    }

    public int getResultValue() {
        if (taskState == TASK_STATE_FINISHED) {
            return taskValue;
        }

        if (taskState == TASK_STATE_FINISHED_AFTER_OVERDUE)
        {
            return 0;
        }
        // 20号以后删除也不在返还点数
        if (taskState == TASK_STATE_REMOVEED && getCreateDate().before(new Date(2018,1,20)))
        {
            return 0;
        }

        return -taskValue;
    }

    public long getRemainTs(){
        return getCreateDate().getTime() + getDeadlineTs() - System.currentTimeMillis();
    }

    public Date getDeadlineDate() {
        long deadlineTs = getCreateDate().getTime() + getDeadlineTs();
        return new Date(deadlineTs);
    }

}

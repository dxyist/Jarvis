package com.ecnu.leon.jarvis.tasks.item;

import java.util.Date;

/**
 * Created by Leon on 2017/8/10.
 */

public class DailyTask {
    public static int TASK_STATE_UNFINISHED = 0;
    public static int TASK_STATE_FINISHED = 1;
    // 表示任务失败
    public static int TASK_STATE_FAILED = -1;


    private String content;
    // 写入日期，作为展示用
    private Date whiteDate;

    // 任务状态
    private int state;

    // 任务价值
    private int value;

    public DailyTask(String content, Date whiteDate, int value) {
        this.content = content;
        this.whiteDate = whiteDate;
        this.state = DailyTask.TASK_STATE_UNFINISHED;
        this.value = value;
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

    public void setWhiteDate(Date whiteDate) {
        this.whiteDate = whiteDate;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

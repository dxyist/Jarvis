package com.ecnu.leon.jarvis.tasks.item;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Leon on 2017/8/10.
 */

public abstract class Task implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -1169296642369641283L;
    /**
     * ************************************ 静态域部分*****************************************
     */
    public static final int DAILY_TASK_TYPE = 1;
    public static final int ROUTINE_TASK_TYPE = 2;
    public static final int REWARD_TASK_TYPE = 3;
    public static final int STEPPING_DAILY_TASK_TYPE = 4;
    public static final int GOAL_TYPE = 10;


    /**
     * ************************************ 数据域部分*****************************************
     */
    // --- 任务ID
    private int ID;
    // 父ID（容器ID）
    private int parentID;
    // --- 任务名称
    private final Date createDate;
    // --- 任务类型
    protected int taskType;

    /**
     ************************************* 方法域部分*****************************************
     */

    /**
     * 抽象方法，子类实现用于选择对应的Task类型
     *
     * @return
     */
    protected abstract boolean setTaskType();

    /**
     * 用于获取任务类型
     *
     * @return 返回任务类型
     */
    public int getTaskType() {
        return this.taskType;
    }

    /**
     * 必须传入一个名称和任务ID
     */
    public Task(int taskID, Date createCalendar) {
        this.ID = taskID;
        // --- 初始化对应数据域
        setTaskType();
        this.createDate = createCalendar;
    }

    /**
     * 获取任务的唯一标示符（ID）
     *
     * @return
     */
    public long getID() {
        return this.ID;
    }


    /**
     * 返回创建时间
     *
     * @return 创建时间
     */
    public Date getCreateDate() {
        return this.createDate;
    }


    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

}

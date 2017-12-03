package com.ecnu.leon.jarvis.model.project;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Leon on 2017/12/3.
 */

public class Action {
    // Action树结构
    private ArrayList<Action> actions;
    private ArrayList<ActionContext> contexts;

    private String content;
    private String comment;

    // 任务状态
    private boolean isFinished = false;

    private boolean isFlaged = false;

    private Date deferredDate = null;
    // 预计完成时间
    private Date dueDate = null;

    public Action() {
        this.actions = new ArrayList<>();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public boolean isFlaged() {
        return isFlaged;
    }

    public void setFlaged(boolean flaged) {
        isFlaged = flaged;
    }

    public Date getDeferredDate() {
        return deferredDate;
    }

    public void setDeferredDate(Date deferredDate) {
        this.deferredDate = deferredDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}

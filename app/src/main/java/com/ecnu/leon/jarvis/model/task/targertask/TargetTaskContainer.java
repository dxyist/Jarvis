package com.ecnu.leon.jarvis.model.task.targertask;

import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Leon on 2017/11/4.
 */

public class TargetTaskContainer {
    private ArrayList<TargetTask> routineTasksArray = new ArrayList<TargetTask>();
    private static final String FILE_NAME = "TargetTask.dat";

    private Context context;

    public TargetTaskContainer(Context context) {
        this.context = context;
    }

    public void addTargetTask(TargetTask targetTask) {

        routineTasksArray.add(targetTask);
    }

    public ArrayList<TargetTask> getTargetTaskList(Date date) {

        ArrayList<TargetTask> tasks = new ArrayList<>();


        return tasks;
    }
}

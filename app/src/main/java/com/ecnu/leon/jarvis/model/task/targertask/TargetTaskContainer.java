package com.ecnu.leon.jarvis.model.task.targertask;

import android.content.Context;

import com.ecnu.leon.jarvis.model.task.routinetask.RoutineTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Leon on 2017/11/4.
 */

public class TargetTaskContainer {
    private ArrayList<TargetTask> targetTasksArray = new ArrayList<TargetTask>();
    private static final String FILE_NAME = "TargetTask.dat";

    private Context context;

    public TargetTaskContainer(Context context) {
        this.context = context;
    }

    public void addTargetTask(TargetTask targetTask) {

        targetTasksArray.add(targetTask);
    }

    public ArrayList<TargetTask> getTargetTaskList() {

        return targetTasksArray;
    }

    public boolean save() throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE));
        out.writeObject(this.targetTasksArray);
        out.flush();
        out.close();
        return false;
    }

    @SuppressWarnings("unchecked")
    public boolean load() throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(context.openFileInput(FILE_NAME));
        this.targetTasksArray = (ArrayList<TargetTask>) in.readObject();
        in.close();
        return false;
    }
}

package com.ecnu.leon.jarvis.tasks.model;


import android.content.Context;

import com.ecnu.leon.jarvis.tasks.item.DailyTask;
import com.ecnu.leon.jarvis.tasks.item.RoutineTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Leon on 2017/9/25.
 */

public class RoutineTaskContainer implements Serializable {
    // String使用2017-08-17格式
    private ArrayList<RoutineTask> routineTasksArray;
    private static final String FILE_NAME = "RoutineTask.dat";

    private Context context;

    public RoutineTaskContainer(Context context) {
        this.routineTasksArray = routineTasksArray;
        this.context = context;
    }

    public void addRoutineTask(RoutineTask routineTask) {

        if (routineTasksArray == null) {
            routineTasksArray = new ArrayList<RoutineTask>();
        }

        routineTasksArray.add(routineTask);
    }

    public ArrayList<RoutineTask> getRoutineTaskList() {


        if (routineTasksArray == null) {
            routineTasksArray = new ArrayList<RoutineTask>();
            // 保证对象的传递性，这样在加入数据的时候可以调用刷
        }

        return routineTasksArray;
    }


    public boolean save() throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE));
        out.writeObject(this.routineTasksArray);
        out.flush();
        out.close();
        return false;
    }

    @SuppressWarnings("unchecked")
    public boolean load() throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(context.openFileInput(FILE_NAME));
        this.routineTasksArray = (ArrayList<RoutineTask>) in.readObject();
        in.close();
        return false;
    }
}

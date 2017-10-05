package com.ecnu.leon.jarvis.model.task.routinetask;


import android.content.Context;

import com.ecnu.leon.jarvis.model.task.routinetask.RoutineTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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

    public ArrayList<RoutineTask> getRoutineTaskList(Date date) {

        ArrayList<RoutineTask> tasks = new ArrayList<>();

        if (routineTasksArray == null) {
            routineTasksArray = new ArrayList<RoutineTask>();
            // 保证对象的传递性，这样在加入数据的时候可以调用刷
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 把合法的任务填写进去
        for (int i = 0; i < routineTasksArray.size(); i++) {
            if (routineTasksArray.get(i).getCreateDate().before(date)) {
                // 如果星期数组为允许表示开启
                if (routineTasksArray.get(i).getDaysOfWeek()[calendar.get(Calendar.DAY_OF_WEEK)]) {
                    tasks.add(routineTasksArray.get(i));
                }
            }
        }


        return tasks;
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

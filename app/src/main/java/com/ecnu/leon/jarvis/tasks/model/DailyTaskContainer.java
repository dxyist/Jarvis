package com.ecnu.leon.jarvis.tasks.model;

import android.content.Context;

import com.ecnu.leon.jarvis.tasks.item.DailyTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by Leon on 2017/9/4.
 */

public class DailyTaskContainer implements Serializable {

    // String使用2017-08-17格式
    private HashMap<String, ArrayList<DailyTask>> dailyTasksHashMap;
    private static final String FILE_NAME = "DailyTask.dat";

    private Context context;


    public DailyTaskContainer(Context context) {
        this.dailyTasksHashMap = new HashMap<String, ArrayList<DailyTask>>();
        this.context = context;

    }


    public void addDailyTask(DailyTask dailyTask) {
        String dateString = dailyTask.getWhiteDateFormatString();
        ArrayList<DailyTask> arrayList = dailyTasksHashMap.get(dateString);

        if (arrayList == null) {
            arrayList = new ArrayList<DailyTask>();
        }

        arrayList.add(dailyTask);
        dailyTasksHashMap.put(dateString, arrayList);
    }

    public ArrayList<DailyTask> getDailyTaskList(Date currentDate) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(currentDate);
        ArrayList<DailyTask> arrayList = dailyTasksHashMap.get(dateString);

        if (arrayList == null) {
            arrayList = new ArrayList<DailyTask>();
            // 保证对象的传递性，这样在加入数据的时候可以调用刷新
            dailyTasksHashMap.put(dateString, arrayList);
        }


        return arrayList;
    }

    public boolean save() throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE));
        out.writeObject(this.dailyTasksHashMap);
        out.flush();
        out.close();
        return false;
    }

    @SuppressWarnings("unchecked")
    public boolean load() throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(context.openFileInput(FILE_NAME));
        this.dailyTasksHashMap = (HashMap<String, ArrayList<DailyTask>>) in.readObject();
        in.close();
        return false;
    }

}

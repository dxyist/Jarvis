package com.ecnu.leon.jarvis.model.task.dailytask;

import android.content.Context;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Leon on 2017/9/4.
 */

public class DailyTaskContainer implements Serializable {

    // String使用2017-08-17格式
    private HashMap<String, ArrayList<DailyTask>> dailyTasksHashMap;
    private static final String FILE_NAME = "DailyTask.dat";

    private Context context;

    // 设置参数
    private int defaultValue = 1;
    private int defaultRate = 1;
    // 当日任务最大最小值
    private int maximumTasksForOneDay = 10;
    private int minimumTasksForOneDay = 5;
    private int maximumValueForOneDay = 15;
    // 全部完成任务的额外奖励,暂定5.
    private int extraBonus = 5;


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

        if (arrayList.size() >= maximumTasksForOneDay) {
            Toast.makeText(context, "每日任务不得超过" + maximumTasksForOneDay + "个", Toast.LENGTH_LONG).show();
            return;
        }

        int currentValue = this.getOneDayMaximumPossibleValue(TaskManager.currentTaskCalendar) + dailyTask.getTaskValue();
        if (currentValue > maximumValueForOneDay) {
            Toast.makeText(context, "每日任务总正能量值不得超过" + maximumValueForOneDay, Toast.LENGTH_LONG).show();
            return;
        }

//        if (arrayList.size() < minimumTasksForOneDay)
//        {
//            Toast.makeText(context, "缺少任务会有正能量惩罚\n还差" + (minimumTasksForOneDay - arrayList.size() - 1) + "个任务", Toast.LENGTH_LONG).show();
//        }

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

    /**
     * 指定每日正能量值计算规则，返回值(还需要一个返回具体列表的参数)
     * <p>
     * \     * @return
     */
    public int getOneDayResultValue(Date date) {
        ArrayList<DailyTask> dailyTasks = getDailyTaskList(date);

        if (dailyTasks == null) {
            return 0;
        }

        int oneDayValue = 0;
        // 规则体
        // 1：如果每天任务小于最小限定值则能量值依次减一
        if (dailyTasks.size() < minimumTasksForOneDay) {
            oneDayValue -= minimumTasksForOneDay - dailyTasks.size();
        }
        // 2：根据每天的完成情况变化
        for (int i = 0; i < dailyTasks.size(); i++) {
            oneDayValue += (dailyTasks.get(i).getResultValue());
        }
        // 3：全天任务完成的额外奖励（全天任务达到15上限，且任务数目超过5，且全部为完成）

        if (dailyTasks.size() != 0 && getOneDayMaximumPossibleValue(date) == maximumValueForOneDay && oneDayValue == maximumValueForOneDay) {
            oneDayValue += extraBonus;
        }

        return oneDayValue;
    }



//    public int getTotalValue() {
//        int totalValue = 0;
//        for (Map.Entry<String, ArrayList<DailyTask>> entry : dailyTasksHashMap.entrySet()) {
//
//            Date date = null;
//            try {
//                // Fri Feb 24 00:00:00 CST 2012
//                date = format.parse(entry.getKey());
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            // 2012-02-24
////            date = java.sql.Date.valueOf(str);
//
//            totalValue += getOneDayResultValue(date);
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//            String dateString = format.format(new Date());
//
////            String nowDateString = CalendarUtil.gregorianCalendarToDateString(new GregorianCalendar());
////            String lastedBeginDateString = CalendarUtil.gregorianCalendarToDateString(MainActivity.latelyBaginCalendar);
////
////            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
////            String targetDateString = entry.getKey();
////            // 可以计算的区域区间在开始执行日到当前日期之前
////            if (!CalendarUtil.firstDateIsLaterThanSecondOne(targetDateString,nowDateString) && !CalendarUtil.firstDateIsLa                                                                                                                                                                    terThanSecondOne(lastedBeginDateString, targetDateString))
////            {
////                totalValue += getOneDayResultValue(entry.getValue());
////            }
//        }
//
//        return totalValue;
//    }


    public int getOneDayMaximumPossibleValue(Date date) {
        ArrayList<DailyTask> dailyTasks = getDailyTaskList(date);
        int totalValue = 0;
        if (dailyTasks == null) {
            return 0;
        }

        for (int i = 0; i < dailyTasks.size(); i++) {
            totalValue += dailyTasks.get(i).getTaskValue();
        }

        return totalValue;

    }


//    public int getTotalTaskValues(){
//
//    };

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

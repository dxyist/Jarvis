package com.ecnu.leon.jarvis.model.task.consumable;

/**
 * Created by Leon on 2017/10/16.
 */

import com.ecnu.leon.jarvis.model.task.dailytask.DailyTask;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Consumable implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5887967544430385236L;

    private String content;
    private int price = 1;
    private Date createDate;
    // --TODO 增加项目
    // --- 任务ID
    private long ID;

    // 存储任务状态
    private HashMap<String, Integer> taskStatuses = new HashMap<>();


    public Consumable(long taskID, String name, int price) {
        this.ID = taskID;
        this.price = price;
        this.content = name;
        createDate = new Date();
    }


    // 减少一次，只能减少当天
    public boolean reduceOnce(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(date);
        int times = 0;
        if (taskStatuses.containsKey(dateString)) {
            times = taskStatuses.get(dateString);
        }

        if (times > 0) {
            taskStatuses.put(dateString, times - 1);
        }
        return true;
    }

    // 减少一次，只能减少当天
    public boolean addOnce(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(date);
        int times = 0;
        if (taskStatuses.containsKey(dateString)) {
            times = taskStatuses.get(dateString);
        }

            taskStatuses.put(dateString, times + 1);

        return true;

    }


    // 获取在上面花费的次数
    public int getTotalCost() {
        int totalCost = 0;
        for (Map.Entry<String, Integer> entry : taskStatuses.entrySet()) {
            int taskStatus = entry.getValue();

            totalCost += entry.getValue() * price;
        }

        return totalCost;
    }

    // 获取在上面花费的次数
    public int getOneDayCost(Date date) {
        int totalCost = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(date);
        if (taskStatuses.containsKey(dateString)) {
            totalCost = taskStatuses.get(dateString) * price;
        }
        return totalCost;
    }

    // 获取在上面花费的次数
    public int getOneDayNumber(Date date) {
        int totalNumber = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(date);
        if (taskStatuses.containsKey(dateString)) {
            totalNumber = taskStatuses.get(dateString);
        }
        return totalNumber;
    }

    // 获取在上面花费的次数
    public int getTotalNumber(Date date) {
        int totalNumber = 0;
        for (Map.Entry<String, Integer> entry : taskStatuses.entrySet()) {
            int taskStatus = entry.getValue();

            totalNumber += entry.getValue();
        }

        return totalNumber;
    }


    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public long getID() {
        return ID;
    }

    public void setID(long iD) {
        ID = iD;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}


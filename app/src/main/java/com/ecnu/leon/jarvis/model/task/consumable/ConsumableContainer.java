package com.ecnu.leon.jarvis.model.task.consumable;

/**
 * Created by Leon on 2017/10/16.
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.content.Context;
import android.widget.Toast;

import com.ecnu.leon.jarvis.model.task.routinetask.RoutineTask;

public class ConsumableContainer {
    private static final String FILENAME = "Comsumable.dat";
    public static final long ENERGY_EXCHANGE_ID = 10000000;
    public static final long ENERGY_EXCHANGE_ID2 = 10000001;

    private Context context;
    private int defaultValue = 1;

    private ArrayList<Consumable> consumableArrayList;

    public ConsumableContainer(Context context) {
        consumableArrayList = new ArrayList<Consumable>();
        this.context = context;
        addDefaultConsumables();
    }

    private boolean addDefaultConsumables() {
        Consumable consumable = new Consumable(ENERGY_EXCHANGE_ID, "10点正能量换100块", 10);
        this.consumableArrayList.add(consumable);
        Consumable consumable2 = new Consumable(ENERGY_EXCHANGE_ID2, "1点正能量换10块", 1);
        this.consumableArrayList.add(consumable);
        return true;
    }

    public boolean addConsumable(Consumable consumable) {
        // 筛选逻辑

        this.consumableArrayList.add(consumable);
        return true;
    }

    public boolean removeConsumable(int ID) {
        if (ID == ENERGY_EXCHANGE_ID) {
            Toast.makeText(context, "改条目不可删除", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (ID == ENERGY_EXCHANGE_ID2) {
            Toast.makeText(context, "改条目不可删除", Toast.LENGTH_SHORT).show();
            return false;
        }

        for (int i = 0; i < consumableArrayList.size(); i++) {
            if (consumableArrayList.get(i).getID() == ID) {
                consumableArrayList.remove(i);
                return true;
            }
        }

        return false;
    }

    public int getExchangeExpenses() {
        int totalExchange = 0;
        for (int i = 0; i < consumableArrayList.size(); i++) {
            if (consumableArrayList.get(i).getID() == ENERGY_EXCHANGE_ID) {
                totalExchange += consumableArrayList.get(i).getTotalCost() * 10;
            }
            if (consumableArrayList.get(i).getID() == ENERGY_EXCHANGE_ID2) {
                totalExchange += consumableArrayList.get(i).getTotalCost() * 10;
            }
        }

        return totalExchange;
    }


    public boolean cleanConsumable(GregorianCalendar calendar) {

        consumableArrayList.clear();

        return true;
    }

    public boolean save() throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(context.openFileOutput(FILENAME, Context.MODE_PRIVATE));
        out.writeObject(this.consumableArrayList);
        out.flush();
        out.close();
        return false;
    }

    @SuppressWarnings("unchecked")
    public boolean load() throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(context.openFileInput(FILENAME));
        this.consumableArrayList = (ArrayList<Consumable>) in.readObject();
        in.close();
        return false;
    }

    public ArrayList<Consumable> getConsumableArrayList(Date date) {

        ArrayList<Consumable> tasks = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // 把合法的任务填写进去
        for (int i = 0; i < consumableArrayList.size(); i++) {
            if (consumableArrayList.get(i).getCreateDate().before(date) && !consumableArrayList.get(i).isHide()) {
                tasks.add(consumableArrayList.get(i));
            }
        }
        return tasks;
    }

    public int getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * 返回某一天的正能量消耗值
     *
     * @return
     */
    public int getOneDayConsumedValue(Date date) {
        int oneDayConsumedValue = 0;
        for (int i = 0; i < consumableArrayList.size(); i++) {
            Consumable consumable = consumableArrayList.get(i);
            oneDayConsumedValue += consumable.getOneDayCost(date);
        }

        return oneDayConsumedValue;
    }

    public int getTotalConsumedValue() {
        int totalConsumedValue = 0;
        for (int i = 0; i < consumableArrayList.size(); i++) {
            totalConsumedValue += consumableArrayList.get(i).getTotalCost();
        }
        return totalConsumedValue;
    }

}

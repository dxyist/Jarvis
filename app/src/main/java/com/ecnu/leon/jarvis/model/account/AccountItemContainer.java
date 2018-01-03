package com.ecnu.leon.jarvis.model.account;

import android.content.Context;
import android.widget.Toast;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LeonDu on 28/10/2017.
 */

public class AccountItemContainer {
    private static final String FILENAME = "AccountItem.dat";
    HashMap<String, ArrayList<AccountItem>> accountItemHashMap;

    private Context context;

    public AccountItemContainer(Context context) {
        this.context = context;
        accountItemHashMap = new HashMap<>();
    }

    public void addNewItem(AccountItem item) {
        Toast.makeText(context, "item value" + item.getCost(), Toast.LENGTH_SHORT).show();
        Date date = new Date(item.getTs());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(date);

        ArrayList<AccountItem> itemList = accountItemHashMap.get(dateString);

        if (itemList == null) {
            itemList = new ArrayList<>();
        }

        itemList.add(0, item);

        accountItemHashMap.put(dateString, itemList);
    }

    public int getItemNumberOfOneDay(Date currentDate) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(currentDate);

        ArrayList<AccountItem> itemList = accountItemHashMap.get(dateString);

        if (itemList == null) {
            return 0;
        }

        return itemList.size();
    }

    public float getItemCostOfOneDay(Date currentDate) {
        float cost = 0;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(currentDate);

        ArrayList<AccountItem> itemList = accountItemHashMap.get(dateString);

        if (itemList == null) {
            return 0;
        }

        for (int i = 0; i < itemList.size(); i++) {
            cost += itemList.get(i).getCost();
        }

        return cost;
    }

    public ArrayList<AccountItem> getOneDayList(Date currentDate) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(currentDate);

        ArrayList<AccountItem> itemList = accountItemHashMap.get(dateString);

        if (itemList == null) {
            return new ArrayList<>();
        }

        return itemList;
    }

    public float getItemCostOfOneWeek(Date currentDate) {
        float cost = 0;

        return cost;
    }

    public float getItemCostOfOneMonth(Date currentDate) {
        float cost = 0;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        String dateString = format.format(currentDate);

        for (Map.Entry<String, ArrayList<AccountItem>> entry : accountItemHashMap.entrySet()) {
            // 取月份
            if (entry.getKey().substring(0, 7).equals(dateString)) {
                ArrayList<AccountItem> itemList = entry.getValue();
                for (int i = 0; i < itemList.size(); i++) {
                    cost += itemList.get(i).getCost();
                }
            }

        }
        return cost;
    }

    public float getItemTotalCost() {
        float cost = 0;

        for (Map.Entry<String, ArrayList<AccountItem>> entry : accountItemHashMap.entrySet()) {

            ArrayList<AccountItem> itemList = entry.getValue();
            for (int i = 0; i < itemList.size(); i++) {
                cost += itemList.get(i).getCost();
            }
        }

        return cost;
    }

    public boolean save() throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(context.openFileOutput(FILENAME, Context.MODE_PRIVATE));
        out.writeObject(this.accountItemHashMap);
        out.flush();
        out.close();
        return false;
    }

    @SuppressWarnings("unchecked")
    public boolean load() throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(context.openFileInput(FILENAME));
        this.accountItemHashMap = (HashMap<String, ArrayList<AccountItem>>) in.readObject();
        in.close();
        return false;
    }

}

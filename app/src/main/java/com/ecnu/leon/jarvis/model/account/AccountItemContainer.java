package com.ecnu.leon.jarvis.model.account;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by LeonDu on 28/10/2017.
 */

public class AccountItemContainer {
    private static final String FILENAME = "AccountItem.dat";
    HashMap<String,ArrayList<AccountItem>> accountItemHashMap;

    private Context context;

    public AccountItemContainer(Context context) {
        this.context = context;
        accountItemHashMap = new HashMap<>();
    }

    public void addNewItem(AccountItem item)
    {
        Date date = new Date(item.getTs());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(date);

        ArrayList<AccountItem> itemList = accountItemHashMap.get(dateString);

        if (itemList == null)
        {
            itemList = new ArrayList<>();
        }

        itemList.add(0,item);

        accountItemHashMap.put(dateString,itemList);
    }

    public int getItemNumberOfOneDay(Date currentDate)
    {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(currentDate);

        ArrayList<AccountItem> itemList = accountItemHashMap.get(dateString);

        if (itemList == null)
        {
            return 0;
        }

        return itemList.size();
    }
}

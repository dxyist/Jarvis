package com.ecnu.leon.jarvis.model.account;

import android.content.Context;
import android.widget.Toast;

import com.ecnu.leon.jarvis.Utils.PrefKeys;
import com.ecnu.leon.jarvis.Utils.PrefUtils;
import com.ecnu.leon.jarvis.model.task.TaskManager;
import com.ecnu.leon.jarvis.model.task.consumable.Consumable;
import com.ecnu.leon.jarvis.model.task.consumable.ConsumableContainer;
import com.ecnu.leon.jarvis.model.task.dailytask.DailyTaskContainer;
import com.ecnu.leon.jarvis.model.task.routinetask.RoutineTaskContainer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Leon on 2017/10/30.
 */

public class AccountManager {

    // 每日生活费
    private int livingExpenses = 20;

    private Date initDate;

    private static AccountManager mAccountManager;

    private AccountItemContainer accountItemContainer;

    private CategoryContainer categoryContainer;


    private Context context;

    private Boolean isLoadSuccess;

    public static Date latelyBaginDate = new Date(2017, 11, 28);


    private AccountManager(Context context) {
        this.context = context;

        // 下面两个顺序不能弄反
        this.isLoadSuccess = true;
        accountItemContainer = new AccountItemContainer(context);
        categoryContainer = new CategoryContainer(context);
        loadContent();
    }


    public static AccountManager getInstance(Context context) {


        if (mAccountManager == null) {
            mAccountManager = new AccountManager(context.getApplicationContext());
        }
        return mAccountManager;
    }

    public void addNewAccountItem(AccountItem item) {
        this.accountItemContainer.addNewItem(item);
    }


    public float getTotalCost() {
        return this.accountItemContainer.getItemTotalCost();
    }

    public float getOneDayCost(Date date) {
        return this.accountItemContainer.getItemCostOfOneDay(date);
    }

    public ArrayList<AccountItem> getOneDayList(Date date) {
        return this.accountItemContainer.getOneDayList(date);
    }

    public int getOneDayCostNumber(Date date) {
        return this.accountItemContainer.getItemNumberOfOneDay(date);
    }

    public float getTotalIncomeValue() {
        int value = 0;

        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(latelyBaginDate);
        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(new Date());
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        value += (day2 - day1) * livingExpenses;

        value += getPositiveIncoming();

        return value;
    }

    private float getPositiveIncoming() {
        return TaskManager.getInstance(context).getIncomingExchangeAmount();
    }

    public void loadContent() {
        // category load
        try {
            categoryContainer.load();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            accountItemContainer.load();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            if (!(Boolean) PrefUtils.getKey(PrefKeys.ACCOUNT_FIRST_TIME_LOAD, true)) {
                isLoadSuccess = false;
                Toast.makeText(context, "账目数据读取失败！！！！！", Toast.LENGTH_SHORT).show();
            } else {
                PrefUtils.setKey(PrefKeys.ACCOUNT_FIRST_TIME_LOAD, false);
            }

        }

    }

    public void saveContent() {
        try {
            categoryContainer.save();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (isLoadSuccess) {
            // account save
            if (accountItemContainer != null) {
                try {
                    accountItemContainer.save();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "账目数据存储失败！！！！！", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(context, "当前数据不可写", Toast.LENGTH_SHORT).show();

        }
    }


    public static int getNewAccountID() {
        int id = (int) PrefUtils.getKey(PrefKeys.ACCOUNT_ID, 0);
        PrefUtils.setKey(PrefKeys.ACCOUNT_ID, id + 1);
        return id;
    }

    public CategoryContainer getCategoryContainer() {
        return categoryContainer;
    }

}

package com.ecnu.leon.jarvis.model.account;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecnu.leon.jarvis.R;
import com.ecnu.leon.jarvis.Utils.DateUtil;
import com.ecnu.leon.jarvis.model.task.TaskManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class AccountDetailsActivity extends AppCompatActivity {

    private TextView actionBarDateTextview;
    private TextView actionBarWeekTextview;
    private TextView actionBarTotalTaskValueTextview;
    private TextView actionBarTodayTaskValueTextview;
    private TextView actionBarLastDaysTextview;
    private TextView actionBarTodayTextview;

    RecyclerView recyclerView;
    private ImageView leftArrowImageview;
    private ImageView rightArrowImageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_account);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(new AccountItemRecyclerViewAdapter(AccountManager.getInstance(this).getOneDayList(TaskManager.currentTaskCalendar), this));


        initActionBar();
    }


    public static void startActivity(Context context) {

        final Intent intent = new Intent();
        intent.setClass(context, AccountDetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    private void initActionBar() {
        actionBarDateTextview = (TextView) findViewById(R.id.txt_actionbar_date);
        actionBarWeekTextview = (TextView) findViewById(R.id.txt_actionbar_week);
        actionBarTotalTaskValueTextview = (TextView) findViewById(R.id.text_total_positive_value);
        actionBarTodayTextview = (TextView) findViewById(R.id.txt_actionbar_day_diff);

        actionBarDateTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskManager.currentTaskCalendar = new Date();   //跳转今天
                refreshActionBar();
                refresh();
            }
        });

        actionBarWeekTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskManager.currentTaskCalendar = new Date();   //跳转今天
                refreshActionBar();
                refresh();

            }
        });


        actionBarTodayTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskManager.currentTaskCalendar = new Date();   //跳转今天
                refreshActionBar();
                refresh();

            }
        });

        actionBarLastDaysTextview = (TextView) findViewById(R.id.text_last_days);

        leftArrowImageview = (ImageView) findViewById(R.id.txt_arrow_left);
        leftArrowImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(TaskManager.currentTaskCalendar);
//                calendar.add(calendar.YEAR, 1);//把日期往后增加一年.整数往后推,负数往前移动
//                calendar.add(calendar.DAY_OF_MONTH, 1);//把日期往后增加一个月.整数往后推,负数往前移动
//
//                calendar.add(calendar.WEEK_OF_MONTH, 1);//把日期往后增加一个月.整数往后推,负数往前移动
                calendar.add(calendar.DATE, -1);//把日期往后增加一天.整数往后推,负数往前移动
                TaskManager.currentTaskCalendar = calendar.getTime();   //这个时间就是日期往后推一天的结果
                refreshActionBar();
                refresh();


            }
        });
        rightArrowImageview = (ImageView) findViewById(R.id.txt_arrow_right);
        rightArrowImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(TaskManager.currentTaskCalendar);
//                calendar.add(calendar.YEAR, 1);//把日期往后增加一年.整数往后推,负数往前移动
//                calendar.add(calendar.DAY_OF_MONTH, 1);//把日期往后增加一个月.整数往后推,负数往前移动
//
//                calendar.add(calendar.WEEK_OF_MONTH, 1);//把日期往后增加一个月.整数往后推,负数往前移动
                calendar.add(calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
                TaskManager.currentTaskCalendar = calendar.getTime();   //这个时间就是日期往后推一天的结果
                refreshActionBar();
                refresh();

            }
        });
        refreshActionBar();
    }

    private void refreshActionBar() {
        // 刷新日期
        if (actionBarDateTextview != null) {
            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String dateString = format.format(TaskManager.currentTaskCalendar);
            actionBarDateTextview.setText(dateString);
        }

        if (actionBarWeekTextview != null) {
            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("EE", Locale.CHINESE);
            String dateString = format.format(TaskManager.currentTaskCalendar);
            actionBarWeekTextview.setText(dateString);
        }
//
//        if (actionBarTotalTaskValueTextview != null) {
//
//            actionBarTotalTaskValueTextview.setText(TaskManager.getInstance(this).getTotalValue() + "");
//        }

        if (actionBarTotalTaskValueTextview != null) {

            actionBarTotalTaskValueTextview.setText(AccountManager.getInstance(this).getOneDayCost(TaskManager.currentTaskCalendar) + "");
        }

        if (actionBarTodayTextview != null) {

            try {
                int dayDiff = 0;
                dayDiff = DateUtil.daysBetween(new Date(), TaskManager.currentTaskCalendar);

                String diffString = "今天";

                if (dayDiff == 0) {
                    diffString = "今天";

                } else if (dayDiff == 1) {
                    diffString = "明天";

                } else if (dayDiff == -1) {
                    diffString = "昨天";

                } else if (dayDiff == 2) {
                    diffString = "后天";

                } else if (dayDiff == -2) {
                    diffString = "前天";

                } else if (dayDiff < -2) {
                    diffString = dayDiff + "天前";

                } else if (dayDiff > 2) {
                    diffString = dayDiff + "天后";

                }

                actionBarTodayTextview.setText("(" + diffString + ")");

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


    }

    private void refresh() {
        {
            ((AccountItemRecyclerViewAdapter) recyclerView.getAdapter()).refresh();
        }
    }

    private class AccountItemRecyclerViewAdapter extends RecyclerView.Adapter<AccountItemRecyclerViewAdapter.ViewHolder> {
        private ArrayList<AccountItem> accountItems;
        private Context mContext;


        public void refresh() {
            this.accountItems = AccountManager.getInstance(mContext).getOneDayList(TaskManager.currentTaskCalendar);
            notifyDataSetChanged();
        }

        public AccountItemRecyclerViewAdapter(ArrayList<AccountItem> accountItems, Context context) {

            this.accountItems = accountItems;
            mContext = context;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_account, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            initViewHolder(holder, position);

            holder.mItem = accountItems.get(position);
        }

        @Override
        public int getItemCount() {
            return accountItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView categoryTextView;
            public final TextView createTimeTextView;

            public final TextView remarkTextView;

            public final TextView valueTextView;

            public AccountItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                categoryTextView = (TextView) view.findViewById(R.id.txt_account_item_category);
                createTimeTextView = (TextView) view.findViewById(R.id.txt_account_item_time);
                remarkTextView = (TextView) view.findViewById(R.id.txt_account_item_remark);
                valueTextView = (TextView) view.findViewById(R.id.txt_account_item_value);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + categoryTextView.getText() + "'";
            }
        }

        private void initViewHolder(ViewHolder holder, final int position) {

            String string = "";
            string = position + 1 + "：" + accountItems.get(position).getRemark();
            holder.remarkTextView.setText(string);
            holder.remarkTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage("确定删除记录，此操作不可逆！");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            accountItems.remove(position);
                            dialog.dismiss();
                            notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.create().show();

                }
            });
            float value = accountItems.get(position).getCost();
            holder.valueTextView.setText(value + "");

            holder.categoryTextView.setText(accountItems.get(position).getCategory());
            SimpleDateFormat format = new SimpleDateFormat("H:mm");
            String dateString = format.format(new Date(accountItems.get(position).getTs()));
            holder.createTimeTextView.setText(dateString);


        }
    }
}

package com.ecnu.leon.jarvis.model.task.consumable;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecnu.leon.jarvis.R;
import com.ecnu.leon.jarvis.model.task.dailytask.TaskManager;
import com.ecnu.leon.jarvis.model.task.routinetask.RoutineTask;
import com.ecnu.leon.jarvis.model.task.routinetask.RoutineTaskFragment;

import java.util.ArrayList;


public class ConsumableRecyclerViewAdapter extends RecyclerView.Adapter<ConsumableRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Consumable> consumables;
    private Context mContext;

    private final ConsumableFragment.OnListFragmentInteractionListener mListener;

    public ConsumableRecyclerViewAdapter(ArrayList<Consumable> items, ConsumableFragment.OnListFragmentInteractionListener listener, Context context) {
        consumables = items;
        mContext = context;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_consumble, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        TaskManager.setDateChanged(true);

        initViewHolder(holder, position);

        holder.mItem = consumables.get(position);


    }

    private void initViewHolder(ViewHolder holder, final int position) {

        String string = "";
        string = position + 1 + "：" + routineTasks.get(position).getContent();
        holder.mContentView.setText(string);
        int value = routineTasks.get(position).getTaskValue();
        holder.routineTaskBounsValueTextview.setText(value + "");
        holder.mCheckBox.setChecked(routineTasks.get(position).isFinished(TaskManager.currentTaskCalendar));

        // 先设置好颜色
        holder.routineTaskBounsValueTextview.setTextColor(Color.argb(255, 99, 204, 33));
        holder.routineTaskBounsTextview.setTextColor(Color.argb(255, 99, 204, 33));


        // 抗锯齿
        holder.routineTaskBounsValueTextview.getPaint().setAntiAlias(true);
        holder.routineTaskBounsTextview.getPaint().setAntiAlias(true);

        // 任务是否完成后的状态
        if (routineTasks.get(position).isFinished(TaskManager.currentTaskCalendar)) {
            holder.mContentView.setTextColor(Color.GRAY);
//            holder.dailytaskPunchValueTextview.setTextColor(Color.GRAY);
//            holder.dailytaskPunchTextview.setTextColor(Color.GRAY);
//
//            holder.dailytaskPunchValueTextview.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//            holder.dailytaskPunchTextview.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            // 设置任务难度颜色
            switch (routineTasks.get(position).getTaskValue()) {
                case 1:
                    holder.mContentView.setTextColor(Color.argb(255, 0, 0, 0));
                    break;
                case 2:
                    holder.mContentView.setTextColor(Color.argb(255, 50, 205, 50));
                    break;
                case 3:
                    holder.mContentView.setTextColor(Color.argb(255, 75, 105, 255));
                    break;
                case 4:
                    holder.mContentView.setTextColor(Color.argb(255, 211, 44, 230));
                    break;
                case 5:
                    holder.mContentView.setTextColor(Color.argb(255, 228, 174, 57));
                    break;
                default:
                    holder.mContentView.setTextColor(Color.argb(255, 0, 0, 0));
                    break;
            }

            holder.mContentView.getPaint().setFlags(0);
            holder.mContentView.getPaint().setAntiAlias(true);
        }

        holder.mCheckBox.setEnabled(true);


        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            // 不能调用外部的View
            @Override
            public void onClick(View v) {

                if (((CompoundButton) v).isChecked()) {
                    consumables.get(position).setFinished(TaskManager.currentTaskCalendar);
//                    moveToBottom(mDailyTasks, position);
//                    notifyDataSetChanged();
                } else {
                    consumables.get(position).setUnfinished(TaskManager.currentTaskCalendar);
//                    moveToBottom(mDailyTasks, position);
                }
                notifyDataSetChanged();
            }
        });

        // 之前日期不可修改
//        if (TaskManager.currentTaskCalendar.before(new Date())) {
//            // 如果任务已经失效
//            holder.mContentView.setTextColor(Color.GRAY);
//            holder.mContentView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//
//            holder.mCheckBox.setChecked(false);
//            holder.mCheckBox.setEnabled(false);
//
//            holder.routineTaskBounsTextview.setTextColor(Color.GRAY);
//            holder.routineTaskBounsTextview.setTextColor(Color.GRAY);
//
//            holder.routineTaskBounsValueTextview.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//            holder.routineTaskBounsValueTextview.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//        }
    }

    @Override
    public int getItemCount() {
        return consumables.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final TextView oneDayNumber;
        public final TextView totalNumber;
        public final TextView valueTextview;
        public final TextView totalValueTextview;

        public final ImageView consumableAdd;
        public final ImageView consumableReduce;

        public Consumable mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            oneDayNumber = (TextView) view.findViewById(R.id.txt_consumable_item_todayNumber);
            totalNumber = (TextView) view.findViewById(R.id.txt_consumable_item_totalNumber);
            mContentView = (TextView) view.findViewById(R.id.txt_consumable_item_title);

            valueTextview = (TextView) view.findViewById(R.id.txt_consumable_item_price);
            totalValueTextview = (TextView) view.findViewById(R.id.txt_consumable_item_totalconsuming);

            consumableAdd = (ImageView) view.findViewById(R.id.btn_consumable_item_add);
            consumableReduce = (ImageView) view.findViewById(R.id.btn_consumable_item_reduce);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}

package com.ecnu.leon.jarvis.model.task.routinetask;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ecnu.leon.jarvis.R;
import com.ecnu.leon.jarvis.model.task.dailytask.TaskManager;
import com.ecnu.leon.jarvis.tasks.ui.dummy.DummyContent.DummyItem;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link RoutineTaskFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class RoutineTaskRecyclerViewAdapter extends RecyclerView.Adapter<RoutineTaskRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<RoutineTask> routineTasks;
    private Context mContext;

    private final RoutineTaskFragment.OnListFragmentInteractionListener mListener;

    public RoutineTaskRecyclerViewAdapter(ArrayList<RoutineTask> items, RoutineTaskFragment.OnListFragmentInteractionListener listener, Context context) {
        routineTasks = items;
        mContext = context;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_routinetask, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        initViewHolder(holder, position);

        holder.mItem = routineTasks.get(position);


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
                    routineTasks.get(position).setFinished(TaskManager.currentTaskCalendar);
//                    moveToBottom(mDailyTasks, position);
//                    notifyDataSetChanged();
                } else {
                    routineTasks.get(position).setUnfinished(TaskManager.currentTaskCalendar);
//                    moveToBottom(mDailyTasks, position);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return routineTasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final CheckBox mCheckBox;

        public final TextView routineTaskBounsTextview;
        public final TextView routineTaskBounsValueTextview;

        public RoutineTask mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCheckBox = (CheckBox) view.findViewById(R.id.checkbox_routineTask);
            mContentView = (TextView) view.findViewById(R.id.text_routineTask_title);

            routineTaskBounsTextview = (TextView) view.findViewById(R.id.text_routineTask_bouns);
            routineTaskBounsValueTextview = (TextView) view.findViewById(R.id.text_routineTask_bouns_value);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}

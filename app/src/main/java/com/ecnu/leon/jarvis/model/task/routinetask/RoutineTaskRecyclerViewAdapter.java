package com.ecnu.leon.jarvis.model.task.routinetask;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ecnu.leon.jarvis.R;
import com.ecnu.leon.jarvis.Utils.DialogUtil;
import com.ecnu.leon.jarvis.model.task.Task;
import com.ecnu.leon.jarvis.model.task.TaskManager;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


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
        TaskManager.setDateChanged(true);

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
        holder.mContentView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                String[] itemsStrings =
                        {"			修改此任务", "			删除此任务", "			显示任务详细状态", "			移到顶部", "			移到底部"};
                DialogUtil.createListDialog(mContext, -1, "做些什么：", itemsStrings, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // 设置任务失败函数
                                amendTask(routineTasks, position);
                                break;
//
                            case 1:
                                // 修改任务
                                Toast.makeText(mContext, "删除此任务", Toast.LENGTH_SHORT).show();
                                break;
//
                            case 2:
                                // 删除任务
                                Toast.makeText(mContext, "显示任务详细状态", Toast.LENGTH_SHORT).show();
                                break;
//
                            case 3:
                                // 显示任务详情
                                Toast.makeText(mContext, "移到顶部", Toast.LENGTH_SHORT).show();
                                break;
//
                            case 4: {
                                // 加入明日任务
                                Toast.makeText(mContext, "移到底部", Toast.LENGTH_SHORT).show();
                                break;
                            }

//                            default:
//                                break;
                        }
                    }
                });
                return false;
            }
        });
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

    private boolean amendTask(final ArrayList<RoutineTask> routineTasks, final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("修改任务");
        View view = null;
        // 关联XML文件，用于布局dialog
        switch (routineTasks.get(position).getTaskType()) {
            case Task.ROUTINE_TASK_TYPE:
                view = LayoutInflater.from(mContext).inflate(R.layout.dlg_routine_task_add, null);
                break;
//            case Task.STEPPING_DAILY_TASK_TYPE:
//                view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_dailytask_stepping, null);
//
//                break;

            default:
                view = LayoutInflater.from(mContext).inflate(R.layout.dlg_routine_task_add, null);
                break;
        }


        final EditText nameEditText = (EditText) view.findViewById(R.id.edt_routineTask_add_content);
        nameEditText.setText(routineTasks.get(position).getContent());

        final EditText valueEditText = (EditText) view.findViewById(R.id.edt_routineTask_add_value);
        valueEditText.setText(routineTasks.get(position).getTaskValue() + "");

        Boolean[] weeks = routineTasks.get(position).getDaysOfWeek();

        ((CheckBox) view.findViewById(R.id.checkbox_routineTask_week1)).setChecked(weeks[1]);
        ((CheckBox) view.findViewById(R.id.checkbox_routineTask_week2)).setChecked(weeks[2]);
        ((CheckBox) view.findViewById(R.id.checkbox_routineTask_week3)).setChecked(weeks[3]);
        ((CheckBox) view.findViewById(R.id.checkbox_routineTask_week4)).setChecked(weeks[4]);
        ((CheckBox) view.findViewById(R.id.checkbox_routineTask_week5)).setChecked(weeks[5]);
        ((CheckBox) view.findViewById(R.id.checkbox_routineTask_week6)).setChecked(weeks[6]);
        ((CheckBox) view.findViewById(R.id.checkbox_routineTask_week7)).setChecked(weeks[7]);

        ((CheckBox) view.findViewById(R.id.checkbox_routineTask_is_step)).setChecked(routineTasks.get(position).isMoreTimes());


//        final EditText rateEditText = (EditText) view.findViewById(R.id.edt_dailytask_add_);
//        rateEditText.setText(taskContainer.get(position).getNegativeEnergyRate() + "");

        // 自动弹出软键盘
        nameEditText.setFocusable(true);
        nameEditText.setFocusableInTouchMode(true);
        nameEditText.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) nameEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(nameEditText, 0);
            }

        }, 300);

        builder.setView(view);
        final View finalView = view;
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String titleString = nameEditText.getText().toString().trim();
                if (titleString.equals("")) {
                    Toast.makeText(mContext, "输入内容不可以为空", Toast.LENGTH_SHORT).show();
                } else {
                    routineTasks.get(position).setContent(nameEditText.getText().toString().trim());
                    routineTasks.get(position).setTaskValue(Integer.valueOf(valueEditText.getText().toString().trim()));

                    Boolean[] weeks = new Boolean[8];
                    weeks[1] = ((CheckBox) finalView.findViewById(R.id.checkbox_routineTask_week1)).isChecked();
                    weeks[2] = ((CheckBox) finalView.findViewById(R.id.checkbox_routineTask_week2)).isChecked();
                    weeks[3] = ((CheckBox) finalView.findViewById(R.id.checkbox_routineTask_week3)).isChecked();
                    weeks[4] = ((CheckBox) finalView.findViewById(R.id.checkbox_routineTask_week4)).isChecked();
                    weeks[5] = ((CheckBox) finalView.findViewById(R.id.checkbox_routineTask_week5)).isChecked();
                    weeks[6] = ((CheckBox) finalView.findViewById(R.id.checkbox_routineTask_week6)).isChecked();
                    weeks[7] = ((CheckBox) finalView.findViewById(R.id.checkbox_routineTask_week7)).isChecked();

                    routineTasks.get(position).setDaysOfWeek(weeks.clone());
                    routineTasks.get(position).setMoreTimes(((CheckBox) finalView.findViewById(R.id.checkbox_routineTask_is_step)).isChecked());
                    notifyDataSetChanged();
                }

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 暂不处理
            }
        });
        builder.show();
        notifyDataSetChanged();

        return true;
    }
}

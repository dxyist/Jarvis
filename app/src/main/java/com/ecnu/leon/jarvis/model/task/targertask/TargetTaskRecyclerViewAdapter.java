package com.ecnu.leon.jarvis.model.task.targertask;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ecnu.leon.jarvis.R;
import com.ecnu.leon.jarvis.Utils.DialogUtil;
import com.ecnu.leon.jarvis.model.task.TaskManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class TargetTaskRecyclerViewAdapter extends RecyclerView.Adapter<TargetTaskRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<TargetTask> TargetTasks;
    private Context mContext;

    private final TargetTaskFragment.OnListFragmentInteractionListener mListener;

    public TargetTaskRecyclerViewAdapter(ArrayList<TargetTask> items, TargetTaskFragment.OnListFragmentInteractionListener listener, Context context) {
        TargetTasks = items;
        mContext = context;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_target_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        TaskManager.setDateChanged(true);

        initViewHolder(holder, position);

        holder.mItem = TargetTasks.get(position);

    }

    private void initViewHolder(ViewHolder holder, final int position) {

        String string = "";
        string = position + 1 + "：" + TargetTasks.get(position).getContent();
        holder.mContentView.setText(string);

        SimpleDateFormat format = new SimpleDateFormat("截止日期：yyyy-MM-dd (EE) HH:mm");
        String dateString = format.format(TargetTasks.get(position).getDeadlineDate());
        holder.mDeadlineView.setText(dateString);

        int value = TargetTasks.get(position).getTaskValue();
        int rate = TargetTasks.get(position).getTaskPunchRate();
        holder.targetTaskBounsValueTextview.setText(value + "");

        holder.targetTaskPunchValueTextview.setText(rate * value + "");
        holder.mCheckBox.setChecked(TargetTasks.get(position).isFinished());
        // 先设置好颜色
        holder.mDeadlineView.setTextColor(Color.GRAY);
        holder.targetTaskBounsValueTextview.setTextColor(Color.argb(255, 99, 204, 33));
        holder.targetTaskBounsTextview.setTextColor(Color.argb(255, 99, 204, 33));
        holder.targetTaskPunchValueTextview.setTextColor(Color.argb(255, 255, 66, 00));
        holder.targetTaskPunchTextview.setTextColor(Color.argb(255, 255, 66, 00));

        // 消除划线效果
        holder.targetTaskBounsValueTextview.getPaint().setFlags(0);
        holder.targetTaskBounsTextview.getPaint().setFlags(0);
        holder.targetTaskPunchValueTextview.getPaint().setFlags(0);
        holder.targetTaskPunchTextview.getPaint().setFlags(0);

        // 抗锯齿
        holder.targetTaskBounsValueTextview.getPaint().setAntiAlias(true);
        holder.targetTaskBounsTextview.getPaint().setAntiAlias(true);
        holder.targetTaskPunchValueTextview.getPaint().setAntiAlias(true);
        holder.targetTaskPunchTextview.getPaint().setAntiAlias(true);

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
                                amendTask(TargetTasks, position);
                                break;
//
                            case 1:
                                // 删除任务
                                deleteTask(TargetTasks, position);
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
//        holder.mCheckBox.setEnabled(true);

        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            // 不能调用外部的View
            @Override
            public void onClick(View v) {

                if (((CompoundButton) v).isChecked()) {
                    if (TargetTasks.get(position).isOverdue(new Date())) {
                        TargetTasks.get(position).setOverdueFinished();
                    } else {
                        TargetTasks.get(position).setFinished();
                    }
                    notifyDataSetChanged();
                } else {
                    TargetTasks.get(position).setUnfinished();
//                    moveToBottom(mDailyTasks, position);
                }
                notifyDataSetChanged();
            }
        });


        // 任务是否完成后的状态
        if (TargetTasks.get(position).isFinished()) {
            holder.mContentView.setTextColor(Color.GRAY);
            holder.mDeadlineView.setText("已完成");
            holder.mDeadlineHintView.setText("0");
            holder.mDeadlineView.setTextColor(Color.GRAY);
            holder.targetTaskPunchValueTextview.setTextColor(Color.GRAY);
            holder.targetTaskPunchTextview.setTextColor(Color.GRAY);

            holder.targetTaskPunchValueTextview.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.targetTaskPunchTextview.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            // 设置任务难度颜色
            switch (TargetTasks.get(position).getPriority()) {
                case TargetTask.TASK_PRIORITY_TRIVIA:
                    holder.mContentView.setTextColor(Color.argb(255, 0, 0, 0));
                    break;
                case TargetTask.TASK_PRIORITY_NORMAL:
                    holder.mContentView.setTextColor(Color.argb(255, 50, 205, 50));
                    break;
                case TargetTask.TASK_PRIORITY_IMPORTANT:
                    holder.mContentView.setTextColor(Color.argb(255, 75, 105, 255));
                    break;
                case TargetTask.TASK_PRIORITY_VERY_IMPORTANT:
                    holder.mContentView.setTextColor(Color.argb(255, 228, 174, 57));
                    break;
                default:
                    holder.mContentView.setTextColor(Color.argb(255, 0, 0, 0));
                    break;
            }

            format = new SimpleDateFormat("截止日期：yyyy-MM-dd (EE) HH:mm");
            dateString = format.format(TargetTasks.get(position).getDeadlineDate());
            holder.mDeadlineView.setText(dateString);
            holder.mDeadlineHintView.setText("" + TargetTasks.get(position).getRemainTs() / (1000 * 24 * 60 * 60));
            holder.targetTaskBounsValueTextview.setTextColor(Color.GRAY);
            holder.targetTaskBounsTextview.setTextColor(Color.GRAY);

            holder.targetTaskBounsValueTextview.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.targetTaskBounsTextview.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            holder.mContentView.getPaint().setFlags(0);
            holder.mContentView.getPaint().setAntiAlias(true);

            if (TargetTasks.get(position).isOverdue(new Date())) {
                holder.mDeadlineView.setText("已超期");
                holder.mContentView.setTextColor(Color.GRAY);
                holder.mContentView.setLongClickable(false);

                holder.mDeadlineView.setTextColor(Color.argb(255, 238, 56, 56));

                holder.targetTaskBounsValueTextview.setTextColor(Color.GRAY);
                holder.targetTaskBounsTextview.setTextColor(Color.GRAY);

                holder.targetTaskBounsValueTextview.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                holder.targetTaskBounsTextview.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            }

            // 两天之内开始警告
            if (TargetTasks.get(position).getRemainTs() >= 0) {
                // 一天以内
                if (TargetTasks.get(position).getRemainTs() <= 24 * 60 * 60 * 1000 * 1) {
                    holder.mDeadlineView.setTextColor(Color.argb(255, 238, 56, 56));

                } else if (TargetTasks.get(position).getRemainTs() <= 24 * 60 * 60 * 1000 * 2) {
                    holder.mDeadlineView.setTextColor(Color.argb(255, 238, 169, 184));
                }

            }
        }
        // 超期完成的特别处理
        if (TargetTasks.get(position).isOverdueFinished()) {
            holder.mDeadlineView.setText("已超期(完成)");
            holder.mContentView.setTextColor(Color.GRAY);
            holder.mContentView.setLongClickable(false);
            holder.mDeadlineView.setTextColor(Color.GRAY);

            holder.targetTaskBounsValueTextview.setTextColor(Color.GRAY);
            holder.targetTaskBounsValueTextview.setText(0 + "");

            holder.targetTaskBounsTextview.setTextColor(Color.GRAY);

            holder.targetTaskBounsValueTextview.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.targetTaskBounsTextview.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }


    }

    @Override
    public int getItemCount() {
        return TargetTasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final TextView mDeadlineView;
        public final TextView mDeadlineHintView;
        public final CheckBox mCheckBox;

        public final TextView targetTaskBounsTextview;
        public final TextView targetTaskBounsValueTextview;
        public final TextView targetTaskPunchTextview;
        public final TextView targetTaskPunchValueTextview;

        public TargetTask mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCheckBox = (CheckBox) view.findViewById(R.id.checkbox_target_task);
            mContentView = (TextView) view.findViewById(R.id.text_target_task_title);
            mDeadlineView = (TextView) view.findViewById(R.id.text_target_task_deadline);
            mDeadlineHintView = (TextView) view.findViewById(R.id.text_target_task_deadline_hint);
            targetTaskBounsTextview = (TextView) view.findViewById(R.id.text_target_task_bouns);
            targetTaskBounsValueTextview = (TextView) view.findViewById(R.id.text_target_task_bouns_value);
            targetTaskPunchTextview = (TextView) view.findViewById(R.id.text_target_task_punch);
            targetTaskPunchValueTextview = (TextView) view.findViewById(R.id.text_target_task_punch_value);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    private boolean deleteTask(final ArrayList<TargetTask> targetTasks, final int position) {
        // 标记
        targetTasks.get(position).setRemoved();
        // 临时列表离删除掉
        targetTasks.remove(targetTasks.get(position));
        TaskManager.setDateChanged(true);
        notifyDataSetChanged();
        return true;
    }

    private boolean amendTask(final ArrayList<TargetTask> TargetTasks, final int position) {

        /**
         AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
         builder.setTitle("修改任务");
         View view = null;
         // 关联XML文件，用于布局dialog
         switch (TargetTasks.get(position).getTaskType()) {
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


         final EditText nameEditText = (EditText) view.findViewById(R.id.edt_TargetTask_add_content);
         nameEditText.setText(TargetTasks.get(position).getContent());

         final EditText valueEditText = (EditText) view.findViewById(R.id.edt_TargetTask_add_value);
         valueEditText.setText(TargetTasks.get(position).getTaskValue() + "");

         Boolean[] weeks = TargetTasks.get(position).getDaysOfWeek();

         ((CheckBox) view.findViewById(R.id.checkbox_TargetTask_week1)).setChecked(weeks[1]);
         ((CheckBox) view.findViewById(R.id.checkbox_TargetTask_week2)).setChecked(weeks[2]);
         ((CheckBox) view.findViewById(R.id.checkbox_TargetTask_week3)).setChecked(weeks[3]);
         ((CheckBox) view.findViewById(R.id.checkbox_TargetTask_week4)).setChecked(weeks[4]);
         ((CheckBox) view.findViewById(R.id.checkbox_TargetTask_week5)).setChecked(weeks[5]);
         ((CheckBox) view.findViewById(R.id.checkbox_TargetTask_week6)).setChecked(weeks[6]);
         ((CheckBox) view.findViewById(R.id.checkbox_TargetTask_week7)).setChecked(weeks[7]);

         ((CheckBox) view.findViewById(R.id.checkbox_TargetTask_is_step)).setChecked(TargetTasks.get(position).isMoreTimes());


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
        @Override public void onClick(DialogInterface dialog, int which) {
        String titleString = nameEditText.getText().toString().trim();
        if (titleString.equals("")) {
        Toast.makeText(mContext, "输入内容不可以为空", Toast.LENGTH_SHORT).show();
        } else {
        TargetTasks.get(position).setContent(nameEditText.getText().toString().trim());
        TargetTasks.get(position).setTaskValue(Integer.valueOf(valueEditText.getText().toString().trim()));

        Boolean[] weeks = new Boolean[8];
        weeks[1] = ((CheckBox) finalView.findViewById(R.id.checkbox_TargetTask_week1)).isChecked();
        weeks[2] = ((CheckBox) finalView.findViewById(R.id.checkbox_TargetTask_week2)).isChecked();
        weeks[3] = ((CheckBox) finalView.findViewById(R.id.checkbox_TargetTask_week3)).isChecked();
        weeks[4] = ((CheckBox) finalView.findViewById(R.id.checkbox_TargetTask_week4)).isChecked();
        weeks[5] = ((CheckBox) finalView.findViewById(R.id.checkbox_TargetTask_week5)).isChecked();
        weeks[6] = ((CheckBox) finalView.findViewById(R.id.checkbox_TargetTask_week6)).isChecked();
        weeks[7] = ((CheckBox) finalView.findViewById(R.id.checkbox_TargetTask_week7)).isChecked();

        TargetTasks.get(position).setDaysOfWeek(weeks.clone());
        TargetTasks.get(position).setMoreTimes(((CheckBox) finalView.findViewById(R.id.checkbox_TargetTask_is_step)).isChecked());
        notifyDataSetChanged();
        }

        }
        });
         builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

        @Override public void onClick(DialogInterface dialog, int which) {
        // 暂不处理
        }
        });
         builder.show();
         notifyDataSetChanged();
         */
        return true;

    }
}

package com.ecnu.leon.jarvis.model.task.dailytask;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
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

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link DailyTaskFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class DailyTaskRecyclerViewAdapter extends RecyclerView.Adapter<DailyTaskRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<DailyTask> mDailyTasks;
    private Context mContext;
    private final DailyTaskFragment.OnListFragmentInteractionListener mListener;

    public DailyTaskRecyclerViewAdapter(ArrayList<DailyTask> items, DailyTaskFragment.OnListFragmentInteractionListener listener, Context context) {
        mDailyTasks = items;
        mListener = listener;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dailytask, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        initViewHolder(holder, position);

        holder.mItem = mDailyTasks.get(position);
        holder.mContentView.setText(position + 1 + ": " + mDailyTasks.get(position).getContent());
        holder.mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDailyTasks.get(position).setFailed();
                notifyDataSetChanged();
            }
        });

        holder.mContentView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                String[] itemsStrings =
                        {"			任务已失败", "			修改此任务", "			删除此任务", "			显示任务详细状态", "			加入明日任务", "			移到顶部", "			移到底部"};
                DialogUtil.createListDialog(mContext, -1, "做些什么：", itemsStrings, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // 设置任务失败函数
                                setTaskFailed(mDailyTasks, position);
                                break;
//
                            case 1:
                                // 修改任务
                                amendTask(mDailyTasks, position);
                                break;
//
                            case 2:
                                // 删除任务
                                deleteTask(mDailyTasks, position);
                                break;
//
                            case 3:
                                // 显示任务详情
                                showTaskDetails(mDailyTasks, position);
                                break;
//
//                            case 4: {
//                                // 加入明日任务
//                                addToTomorrow(mDailyTasks, position);
//                                break;
//                            }
//
                            case 5: {
                                // 移到任务底顶部
                                moveToTop(mDailyTasks, position);

                                notifyDataSetChanged();
                                break;
                            }

                            case 6: {
                                // 移到任务底部
                                moveToBottom(mDailyTasks, position);
                                notifyDataSetChanged();
                                break;
                            }
//
//                            default:
//                                break;
                        }
                    }
                });
                return false;
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }


    private boolean setTaskFailed(final ArrayList<DailyTask> dailyTasks, final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("确认任务已经失败么，此操作不可逆！");
        builder.setTitle("提示");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dailyTasks.get(position).setFailed();
                moveToBottom(dailyTasks, position);
                dialog.dismiss();
                notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dailyTasks.get(position).setUnfinished();
                moveToBottom(dailyTasks, position);
                dialog.dismiss();
                notifyDataSetChanged();
            }
        });
        builder.create().show();

        return true;
    }

    private boolean deleteTask(final ArrayList<DailyTask> dailyTasks, final int position) {
        mDailyTasks.remove(mDailyTasks.get(position));
        notifyDataSetChanged();
        return true;
    }


    // 算法逻辑需要清晰化
    private boolean moveToBottom(ArrayList<DailyTask> arrayList, int position) {
        DailyTask dailyTask = arrayList.get(position);
        arrayList.remove(position);
        System.out.println("调用一次");

        for (int i = arrayList.size() - 1; i >= 0; i--) {
            if (arrayList.get(i).isFinished() || arrayList.get(i).isFailed())
                continue;

            // 出现了增加情况直接跳出
            arrayList.add(i + 1, dailyTask);
            return true;

        }
        //
        arrayList.add(0, dailyTask);
        return true;
    }

    private boolean moveToTop(ArrayList<DailyTask> arrayList, int position) {
        DailyTask dailyTask = arrayList.get(position);
        arrayList.remove(position);

        arrayList.add(0, dailyTask);
        return true;
    }


//    private boolean addToTomorrow(final ArrayList<DailyTask> dailyTasks, final int position) {
//        GregorianCalendar calendar = (GregorianCalendar) new GregorianCalendar().clone();
//        calendar.setTimeInMillis(calendar.getTimeInMillis() + 86400 * 1000);
//        long id = MainActivity.getTaskID();
//        DailyTask dailyTask = new DailyTask(id, taskContainer.get(position).getName(), (GregorianCalendar) calendar.clone());
//
//        dailyTask.setPositiveEnergyValue(taskContainer.get(position).getPositiveEnergyValue());
//        dailyTask.setNegativeEnergyRate(taskContainer.get(position).getNegativeEnergyRate());
//
//        switch (taskContainer.get(position).getTaskType()) {
//            case Task.STEPPING_DAILY_TASK_TYPE:
//
//                SteppingDailyTask steppingDailyTask = new SteppingDailyTask(dailyTask.getID(), dailyTask.getName(), dailyTask.getCreateDate());
//                steppingDailyTask.setFinishedLevel(((SteppingDailyTask) taskContainer.get(position)).getFinishedLevel());
//                steppingDailyTask.setPositiveEnergyValue(taskContainer.get(position).getPositiveEnergyValue());
//                steppingDailyTask.setNegativeEnergyRate(taskContainer.get(position).getNegativeEnergyRate());
//                dailyTask = steppingDailyTask;
//                break;
//
//            default:
//                break;
//        }
//        MainActivity.dailyTaskContainter.addDailyTask(dailyTask);
//        return true;
//    }


    private boolean showTaskDetails(final ArrayList<DailyTask> dailyTasks, final int position) {
        Toast.makeText(
                mContext,
                "任务ID：" + dailyTasks.get(position).getID() + "\n" + "任务名称：" + dailyTasks.get(position).getContent() + "\n" + "任务是否完成：" + dailyTasks.get(position).isFinished() + "\n"
                        + "任务是否失败：" + dailyTasks.get(position).isFailed(), Toast.LENGTH_SHORT).show();
        // 追加+++

        return true;
    }

    private boolean amendTask(final ArrayList<DailyTask> dailyTasks, final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("修改任务");
        View view = null;
        // 关联XML文件，用于布局dialog
        switch (dailyTasks.get(position).getTaskType()) {
            case Task.DAILY_TASK_TYPE:
                view = LayoutInflater.from(mContext).inflate(R.layout.dlg_dailytask_add, null);
                break;
//            case Task.STEPPING_DAILY_TASK_TYPE:
//                view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_dailytask_stepping, null);
//
//                break;

            default:
                view = LayoutInflater.from(mContext).inflate(R.layout.dlg_dailytask_add, null);
                break;
        }


        final EditText nameEditText = (EditText) view.findViewById(R.id.edt_dailytask_add_content);
        nameEditText.setText(dailyTasks.get(position).getContent());

        final EditText valueEditText = (EditText) view.findViewById(R.id.edt_dailytask_add_value);
        valueEditText.setText(dailyTasks.get(position).getTaskValue() + "");

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
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String titleString = nameEditText.getText().toString().trim();
                if (titleString.equals("")) {
                    Toast.makeText(mContext, "输入内容不可以为空", Toast.LENGTH_SHORT).show();
                } else {
                    dailyTasks.get(position).setContent(nameEditText.getText().toString().trim());
                    dailyTasks.get(position).setTaskValue(Integer.valueOf(valueEditText.getText().toString().trim()));
//                    dailyTasks.get(position).setNegativeEnergyRate(Integer.valueOf(rateEditText.getText().toString().trim()));

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


    private void initViewHolder(final ViewHolder holder, final int position) {

        String string = "";
        string = position + 1 + "：" + mDailyTasks.get(position).getContent();
        holder.mContentView.setText(string);
        int value = mDailyTasks.get(position).getTaskValue();
        int rate = mDailyTasks.get(position).getTaskPunchRate();
        holder.dailytaskBounsValueTextview.setText(value + "");
        holder.dailytaskPunchValueTextview.setText(rate * value + "");
        holder.mCheckBox.setChecked(mDailyTasks.get(position).isFinished());

        // 先设置好颜色
        holder.dailytaskBounsValueTextview.setTextColor(Color.argb(255, 99, 204, 33));
        holder.dailytaskBounsTextview.setTextColor(Color.argb(255, 99, 204, 33));
        holder.dailytaskPunchValueTextview.setTextColor(Color.argb(255, 255, 66, 00));
        holder.dailytaskPunchTextview.setTextColor(Color.argb(255, 255, 66, 00));
        // 消除划线效果
        holder.dailytaskBounsValueTextview.getPaint().setFlags(0);
        holder.dailytaskBounsTextview.getPaint().setFlags(0);
        holder.dailytaskPunchValueTextview.getPaint().setFlags(0);
        holder.dailytaskPunchTextview.getPaint().setFlags(0);
        // 抗锯齿
        holder.dailytaskBounsValueTextview.getPaint().setAntiAlias(true);
        holder.dailytaskBounsTextview.getPaint().setAntiAlias(true);
        holder.dailytaskPunchValueTextview.getPaint().setAntiAlias(true);
        holder.dailytaskPunchTextview.getPaint().setAntiAlias(true);

        // 任务是否完成后的状态
        if (mDailyTasks.get(position).isFinished()) {
            holder.mContentView.setTextColor(Color.GRAY);
            holder.dailytaskPunchValueTextview.setTextColor(Color.GRAY);
            holder.dailytaskPunchTextview.setTextColor(Color.GRAY);

            holder.dailytaskPunchValueTextview.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.dailytaskPunchTextview.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            // 设置任务难度颜色
            switch (mDailyTasks.get(position).getTaskValue()) {
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

        boolean isTaskFailed = mDailyTasks.get(position).isFailed();
        holder.mCheckBox.setEnabled(true);
        // 先取消flag设置，以免内部属性在删除过程中影响后面的操作
        holder.mContentView.getPaint().setFlags(0);
        holder.mContentView.getPaint().setAntiAlias(true);
        if (isTaskFailed) {
            // 如果任务已经失效
            holder.mContentView.setTextColor(Color.GRAY);
            holder.mContentView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            holder.mCheckBox.setChecked(false);
            holder.mCheckBox.setEnabled(false);

            holder.dailytaskBounsValueTextview.setTextColor(Color.GRAY);
            holder.dailytaskBounsTextview.setTextColor(Color.GRAY);

            holder.dailytaskBounsValueTextview.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.dailytaskBounsTextview.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }

        holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            // 不能调用外部的View
            @Override
            public void onClick(View v) {

                if (((CompoundButton) v).isChecked()) {
                    mDailyTasks.get(position).setFinished();
                    moveToBottom(mDailyTasks, position);
                    notifyDataSetChanged();
                } else {
                    mDailyTasks.get(position).setUnfinished();
                    moveToBottom(mDailyTasks, position);
                }
                notifyDataSetChanged();
            }
        });


    }


    @Override
    public int getItemCount() {

        if (mDailyTasks == null)
            return 0;
        else
            return mDailyTasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public final CheckBox mCheckBox;

        public final TextView dailytaskBounsTextview;
        public final TextView dailytaskBounsValueTextview;
        public final TextView dailytaskPunchTextview;
        public final TextView dailytaskPunchValueTextview;
        public DailyTask mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCheckBox = (CheckBox) view.findViewById(R.id.checkbox_dailytask);
            mContentView = (TextView) view.findViewById(R.id.text_dailytask_title);

            dailytaskBounsTextview = (TextView) view.findViewById(R.id.text_dailytask_bouns);
            dailytaskBounsValueTextview = (TextView) view.findViewById(R.id.text_dailytask_bouns_value);
            dailytaskPunchTextview = (TextView) view.findViewById(R.id.text_dailytask_punch);
            dailytaskPunchValueTextview = (TextView) view.findViewById(R.id.text_dailytask_punch_value);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }


}

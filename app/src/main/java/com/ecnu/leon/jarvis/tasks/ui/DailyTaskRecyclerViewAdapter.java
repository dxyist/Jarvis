package com.ecnu.leon.jarvis.tasks.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.ecnu.leon.jarvis.R;
import com.ecnu.leon.jarvis.Utils.DialogUtil;
import com.ecnu.leon.jarvis.tasks.item.DailyTask;
import com.ecnu.leon.jarvis.tasks.item.Task;
import com.ecnu.leon.jarvis.tasks.ui.DailyTaskFragment.OnListFragmentInteractionListener;
import com.ecnu.leon.jarvis.tasks.ui.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class DailyTaskRecyclerViewAdapter extends RecyclerView.Adapter<DailyTaskRecyclerViewAdapter.ViewHolder> {

    private final List<DailyTask> mDailyTasks;
    private Context mContext;
    private final OnListFragmentInteractionListener mListener;

    public DailyTaskRecyclerViewAdapter(List<DailyTask> items, OnListFragmentInteractionListener listener, Context context) {
        mDailyTasks = items;
        mListener = listener;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mDailyTasks.get(position);
        holder.mIdView.setText(Integer.valueOf(mDailyTasks.get(position).getTaskValue()) + ":");

        holder.mIdView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mDailyTasks.get(position).getWhiteDateFormatString(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.mContentView.setText(mDailyTasks.get(position).getContent());
        holder.mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDailyTasks.remove(position);
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
//                            case 0:
//                                // 设置任务失败函数
//                                setTaskFailed(mDailyTasks, position);
//                                break;
//
//                            case 1:
//                                // 修改任务
//                                amendTask(mDailyTasks, position);
//                                break;
//
//                            case 2:
//                                // 删除任务
//                                deleteTask(mDailyTasks, position);
//                                break;
//
//                            case 3:
//                                // 显示任务详情
//                                showTaskDetails(mDailyTasks, position);
//                                break;
//
//                            case 4: {
//                                // 加入明日任务
//                                addToTomorrow(mDailyTasks, position);
//                                break;
//                            }
//
//                            case 5: {
//                                // 移到任务底顶部
//                                moveToTop(mDailyTasks, position);
//
//                                onResume();
//                                break;
//                            }
//
//                            case 6: {
//                                // 移到任务底部
//                                moveToBottom(mDailyTasks, position);
//                                onResume();
//                                break;
//                            }
//
//                            default:
//                                break;
                        }
                    }
                });
                return false;
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
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


    @Override
    public int getItemCount() {

        if (mDailyTasks == null)
            return 0;
        else
            return mDailyTasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final CheckBox mCheckBox;
        public DailyTask mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.text_task_order_number);
            mCheckBox = (CheckBox) view.findViewById(R.id.checkbox_dailytask);
            mContentView = (TextView) view.findViewById(R.id.text_dailytask_title);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }


}

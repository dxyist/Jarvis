package com.ecnu.leon.jarvis.model.task.consumable;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ecnu.leon.jarvis.R;
import com.ecnu.leon.jarvis.Utils.BackgroundExecutor;
import com.ecnu.leon.jarvis.model.task.TaskManager;

import java.util.ArrayList;
import java.util.Date;


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
        string = position + 1 + "：" + consumables.get(position).getContent();
        holder.mContentView.setText(string);
        int value = consumables.get(position).getPrice();
        int totalValue = consumables.get(position).getTotalCost();
        holder.valueTextview.setText(value + "");
        holder.totalValueTextview.setText(totalValue + "");


        // 抗锯齿
        holder.mContentView.getPaint().setAntiAlias(true);
        holder.valueTextview.getPaint().setAntiAlias(true);
        holder.oneDayNumber.getPaint().setAntiAlias(true);
        holder.totalNumber.getPaint().setAntiAlias(true);

        holder.oneDayNumber.setText(consumables.get(position).getOneDayNumber(TaskManager.currentTaskCalendar) + "");
        holder.totalNumber.setText(consumables.get(position).getTotalNumber(TaskManager.currentTaskCalendar) + "");


        holder.consumableAdd.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                consumables.get(position).addOnce(TaskManager.currentTaskCalendar);
                notifyDataSetChanged();
            }
        });

        holder.consumableReduce.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                consumables.get(position).reduceOnce(TaskManager.currentTaskCalendar);
                notifyDataSetChanged();
            }
        });


        holder.mContentView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext, "is hide" + consumables.get(position).isHide(), Toast.LENGTH_SHORT).show();
                notifyDataSetChanged();
                return false;
            }
        });
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

            consumableAdd = (ImageButton) view.findViewById(R.id.btn_consumable_item_add);
            consumableReduce = (ImageButton) view.findViewById(R.id.btn_consumable_item_reduce);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}

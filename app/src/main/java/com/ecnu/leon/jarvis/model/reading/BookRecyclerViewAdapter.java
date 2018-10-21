package com.ecnu.leon.jarvis.model.reading;

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
import com.ecnu.leon.jarvis.model.reading.model.Book;
import com.ecnu.leon.jarvis.model.task.TaskManager;
import com.ecnu.leon.jarvis.model.task.consumable.Consumable;
import com.ecnu.leon.jarvis.model.task.consumable.ConsumableFragment;

import java.util.ArrayList;


public class BookRecyclerViewAdapter extends RecyclerView.Adapter<BookRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Book> books;
    private Context mContext;

    private final ReadingFragment.OnListFragmentInteractionListener mListener;

    public BookRecyclerViewAdapter(ArrayList<Book> items, ReadingFragment.OnListFragmentInteractionListener listener, Context context) {
        books = items;
        mContext = context;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_book, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        initViewHolder(holder, position);

        holder.mItem = books.get(position);


    }

    private void initViewHolder(ViewHolder holder, final int position) {

        String string = "";
        string = position + 1 + "：" + books.get(position).getTitle();
        holder.titleTextview.setText(string);
        int readPageNumber = books.get(position).getReadPageNumber();
        int totalPageNumber = books.get(position).getPageNumber();
        String readStatusString = readPageNumber + "/" + totalPageNumber + "页";
        holder.subtitleTextview.setText(books.get(position).getSubtitle());
        holder.readingStatusTextview.setText(readStatusString + "");


        // 抗锯齿
        holder.titleTextview.getPaint().setAntiAlias(true);
        holder.readingStatusTextview.getPaint().setAntiAlias(true);
        holder.subtitleTextview.getPaint().setAntiAlias(true);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookActivity.startActivity(mContext, books.get(position).getId());
            }
        });


    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView titleTextview;
        public final TextView subtitleTextview;
        public final TextView readingStatusTextview;


        public Book mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            titleTextview = (TextView) view.findViewById(R.id.text_book_title);
            subtitleTextview = (TextView) view.findViewById(R.id.text_book_subtitle);
            readingStatusTextview = (TextView) view.findViewById(R.id.text_book_read_status);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + titleTextview.getText() + "'";
        }
    }
}

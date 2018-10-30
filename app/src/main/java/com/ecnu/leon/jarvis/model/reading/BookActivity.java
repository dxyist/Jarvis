package com.ecnu.leon.jarvis.model.reading;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ecnu.leon.jarvis.R;
import com.ecnu.leon.jarvis.model.reading.model.Bookmark;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class BookActivity extends AppCompatActivity {


    protected long bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bookId = getIntent().getIntExtra("book_id", 0);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BookActivity.this, R.style.NoBackGroundDialog);
                final View tempView = View
                        .inflate(BookActivity.this, R.layout.dlg_bookmark_add, null);
                builder.setView(tempView);
                builder.setCancelable(true);
                final AlertDialog dialog = builder.create();

                final EditText startEditText = (EditText) tempView.findViewById(R.id.edt_bookmark_page_start);
                // 自动弹出软键盘
                startEditText.setFocusable(true);
                startEditText.setFocusableInTouchMode(true);
                startEditText.requestFocus();
                Timer timer = new Timer();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    public void run() {
                        InputMethodManager inputManager = (InputMethodManager) startEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.showSoftInput(startEditText, 0);
                    }
                }, 400);


                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    public void onShow(DialogInterface dialog) {
                        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput((EditText) tempView.findViewById(R.id.edt_bookmark_page_start), InputMethodManager.SHOW_IMPLICIT);
                    }
                });

                final EditText endEditText = (EditText) tempView.findViewById(R.id.edt_bookmark_page_end);
                // 自动弹出软键盘
                endEditText.setFocusable(true);
                endEditText.setFocusableInTouchMode(true);
                endEditText.requestFocus();
                timer = new Timer();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    public void run() {
                        InputMethodManager inputManager = (InputMethodManager) startEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.showSoftInput(startEditText, 0);
                    }
                }, 400);


                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    public void onShow(DialogInterface dialog) {
                        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput((EditText) tempView.findViewById(R.id.edt_bookmark_page_end), InputMethodManager.SHOW_IMPLICIT);
                    }
                });


                ((Button) tempView.findViewById(R.id.btn_confirm)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int start_page = Integer.valueOf(startEditText.getText().toString().trim());
                        int end_page = Integer.valueOf(endEditText.getText().toString().trim());
                        boolean isValid = ReadingManager.getInstance(BookActivity.this).getBook(bookId).isBookmarkRangeValid(start_page, end_page);

                        if (isValid) {
                            ReadingManager.getInstance(BookActivity.this).getBook(bookId).addBookmark(start_page, end_page);
                            Toast.makeText(BookActivity.this, start_page + "to" + end_page, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(BookActivity.this, "invalid bookmark range !!!", Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();

                    }
                });

                ((Button) tempView.findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                //取消或确定按钮监听事件处理
                dialog.show();
            }

        });
    }

    public static void startActivity(Context context, long bookId) {
        final Intent intent = new Intent();
        intent.setClass(context, BookActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("book_id", bookId);
        context.startActivity(intent);
    }

    private class BookmarkRecyclerViewAdapter extends RecyclerView.Adapter<BookActivity.BookmarkRecyclerViewAdapter.ViewHolder> {
        private ArrayList<Bookmark> bookmarks;
        private Context mContext;


        public void refresh() {
            this.bookmarks = ReadingManager.getInstance(mContext).getBook(bookId).getBookMarks();
            notifyDataSetChanged();
        }

        public BookmarkRecyclerViewAdapter(ArrayList<Bookmark> bookmarks, Context context) {

            this.bookmarks = bookmarks;
            mContext = context;

        }

        @Override
        public BookActivity.BookmarkRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_account, parent, false);
            return new BookActivity.BookmarkRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final BookActivity.BookmarkRecyclerViewAdapter.ViewHolder holder, int position) {
            initViewHolder(holder, position);

            holder.mItem = bookmarks.get(position);
        }

        @Override
        public int getItemCount() {
            return bookmarks.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView pageRangeTextView;
            public final TextView deadlineTextView;
            public final CheckBox reviewCheckBox1;
            public final CheckBox reviewCheckBox2;
            public final CheckBox reviewCheckBox3;
            public final CheckBox reviewCheckBox4;
            public final CheckBox reviewCheckBox5;
            public final CheckBox reviewCheckBox6;
            public final CheckBox reviewCheckBox7;


            public Bookmark mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                pageRangeTextView = (TextView) view.findViewById(R.id.txt_bookmark_page_range);
                deadlineTextView = (TextView) view.findViewById(R.id.txt_bookmark_deadline);
                reviewCheckBox1 = (CheckBox) view.findViewById(R.id.checkbox_review_level1);
                reviewCheckBox2 = (CheckBox) view.findViewById(R.id.checkbox_review_level2);
                reviewCheckBox3 = (CheckBox) view.findViewById(R.id.checkbox_review_level3);
                reviewCheckBox4 = (CheckBox) view.findViewById(R.id.checkbox_review_level4);
                reviewCheckBox5 = (CheckBox) view.findViewById(R.id.checkbox_review_level5);
                reviewCheckBox6 = (CheckBox) view.findViewById(R.id.checkbox_review_level6);
                reviewCheckBox7 = (CheckBox) view.findViewById(R.id.checkbox_review_level7);

            }

            @Override
            public String toString() {
                String bookName = ReadingManager.getInstance(mContext).getBook(bookId).getTitle();
                return super.toString() + " '" + bookName + "'";
            }
        }

        private void initViewHolder(BookActivity.BookmarkRecyclerViewAdapter.ViewHolder holder, final int position) {

            int startPage = bookmarks.get(position).getStartPage();
            int endPage = bookmarks.get(position).getEndPage();

            holder.pageRangeTextView.setText(startPage + "-" + endPage);
            holder.deadlineTextView.setText("测试 deadlines");
            holder.pageRangeTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(mContext);
                    builder.setMessage("确定删除记录，此操作不可逆！");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            bookmarks.remove(position);
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
        }
    }

}

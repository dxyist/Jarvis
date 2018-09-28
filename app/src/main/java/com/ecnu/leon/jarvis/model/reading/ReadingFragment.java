package com.ecnu.leon.jarvis.model.reading;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ecnu.leon.jarvis.R;
import com.ecnu.leon.jarvis.model.reading.model.Book;
import com.ecnu.leon.jarvis.model.task.TaskManager;
import com.ecnu.leon.jarvis.model.task.targertask.TargetTask;
import com.ecnu.leon.jarvis.model.task.targertask.TargetTaskContainer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ReadingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReadingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    // TODO: Rename and change types of parameters

    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private ReadingFragment.OnListFragmentInteractionListener mListener;
    RecyclerView recyclerView;

    public ReadingFragment() {
        // Required empty public constructor
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ReadingFragment newInstance() {
        ReadingFragment fragment = new ReadingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reading, container, false);

        // Set the adapter
        final Context context = rootView.getContext();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_books);
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
        recyclerView.setAdapter(new BookRecyclerViewAdapter(ReadingManager.getInstance(getContext()).getFakeBookList(), mListener, getContext()));

        FloatingActionButton fabButton = (FloatingActionButton) rootView.findViewById(R.id.fab_book);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.NoBackGroundDialog);
                final View tempView = View
                        .inflate(getActivity(), R.layout.dlg_new_book_add, null);
                builder.setView(tempView);
                builder.setCancelable(true);
                final AlertDialog dialog = builder.create();

                final EditText titleEditText = (EditText) tempView.findViewById(R.id.edt_book_add_title);
                // 自动弹出软键盘
                titleEditText.setFocusable(true);
                titleEditText.setFocusableInTouchMode(true);
                titleEditText.requestFocus();
                Timer timer = new Timer();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    public void run() {
                        InputMethodManager inputManager = (InputMethodManager) titleEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.showSoftInput(titleEditText, 0);
                    }
                }, 400);

                final EditText limitDaysEditTest = (EditText) tempView.findViewById(R.id.edt_book_add_title);


                final int limitDays = Integer.valueOf(limitDaysEditTest.getText().toString().trim());

                final TextView textViewLimitDaysHint = (TextView) tempView.findViewById(R.id.text_book_deadline);

                Date deadlineDate = new Date(System.currentTimeMillis() + limitDays * 24 * 60 * 60 * 1000);
                SimpleDateFormat format = new SimpleDateFormat("截止日期：yyyy-MM-dd (EE)");
                String dateString = format.format(deadlineDate);
                textViewLimitDaysHint.setText(dateString);
                limitDaysEditTest.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        long days = 0;
                        try {
                            days = Integer.valueOf(s.toString().trim());
                            if (days > 0) {
                                long ts = System.currentTimeMillis() + days * 24 * 60 * 60 * 1000;
                                Date deadlineDate = new Date(ts);
                                SimpleDateFormat format = new SimpleDateFormat("截止日期：yyyy-MM-dd (EE)");
                                String dateString = format.format(deadlineDate);
                                textViewLimitDaysHint.setText(dateString);
                            } else {
                                textViewLimitDaysHint.setText("日期必须大于0");
                            }
                        } catch (NumberFormatException e) {
                            textViewLimitDaysHint.setText("输入有误");
                        }

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    public void onShow(DialogInterface dialog) {
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput((EditText) tempView.findViewById(R.id.edt_routineTask_add_content), InputMethodManager.SHOW_IMPLICIT);
                    }
                });


                ((Button) tempView.findViewById(R.id.btn_confirm)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String content = titleEditText.getText().toString().trim();

                        int value = Integer.valueOf(((EditText) tempView.findViewById(R.id.edt_book_total_pages)).getText().toString().trim());
                        long tempLimitDays = Integer.valueOf(limitDaysEditTest.getText().toString().trim());
                        if (tempLimitDays <= 0) {
                            Toast.makeText(getContext(), "截止日期必须大于等于0天", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }


                        int importance = 0;
                        switch (((RadioGroup) tempView.findViewById(R.id.radioGroup_book_importance)).getCheckedRadioButtonId()) {
                            case R.id.radio_book_importance_low: {
                                importance = Book.BOOK_IMPORTANCE_LOW;
                                break;
                            }
                            case R.id.radio_book_importance_normal: {
                                importance = Book.BOOK_IMPORTANCE_NORMAL;
                                break;
                            }
                            case R.id.radio_book_importance_important: {
                                importance = Book.BOOK_IMPORTANCE_HIGH;
                                break;
                            }
                            case R.id.radio_book_importance_very_important: {
                                importance = Book.BOOK_IMPORTANCE_VERY_HIGH;
                                break;
                            }
                        }

                        // 任务优先级加入逻辑限制
                        String priorityString = "";
                        switch (importance) {
                            case TargetTask.TASK_PRIORITY_TRIVIA: {
                                priorityString = "琐事";
                                break;
                            }
                            case TargetTask.TASK_PRIORITY_NORMAL: {
                                priorityString = "普通任务";
                                break;
                            }
                            case TargetTask.TASK_PRIORITY_IMPORTANT: {
                                priorityString = "重要任务";
                                break;
                            }
                            case TargetTask.TASK_PRIORITY_VERY_IMPORTANT: {
                                priorityString = "核心任务";
                                break;
                            }
                        }

                        if (TaskManager.getInstance(getContext()).getCurrentTargetTaskQuantityByPriority(importance) >= TargetTaskContainer.getQuantityCeilingByPriority(importance)) {
                            Toast.makeText(getContext(), priorityString + "总量超过上限", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            return;
                        }

                        if (value > TargetTaskContainer.getValueCeilingByPriority(importance)) {
                            Toast.makeText(getContext(), priorityString + "可分配Value超过上限", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            return;
                        }

                        TaskManager.getInstance(getContext()).addNewTargetTask(content, value, importance, tempLimitDays * 24 * 60 * 60 * 1000);

                        refresh();

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

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void refresh() {
        if (recyclerView != null) {
            recyclerView.setAdapter(new BookRecyclerViewAdapter(ReadingManager.getInstance(getContext()).getFakeBookList(), mListener, getContext()));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Book item);
    }

}

package com.ecnu.leon.jarvis.model.task;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecnu.leon.jarvis.R;
import com.ecnu.leon.jarvis.Utils.DateUtil;
import com.ecnu.leon.jarvis.model.task.dailytask.DailyTask;
import com.ecnu.leon.jarvis.model.task.dailytask.DailyTaskFragment;
import com.ecnu.leon.jarvis.model.task.dailytask.TaskManager;
import com.ecnu.leon.jarvis.model.task.routinetask.RoutineTaskFragment;

import org.jsoup.helper.DataUtil;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class TaskFragment extends Fragment {


    private TextView actionBarDateTextview;
    private TextView actionBarWeekTextview;
    private TextView actionBarTotalTaskValueTextview;
    private TextView actionBarTodayTaskValueTextview;
    private TextView actionBarLastDaysTextview;
    private TextView actionBarTodayTextview;

    private ImageView leftArrowImageview;
    private ImageView rightArrowImageview;

    ViewPager viewPager;
    int currentPosition;
    private SectionsPagerAdapter mSectionsPagerAdapter;


    public TaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TaskFragment newInstance() {
        TaskFragment fragment = new TaskFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TaskManager.getInstance(getContext()).loadContent();

    }

    @Override
    public void onPause() {
        super.onPause();
        TaskManager.getInstance(getContext()).saveContent();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_task, container, false);

        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager_task);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(mSectionsPagerAdapter);

        currentPosition = viewPager.getCurrentItem();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //
                currentPosition = position;
                TaskManager.currentTaskCalendar = new Date();
                refreshActionBar();
                refreshSubFragment();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        initActionBar(rootView);

        TaskManager.setDateChanged(true);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab_task);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                switch (viewPager.getCurrentItem()) {
                    case 0: {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.NoBackGroundDialog);
                        final View tempView = View
                                .inflate(getActivity(), R.layout.dlg_dailytask_add, null);
                        builder.setView(tempView);
                        builder.setCancelable(true);
                        final AlertDialog dialog = builder.create();

                        final EditText titleEditText = (EditText) tempView.findViewById(R.id.edt_dailytask_add_content);
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


                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            public void onShow(DialogInterface dialog) {
                                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput((EditText) tempView.findViewById(R.id.edt_dailytask_add_content), InputMethodManager.SHOW_IMPLICIT);
                            }
                        });


                        ((Button) tempView.findViewById(R.id.btn_confirm)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String content = titleEditText.getText().toString().trim();

                                int value = Integer.valueOf(((EditText) tempView.findViewById(R.id.edt_dailytask_add_value)).getText().toString().trim());
                                TaskManager.getInstance(getContext()).addNewDailyTask(content, value);
                                viewPager.getAdapter().notifyDataSetChanged();
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
                    break;

                    case 1: {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.NoBackGroundDialog);
                        final View tempView = View
                                .inflate(getActivity(), R.layout.dlg_routine_add, null);
                        builder.setView(tempView);
                        builder.setCancelable(true);
                        final AlertDialog dialog = builder.create();

                        final EditText titleEditText = (EditText) tempView.findViewById(R.id.edt_routineTask_add_content);
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

                                int value = Integer.valueOf(((EditText) tempView.findViewById(R.id.edt_routineTask_add_value)).getText().toString().trim());
                                Boolean[] weeks = new Boolean[8];
                                weeks[1] = ((CheckBox) tempView.findViewById(R.id.checkbox_routineTask_week1)).isChecked();
                                weeks[2] = ((CheckBox) tempView.findViewById(R.id.checkbox_routineTask_week2)).isChecked();
                                weeks[3] = ((CheckBox) tempView.findViewById(R.id.checkbox_routineTask_week3)).isChecked();
                                weeks[4] = ((CheckBox) tempView.findViewById(R.id.checkbox_routineTask_week4)).isChecked();
                                weeks[5] = ((CheckBox) tempView.findViewById(R.id.checkbox_routineTask_week5)).isChecked();
                                weeks[6] = ((CheckBox) tempView.findViewById(R.id.checkbox_routineTask_week6)).isChecked();
                                weeks[7] = ((CheckBox) tempView.findViewById(R.id.checkbox_routineTask_week7)).isChecked();

                                TaskManager.getInstance(getContext()).addNewRoutineTask(content, value, weeks);
                                refreshFragment();
                                viewPager.setCurrentItem(currentPosition);
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
                    break;
                }


            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }


    public void startMainThread() {
        mainThread.start();
    }


    Thread mainThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                if (TaskManager.isDateChanged()) {
                    TaskManager.setDateChanged(false);
                    Message message = mainHandler.obtainMessage();
                    mainHandler.sendMessage(message);
//                    refreshActionBar();
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    });

    Handler mainHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (actionBarTotalTaskValueTextview != null) {

                actionBarTotalTaskValueTextview.setText(TaskManager.getInstance(getContext()).getTotalValue() + "");
            }

            if (actionBarTodayTaskValueTextview != null) {

                actionBarTodayTaskValueTextview.setText(TaskManager.getInstance(getContext()).getOneDayValue(TaskManager.currentTaskCalendar) + "");
            }

        }
    };


    private void initActionBar(View rootView) {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

        actionBarDateTextview = (TextView) rootView.findViewById(R.id.txt_actionbar_date);
        actionBarWeekTextview = (TextView) rootView.findViewById(R.id.txt_actionbar_week);
        actionBarTotalTaskValueTextview = (TextView) rootView.findViewById(R.id.text_total_positive_value);
        actionBarTodayTaskValueTextview = (TextView) rootView.findViewById(R.id.text_today_positive_value);
        actionBarTodayTextview = (TextView) rootView.findViewById(R.id.txt_actionbar_day_diff);

        actionBarDateTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskManager.currentTaskCalendar = new Date();   //跳转今天
                refreshActionBar();
                refreshSubFragment();
            }
        });

        actionBarWeekTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskManager.currentTaskCalendar = new Date();   //跳转今天
                refreshActionBar();
                refreshSubFragment();

            }
        });


        actionBarTodayTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskManager.currentTaskCalendar = new Date();   //跳转今天
                refreshActionBar();
                refreshSubFragment();

            }
        });


        actionBarTodayTaskValueTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshActionBar();
                refreshSubFragment();

            }
        });
        actionBarTodayTaskValueTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshActionBar();
                refreshSubFragment();

            }
        });
        actionBarLastDaysTextview = (TextView) rootView.findViewById(R.id.text_last_days);

        leftArrowImageview = (ImageView) rootView.findViewById(R.id.txt_arrow_left);
        leftArrowImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(TaskManager.currentTaskCalendar);
//                calendar.add(calendar.YEAR, 1);//把日期往后增加一年.整数往后推,负数往前移动
//                calendar.add(calendar.DAY_OF_MONTH, 1);//把日期往后增加一个月.整数往后推,负数往前移动
//
//                calendar.add(calendar.WEEK_OF_MONTH, 1);//把日期往后增加一个月.整数往后推,负数往前移动
                calendar.add(calendar.DATE, -1);//把日期往后增加一天.整数往后推,负数往前移动
                TaskManager.currentTaskCalendar = calendar.getTime();   //这个时间就是日期往后推一天的结果
                refreshActionBar();
                refreshSubFragment();


            }
        });
        rightArrowImageview = (ImageView) rootView.findViewById(R.id.txt_arrow_right);
        rightArrowImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(TaskManager.currentTaskCalendar);
//                calendar.add(calendar.YEAR, 1);//把日期往后增加一年.整数往后推,负数往前移动
//                calendar.add(calendar.DAY_OF_MONTH, 1);//把日期往后增加一个月.整数往后推,负数往前移动
//
//                calendar.add(calendar.WEEK_OF_MONTH, 1);//把日期往后增加一个月.整数往后推,负数往前移动
                calendar.add(calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
                TaskManager.currentTaskCalendar = calendar.getTime();   //这个时间就是日期往后推一天的结果
                refreshActionBar();
                refreshSubFragment();

            }
        });
        refreshActionBar();
    }

    private void getCurrentPositive() {

    }

    private void refreshSubFragment()
    {
        Fragment fragment = ((SectionsPagerAdapter) viewPager.getAdapter()).getCurrentFragment();
        if (fragment instanceof DailyTaskFragment) {
            ((DailyTaskFragment) fragment).refresh();
        }
        if (fragment instanceof RoutineTaskFragment) {
            ((RoutineTaskFragment) fragment).refresh();
        }
    }

    private void refreshActionBar() {
        // 刷新日期
        if (actionBarDateTextview != null) {
            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String dateString = format.format(TaskManager.currentTaskCalendar);
            actionBarDateTextview.setText(dateString);
        }

        if (actionBarWeekTextview != null) {
            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("EE", Locale.CHINESE);
            String dateString = format.format(TaskManager.currentTaskCalendar);
            actionBarWeekTextview.setText(dateString);
        }

        if (actionBarTotalTaskValueTextview != null) {

            actionBarTotalTaskValueTextview.setText(TaskManager.getInstance(getContext()).getTotalValue() + "");
        }

        if (actionBarTodayTaskValueTextview != null) {

            actionBarTodayTaskValueTextview.setText(TaskManager.getInstance(getContext()).getOneDayValue(TaskManager.currentTaskCalendar) + "");
        }

        if (actionBarTodayTextview != null) {

            try {
                int dayDiff = 0;
                dayDiff = DateUtil.daysBetween(new Date(), TaskManager.currentTaskCalendar);

                String diffString = "今天";

                if (dayDiff == 0) {
                    diffString = "今天";

                } else if (dayDiff == 1) {
                    diffString = "明天";

                } else if (dayDiff == -1) {
                    diffString = "昨天";

                } else if (dayDiff == 2) {
                    diffString = "后天";

                } else if (dayDiff == -2) {
                    diffString = "前天";

                } else if (dayDiff < -2) {
                    diffString = dayDiff + "天前";

                } else if (dayDiff > 2) {
                    diffString = dayDiff + "天后";

                }

                actionBarTodayTextview.setText("(" + diffString + ")");

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


    }

    public void refreshFragment() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());
        viewPager.setAdapter(mSectionsPagerAdapter);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private Fragment mCurrentFragment;


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return DailyTaskFragment.newInstance(1);
                case 1:
                    return RoutineTaskFragment.newInstance(1);

            }
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            mCurrentFragment = (Fragment) object;
            super.setPrimaryItem(container, position, object);
        }


        public Fragment getCurrentFragment() {
            return mCurrentFragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "每日任务";
                case 1:
                    return "日常任务";
                case 2:
                    return "目标任务";
                case 3:
                    return "消费页面";
            }
            return null;
        }
    }
}





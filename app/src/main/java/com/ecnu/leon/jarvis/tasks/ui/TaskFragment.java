package com.ecnu.leon.jarvis.tasks.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.print.PrintHelper;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ecnu.leon.jarvis.JarvisApplication;
import com.ecnu.leon.jarvis.R;
import com.ecnu.leon.jarvis.Utils.PrefKeys;
import com.ecnu.leon.jarvis.Utils.PrefUtil;
import com.ecnu.leon.jarvis.tasks.item.DailyTask;
import com.ecnu.leon.jarvis.tasks.item.Task;
import com.ecnu.leon.jarvis.tasks.model.TaskManager;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class TaskFragment extends Fragment {
    // 存储当前全局时间
    private static Date currentTaskCalendar;


    private TextView actionBarDateTextview;
    private TextView actionBarWeekTextview;
    private TextView actionBarTaskValueTextview;
    private TextView actionBarLastDaysTextview;

    private ImageView leftArrowImageview;
    private ImageView rightArrowImageview;

    ViewPager viewPager;
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

        currentTaskCalendar = new Date();
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

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        initActionBar(rootView);


        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab_task);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
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
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    private void initActionBar(View rootView) {
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

        actionBarDateTextview = (TextView) rootView.findViewById(R.id.txt_actionbar_date);
        actionBarDateTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTaskCalendar = new Date();   //这个时间就是日期往后推一天的结果
                refreshActionBar();
            }
        });
        actionBarWeekTextview = (TextView) rootView.findViewById(R.id.txt_actionbar_week);
        actionBarTaskValueTextview = (TextView) rootView.findViewById(R.id.text_positive_value);
        actionBarLastDaysTextview = (TextView) rootView.findViewById(R.id.text_last_days);

        leftArrowImageview = (ImageView) rootView.findViewById(R.id.txt_arrow_left);
        leftArrowImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(currentTaskCalendar);
//                calendar.add(calendar.YEAR, 1);//把日期往后增加一年.整数往后推,负数往前移动
//                calendar.add(calendar.DAY_OF_MONTH, 1);//把日期往后增加一个月.整数往后推,负数往前移动
//
//                calendar.add(calendar.WEEK_OF_MONTH, 1);//把日期往后增加一个月.整数往后推,负数往前移动
                calendar.add(calendar.DATE, -1);//把日期往后增加一天.整数往后推,负数往前移动
                currentTaskCalendar = calendar.getTime();   //这个时间就是日期往后推一天的结果
                refreshActionBar();
            }
        });
        rightArrowImageview = (ImageView) rootView.findViewById(R.id.txt_arrow_right);
        rightArrowImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(currentTaskCalendar);
//                calendar.add(calendar.YEAR, 1);//把日期往后增加一年.整数往后推,负数往前移动
//                calendar.add(calendar.DAY_OF_MONTH, 1);//把日期往后增加一个月.整数往后推,负数往前移动
//
//                calendar.add(calendar.WEEK_OF_MONTH, 1);//把日期往后增加一个月.整数往后推,负数往前移动
                calendar.add(calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
                currentTaskCalendar = calendar.getTime();   //这个时间就是日期往后推一天的结果
                refreshActionBar();
            }
        });
        refreshActionBar();
    }

    private void refreshActionBar() {
        // 刷新日期
        if (actionBarDateTextview != null) {
            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
            String dateString = format.format(currentTaskCalendar);
            actionBarDateTextview.setText(dateString);
        }

        if (actionBarWeekTextview != null) {
            java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("EE", Locale.CHINESE);
            String dateString = format.format(currentTaskCalendar);
            actionBarWeekTextview.setText(dateString);
        }


    }

    private void getCurrentPositive() {

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
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "每日任务";
                case 1:
                    return "日常任务";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
    }
}





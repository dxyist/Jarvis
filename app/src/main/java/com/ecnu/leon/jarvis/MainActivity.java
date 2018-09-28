package com.ecnu.leon.jarvis;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.cootek.feedsnews.sdk.FeedsManager;
import com.ecnu.leon.jarvis.model.account.AccountFragment;
import com.ecnu.leon.jarvis.model.news.FeedsListFragment;
import com.ecnu.leon.jarvis.model.news.MockNewsUtil;
import com.ecnu.leon.jarvis.model.project.ui.ProjectFragment;
import com.ecnu.leon.jarvis.model.reading.ReadingFragment;
import com.ecnu.leon.jarvis.model.task.TaskFragment;

import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {


    private Fragment taskFragment;
    private Fragment newsFragment;
    private Fragment accountFragment;
    private Fragment projectFragment;
    private Fragment targetFragment;
    private Fragment graphicFragment;
    private Fragment readingFragment;

    // 各类 dialog
    static final int ACTION_ADD_WEEKLYTASK_DIALOG = 0;
    static final int ACTION_ADD_NEW_DAILYTASK_DIALOG = 1;
    static final int ACTION_ADD_NEW_DAILYTASK_STEPPING_DIALOG = 101;
    static final int ACTION_GET_DAILYTASK_FROM_UNREALIZEDTASK_DIALOG = 2;
    static final int ACTION_ADD_UNREALIZEDTASK_DIALOG = 3;
    static final int ACTION_ADD_IDEA_DIALOG = 4;
    static final int ACTION_AMEND_ACTIONBAR_DIALOG = 5;
    static final int ACTION_REMOVEALL_DAILYTASK_DIALOG = 6;
    static final int ACTION_ADD_ROUTINETASK_DIALOG = 7;
    static final int ACTION_CURRENT_DATE_CHANGE_DIALOG = 8;

    public static GregorianCalendar latelyBaginCalendar = new GregorianCalendar(2018, 4, 3);
    private static long latelyBaginCalendarMill;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_task:
                    hideFragment(transaction);
                    addTaskPage();
                    return true;
                case R.id.navigation_account:
                    hideFragment(transaction);
                    addAccountPage();
                    return true;
                case R.id.navigation_project:
                    hideFragment(transaction);
                    addProjectPage();
                    return true;

                case R.id.navigation_reading:
                    hideFragment(transaction);
                    addReadingPage();
                    return true;


            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        initData();
        initUI();

    }

    private void initData() {
        FeedsManager.getIns().init(new MockNewsUtil());
    }


    private void initUI() {
        initTaskFragment();

    }

    private void initActionbar() {

    }

    private void initTaskFragment() {
        addTaskPage();
    }

    private void addNewsPage() {
        //开启事务，fragment的控制是由事务来实现的

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (newsFragment == null) {
            newsFragment = FeedsListFragment.newInstance();
            transaction.add(R.id.fragment_container, newsFragment);
        }
        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(newsFragment);

        transaction.commit();
    }

    private void addProjectPage() {
        //开启事务，fragment的控制是由事务来实现的

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (projectFragment == null) {
            projectFragment = ProjectFragment.newInstance();
            transaction.add(R.id.fragment_container, projectFragment);
        }
        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(projectFragment);

        transaction.commit();
    }

    private void addAccountPage() {
        //开启事务，fragment的控制是由事务来实现的

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (accountFragment == null) {
            accountFragment = AccountFragment.newInstance();
            transaction.add(R.id.fragment_container, accountFragment);
        }
        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(accountFragment);

        transaction.commit();
    }


    private void addTaskPage() {
        //开启事务，fragment的控制是由事务来实现的

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (taskFragment == null) {
            taskFragment = TaskFragment.newInstance();
            transaction.add(R.id.fragment_container, taskFragment);
            ((TaskFragment) taskFragment).startMainThread();
        }
        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(taskFragment);

        transaction.commit();
    }

    private void addReadingPage() {
        //开启事务，fragment的控制是由事务来实现的

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (readingFragment == null) {
            readingFragment = ReadingFragment.newInstance();
            transaction.add(R.id.fragment_container, readingFragment);
        }
        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(readingFragment);

        transaction.commit();
    }

    private void setupViewPager(ViewPager viewPager) {
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new TaskFragment();
                    case 1:
                        return new TaskFragment();
                    case 2:
                        return new TaskFragment();
                }
                return null;
            }

        };

        viewPager.setAdapter(adapter);
    }

    private void hideFragment(FragmentTransaction transaction) {
        if (taskFragment != null) {
            transaction.hide(taskFragment);
        }
        if (newsFragment != null) {
            transaction.hide(newsFragment);
        }
        if (targetFragment != null) {
            transaction.hide(targetFragment);
        }
        if (graphicFragment != null) {
            transaction.hide(graphicFragment);
        }
        if (accountFragment != null) {
            transaction.hide(accountFragment);
        }
        if (projectFragment != null) {
            transaction.hide(projectFragment);
        }
        if (readingFragment != null) {
            transaction.hide(readingFragment);
        }
    }

}

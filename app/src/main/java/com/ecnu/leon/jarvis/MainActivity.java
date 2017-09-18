package com.ecnu.leon.jarvis;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.cootek.feedsnews.sdk.FeedsManager;
import com.ecnu.leon.jarvis.news.FeedsListFragment;
import com.ecnu.leon.jarvis.news.MockNewsUtil;
import com.ecnu.leon.jarvis.tasks.item.DailyTask;
import com.ecnu.leon.jarvis.tasks.model.TaskFragmentPagerAdapter;
import com.ecnu.leon.jarvis.tasks.model.TaskManager;
import com.ecnu.leon.jarvis.tasks.ui.DailyTaskFragment;
import com.ecnu.leon.jarvis.tasks.ui.TaskFragment;
import com.ecnu.leon.jarvis.tasks.ui.dummy.DummyContent;

public class MainActivity extends AppCompatActivity {


    private Fragment taskFragment;
    private Fragment newsFragment;
    private Fragment targetFragment;
    private Fragment graphicFragment;


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
                case R.id.navigation_target:
                    return true;
                case R.id.navigation_news:
                    hideFragment(transaction);
                    addNewsPage();
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

    private void initTaskFragment() {
        addTaskPage();
    }

    private void addNewsPage(){
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

    private void addTaskPage(){
        //开启事务，fragment的控制是由事务来实现的

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (taskFragment == null) {
            taskFragment = TaskFragment.newInstance();
            transaction.add(R.id.fragment_container, taskFragment);
        }
        //隐藏所有fragment
        hideFragment(transaction);
        //显示需要显示的fragment
        transaction.show(taskFragment);

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
    }

}

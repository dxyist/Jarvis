package com.ecnu.leon.jarvis.model.task;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ecnu.leon.jarvis.model.task.dailytask.DailyTaskFragment;

/**
 * Created by Leon on 2017/8/10.
 */

public class TaskFragmentPagerAdapter extends FragmentPagerAdapter {
    public TaskFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return DailyTaskFragment.newInstance(1);
            case 1:
                return DailyTaskFragment.newInstance(2);
            case 2:
                return DailyTaskFragment.newInstance(3);
        }
        return null;
    }

    @Override
    public int getCount() {
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
                return "悬赏任务";
        }
        return null;
    }
}

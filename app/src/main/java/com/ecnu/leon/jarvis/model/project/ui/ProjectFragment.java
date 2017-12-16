package com.ecnu.leon.jarvis.model.project.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecnu.leon.jarvis.R;
import com.ecnu.leon.jarvis.model.project.Project;
import com.ecnu.leon.jarvis.model.task.TaskFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ProjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    ViewPager viewPager;
    int currentPosition;
    private ProjectFragment.SectionsPagerAdapter mSectionsPagerAdapter;

    public ProjectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ProjectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectFragment newInstance() {
        ProjectFragment fragment = new ProjectFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectFragment.newInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_project, container, false);

        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager_project);
        mSectionsPagerAdapter = new ProjectFragment.SectionsPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(mSectionsPagerAdapter);

        currentPosition = viewPager.getCurrentItem();
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        return rootView;
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
                    return InboxFragment.newInstance(1);


            }
            return TaskFragment.PlaceholderFragment.newInstance(position + 1);
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
                    return "Inbox";
                case 1:
                    return "Projects";
                case 2:
                    return "Contexts";
                case 3:
                    return "Flagged";
            }
            return null;
        }
    }
}

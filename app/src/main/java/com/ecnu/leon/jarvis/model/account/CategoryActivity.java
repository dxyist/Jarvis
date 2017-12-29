package com.ecnu.leon.jarvis.model.account;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.Intent;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.AndroidCharacter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ecnu.leon.jarvis.R;

public class CategoryActivity extends AppCompatActivity {
    public static int currentSelectedRootGategory = 0;

    private static Fragment rootFragment;
    private static Fragment subFragment;

    private static CategoryContainer categoryContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        subFragment = getFragmentManager().findFragmentById(R.id.frag_sub);
        rootFragment = getFragmentManager().findFragmentById(R.id.frag_root);
        categoryContainer = AccountManager.getInstance(this).getCategoryContainer();
        if (categoryContainer.getCategories().size() <= 1) {
            categoryContainer.addDefaultCategory();
        }

    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class RootFragment extends ListFragment {
        public LayoutInflater mInflater = null;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            mInflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public void onResume() {
            super.onResume();
            categoryContainer.generatePinnedCategory();
            RootCategoryAdapter adapter = new RootCategoryAdapter();
            setListAdapter(adapter);
        }

        ;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_root, container, false);
            return rootView;
        }

        class ViewHolder {
            public TextView titleTextView = null;
        }

        class RootCategoryAdapter extends BaseAdapter {

            public RootCategoryAdapter() {
                super();
                // 不懂这个什么用？（这个必须有，否则失败！）
            }

            @Override
            public int getCount() {
                if (categoryContainer.getCategories() == null) {
                    return 0;
                }
                return categoryContainer.getCategories().size();
            }

            @Override
            public Object getItem(int position) {
                return categoryContainer.getCategories().get(position);
            }

            @Override
            public long getItemId(int position) {
                // 返回时间戳
                return categoryContainer.getCategories().get(position).getCreateCalendar().getTimeInMillis();
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                // 申请一个类的空间
                ViewHolder holder = null;
                // 图象关联
                if (convertView == null) {
                    // 一定要初始化
                    holder = new ViewHolder();
                    convertView = mInflater.inflate(R.layout.item_category_root, null);
                    holder.titleTextView = (TextView) convertView.findViewById(R.id.txt_item_category_root_title);

                    convertView.setBackgroundColor(Color.WHITE);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                if (CategoryActivity.currentSelectedRootGategory == position) {
                    convertView.setBackgroundColor(Color.GRAY);
                }

                holder.titleTextView.setText(categoryContainer.getCategories().get(position).getTitleString());

                convertView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        CategoryActivity.currentSelectedRootGategory = position;
                        refreshRootFragment();
                        refreshSubFragment();
                    }
                });
                return convertView;
            }
        }

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class SubFragment extends ListFragment {

        public LayoutInflater mInflater = null;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            mInflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        }

        public SubFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_sub, container, false);
            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();
            SubCategoryAdapter adapter = new SubCategoryAdapter();
            setListAdapter(adapter);
        }

        class ViewHolder {
            public TextView titleTextView = null;
            public CheckBox pinnedCheckBox = null;
        }

        class SubCategoryAdapter extends BaseAdapter {

            public SubCategoryAdapter() {
                super();
                // 不懂这个什么用？（这个必须有，否则失败！）
            }

            @Override
            public int getCount() {
                if (categoryContainer.getCategories().get(currentSelectedRootGategory) == null) {
                    return 0;
                }
                return categoryContainer.getCategories().get(currentSelectedRootGategory).categories.size();
            }

            @Override
            public Object getItem(int position) {
                return categoryContainer.getCategories().get(currentSelectedRootGategory).categories.get(position);
            }

            @Override
            public long getItemId(int position) {
                return categoryContainer.getCategories().get(currentSelectedRootGategory).categories.get(position).getCreateCalendar().getTimeInMillis();
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                // 申请一个类的空间
                ViewHolder holder = null;
                // 图象关联
                if (convertView == null) {
                    // 一定要初始化
                    holder = new ViewHolder();
                    convertView = mInflater.inflate(R.layout.item_category_sub, null);
                    holder.titleTextView = (TextView) convertView.findViewById(R.id.txt_item_category_sub_title);
                    holder.pinnedCheckBox = (CheckBox) convertView.findViewById(R.id.ckb_item_category_sub_pinned);
                    // holder.detailTextView = (TextView)
                    // convertView.findViewById(R.id.txt_idea_item_detail);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }

                holder.titleTextView.setText(categoryContainer.getCategories().get(currentSelectedRootGategory).categories.get(position).getTitleString());

                if (categoryContainer.getCategories().get(currentSelectedRootGategory).categories.get(position).isPinned()) {
                    holder.pinnedCheckBox.setChecked(true);
                } else {
                    holder.pinnedCheckBox.setChecked(false);
                }

                holder.pinnedCheckBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            categoryContainer.getCategories().get(currentSelectedRootGategory).categories.get(position).setPinned(true);
                        } else {
                            categoryContainer.getCategories().get(currentSelectedRootGategory).categories.get(position).setPinned(false);
                        }

                        CategoryActivity.refreshRootFragment();
                        CategoryActivity.refreshSubFragment();

                    }
                });

                convertView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        // 添加给第一个Activity的返回值，并设置resultCode
                        String string = categoryContainer.getCategories().get(currentSelectedRootGategory).categories.get(position).getTitleString();
                        Intent intent = new Intent();
                        intent.putExtra("return", string);
                        getActivity().setResult(RESULT_OK, intent);
                        getActivity().finish();
                    }
                });

                return convertView;
            }

        }

    }

    static public boolean refreshRootFragment() {
        rootFragment.onResume();
        return true;
    }

    static public boolean refreshSubFragment() {
        subFragment.onResume();
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            categoryContainer.save();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

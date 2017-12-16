package com.ecnu.leon.jarvis.model.project;

import android.content.Context;
import android.widget.AbsListView;
import android.widget.Toast;

import com.ecnu.leon.jarvis.Utils.PrefKeys;
import com.ecnu.leon.jarvis.Utils.PrefUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Leon on 2017/12/3.
 */

public class ProjectManager {

    private ArrayList<Project> projects;
    private ArrayList<Action> inboxActions;
    private Context context;
    private Boolean isLoadSuccess;


    private static ProjectManager mProjectManager;

    public static ProjectManager getInstance(Context context){
        if (mProjectManager == null) {
            mProjectManager = new ProjectManager(context.getApplicationContext());
        }
        return mProjectManager;
    }
    private ProjectManager(Context context) {
        this.context = context;

        projects = new ArrayList<>();
        inboxActions = new ArrayList<>();
        this.context = context;

        // 下面两个顺序不能弄反
        this.isLoadSuccess = true;

        loadContent();

    }

    public void loadContent() {

    }

    public void addNewActionToInbox(Action action)
    {
        this.inboxActions.add(action);
    }

}

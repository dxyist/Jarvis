package com.ecnu.leon.jarvis.model.reading;

import android.content.Context;

import com.ecnu.leon.jarvis.model.reading.model.Book;
import com.ecnu.leon.jarvis.model.reading.model.BookContainer;
import com.ecnu.leon.jarvis.model.task.TaskManager;
import com.ecnu.leon.jarvis.model.task.consumable.ConsumableContainer;
import com.ecnu.leon.jarvis.model.task.dailytask.DailyTaskContainer;
import com.ecnu.leon.jarvis.model.task.routinetask.RoutineTask;
import com.ecnu.leon.jarvis.model.task.routinetask.RoutineTaskContainer;
import com.ecnu.leon.jarvis.model.task.targertask.TargetTaskContainer;

import java.util.ArrayList;
import java.util.Date;

import lombok.Setter;

/**
 * Created by LeonDu on 19/09/2018.
 */
@Setter
public class ReadingManager {
    private static boolean isDataChanged = false;

    private BookContainer bookContainer;
    private Context context;
    private Boolean isLoadSuccess;
    private static ReadingManager mReadingManager;


    private ReadingManager(Context context) {
        this.context = context;

        // 下面两个顺序不能弄反
        this.isLoadSuccess = true;

        this.bookContainer = new BookContainer(context);
//        loadContent();

    }


    public static ReadingManager getInstance(Context context) {


        if (mReadingManager == null) {
            mReadingManager = new ReadingManager(context.getApplicationContext());
        }
        return mReadingManager;
    }

    public ArrayList<Book> getFullBookList() {
        return this.bookContainer.getFullBookList();
    }

    public ArrayList<Book> getFakeBookList() {
        return this.bookContainer.getFakeBookList();
    }
}

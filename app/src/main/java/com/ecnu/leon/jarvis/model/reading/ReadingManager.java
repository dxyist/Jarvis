package com.ecnu.leon.jarvis.model.reading;

import android.content.Context;
import android.widget.Toast;

import com.ecnu.leon.jarvis.Utils.PrefKeys;
import com.ecnu.leon.jarvis.Utils.PrefUtils;
import com.ecnu.leon.jarvis.model.reading.model.Book;
import com.ecnu.leon.jarvis.model.reading.model.BookContainer;
import com.ecnu.leon.jarvis.model.task.TaskManager;
import com.ecnu.leon.jarvis.model.task.consumable.ConsumableContainer;
import com.ecnu.leon.jarvis.model.task.dailytask.DailyTaskContainer;
import com.ecnu.leon.jarvis.model.task.routinetask.RoutineTask;
import com.ecnu.leon.jarvis.model.task.routinetask.RoutineTaskContainer;
import com.ecnu.leon.jarvis.model.task.targertask.TargetTaskContainer;

import java.io.IOException;
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
        loadContent();

    }


    public static ReadingManager getInstance(Context context) {


        if (mReadingManager == null) {
            mReadingManager = new ReadingManager(context.getApplicationContext());
        }
        return mReadingManager;
    }

    public void addNewBook(String bookName, int pageNumber, int importance, long deadlineTs) {
        Book book = new Book(bookName, "subtitle", pageNumber, importance, deadlineTs);
        this.bookContainer.addNewBook(book);
    }


    public ArrayList<Book> getFullBookList() {
        return this.bookContainer.getFullBookList();
    }

    public ArrayList<Book> getFakeBookList() {
        return this.bookContainer.getFakeBookList();
    }

    public void saveContent() {
        if (isLoadSuccess) {
            // daily save
            if (bookContainer != null) {
                try {
                    bookContainer.save();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "数据存储失败！！！！！", Toast.LENGTH_SHORT).show();
                }
            }

        } else {
            Toast.makeText(context, "当前数据不可写", Toast.LENGTH_SHORT).show();
        }
    }

    public void loadContent() {
        try {
            bookContainer.load();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            if (!(Boolean) PrefUtils.getKey(PrefKeys.TASK_FIRST_TIME_LOAD, true)) {
                isLoadSuccess = false;
                Toast.makeText(context, "数据读取失败！！！！！", Toast.LENGTH_SHORT).show();
            } else {
                PrefUtils.setKey(PrefKeys.BOOK_FIRST_TIME_LOAD, false);
            }

        }

    }

}

package com.ecnu.leon.jarvis.model.reading.model;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by LeonDu on 14/09/2018.
 */
@Getter
@Setter
public class Book {
    private ArrayList<BookMark> bookMarks = new ArrayList<>();
    private String title;
    private String subtitle;
    private int importance;
    private int pageNumber;
    private int currentPage;

    // 剩余完成毫秒数
    private long deadlineTs = 0;

    // 重要性列表
    public final static int BOOK_IMPORTANCE_LOW = 0;
    public final static int BOOK_IMPORTANCE_NORMAL = 1;
    public final static int BOOK_IMPORTANCE_HIGH = 2;
    public final static int BOOK_IMPORTANCE_VERY_HIGH = 3;

    public Book(String title, String subtitle, int pageNumber, int importance) {
        this.title = title;
        this.subtitle = subtitle;
        this.pageNumber = pageNumber;
        this.importance = importance;
        this.deadlineTs = 0;
    }

    public Book(String title, String subtitle, int pageNumber, int importance, long deadlineTs) {
        this.title = title;
        this.subtitle = subtitle;
        this.pageNumber = pageNumber;
        this.importance = importance;
        this.deadlineTs = deadlineTs;
    }

    public int getReadPageNumber() {

        return 0;
    }
}

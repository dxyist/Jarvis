package com.ecnu.leon.jarvis.model.reading.model;

import com.cootek.feedsnews.model.http.FeedsInAppUpdateService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by LeonDu on 14/09/2018.
 */
@Getter
@Setter
public class Book implements Serializable {

    private long id;

    private ArrayList<Bookmark> bookMarks = new ArrayList<>();
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

    public boolean isBookmarkRangeValid(int startPage, int endPage) {

        if (startPage <= 0) {
            return false;
        }
        if (endPage > this.pageNumber) {
            return false;
        }
        if (endPage < startPage) {
            return false;
        }
        for (Bookmark bookmark : this.bookMarks
                ) {
            int bookmarkStartPage = bookmark.getStartPage();
            int bookmarkEndPage = bookmark.getEndPage();
            if (startPage > bookmarkStartPage && startPage < bookmarkEndPage) {
                return false;
            }
            if (endPage > bookmarkStartPage && endPage < bookmarkEndPage) {
                return false;
            }
            if (startPage <= bookmarkStartPage && endPage >= bookmarkEndPage) {
                return false;
            }
        }
        return true;

    }
}

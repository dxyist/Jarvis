package com.ecnu.leon.jarvis.model.reading.model;

/**
 * Created by LeonDu on 14/09/2018.
 */

import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Bookmark implements Serializable {
    private long id;
    private long bookID;
    private int reviewLevel;
    private int importance;
    private int startPage;
    private int endPage;

    public Bookmark(long id, long bookID, int importance, int startPage, int endPage) {
        this.id = id;
        this.bookID = bookID;
        this.importance = importance;
        this.reviewLevel = 0;
        this.startPage = startPage;
        this.endPage = endPage;
    }
}

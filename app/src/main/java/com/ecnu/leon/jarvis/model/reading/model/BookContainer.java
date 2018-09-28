package com.ecnu.leon.jarvis.model.reading.model;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by LeonDu on 18/09/2018.
 */

public class BookContainer {


    private ArrayList<Book> books = new ArrayList<>();

    private static final String FILE_NAME = "Books.dat";

    private Context context;


    public BookContainer(Context context) {
        this.context = context;
    }

    public ArrayList<Book> getFullBookList() {
        return this.books;
    }

    public ArrayList<Book> getFakeBookList() {

        ArrayList<Book> fakeBooks = new ArrayList<>();

        Book book = new Book("葵花宝典", "娘化首选", 1000);
        fakeBooks.add(book);
        return fakeBooks;
    }

}

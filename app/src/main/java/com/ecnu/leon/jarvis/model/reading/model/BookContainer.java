package com.ecnu.leon.jarvis.model.reading.model;

import android.content.Context;

import com.ecnu.leon.jarvis.Utils.BackgroundExecutor;
import com.ecnu.leon.jarvis.model.reading.ReadingManager;
import com.ecnu.leon.jarvis.model.task.targertask.TargetTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by LeonDu on 18/09/2018.
 */

public class BookContainer {


    private ArrayList<Book> books;

    private static final String FILE_NAME = "Books.dat";

    private Context context;


    public BookContainer(Context context) {
        this.context = context;
        this.books = new ArrayList<>();
    }

    public void addNewBook(Book book) {
        this.books.add(book);
    }

    public Book getBook(long id) {
        for (Book book : this.books
                ) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }

    public ArrayList<Book> getFullBookList() {
        return this.books;
    }

    public ArrayList<Book> getFakeBookList() {

        ArrayList<Book> fakeBooks = new ArrayList<>();

        Book book = new Book(ReadingManager.getNewBookID(), "葵花宝典", "娘化首选", 1000, Book.BOOK_IMPORTANCE_NORMAL);
        fakeBooks.add(book);
        return fakeBooks;
    }

    public boolean save() throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE));
        out.writeObject(this.books);
        out.flush();
        out.close();
        return false;
    }

    @SuppressWarnings("unchecked")
    public boolean load() throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(context.openFileInput(FILE_NAME));
        this.books = (ArrayList<Book>) in.readObject();
        in.close();
        return false;
    }

}

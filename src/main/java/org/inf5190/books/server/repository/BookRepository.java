package org.inf5190.books.server.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.inf5190.books.Books.Book;

public class BookRepository {
    private final AtomicInteger idGenerator = new AtomicInteger(0);
    private final List<Book> books = new ArrayList<Book>();

    public Book addBook(Book book) {
        final Book newBook = book.toBuilder().setId(Integer.toString(idGenerator.incrementAndGet())).build();
        books.add(newBook);
        return newBook;
    }

    public List<Book> getBooks() {
        return books;
    }
}

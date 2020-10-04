package com.sungur.bookstore.service;

import com.sungur.bookstore.model.Author;
import com.sungur.bookstore.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    public Optional<Book> getByBook(long bookId);

    public List<Book> allBooks();

    public Book saveOrUpdateBook(Book books) throws Exception;

    public void deleteBook(long bookId) throws Exception;

    public List<Book> findByNameOrSerialNameOrAuthorOrIsbn(String name, String serialName, Author author, String isbn);

}

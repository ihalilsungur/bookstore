package com.sungur.bookstore.service;

import com.sungur.bookstore.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    public Optional<Author> getByAuthor(long authorId);

    public List<Author> allAuthors();

    public Author saveOrUpdateAuthor(Author author) throws Exception;

    public void deleteAuthor(long authorId) throws Exception;
}

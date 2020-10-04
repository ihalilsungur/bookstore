package com.sungur.bookstore.service;

import com.sungur.bookstore.model.Author;
import com.sungur.bookstore.model.Book;
import com.sungur.bookstore.repository.AuthorRepository;
import com.sungur.bookstore.repository.BookRepository;
import com.sungur.bookstore.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorServiceImpl implements AuthorService {

    private AuthorRepository authorRepository;
    private BookRepository bookRepository;

    @Autowired
    AuthorServiceImpl(@Qualifier("authorRepository") AuthorRepository authorRepository,
                      @Qualifier("bookRepository") BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }


    @Override
    public Optional<Author> getByAuthor(long authorId) {
        return authorRepository.findById(authorId);
    }

    @Override
    public List<Author> allAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public Author saveOrUpdateAuthor(Author author) throws Exception {
        if (author == null) {
            author = authorRepository.save(author);
            return author;
        } else {
            Optional<Author> tempAuthor = authorRepository.findById(author.getId());
            if (tempAuthor.isPresent()) {
                Author newAuthor = tempAuthor.get();
                System.out.println(newAuthor);
                newAuthor.setId(author.getId());
                newAuthor.setName(author.getName());
                newAuthor.setDescription(author.getDescription());
                newAuthor = authorRepository.save(newAuthor);
                return newAuthor;
            } else {
                author = authorRepository.save(author);
                return author;
            }
        }

    }

    @Override
    public void deleteAuthor(long authorId) throws Exception {
        Optional<Author> tempAuthor = authorRepository.findById(authorId);
       Optional<Book> tempBooks = bookRepository.findById(authorId);
        if (tempAuthor.isEmpty()) {
            throw new Exception("Silinecek Yazar bulunamadı : " + authorId);
        }

         authorRepository.deleteById(authorId);
    }

}

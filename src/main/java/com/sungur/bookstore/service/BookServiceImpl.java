package com.sungur.bookstore.service;

import com.sungur.bookstore.model.Author;
import com.sungur.bookstore.model.Book;
import com.sungur.bookstore.repository.BookRepository;
import com.sungur.bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(@Qualifier("bookRepository") BookRepository bookRepository){
        this.bookRepository=bookRepository;
    }

    @Override
    public Optional<Book> getByBook(long bookId) {
        return bookRepository.findById(bookId);
    }

    @Override
    public List<Book> allBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book saveOrUpdateBook(Book books) throws Exception {
        if (books == null){
            books = bookRepository.save(books);
            return books;
        }else{
            Optional<Book> tempBook = bookRepository.findById(books.getId());
            if(tempBook.isPresent()){
                Book newBooks = tempBook.get();
                System.out.println(newBooks);
                newBooks.setId(books.getId());
                newBooks.setName(books.getName());
                newBooks.setAltName(books.getAltName());
                newBooks.setDescription(books.getDescription());
                newBooks.setAuthor(books.getAuthor());
                newBooks.setPublisher(books.getPublisher());
                newBooks = bookRepository.save(newBooks);
                return newBooks;
            }else{
                books = bookRepository.save(books);
                return books;
            }

        }
    }

    @Override
    public void deleteBook(long bookId) throws Exception {
        Optional<Book> tempBook = bookRepository.findById(bookId);
        if(tempBook.isEmpty()){
            throw new Exception("Silinecek Kitap bulunamadı : " + bookId);
        }
        bookRepository.deleteById(bookId);
    }


    @Override
    public List<Book> findByNameOrSerialNameOrAuthorOrIsbn(String name, String serialName, Author author, String isbn) {
        return bookRepository.findByNameOrSerialNameOrAuthorOrIsbn(name, serialName, author, isbn);
    }



}

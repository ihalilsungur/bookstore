package com.sungur.bookstore.controller;

import com.sungur.bookstore.exception.RecordNotFoundException;
import com.sungur.bookstore.model.Author;
import com.sungur.bookstore.model.Book;
import com.sungur.bookstore.model.Publisher;
import com.sungur.bookstore.model.User;
import com.sungur.bookstore.service.AuthorService;
import com.sungur.bookstore.service.BookService;
import com.sungur.bookstore.service.PublisherService;
import com.sungur.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@RestController
public class BookController {

    private BookService bookService;
    private UserService userService;
    private AuthorService authorService;
    private PublisherService publisherService;


    @Autowired
    BookController(BookService bookService, UserService userService,
                   AuthorService authorService, PublisherService publisherService) {
        this.bookService = bookService;
        this.userService = userService;
        this.authorService = authorService;
        this.publisherService = publisherService;
    }

    @GetMapping(value = "new-book")
    public ModelAndView registration() {
        ModelAndView modelAndView = new ModelAndView();
        List<Author> authors = authorService.allAuthors();
        List<Publisher> publishers = publisherService.allPublishers();
        Book book = new Book();
        modelAndView.addObject("author", authors);
        modelAndView.addObject("publisher", publishers);
        modelAndView.addObject("book", book);
        modelAndView.setViewName("user/new-book");
        return modelAndView;
    }

    @GetMapping("book")
    public ModelAndView allBooks() {
        ModelAndView modelAndView = new ModelAndView();
        List<Book> books = bookService.allBooks();
        modelAndView.addObject("book", books);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getUserName());
        modelAndView.addObject("BookMessage", "Content Available Only for Users withUser Role");
        modelAndView.setViewName("user/book");
        return modelAndView;
    }

    @GetMapping("search-book?name=/{name}")
    public ModelAndView findBook(@PathVariable("name") String name) {
        System.out.println("------------------Geldi ---------->"+name);
        List<Book> books = bookService.findByNameOrSerialNameOrAuthorOrIsbn(name, "", null, "");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ModelAndView modelAndView = new ModelAndView();

        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("book", books);
        modelAndView.addObject("userName", "Welcome " + user.getUserName());
        modelAndView.addObject("BookMessage", "Content Available Only for Users withUser Role");
        modelAndView.setViewName("user/book");
        return modelAndView;
    }


    @PostMapping("new-book")
    public ModelAndView createBook(Book book, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("user/new-book");
        } else {
            try {
                bookService.saveOrUpdateBook(book);
            } catch (Exception e) {
                e.printStackTrace();
                e.getMessage();
            }
            modelAndView.addObject("successMessage", "Kitap başarılı bir şekilde oluşturuldu.");
            modelAndView.addObject("userName", "Welcome " + user.getUserName());
            modelAndView.addObject("book", new Book());
            modelAndView.setViewName("redirect:/book");
        }
        return modelAndView;
    }

    @GetMapping("/book/{id}")
    public ModelAndView deleteBook(@PathVariable("id") long id) throws Exception {
        Optional<Book> book = bookService.getByBook(id);
        if (book.isEmpty()) {
            throw new Exception("Silinecek Kitap bulanamadı ?" + book);
        }
        ModelAndView modelAndView = new ModelAndView();
        bookService.deleteBook(id);
        modelAndView.addObject("book", bookService.allBooks());
        modelAndView.setViewName("/user/book");
        return modelAndView;
    }

    @GetMapping("/edit-book/{id}")
    public ModelAndView updateBook(@PathVariable("id") long id) throws RecordNotFoundException {
        Optional<Book> book = bookService.getByBook(id);
        List<Author> authors = authorService.allAuthors();
        List<Publisher> publishers = publisherService.allPublishers();
        ModelAndView modelAndView = new ModelAndView();
        if (book.isPresent()) {
            modelAndView.addObject("book", book);
            modelAndView.addObject("author", authors);
            modelAndView.addObject("publisher", publishers);
        }
        modelAndView.setViewName("/user/new-book");
        return modelAndView;
    }


}

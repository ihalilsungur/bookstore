package com.sungur.bookstore.controller;

import com.sungur.bookstore.exception.RecordNotFoundException;
import com.sungur.bookstore.model.Author;
import com.sungur.bookstore.model.User;
import com.sungur.bookstore.service.AuthorService;
import com.sungur.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RestController
public class AuthorController {

    private AuthorService authorService;
    private UserService userService;

    @Autowired
    AuthorController(AuthorService authorService,UserService userService){
        this.authorService=authorService;
        this.userService=userService;
    }

    @GetMapping(value = "new-author")
    public ModelAndView registration() {
        ModelAndView modelAndView = new ModelAndView();
        Author author = new Author();
        modelAndView.addObject("author", author);
        modelAndView.setViewName("user/new-author");
        return modelAndView;
    }

    @GetMapping("author")
    public ModelAndView allAuthors() {
        ModelAndView modelAndView = new ModelAndView();
        List<Author> authors = authorService.allAuthors();
        modelAndView.addObject("author", authors);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("USER--------->:"+auth.getAuthorities());
        Collection<? extends GrantedAuthority> isAdmin= auth.getAuthorities();
        System.out.println("isAdmin--------->:"+isAdmin);
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getUserName());
        modelAndView.addObject("AuthorMessage", "Content Available Only for Users withUser Role");
        modelAndView.setViewName("user/author");
        return modelAndView;
    }

    @PostMapping("new-author")
    public ModelAndView createAuthor(Author author, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("user/new-author");
        } else {
            try {
                authorService.saveOrUpdateAuthor(author);
            } catch (Exception e) {
                e.printStackTrace();
                e.getMessage();
            }
            modelAndView.addObject("successMessage", "Yazar başarılı bir şekilde oluşturuldu.");
            modelAndView.addObject("userName", "Welcome " + user.getUserName());
            modelAndView.addObject("author", new Author());
            modelAndView.setViewName("redirect:/author");
        }
        return modelAndView;
    }

    @GetMapping("/author/{id}")
    public ModelAndView deleteAuthor(@PathVariable("id") long id) throws Exception {
        Optional<Author>author = authorService.getByAuthor(id);
        if (author.isEmpty()) {
            throw new Exception("Silinecek Yazar bulanamadı ?" + author);
        }
        ModelAndView modelAndView = new ModelAndView();
        authorService.deleteAuthor(id);
        modelAndView.addObject("author", authorService.allAuthors());
        modelAndView.setViewName("/user/author");
        return modelAndView;
    }

    @GetMapping("/edit-author/{id}")
    public ModelAndView updateAuthor(@PathVariable("id") long id) throws RecordNotFoundException {
        Optional<Author> author = authorService.getByAuthor(id);
        ModelAndView modelAndView = new ModelAndView();
        if (author.isPresent()) {
            modelAndView.addObject("author",author);
        }
        modelAndView.setViewName("/user/new-author");
        return modelAndView;
    }
}

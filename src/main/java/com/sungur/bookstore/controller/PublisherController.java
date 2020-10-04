package com.sungur.bookstore.controller;

import com.sungur.bookstore.exception.RecordNotFoundException;
import com.sungur.bookstore.model.Publisher;
import com.sungur.bookstore.model.User;
import com.sungur.bookstore.service.PublisherService;
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
public class PublisherController {

    private PublisherService publisherService;
    private UserService userService;

    @Autowired
    public PublisherController(PublisherService publisherService,UserService userService){
        this.publisherService=publisherService;
        this.userService=userService;
    }

    @GetMapping(value = "new-publisher")
    public ModelAndView registration() {
        ModelAndView modelAndView = new ModelAndView();
        Publisher publisher = new Publisher();
        modelAndView.addObject("publisher", publisher);
        modelAndView.setViewName("user/new-publisher");
        return modelAndView;
    }

    @GetMapping("publisher")
    public ModelAndView allAuthors() {
        ModelAndView modelAndView = new ModelAndView();
        List<Publisher> publishers = publisherService.allPublishers();
        modelAndView.addObject("publisher", publishers);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        Collection<? extends GrantedAuthority> isAdmin= auth.getAuthorities();
        modelAndView.addObject("isAdmin", "" + isAdmin);
        System.out.println("isAdmin ------>:"+isAdmin);
        modelAndView.addObject("userName", "Welcome " + user.getUserName());
        modelAndView.addObject("PublisherMessage", "Content Available Only for Users withUser Role");
        modelAndView.setViewName("user/publisher");
        return modelAndView;
    }

    @PostMapping("new-publisher")
    public ModelAndView createPublisher(Publisher publisher, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("user/new-publisher");
        } else {
            try {
                publisherService.saveOrUpdatePublisher(publisher);
            } catch (Exception e) {
                e.printStackTrace();
                e.getMessage();
            }
            modelAndView.addObject("successMessage", "Publisher başarılı bir şekilde oluşturuldu.");
            modelAndView.addObject("userName", "Welcome " + user.getUserName());
            modelAndView.addObject("publisher", new Publisher());
            modelAndView.setViewName("redirect:/publisher");
        }
        return modelAndView;
    }

    @GetMapping("/publisher/{id}")
    public ModelAndView deletePublisher(@PathVariable("id") long id) throws Exception {
        Optional<Publisher> publisher =publisherService.getByPublisher(id);
        if (publisher.isEmpty()) {
            throw new Exception("Silinecek Yayın Evi bulanamadı ?" + publisher);
        }
        ModelAndView modelAndView = new ModelAndView();
        publisherService.deletePublisher(id);
        modelAndView.addObject("publisher", publisherService.allPublishers());
        modelAndView.setViewName("/user/publisher");
        return modelAndView;
    }

    @GetMapping("/edit-publisher/{id}")
    public ModelAndView updatePublisher(@PathVariable("id") long id) throws RecordNotFoundException {
        Optional<Publisher> publisher = publisherService.getByPublisher(id);
        ModelAndView modelAndView = new ModelAndView();
        if (publisher.isPresent()) {
            modelAndView.addObject("publisher",publisher);
        }
        modelAndView.setViewName("/user/new-publisher");
        return modelAndView;
    }
}

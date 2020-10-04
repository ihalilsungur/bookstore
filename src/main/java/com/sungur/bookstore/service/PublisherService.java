package com.sungur.bookstore.service;

import com.sungur.bookstore.model.Publisher;

import java.util.List;
import java.util.Optional;

public interface PublisherService {

    public Optional<Publisher> getByPublisher(long publisherId);

    public List<Publisher> allPublishers();

    public Publisher saveOrUpdatePublisher(Publisher publisher) throws Exception;

    public void deletePublisher(long publisher) throws Exception;
}

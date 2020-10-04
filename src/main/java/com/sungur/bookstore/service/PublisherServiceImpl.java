package com.sungur.bookstore.service;

import com.sungur.bookstore.model.Publisher;
import com.sungur.bookstore.repository.PublisherRepository;
import com.sungur.bookstore.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PublisherServiceImpl implements PublisherService {

    private PublisherRepository publisherRepository;

    @Autowired
    PublisherServiceImpl(PublisherRepository publisherRepository){
        this.publisherRepository=publisherRepository;
    }


    @Override
    public Optional<Publisher> getByPublisher(long publisherId) {
        return publisherRepository.findById(publisherId);
    }

    @Override
    public List<Publisher> allPublishers() {

        return publisherRepository.findAll();
    }

    @Override
    public Publisher saveOrUpdatePublisher(Publisher publisher) throws Exception {
        if (publisher == null){
            publisher = publisherRepository.save(publisher);
            return publisher;
        }else{
            Optional<Publisher> tempPublisher = publisherRepository.findById(publisher.getId());
            if (tempPublisher.isPresent()){
                Publisher newPublisher = tempPublisher.get();
                System.out.println(newPublisher);
                newPublisher.setId(publisher.getId());
                newPublisher.setName(publisher.getName());
                newPublisher.setDescription(publisher.getDescription());
                newPublisher = publisherRepository.save(newPublisher);
                return newPublisher;

            }else{
                publisher = publisherRepository.save(publisher);
                return publisher;
            }
        }
    }

    @Override
    public void deletePublisher(long publisherId) throws Exception {
        Optional<Publisher> tempPublisher = publisherRepository.findById(publisherId);
        if (tempPublisher.isEmpty()){
            throw new Exception("Silinecek Yayın Evi bulunamadı : " + publisherId);
        }
        publisherRepository.deleteById(publisherId);
    }
}

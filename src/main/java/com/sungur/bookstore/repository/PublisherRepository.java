package com.sungur.bookstore.repository;

import com.sungur.bookstore.model.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("publisherRepository")
public interface PublisherRepository extends JpaRepository<Publisher,Long> {
}






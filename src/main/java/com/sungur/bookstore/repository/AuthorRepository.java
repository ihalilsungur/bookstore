package com.sungur.bookstore.repository;

import com.sungur.bookstore.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("authorRepository")
public interface AuthorRepository extends JpaRepository<Author,Long> {
}

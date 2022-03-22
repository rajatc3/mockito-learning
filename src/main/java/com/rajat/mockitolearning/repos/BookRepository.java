package com.rajat.mockitolearning.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rajat.mockitolearning.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

}

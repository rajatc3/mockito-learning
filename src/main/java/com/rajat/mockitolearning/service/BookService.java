package com.rajat.mockitolearning.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rajat.mockitolearning.entity.Book;
import com.rajat.mockitolearning.exceptions.InvalidDataInRequestException;
import com.rajat.mockitolearning.repos.BookRepository;

@Service
public class BookService {

	@Autowired
	private BookRepository bookRepository;

	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}

	public Book findById(Long id) {

		if (id > 0L && id <= 4L) {
			Optional<Book> book = bookRepository.findById(id);
			if (book.isPresent())
				return book.get();
			else
				return null;
		}
		return null;
	}

	public Book updateBook(Long id, Book data) {
		System.out.println(id);
		if (id != data.getId())
			throw new InvalidDataInRequestException("Id do not match");
		if (findById(id) == null)
			throw new InvalidDataInRequestException("Data not found.");

		return bookRepository.save(data);
	}
}
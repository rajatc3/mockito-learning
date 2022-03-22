package com.rajat.mockitolearning.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.rajat.mockitolearning.entity.Book;
import com.rajat.mockitolearning.service.BookService;
import com.rajat.mockitolearning.utility.ResponseObject;
import com.rajat.mockitolearning.utility.Utility;

@RestController
public class BookController {

	@Autowired
	private BookService bookService;

	@GetMapping("/book")
	public ResponseEntity<ResponseObject> findAllBooks() {
		return success(Utility.createSuccessResponseObject(bookService.getAllBooks()));
	}

	@GetMapping("/book/{bookId}")
	public ResponseEntity<ResponseObject> findBook(@PathVariable(value = "bookId") Long id) {
		Book book = bookService.findById(id);
		if (book != null) {
			return success(Utility.createSuccessResponseObject(book));
		} else
			return badRequest(Utility.createBadRequestResponseObject());
	}

	private static ResponseEntity<ResponseObject> success(ResponseObject body) {
		return ResponseEntity.ok(body);
	}

	private static ResponseEntity<ResponseObject> badRequest(ResponseObject body) {
		return ResponseEntity.badRequest().body(body);
	}

}
package com.rajat.mockitolearning.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rajat.mockitolearning.entity.Book;
import com.rajat.mockitolearning.exceptions.InvalidDataInRequestException;
import com.rajat.mockitolearning.service.AuthenticationService;
import com.rajat.mockitolearning.service.BookService;
import com.rajat.mockitolearning.utility.RequestObject;
import com.rajat.mockitolearning.utility.ResponseObject;
import com.rajat.mockitolearning.utility.Utility;

@RestController
public class BookController {

	@Autowired
	private BookService bookService;
	private AuthenticationService authenticationService;

	@GetMapping("/book")
	public ResponseEntity<ResponseObject> findAllBooks() {
		return Utility.success(Utility.createSuccessResponseObject(bookService.getAllBooks()));
	}

	@GetMapping("/book/{bookId}")
	public ResponseEntity<ResponseObject> findBook(@PathVariable(value = "bookId") Long id) {
		Book book = bookService.findById(id);
		if (book != null) {
			return Utility.success(Utility.createSuccessResponseObject(book));
		} else
			return Utility.badRequest(Utility.createBadRequestResponseObject());
	}

	@PostMapping("/book")
	public ResponseEntity<ResponseObject> updateBookDetails(@RequestBody RequestObject request) {
		boolean validRequest = authenticationService.authenticateRequest(request);
		if (!validRequest) {
			return Utility.badRequest(Utility.createBadRequestResponseObject());
		}
		try {
			Book book = bookService.updateBook((Book) request.getData());
			return Utility.success(Utility.createSuccessResponseObject(book));
		} catch (ClassCastException e) {
			throw new InvalidDataInRequestException("Invalid data passed in request.");
		}
	}
}
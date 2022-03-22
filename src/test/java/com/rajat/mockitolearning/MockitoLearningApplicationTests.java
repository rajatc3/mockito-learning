package com.rajat.mockitolearning;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.rajat.mockitolearning.controllers.BookController;
import com.rajat.mockitolearning.entity.Book;
import com.rajat.mockitolearning.repos.BookRepository;
import com.rajat.mockitolearning.service.BookService;
import com.rajat.mockitolearning.utility.Utility;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@DisplayName("Books API Tests")
class MockitoLearningApplicationTests {

	private MockMvc mockMvc;

	Book RECORD1 = Book.builder().bookName("Introduction to C").description("All about C").Id(1L).rating(4).build();
	Book RECORD2 = Book.builder().bookName("Introduction to Java").description("All about Java").Id(2L).rating(5)
			.build();
	Book RECORD3 = Book.builder().bookName("Introduction to Python").description("All about Python").Id(3L).rating(3)
			.build();
	Book RECORD4 = Book.builder().bookName("Introduction to Jenkins").description("All about Jenkins").Id(4L).rating(4)
			.build();

	List<Book> books = new ArrayList<>(Arrays.asList(RECORD1, RECORD2, RECORD3, RECORD4));

	@Nested
	@DisplayName("Books Controller layer Tests")
	class ControllerLayerTests {
		@Mock
		private BookService bookService;
		@InjectMocks
		private BookController bookController;

		@BeforeEach
		public void setup() {
			MockitoAnnotations.openMocks(this);
			mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
		}

		@Test
		@DisplayName("Find all books: Success")
		public void findAllBooks_success() throws Exception {

			Mockito.when(bookService.getAllBooks()).thenReturn(books);

			mockMvc.perform(MockMvcRequestBuilders.get("/book").accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.data", hasSize(4)))
					.andExpect((jsonPath("$.data.[0].bookName", is("Introduction to C"))))
					.andExpect((jsonPath("$.data.[1].bookName", is("Introduction to Java"))));
		}

		@Test
		@DisplayName("Find all books : No book present")
		public void findAllBooks_noBooksFound() throws Exception {

			Mockito.when(bookService.getAllBooks()).thenReturn(new ArrayList<Book>());

			mockMvc.perform(MockMvcRequestBuilders.get("/book").accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.data", hasSize(0)));
		}

		@Test
		@DisplayName("Find one book: Success")
		public void findOneBook_success() throws Exception {
			Mockito.when(bookService.findById(1L)).thenReturn(RECORD1);
			Mockito.when(bookService.findById(2L)).thenReturn(RECORD2);

			mockMvc.perform(MockMvcRequestBuilders.get("/book/{bookId}", 1L).accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
					.andExpect(jsonPath("$.data.bookName", is("Introduction to C")));

			mockMvc.perform(MockMvcRequestBuilders.get("/book/{bookId}", 2L).accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk()).andExpect(jsonPath("$", notNullValue()))
					.andExpect(jsonPath("$.data.bookName", is("Introduction to Java")));
		}

		@Test
		@DisplayName("Find one book: Not present in database")
		public void findOneBook_bookNotPresent() throws Exception {
			Mockito.when(bookService.findById(1L)).thenReturn(null);

			mockMvc.perform(MockMvcRequestBuilders.get("/book/{bookId}", 1L).accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest())
					.andExpect(jsonPath("$.message", is(Utility.BAD_REQUEST_MESSAGE)));
		}

		@Test
		@DisplayName("Find one book: Invalid input")
		public void findOneBook_noParameterPassed() throws Exception {
			mockMvc.perform(MockMvcRequestBuilders.get("/book/{bookId}", "fakeId").accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isBadRequest());
		}
	}

	@Nested
	@DisplayName("Books Service layer Tests")
	class ServiceLayerTests {
		@Mock
		private BookRepository bookRepo;

		@InjectMocks
		private BookService bookService;

		@BeforeEach
		public void setup() {
			MockitoAnnotations.openMocks(this);
		}

		@Test
		@DisplayName("Get All Books: When books are present in database")
		public void getAllBooks_Success() {
			Mockito.when(bookRepo.findAll()).thenReturn(books);
			List<Book> bookList = bookService.getAllBooks();
			assertEquals(books, bookList);

		}

		@Test
		@DisplayName("Get All Books: When no books are present in database")
		public void getAllBooks_NoBooks() {
			Mockito.when(bookRepo.findAll()).thenReturn(null);

			List<Book> bookList = bookService.getAllBooks();
			assertEquals(null, bookList);

		}

		@Test
		@DisplayName("Get All Books: When database throws Exception")
		public void giot() {
			Mockito.when(bookRepo.findAll()).thenThrow(RuntimeException.class);

			assertThrows(RuntimeException.class, () -> bookService.getAllBooks());

		}
	}
}

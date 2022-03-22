package com.rajat.mockitolearning;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

			when(bookService.getAllBooks()).thenReturn(books);

			mockMvc.perform(MockMvcRequestBuilders.get("/book").accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.data", hasSize(4)))
					.andExpect((jsonPath("$.data.[0].bookName", is("Introduction to C"))))
					.andExpect((jsonPath("$.data.[1].bookName", is("Introduction to Java"))));
		}

		@Test
		@DisplayName("Find all books : No book present")
		public void findAllBooks_noBooksFound() throws Exception {

			when(bookService.getAllBooks()).thenReturn(new ArrayList<Book>());

			mockMvc.perform(MockMvcRequestBuilders.get("/book").accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$.data", hasSize(0)));
		}

		@Test
		@DisplayName("Find one book: Success")
		public void findOneBook_success() throws Exception {
			when(bookService.findById(1L)).thenReturn(RECORD1);
			when(bookService.findById(2L)).thenReturn(RECORD2);

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
			when(bookService.findById(anyLong())).thenReturn(null);

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
			when(bookRepo.findAll()).thenReturn(books);
			List<Book> bookList = bookService.getAllBooks();
			assertEquals(books, bookList);

		}

		@Test
		@DisplayName("Get All Books: When no books are present in database")
		public void getAllBooks_NoBooks() {
			when(bookRepo.findAll()).thenReturn(null);

			List<Book> bookList = bookService.getAllBooks();
			assertEquals(null, bookList);

		}

		@Test
		@DisplayName("Get All Books: When database throws Exception")
		public void getAllBooks_DBException() {
			when(bookRepo.findAll()).thenThrow(RuntimeException.class);

			assertThrows(RuntimeException.class, () -> bookService.getAllBooks());

		}
		
		@ParameterizedTest
		@DisplayName("Find by ID: ID is valid and Record is Available")
		@ValueSource(longs = {1L, 2L, 3L, 4L})
		public void findById_RecordAvailable(Long Id) {
			when(bookRepo.findById(Id)).thenReturn(Optional.of(RECORD1));
			assertEquals(RECORD1, bookService.findById(Id));
		}
		
		@ParameterizedTest
		@DisplayName("Find by ID: ID is not valid")
		@ValueSource(longs = {0L, 5L, 6L, Long.MAX_VALUE})
		public void findById_IdNotValid(Long Id) {			
			assertEquals(null, bookService.findById(Id));
			assertThatNoException();
		}
		
		@Test
		@DisplayName("Find by ID: ID valid but Record is not Available")
		public void findById_RecordNotAvailabe() {
			assertEquals(null, bookService.findById(1L));
		}
		
		@Test
		@DisplayName("Find by ID: When database throws Exception")
		public void findById_Exception() {
			Long Id = 3L;
			when(bookRepo.findById(Id)).thenThrow(RuntimeException.class);
			assertThrows(RuntimeException.class, () -> bookService.findById(Id));
		}
	}
}

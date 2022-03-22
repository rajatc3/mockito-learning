/**
 * 
 */
package com.rajat.mockitolearning;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rajat.mockitolearning.controllers.BookController;
import com.rajat.mockitolearning.entity.Book;
import com.rajat.mockitolearning.service.AuthenticationService;
import com.rajat.mockitolearning.service.BookService;
import com.rajat.mockitolearning.utility.RequestObject;
import com.rajat.mockitolearning.utility.Utility;

/**
 * @author b0224737
 *
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Update Book API Tests")
class UpdateBookAPITests {

	ObjectMapper mapper = new ObjectMapper();

	Book RECORD1 = Book.builder().bookName("Introduction to C").description("All about C").Id(1L).rating(4).build();
	Book RECORD2 = Book.builder().bookName("Introduction to Java").description("All about Java").Id(2L).rating(5)
			.build();
	Book RECORD3 = Book.builder().bookName("Introduction to Python").description("All about Python").Id(3L).rating(3)
			.build();
	Book RECORD4 = Book.builder().bookName("Introduction to Jenkins").description("All about Jenkins").Id(4L).rating(4)
			.build();

	@Nested
	@DisplayName("Controller layer tests")
	@TestInstance(Lifecycle.PER_CLASS)
	class ControllerTests {

		MockMvc mockMvc;

		@Mock
		private AuthenticationService authenticationService;

		@Mock
		private BookService bookService;

		@InjectMocks
		private BookController bookController;
		
		RequestObject requestObject = RequestObject.builder().data(RECORD1).osType("android").osVersion("8.0")
				.userIdentifier("B0224737").tokenId("RANDOM-TOKEN").build();

		@BeforeAll
		public void setup() {
			MockitoAnnotations.openMocks(this);
			mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
		}

		@Test
		public void validRequestWithDataInDatabase() {

		}

		@Test
		public void validRequestWithoutDataInDatabase() throws Exception {
			
			when(authenticationService.authenticateRequest(null)).thenReturn(true);
			when(bookService.updateBook(RECORD1)).thenReturn(RECORD1);
			
			mockMvc.perform(post("/book").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(requestObject)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message", is(Utility.SUCCESS_MESSAGE)))
			.andExpect(jsonPath("$.data", is(RECORD1)));
		}

		@Test
		public void invalidRequest() throws Exception {

			when(authenticationService.authenticateRequest(requestObject)).thenReturn(false);

			mockMvc.perform(post("/book").contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(requestObject)))
					.andExpect(status().isBadRequest())
					.andExpect(jsonPath("$.message", is(Utility.BAD_REQUEST_MESSAGE)));
		}
	}

}

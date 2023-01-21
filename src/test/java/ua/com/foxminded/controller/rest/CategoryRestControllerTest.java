package ua.com.foxminded.controller.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ua.com.foxminded.dto.CategoryDTO;
import ua.com.foxminded.model.Category;
import ua.com.foxminded.model.search.request.PageOptions;
import ua.com.foxminded.rest.CategoryRestController;
import ua.com.foxminded.security.SecurityConfig;
import ua.com.foxminded.service.CategoryService;

@WebMvcTest(controllers = { CategoryRestController.class })
@Import(SecurityConfig.class)
class CategoryRestControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	CategoryService categoryService;
	
	protected String mapToJson(Object obj) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		String s = objectMapper.writeValueAsString(obj);
		return s;
	}

	protected <T> T mapFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, clazz);
	}
	
	@Test
	void returnsUnauthorizedWhenNoUser() throws Exception {
		mockMvc.perform(post("/api/v1/categories"))
				.andDo(print())
				.andExpect(status().isUnauthorized())
				.andReturn();
	}

	@Test
	@WithMockUser(username = "testUser")
	void shouldFindById() throws Exception {

		String uri = "/api/v1/categories/5";

		mockMvc.perform(MockMvcRequestBuilders.get(uri));
				
		verify(categoryService).findById(5L);
	}

	@Test
	@WithMockUser(username = "testUser")
	void shouldFindAll() throws Exception {
		
		PageOptions options = new PageOptions();
		
		String uri = "/api/v1/categories";

		mockMvc.perform(get(uri));
		
		verify(categoryService).findAll(
				PageRequest.of(options.getPage(), options.getSize(), Sort.by(options.getSort())));
	}

	@Test
	@WithMockUser(username = "testUser")
	void shouldCreate() throws Exception {
		
		CategoryDTO categoryDto = new CategoryDTO(null, "CategoryDTO");
		Category category = CategoryDTO.fromDTO(categoryDto);
				
		String uri = "/api/v1/categories";
		
		String inputJson = mapToJson(categoryDto);

		MvcResult mvcResult = mockMvc.perform(post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		
		assertEquals(201, status);
		verify(categoryService).save(category);
		
	}
	
	@Test
	@WithMockUser(username = "testUser")
	void shouldUpdate() throws Exception {
		
		CategoryDTO categoryDto = new CategoryDTO(5L, "updated");
		Category category = CategoryDTO.fromDTO(categoryDto);
				
		String uri = "/api/v1/categories/5";
		
		String inputJson = mapToJson(categoryDto);

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		
		assertEquals(200, status);
		verify(categoryService).save(category);
	}
	
	@Test
	@WithMockUser(username = "testUser")
	void shouldRemoveById() throws Exception {
		
		Long id = 3L;

		when(categoryService.findById(id)).thenReturn(Optional.of(new Category()));
		
		String uri = "/api/v1/categories/" + id + "/delete";

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		
		assertEquals(200, status);
		verify(categoryService).deleteById(id);
	}

}

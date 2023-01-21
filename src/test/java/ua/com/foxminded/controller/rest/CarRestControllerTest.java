package ua.com.foxminded.controller.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.List;
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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ua.com.foxminded.dto.CarDTO;
import ua.com.foxminded.dto.CategoryDTO;
import ua.com.foxminded.dto.MakeDTO;
import ua.com.foxminded.dto.ModelDTO;
import ua.com.foxminded.model.Car;
import ua.com.foxminded.model.search.request.PageOptions;
import ua.com.foxminded.rest.CarRestController;
import ua.com.foxminded.security.SecurityConfig;
import ua.com.foxminded.service.CarService;
import ua.com.foxminded.service.CategoryService;
import ua.com.foxminded.service.MakeService;
import ua.com.foxminded.service.ModelService;

@Import(SecurityConfig.class)
@WebMvcTest(controllers = { CarRestController.class })
class CarRestControllerTest {

	@MockBean
	CarService carService;
	
	@MockBean
	MakeService makeService;

	@MockBean
	ModelService modelService;

	@MockBean
	CategoryService categoryService;

	@Autowired
	MockMvc mockMvc;

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
		mockMvc.perform(post("/api/v1/cars"))
				.andDo(print())
				.andExpect(status().isUnauthorized())
				.andReturn();
	}

	@Test
	@WithMockUser(username = "testUser")
	void shouldFindById() throws Exception {
		String uri = "/api/v1/cars/10";
		
		mockMvc.perform(get(uri));
		
		verify(carService).findById(10L);
	}

	@Test
	@WithMockUser(username = "testUser")
	void shouldFindAll() throws Exception {
		
		PageOptions options = new PageOptions();
		
		String uri = "/api/v1/cars";

		mockMvc.perform(get(uri));

		verify(carService).findAll(
				PageRequest.of(options.getPage(), options.getSize(), Sort.by(options.getSort())));
	}

	@Test
	@WithMockUser(username = "testUser")
	void shouldCreate() throws Exception {
		
		CarDTO carDto = new CarDTO(null, "CarDTO", 2020,
				new ModelDTO(null, "modelDto", new MakeDTO(null, "makeDto")), List.of(new CategoryDTO(null, "catDto")));
		Car car = CarDTO.fromDTO(carDto);
				
		String uri = "/api/v1/cars";
		
		String inputJson = mapToJson(carDto);

		MvcResult mvcResult = mockMvc.perform(post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		
		assertEquals(201, status);
		verify(carService).save(car);

	}
	
	@Test
	@WithMockUser(username = "testUser")
	void shouldUpdate() throws Exception {
		
		CarDTO carDto = new CarDTO(5L, "CarDTO", 2020,
				new ModelDTO(null, "modelDto", new MakeDTO(null, "makeDto")), List.of(new CategoryDTO(null, "catDto")));
		Car car = CarDTO.fromDTO(carDto);
				
		String uri = "/api/v1/cars/5";
		
		String inputJson = mapToJson(carDto);

		MvcResult mvcResult = mockMvc.perform(put(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		
		assertEquals(200, status);
		verify(carService).save(car);

	}
	
	@Test
	@WithMockUser(username = "testUser")
	void shouldRemoveById() throws Exception {
		
		Long id = 3L;

		when(carService.findById(id)).thenReturn(Optional.of(new Car()));
		
		String uri = "/api/v1/cars/" + id + "/delete";

		MvcResult mvcResult = mockMvc.perform(delete(uri))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();
		
		assertEquals(200, status);
		verify(carService).deleteById(id);
	}
}

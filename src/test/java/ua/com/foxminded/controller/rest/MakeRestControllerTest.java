package ua.com.foxminded.controller.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
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

import ua.com.foxminded.dto.MakeDTO;
import ua.com.foxminded.model.Make;
import ua.com.foxminded.model.search.request.PageOptions;
import ua.com.foxminded.rest.MakeRestController;
import ua.com.foxminded.security.SecurityConfig;
import ua.com.foxminded.service.MakeService;

@WebMvcTest(controllers = { MakeRestController.class })
@Import(SecurityConfig.class)
class MakeRestControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	MakeService makeService;

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
		mockMvc.perform(post("/api/v1/makes"))
				.andDo(print())
				.andExpect(status().isUnauthorized())
				.andReturn();
	}

	@Test
	@WithMockUser(username = "testUser")
	void shouldFindById() throws Exception {

		String uri = "/api/v1/makes/10";

		mockMvc.perform(MockMvcRequestBuilders.get(uri));

		verify(makeService).findById(10L);
	}

	@Test
	@WithMockUser(username = "testUser")
	void shouldFindAll() throws Exception {

		PageOptions options = new PageOptions();

		String uri = "/api/v1/makes";

		mockMvc.perform(MockMvcRequestBuilders.get(uri));

		verify(makeService).findAll(
				PageRequest.of(options.getPage(), options.getSize(), Sort.by(options.getSort())));
	}

	@Test
	@WithMockUser(username = "testUser")
	void shouldCreate() throws Exception {

		MakeDTO makeDto = new MakeDTO(null, "MakeDTO");
		Make make = MakeDTO.fromDTO(makeDto);

		String uri = "/api/v1/makes";

		String inputJson = mapToJson(makeDto);

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();

		assertEquals(201, status);
		verify(makeService).save(make);

	}

	@Test
	@WithMockUser(username = "testUser")
	void shouldUpdate() throws Exception {

		MakeDTO makeDto = new MakeDTO(5L, "updated");
		Make make = MakeDTO.fromDTO(makeDto);

		String uri = "/api/v1/makes/5";

		String inputJson = mapToJson(makeDto);

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(uri)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();

		assertEquals(200, status);
		verify(makeService).save(make);

	}

	@Test
	@WithMockUser(username = "testUser")
	void shouldRemoveById() throws Exception {

		Long id = 3L;

		when(makeService.findById(id)).thenReturn(Optional.of(new Make()));

		String uri = "/api/v1/makes/" + id + "/delete";

		MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri))
				.andReturn();

		int status = mvcResult.getResponse().getStatus();

		assertEquals(200, status);
		verify(makeService).deleteById(id);
	}

}

package ua.com.foxminded.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ua.com.foxminded.model.Model;
import ua.com.foxminded.repository.ModelRepository;
import ua.com.foxminded.service.impl.ModelServiceImpl;

@SpringBootTest(classes = { ModelServiceImpl.class })
class ModelServiceTest {

	@MockBean
	ModelRepository repository;
	
	@MockBean
	MakeService makeService;
	
	@Autowired
	ModelServiceImpl service;
	
	@Test
	void shouldFindById() {
		Model expected = new Model(1L, "Test");
		when(repository.findById(expected.getId())).thenReturn(Optional.of(expected));
	
		Optional<Model> actual = service.findById(expected.getId());
		
		assertTrue(actual.isPresent());
		assertEquals(expected, actual.get());
	}
	
	@Test
	void shouldFindAll() {
		
		List<Model> expected = List.of(new Model(1L, "Test1"), new Model(2L, "Test2"));
		
		when(repository.findAll()).thenReturn(expected);

		List<Model> cars = service.findAll();

		assertFalse(cars.isEmpty());
		assertEquals(expected, cars);
	}
	
	@Test
	void shouldDeleteById() {
		service.deleteById(1L);
		verify(repository).deleteById(1L);
	}
	
	@Test
	void shouldUpdate() {
		Model toUpdate = new Model(1L, "to update");
		
		when(repository.save(toUpdate)).thenReturn(new Model(1L, "updated"));

		Model updated = service.save(toUpdate);
		
		assertSame(toUpdate.getId(), updated.getId());
		assertNotSame(toUpdate.getName(), updated.getName());
	}	
	
	@Test
	void shouldSave() {
		Model toSave = new Model(1L, "Test");
		when(repository.save(toSave)).thenReturn(toSave);
		
		Model actual = service.save(toSave);
		
		assertTrue(Optional.of(actual).isPresent());
		assertEquals(toSave, actual);
	}
}

package ua.com.foxminded.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ua.com.foxminded.model.Category;
import ua.com.foxminded.repository.CategoryRepository;
import ua.com.foxminded.service.impl.CategoryServiceImpl;

@SpringBootTest(classes = { CategoryServiceImpl.class })
class CategoryServiceTest {
	
	@MockBean
	CategoryRepository repository;
	
	@Autowired
	CategoryServiceImpl service;
	
	@Test
	void shouldFindById() {
		Category expected = new Category(1L, "test");
		
		when(repository.findById(expected.getId())).thenReturn(Optional.of(expected));
		
		Optional<Category> actual = service.findById(expected.getId());
		
		assertTrue(actual.isPresent());
		assertEquals(expected, actual.get());
	}
	
	@Test
	void shouldFindByName() {
		Category expected = new Category(1L, "test");
		when(repository.findByName(expected.getName())).thenReturn(Optional.of(expected));
		
		Optional<Category> actual = service.findByName(expected.getName());
		
		assertTrue(actual.isPresent());
		assertEquals(expected, actual.get());
	}
	
	@Test
	void shouldFindAll() {
		
		List<Category> expected = List.of(new Category("name"), new Category("name2"));
		
		when(repository.findAll()).thenReturn(expected);

		List<Category> actual = service.findAll();

		assertFalse(actual.isEmpty());
		assertEquals(expected, actual);
	}
	
	@Test
	void shouldDeleteById() {
		service.deleteById(1L);
		verify(repository).deleteById(1L);
	}
	
	@Test
	void shouldSave() {
		Category toSave = new Category("test");
		when(repository.save(toSave)).thenReturn(toSave);
		
		Category actual = service.save(toSave);
		
		assertTrue(Optional.of(actual).isPresent());
		assertEquals(toSave, actual);
	}
}

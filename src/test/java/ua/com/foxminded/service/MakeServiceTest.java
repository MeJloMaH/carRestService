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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import ua.com.foxminded.model.Make;
import ua.com.foxminded.repository.MakeRepository;
import ua.com.foxminded.service.impl.MakeServiceImpl;

@SpringBootTest(classes = { MakeServiceImpl.class })
class MakeServiceTest {

	@MockBean
	MakeRepository repository;
	
	@Autowired
	MakeServiceImpl service;
	
	@Test
	void shouldFindById() {
		Make expected = new Make(1L, "Test");
		when(repository.findById(expected.getId())).thenReturn(Optional.of(expected));
	
		Optional<Make> actual = service.findById(expected.getId());
		
		assertTrue(actual.isPresent());
		assertEquals(expected, actual.get());
	}
	
	@Test
	void shouldFindAllFiltered() {
		
		Page<Make> expected = new PageImpl<>(List.of(new Make("make1"), new Make("make2")));

		when(repository.findAll(PageRequest.of(0, 10))).thenReturn(expected);

		Page<Make> makes = service.findAll(PageRequest.of(0, 10));

		assertFalse(makes.isEmpty());
		assertEquals(expected, makes);
	}
	
	@Test
	void shouldFindAll() {
		
		List<Make> expected = List.of(new Make(1L, "Test1"), new Make(2L, "Test2"));
		
		when(repository.findAll()).thenReturn(expected);

		List<Make> cars = service.findAll();

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
		Make toUpdate = new Make(1L, "to update");
		
		when(repository.save(toUpdate)).thenReturn(new Make(1L, "updated"));

		Make updated = service.save(toUpdate);
		
		assertSame(toUpdate.getId(), updated.getId());
		assertNotSame(toUpdate.getName(), updated.getName());
	}	
	
	@Test
	void shouldSave() {
		Make toSave = new Make(1L, "Test");
		when(repository.save(toSave)).thenReturn(toSave);
		
		Make actual = service.save(toSave);
		
		assertTrue(Optional.of(actual).isPresent());
		assertEquals(toSave, actual);
	}
}

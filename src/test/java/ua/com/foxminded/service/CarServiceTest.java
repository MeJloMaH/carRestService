package ua.com.foxminded.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import ua.com.foxminded.dto.CarDTO;
import ua.com.foxminded.model.Car;
import ua.com.foxminded.model.Category;
import ua.com.foxminded.model.Make;
import ua.com.foxminded.model.Model;
import ua.com.foxminded.model.search.SearchRequest;
import ua.com.foxminded.model.search.request.CarSearchRequest;
import ua.com.foxminded.repository.CarRepository;
import ua.com.foxminded.service.impl.CarServiceImpl;

@SpringBootTest(classes = { CarServiceImpl.class})
class CarServiceTest {

	@MockBean
	CarRepository repository;

	@MockBean
	MakeService makeService;
	
	@MockBean
	ModelService modelService;
	
	@MockBean
	CategoryService categoryService;

	@Autowired
	CarServiceImpl service;

	@Test
	void shouldFindById() {
		Car expected = new Car(1L);
		when(repository.findById(expected.getId())).thenReturn(Optional.of(expected));

		Optional<Car> actual = service.findById(expected.getId());

		assertTrue(actual.isPresent());
		assertEquals(expected, actual.get());
	}

	@Test
	void shouldFindByMake() {
		Car car = new Car(1L);
		Make make = new Make("make");
		car.setMake(make);

		Page<Car> expected = new PageImpl<>(List.of(car));

		when(repository.findByMake(make, PageRequest.of(0, 10))).thenReturn(expected);

		Page<Car> actual = service.findByMake(make, PageRequest.of(0, 10));

		assertFalse(actual.isEmpty());
		assertEquals(expected, actual);
	}

	@Test
	void shouldFindByModel() {
		Car car = new Car(1L);
		Model model = new Model("model");
		car.setModel(model);

		Page<Car> expected = new PageImpl<>(List.of(car));

		when(repository.findByModel(model, PageRequest.of(0, 10))).thenReturn(expected);

		Page<Car> actual = service.findByModel(model, PageRequest.of(0, 10));

		assertFalse(actual.isEmpty());
		assertEquals(expected, actual);
	}

	@Test
	void shouldFindByYear() {
		Car car = new Car(1L);
		Integer year = 2022;
		car.setModelYear(year);

		Page<Car> expected = new PageImpl<>(List.of(car));

		when(repository.findByYear(year, PageRequest.of(0, 10))).thenReturn(expected);

		Page<Car> actual = service.findByYear(year, PageRequest.of(0, 10));

		assertFalse(actual.isEmpty());
		assertEquals(expected, actual);
	}

	@Test
	void shouldFindAll() {

		Page<Car> expected = new PageImpl<>(List.of(new Car(1L), new Car(2L)));

		when(repository.findAll(PageRequest.of(0, 10))).thenReturn(expected);

		Page<Car> cars = service.findAll(PageRequest.of(0, 10));

		assertFalse(cars.isEmpty());
		assertEquals(expected, cars);

	}

	@Test
	void shouldFindByParameters() {
		CarSearchRequest carSearchRequest = new CarSearchRequest();

		Make make = new Make(1L, "make");
		Model model = new Model(1L, "model", make);
		Set<Category> categories = Set.of(new Category(1L, "cat"));

		SearchRequest searchRequest = carSearchRequest.asSearchRequest();

		Page<Car> expected = new PageImpl<>(List.of(
				new Car(1L, "1", 2020, make, model, categories),
				new Car(2L, "2", 2020, make, model, categories)));

		when(repository.findAll(any(), any(Pageable.class))).thenReturn(expected);

		List<CarDTO> cars = service.findAllFilteredAsListDTO(searchRequest);

		assertFalse(cars.isEmpty());
		assertEquals(
				expected.getContent().stream().map(Car::getId).collect(Collectors.toList()),
				cars.stream().map(CarDTO::getId).collect(Collectors.toList()));

	}

	@Test
	void shouldDeleteById() {
		service.deleteById(1L);
		verify(repository).deleteById(1L);
	}

	@Test
	void shouldUpdate() {
		Car toUpdate = new Car(1L, "to update", 2022, new Make("to update"), new Model("to update"),
				Set.of(new Category("to update")));

		when(repository.save(toUpdate)).thenReturn(
				new Car(1L, "updated", 1111, new Make("updated"), new Model("updated"),
						Set.of(new Category("updated"))));

		Car updated = service.save(toUpdate);

		assertSame(toUpdate.getId(), updated.getId());

		assertNotSame(toUpdate.getObjectId(), updated.getObjectId());
		assertNotSame(toUpdate.getModelYear(), updated.getModelYear());
		assertNotSame(toUpdate.getModel(), updated.getModel());
		assertNotSame(toUpdate.getMake(), updated.getMake());
		assertNotSame(toUpdate.getCategories(), updated.getCategories());
	}

	@Test
	void shouldSave() {
		Car toSave = new Car(1L, "car", 2022, new Make("m"), new Model("m"), Set.of(new Category("c")));
		when(repository.save(toSave)).thenReturn(toSave);

		Car actual = service.save(toSave);

		assertTrue(Optional.of(actual).isPresent());
		assertEquals(toSave, actual);
	}

}

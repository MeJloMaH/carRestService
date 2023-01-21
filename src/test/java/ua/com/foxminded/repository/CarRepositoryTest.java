package ua.com.foxminded.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import ua.com.foxminded.BaseDaoTest;
import ua.com.foxminded.model.Car;
import ua.com.foxminded.model.Category;
import ua.com.foxminded.model.Make;
import ua.com.foxminded.model.Model;
import ua.com.foxminded.model.search.SearchRequest;
import ua.com.foxminded.model.search.SearchSpecification;
import ua.com.foxminded.model.search.request.CarSearchRequest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CarRepositoryTest extends BaseDaoTest{

	@Autowired
	private CarRepository repository;
	
	@Test
	@Sql({ "/sql/clear_tables.sql", "/sql/car/find_by_id_test.sql" })
	void shouldFindById() {
		Make make = new Make(1L, "find me!");
		Category category = new Category(1L, "find me!");

		Car expected = new Car(
				11000L,
				"find me!",
				2020,
				make,
				new Model(1L, "find me!", make),
				Set.of(category));

		Car actual = repository.findById(expected.getId()).orElseThrow();
		assertEquals(actual, expected);
	}
	
	@Test
	@Sql({ "/sql/clear_tables.sql", "/sql/car/find_by_id_test.sql" })
	void shouldFindByMake() {
		Make make = new Make(1L, "find me!");
		Category category = new Category(1L, "find me!");

		Car expected = new Car(
				11000L,
				"find me!",
				2020,
				make,
				new Model(1L, "find me!", make),
				Set.of(category));

		Car actual = repository.findByMake(expected.getMake(), 
				PageRequest.of(0, 50)).toList().get(0);
		
		assertEquals(actual, expected);		
	}
	
	@Test
	@Sql({ "/sql/clear_tables.sql", "/sql/car/find_by_id_test.sql" })
	void shouldFindByModel() {
		Make make = new Make(1L, "find me!");
		Category category = new Category(1L, "find me!");

		Car expected = new Car(
				11000L,
				"find me!",
				2020,
				make,
				new Model(1L, "find me!", make),
				Set.of(category));
		
		Car actual = repository.findByModel(expected.getModel(), 
				PageRequest.of(0, 50)).toList().get(0);
		
		assertEquals(actual, expected);		
	}
	
	@Test
	@Sql({ "/sql/clear_tables.sql", "/sql/car/find_by_id_test.sql" })
	void shouldFindByYear() {
		Make make = new Make(1L, "find me!");
		Category category = new Category(1L, "find me!");

		Car expected = new Car(
				11000L,
				"find me!",
				2020,
				make,
				new Model(1L, "find me!", make),
				Set.of(category));

		Car actual = repository.findByYear(expected.getModelYear(), 
				PageRequest.of(0, 50)).toList().get(0);
		
		assertEquals(actual, expected);		
	}
	
	@Test
	@Sql({ "/sql/clear_tables.sql", "/sql/car/find_by_id_test.sql" })
	void shouldFindByCategory() {
		Make make = new Make(1L, "find me!");
		Set<Category> category = Set.of(new Category(1L, "find me!"));

		Car expected = new Car(
				11000L,
				"find me!",
				2020,
				make,
				new Model(1L, "find me!", make),
				category);
		
		Car actualPAGING = repository.findByCategory(expected.getCategories(), PageRequest.of(0, 1)).toList().get(0);
		assertEquals(actualPAGING, expected);		
	}
	
	@Test
	@Sql({ "/sql/clear_tables.sql", "/sql/car/find_by_parameters_test.sql" })
	void shouldFindByParameters() {
		Make make = new Make(1L, "find me!1");
		Model model = new Model(1L, "find me!1", make);
		Category category = new Category(1L, "find me!1");
		Set<Category> setCategory = Set.of(category);

		List<Car> expected = List.of(new Car(
				11000L,
				"find me!1",
				2020,
				make,
				model,
				setCategory));
	
		CarSearchRequest carSearchRequest = new CarSearchRequest();
		carSearchRequest.setModel(model);
		carSearchRequest.setMake(make);
		carSearchRequest.setCategories(setCategory);
		
		SearchRequest searchRequest = carSearchRequest.asSearchRequest();
		SearchSpecification<Car> specification = new SearchSpecification<>(searchRequest);
		Pageable pageable = searchRequest.asPageble();
		
		List<Car> actual = repository.findAll(specification, pageable).toList();
		assertEquals(actual, expected);	
	}
	
	
	@Test
	@Sql({ "/sql/clear_tables.sql", "/sql/car/find_all_test.sql" })
	void shouldFindAll() {
		Make firstMake = new Make(1L, "first");
		Category firstCategory = new Category(1L, "first");

		Make secondMake = new Make(2L, "second");
		Category secondCategory = new Category(2L, "second");

		List<Car> expected = List.of(
				new Car(99L,
						"first",
						2020,
						firstMake,
						new Model(1L, "first", firstMake),
						Set.of(firstCategory)),

				new Car(11000L,
						"second",
						2020,
						secondMake,
						new Model(2L, "second", secondMake),
						Set.of(secondCategory))

		);
		List<Car> actual = (List<Car>) repository.findAll();
		assertEquals(actual, expected);
	}

	@Test
	@Sql({ "/sql/clear_tables.sql" })
	void shouldNotFindById() {
		Optional<Car> result = repository.findById(11000L);
		assertTrue(result.isEmpty());
	}

	@Test
	@Sql({"/sql/clear_tables.sql", "/sql/car/create_test.sql" })
	void shouldCreateOne() {
		Make make = new Make(1L, "test");

		Car car = new Car(
				11000L,
				"test",
				2020,
				make,
				new Model(1L, "test", make),
				Set.of(new Category(1L, "test")));

		Car actual = repository.save(car);

		assertNotNull(actual.getId());
		assertEquals("test", actual.getObjectId());

		Car fromDB = repository.findById(actual.getId()).orElseThrow();
		assertEquals(actual, fromDB);
	}

	@Test
	@Sql({ "/sql/clear_tables.sql", "/sql/car/update_test.sql" })
	void shouldUpdateOne() {

		String objectIdBefore = "before";
		Integer yearBefore = 1000;
		Make makeBefore = new Make(1L, "before");
		Model modelBefore = new Model(1L, "before", makeBefore);
		Category categoryBefore = new Category(1L, "before");

		String objectIdAfter = "after";
		Integer yearAfter = 9200;
		Make makeAfter = new Make(100L, "after");
		Model modelAfter = new Model(100L, "after", makeAfter);
		Category categoryAfter = new Category(100L, "after");
		
		Car car = new Car(
				11000L,
				objectIdAfter,
				yearAfter,
				makeAfter,
				modelAfter,
				Set.of(categoryAfter));
		
		Car toUpdate = car;

		Car before = repository.findById(toUpdate.getId()).orElseThrow();
		
		assertEquals(objectIdBefore, before.getObjectId());
		assertEquals(yearBefore, before.getModelYear());
		assertEquals(makeBefore, before.getMake());
		assertEquals(modelBefore, before.getModel());
		assertEquals(Set.of(categoryBefore), before.getCategories());
		
		repository.save(toUpdate);
		Car after = repository.findById(toUpdate.getId()).orElseThrow();
		
		assertEquals(objectIdAfter, after.getObjectId());
		assertEquals(yearAfter, after.getModelYear());
		assertEquals(makeAfter, after.getMake());
		assertEquals(modelAfter, after.getModel());
		assertEquals(Set.of(categoryAfter), after.getCategories());
	}
	
	@Test
	@Sql({ "/sql/clear_tables.sql", "/sql/car/delete_test.sql" })
	void shouldRemoveById() {

		Long exist = 11000L;
		Optional<Car> toRemove = repository.findById(exist);
		
		assertTrue(toRemove.isPresent());
		
		repository.deleteById(exist);

		Optional<Car> afterRemove = repository.findById(exist);
		
		assertTrue(afterRemove.isEmpty());
	}

}

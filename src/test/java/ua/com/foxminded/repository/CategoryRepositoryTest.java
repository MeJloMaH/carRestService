package ua.com.foxminded.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import ua.com.foxminded.BaseDaoTest;
import ua.com.foxminded.model.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepositoryTest extends BaseDaoTest{

	@Autowired
	private CategoryRepository repository;
	
	@Test
	@Sql({ "/sql/clear_tables.sql", "/sql/category/find_by_id_test.sql" })
	void shouldFindById() {
		
		Category expected = new Category(1L, "find me!");

		Category actual = repository.findById(expected.getId()).orElseThrow();
		assertEquals(actual, expected);
	}
	
	@Test
	@Sql({ "/sql/clear_tables.sql", "/sql/category/find_all_test.sql" })
	void shouldFindAll() {

		List<Category> expected = List.of(
				new Category(1L, "first"),
				new Category(2L, "second")

		);
		
		List<Category> actual = (List<Category>) repository.findAll();
		assertEquals(actual, expected);
	}
	
	@Test
	@Sql({ "/sql/clear_tables.sql" })
	void shouldNotFindById() {
		Optional<Category> result = repository.findById(11000L);
		assertTrue(result.isEmpty());
	}
	
	@Test
	@Sql("/sql/clear_tables.sql")
	void shouldCreateOne() {
		
		Category actual = repository.save(new Category(1L, "test"));

		assertNotNull(actual.getId());
		assertEquals("test", actual.getName());

		Category fromDB = repository.findById(actual.getId()).orElseThrow();
		assertEquals(actual, fromDB);
	}
	
	@Test
	@Sql({ "/sql/clear_tables.sql", "/sql/category/update_test.sql" })
	void shouldUpdateOne() {
		
		String nameBefore = "before";		
		String nameAfter = "after";

		Category toUpdate = new Category(1L, nameAfter);

		Category before = repository.findById(toUpdate.getId()).orElseThrow();
		
		assertEquals(nameBefore, before.getName());

		repository.save(toUpdate);
		Category after = repository.findById(toUpdate.getId()).orElseThrow();
		
		assertEquals(nameAfter, after.getName());
	}
	
	@Test
	@Sql({ "/sql/clear_tables.sql", "/sql/category/delete_test.sql" })
	void shouldRemoveById() {

		Long exist = 11000L;
		Optional<Category> toRemove = repository.findById(exist);
		
		assertTrue(toRemove.isPresent());
		
		repository.deleteById(exist);

		Optional<Category> afterRemove = repository.findById(exist);
		
		assertTrue(afterRemove.isEmpty());
	}
	
}

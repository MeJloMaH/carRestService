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
import ua.com.foxminded.model.Make;
import ua.com.foxminded.model.Model;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ModelRepositoryTest extends BaseDaoTest{

	@Autowired
	private ModelRepository repository;

	@Test
	@Sql({ "/sql/clear_tables.sql", "/sql/model/find_by_id_test.sql" })
	void shouldFindById() {
		Make make = new Make(1L, "find me!");
		Model expected = new Model(1L, "find me!", make);
		

		Model actual = repository.findById(expected.getId()).orElseThrow();
		assertEquals(actual, expected);
	}
	
	@Test
	@Sql({ "/sql/clear_tables.sql", "/sql/model/find_all_test.sql" })
	void shouldFindAll() {
		Make firstMake = new Make(1L, "first");

		Make secondMake = new Make(2L, "second");

		List<Model> expected = List.of(
				new Model(1L, "first", firstMake),
				new Model(2L, "second", secondMake));
		
		List<Model> actual = (List<Model>) repository.findAll();
		assertEquals(actual, expected);
	}

	@Test
	@Sql({ "/sql/clear_tables.sql" })
	void shouldNotFindById() {
		Optional<Model> result = repository.findById(11000L);
		assertTrue(result.isEmpty());
	}
	
	@Test
	@Sql({"/sql/clear_tables.sql", "/sql/model/create_test.sql"})
	void shouldCreateOne() {
		Make make = new Make(1L, "test");

		Model model = new Model(null, "test", make);

		Model actual = repository.save(model);

		assertNotNull(actual.getId());
		assertEquals("test", actual.getName());

		Model fromDB = repository.findById(actual.getId()).orElseThrow();
		assertEquals(actual, fromDB);
	}
	
	@Test
	@Sql({ "/sql/clear_tables.sql", "/sql/model/update_test.sql" })
	void shouldUpdateOne() {

		Make makeBefore = new Make(1L, "before");
		String nameBefore = "before";

		Make makeAfter = new Make(100L, "after");
		String nameAfter = "after";
		
		Model toUpdate = new Model(9999L, nameAfter, makeAfter);	

		Model before = repository.findById(toUpdate.getId()).orElseThrow();
		
		assertEquals(nameBefore, before.getName());
		assertEquals(makeBefore, before.getMake());

		repository.save(toUpdate);
		Model after = repository.findById(toUpdate.getId()).orElseThrow();
		
		assertEquals(nameAfter, after.getName());
		assertEquals(makeAfter, after.getMake());
	}
	
	@Test
	@Sql({ "/sql/clear_tables.sql", "/sql/model/delete_test.sql" })
	void shouldRemoveById() {

		Long exist = 11000L;
		Optional<Model> toRemove = repository.findById(exist);
		
		assertTrue(toRemove.isPresent());
		
		repository.deleteById(exist);

		Optional<Model> afterRemove = repository.findById(exist);
		
		assertTrue(afterRemove.isEmpty());
	}
}

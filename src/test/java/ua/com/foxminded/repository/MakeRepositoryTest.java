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

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MakeRepositoryTest extends BaseDaoTest{
	
	@Autowired
	private MakeRepository repository;
	
	@Test
	@Sql({ "/sql/clear_tables.sql", "/sql/make/find_by_id_test.sql" })
	void shouldFindById() {
		Make expected = new Make(1L, "find me!");
		
		Make actual = repository.findById(expected.getId()).orElseThrow();
		assertEquals(actual, expected);
	}
	
	@Test
	@Sql({ "/sql/clear_tables.sql", "/sql/make/find_all_test.sql" })
	void shouldFindAll() {
		Make firstMake = new Make(1L, "first");

		Make secondMake = new Make(2L, "second");

		List<Make> expected = List.of(firstMake, secondMake);
				
		List<Make> actual = (List<Make>) repository.findAll();
		assertEquals(actual, expected);
	}
	
	@Test
	@Sql({ "/sql/clear_tables.sql" })
	void shouldNotFindById() {
		Optional<Make> result = repository.findById(11000L);
		assertTrue(result.isEmpty());
	}
	
	@Test
	@Sql("/sql/clear_tables.sql")
	void shouldCreateOne() {
		Make make = new Make(1L, "test");
		
		Make actual = repository.save(make);

		assertNotNull(actual.getId());
		assertEquals("test", actual.getName());

		Make fromDB = repository.findById(actual.getId()).orElseThrow();
		assertEquals(actual, fromDB);
	}
	
	@Test
	@Sql({ "/sql/clear_tables.sql", "/sql/make/update_test.sql" })
	void shouldUpdateOne() {
		
		String nameBefore = "before";
		String nameAfter = "after";

		Make toUpdate = new Make(1L, nameAfter);

		Make before = repository.findById(toUpdate.getId()).orElseThrow();
		
		assertEquals(nameBefore, before.getName());

		repository.save(toUpdate);
		Make after = repository.findById(toUpdate.getId()).orElseThrow();
		
		assertEquals(nameAfter, after.getName());
	}
	
	@Test
	@Sql({ "/sql/clear_tables.sql", "/sql/make/delete_test.sql" })
	void shouldRemoveById() {

		Long exist = 11000L;
		Optional<Make> toRemove = repository.findById(exist);
		
		assertTrue(toRemove.isPresent());
		
		repository.deleteById(exist);

		Optional<Make> afterRemove = repository.findById(exist);
		
		assertTrue(afterRemove.isEmpty());
	}
	
}

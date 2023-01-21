package ua.com.foxminded.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ua.com.foxminded.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
		
	@Query("select C from Category C where C.name = :name")
	Optional<Category> findByName(@Param("name") String name);
}
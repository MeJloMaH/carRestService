package ua.com.foxminded.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ua.com.foxminded.model.Make;

public interface MakeRepository extends JpaRepository<Make, Long>{

	@Query("select M from Make M where M.name = :name")
	Optional<Make> findByName(@Param("name") String name);
	
}
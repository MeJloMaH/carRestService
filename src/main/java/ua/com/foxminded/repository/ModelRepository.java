package ua.com.foxminded.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import ua.com.foxminded.model.Model;

public interface ModelRepository extends PagingAndSortingRepository<Model, Long>{
	
	@Query("select M from Model M where M.name = :name")
	Optional<Model> findByName(@Param("name") String name);
	
	@Query("select M from Model M")
	List<Model> findAll();
}
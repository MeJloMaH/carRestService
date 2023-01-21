package ua.com.foxminded.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ua.com.foxminded.model.Category;

public interface CategoryService {

	Optional<Category> findById(Long id);
	
	Category save(Category entity);
	
	void deleteById(Long id);

	Optional<Category> findByName(String name);

	Page<Category> findAll(Pageable pageable);
	
	List<Category> findAll();

}

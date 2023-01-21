package ua.com.foxminded.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ua.com.foxminded.model.Model;

public interface ModelService {

	Optional<Model> findById(Long id);
	
	Page<Model> findAll(Pageable pageable);
	
	Model save(Model entity);
	
	void deleteById(Long id);

	Optional<Model> findByName(String name);
	
	List<Model> findAll();

}

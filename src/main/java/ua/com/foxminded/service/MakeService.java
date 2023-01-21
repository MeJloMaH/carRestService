
package ua.com.foxminded.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ua.com.foxminded.model.Make;

public interface MakeService {

	Optional<Make> findById(Long id);
	
	Optional<Make> findByName(String name);
	
	List<Make> findAll();
	
	Page<Make> findAll(Pageable pageable);
	
	Make save(Make entity);
	
	void deleteById(Long id);

}

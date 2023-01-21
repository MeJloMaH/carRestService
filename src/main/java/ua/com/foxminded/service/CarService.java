package ua.com.foxminded.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import ua.com.foxminded.dto.CarDTO;
import ua.com.foxminded.model.Car;
import ua.com.foxminded.model.Category;
import ua.com.foxminded.model.Make;
import ua.com.foxminded.model.Model;
import ua.com.foxminded.model.search.SearchRequest;

public interface CarService{

	Optional<Car> findById(Long id);
	
	Car save(Car entity);
	
	void deleteById(Long id);

	Page<Car> findByYear(Integer year, Pageable pageable);
	
	Page<Car> findByMinAndMaxYear(Integer minYear, Integer maxYear, Pageable pageable);
	
	Page<Car> findByMake(Make make, Pageable pageable);
	
	Page<Car> findByModel(Model model, Pageable pageable);
	
	Page<Car> findByCategory(Category category, Pageable pageable);
	
	Page<Car> findAll(Pageable pageable);
	
	Page<Car> findAllFiltered(SearchRequest searchRequest);
	
	List<CarDTO> findAllFilteredAsListDTO(SearchRequest searchRequest);

	Optional<Car> findByObjectId(String objectId);

}

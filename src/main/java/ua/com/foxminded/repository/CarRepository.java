package ua.com.foxminded.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import ua.com.foxminded.model.Car;
import ua.com.foxminded.model.Category;
import ua.com.foxminded.model.Make;
import ua.com.foxminded.model.Model;

public interface CarRepository extends PagingAndSortingRepository<Car, Long>, JpaSpecificationExecutor<Car>{
	
	@Query("select C from Car C where C.objectId = :objectId")
	Optional<Car> findByObjectId (@Param("objectId") String objectId);
	
	@Query("select C from Car C where C.modelYear = :year")
	Page<Car> findByYear (@Param("year") Integer year, Pageable pageable);
	
	@Query("select C from Car C where C.modelYear between :minYear and :maxYear")
	Page<Car> findByMinAndMaxYear (@Param("minYear") Integer minYear, @Param("maxYear") Integer maxYear, Pageable pageable);
	
	@Query("select C from Car C where C.make = :make")
	Page<Car> findByMake (@Param("make") Make make, Pageable pageable);
	
	@Query("select C from Car C where C.model = :model")
	Page<Car> findByModel (@Param("model") Model model, Pageable pageable);
	
	@Query("select C from Car C join C.categories cc where cc in :category")
	Page<Car> findByCategory (@Param("category") Set<Category> category, Pageable pageable);

}
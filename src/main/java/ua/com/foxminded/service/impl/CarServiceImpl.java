package ua.com.foxminded.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.dto.CarDTO;
import ua.com.foxminded.model.Car;
import ua.com.foxminded.model.Category;
import ua.com.foxminded.model.Make;
import ua.com.foxminded.model.Model;
import ua.com.foxminded.model.search.SearchRequest;
import ua.com.foxminded.model.search.SearchSpecification;
import ua.com.foxminded.repository.CarRepository;
import ua.com.foxminded.service.CarService;
import ua.com.foxminded.service.CategoryService;
import ua.com.foxminded.service.MakeService;
import ua.com.foxminded.service.ModelService;

@Service
public class CarServiceImpl implements CarService {

	private final CarRepository carRepository;
	private final MakeService makeService;
	private final ModelService modelService;
	private final CategoryService categoryService;

	Logger log = LoggerFactory.getLogger(CarServiceImpl.class);

	public CarServiceImpl(CarRepository repository, MakeService makeService, ModelService modelService, CategoryService categoryService) {
		this.carRepository = repository;
		this.makeService = makeService;
		this.modelService = modelService;
		this.categoryService = categoryService;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Car> findById(Long id) {
		return carRepository.findById(id);
	}

	@Override
	public Page<Car> findByMake(Make make, Pageable pageable) {
		return carRepository.findByMake(make, pageable);
	}

	@Override
	public Page<Car> findByModel(Model model, Pageable pageable) {
		return carRepository.findByModel(model, pageable);
	}

	@Override
	public Page<Car> findByCategory(Category category, Pageable pageable) {
		return carRepository.findByCategory(Set.of(category), pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Car> findByYear(Integer year, Pageable pageable) {
		return carRepository.findByYear(year, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Car> findByObjectId(String objectId) {
		return carRepository.findByObjectId(objectId);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Car> findAll(Pageable pageable) {
		return carRepository.findAll(pageable);
	}

	@Override
	@Transactional
	public Car save(Car entity) {

		Optional<Make> optionalMake = makeService.findByName(entity.getMake().getName());
		Optional<Model> optionalModel = modelService.findByName(entity.getModel().getName());
		Set<Category> categories = entity.getCategories();

		try {
			if (optionalMake.isPresent()) {
				entity.setMake(optionalMake.get());
			} else {
				entity.setMake(makeService.save(entity.getMake()));
			}

			if (optionalModel.isPresent()) {
				entity.setModel(optionalModel.get());
			} else {
				entity.getModel().setMake(entity.getMake());
				entity.setModel(modelService.save(entity.getModel()));
			}
			
			Set<Category> categoriesToCar = new HashSet<>();
			
			for(Category c: categories) {
				Optional<Category> optionalCategory = categoryService.findByName(c.getName());
				if(optionalCategory.isPresent()) {
					categoriesToCar.add(optionalCategory.get());
				} else {
					categoriesToCar.add(categoryService.save(c));
				}
			}
			entity.setCategories(categoriesToCar);

			Car saved = carRepository.save(entity);
			log.info("Success, Car by id = {} was saved", saved.getId());
			return saved;
		} catch (Exception e) {
			log.error("Unable create Car by id = {} due: {} ", entity.getId(), e.getMessage(), e);
			return entity;
		}
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		try {
			carRepository.deleteById(id);
			log.info("Car by id = {} was deleted", id);
		} catch (Exception e) {
			log.error("Unable delete Car by id = {} due: {} ", id, e.getMessage(), e);
		}
	}

	@Override
	public Page<Car> findByMinAndMaxYear(Integer minYear, Integer maxYear, Pageable pageable) {
		return carRepository.findByMinAndMaxYear(minYear, maxYear, pageable);
	}

	@Override
	public Page<Car> findAllFiltered(SearchRequest searchRequest) {
		SearchSpecification<Car> specification = new SearchSpecification<>(searchRequest);
		Pageable pageable = searchRequest.asPageble();

		return carRepository.findAll(specification, pageable);
	}

	@Override
	public List<CarDTO> findAllFilteredAsListDTO(SearchRequest searchRequest) {
		return CarDTO.fromPage(findAllFiltered(searchRequest));

	}

}

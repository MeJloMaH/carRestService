package ua.com.foxminded.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ua.com.foxminded.model.Category;
import ua.com.foxminded.repository.CategoryRepository;
import ua.com.foxminded.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService{

	private final CategoryRepository repository;
	
	Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);
	
	public CategoryServiceImpl(CategoryRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<Category> findById(Long id) {
		return repository.findById(id);				
	}
	
	@Override
	public Optional<Category> findByName(String name) {
		return repository.findByName(name);
	}

	@Override
	public Page<Category> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public void deleteById(Long id) {
		try {
			repository.deleteById(id);
			log.info("Category by id = {} was deleted", id);
		} catch (Exception e) {
			log.error("Unable delete Category by id = {} due: {} ", id, e.getMessage(), e);
		}
	}

	@Override
	public Category save(Category entity) {
		try {
			Category saved = repository.save(entity);
			log.info("Success, Category by id = {} was saved", saved.getId());
			return saved;
		} catch (Exception e) {
			log.error("Unable create Category by id = {} due: {} ", entity.getId(), e.getMessage(), e);
			return entity;
		}
	}

	@Override
	public List<Category> findAll() {
		return repository.findAll();
	}
	
}

package ua.com.foxminded.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ua.com.foxminded.model.Make;
import ua.com.foxminded.model.Model;
import ua.com.foxminded.repository.ModelRepository;
import ua.com.foxminded.service.MakeService;
import ua.com.foxminded.service.ModelService;

@Service
public class ModelServiceImpl implements ModelService {

	private final ModelRepository repository;
	private final MakeService makeService;

	Logger log = LoggerFactory.getLogger(ModelServiceImpl.class);

	public ModelServiceImpl(ModelRepository repository, MakeService makeService) {
		this.repository = repository;
		this.makeService = makeService;
	}

	@Override
	public Optional<Model> findById(Long id) {
		return repository.findById(id);
	}

	@Override
	public Optional<Model> findByName(String name) {
		return repository.findByName(name);
	}

	@Override
	public Page<Model> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public Model save(Model entity) {
		Optional<Make> optionalMake = makeService.findByName(entity.getName());
		try {
			if(optionalMake.isPresent()) {
				entity.setMake(optionalMake.get());
			} else {
				entity.setMake(makeService.save(entity.getMake()));
			}			
			Model saved = repository.save(entity);
			log.info("Success, Model by id = {} was saved", saved.getId());
			return saved;
		} catch (Exception e) {
			log.error("Unable create Model by id = {} due: {} ", entity.getId(), e.getMessage(), e);
			return entity;
		}
	}

	@Override
	public void deleteById(Long id) {
		try {
			repository.deleteById(id);
			log.info("Model by id = {} was deleted", id);
		} catch (Exception e) {
			log.error("Unable delete Model by id = {} due: {} ", id, e.getMessage(), e);
		}
	}

	@Override
	public List<Model> findAll() {
		return repository.findAll();
	}

}

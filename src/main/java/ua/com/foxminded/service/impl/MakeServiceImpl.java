package ua.com.foxminded.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import ua.com.foxminded.model.Make;
import ua.com.foxminded.repository.MakeRepository;
import ua.com.foxminded.service.MakeService;

@Service
public class MakeServiceImpl implements MakeService{

	private final MakeRepository repository;
	
	Logger log = LoggerFactory.getLogger(MakeServiceImpl.class);
	
	public MakeServiceImpl(MakeRepository repository) {
		this.repository = repository;
	}

	@Override
	public Optional<Make> findById(Long id) {
		return repository.findById(id);
	}
	
	@Override
	public Optional<Make> findByName(String name) {
		return repository.findByName(name);
	}

	@Override
	public List<Make> findAll() {
		return repository.findAll();
	}

	@Override
	public Make save(Make entity) {
		try {
			Make saved = repository.save(entity);
			log.info("Success, Make by id = {} was saved", saved.getId());
			return saved;
		} catch (Exception e) {
			log.error("Unable create Make by id = {} due: {} ", entity.getId(), e.getMessage(), e);
			return entity;
		}
	}
	
	@Override
	public void deleteById(Long id) {
		try {
			repository.deleteById(id);
			log.info("Make by id = {} was deleted", id);
		} catch (Exception e) {
			log.error("Unable delete Make by id = {} due: {} ", id, e.getMessage(), e);
		}
	}

	@Override
	public Page<Make> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

}

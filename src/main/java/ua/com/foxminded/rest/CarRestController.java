package ua.com.foxminded.rest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import ua.com.foxminded.dto.CarDTO;
import ua.com.foxminded.model.Car;
import ua.com.foxminded.model.search.SearchRequest;
import ua.com.foxminded.model.search.request.CarSearchRequest;
import ua.com.foxminded.model.search.request.PageOptions;
import ua.com.foxminded.service.CarService;
import ua.com.foxminded.service.CategoryService;
import ua.com.foxminded.service.MakeService;
import ua.com.foxminded.service.ModelService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/cars")

public class CarRestController {

	private CarService carService;
	private MakeService makeService;
	private ModelService modelService;
	private CategoryService categoryService;

	@Operation(summary = "Get all cars")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found all entities", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CarDTO.class)))
			}),
			@ApiResponse(responseCode = "404", description = "Entity not found")
	})
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CarDTO> showCars(PageOptions pageOptions) {
		Pageable pageable;
		if (pageOptions.isDescent()) {
			pageable = PageRequest.of(pageOptions.getPage(), pageOptions.getSize(),
					Sort.by(pageOptions.getSort()).descending());
		} else {
			pageable = PageRequest.of(pageOptions.getPage(), pageOptions.getSize(), Sort.by(pageOptions.getSort()));
		}
		return CarDTO.fromPage(carService.findAll(pageable));
	}

	@Operation(summary = "Get an entity by its id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the entity", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = CarDTO.class))
			}),
			@ApiResponse(responseCode = "404", description = "Entity not found")
	})
	@GetMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public CarDTO showById(@PathVariable("id") Long id) {
		Optional<Car> car = carService.findById(id);
		if (car.isPresent()) {
			return CarDTO.fromCar(car.get());
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Create an entity", security = @SecurityRequirement(name = "bearerAuth"))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Create the entity", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = CarDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "BAD_REQUEST"),
			@ApiResponse(responseCode = "401", description = "Unauthorized") })
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CarDTO create(@Valid @RequestBody CarDTO carDto) {
		Car car = new Car();
		try {
			car = CarDTO.fromDTO(carDto);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST);
		}
		car.setId(null);
		return CarDTO.fromCar(carService.save(car));
	}

	@Operation(summary = "Update existing entity with given id", security = @SecurityRequirement(name = "bearerAuth"))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Updated the entity", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = CarDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "BAD_REQUEST"),
			@ApiResponse(responseCode = "401", description = "Unauthorized") })
	@PutMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public CarDTO update(@PathVariable("id") Long id, @Valid @RequestBody CarDTO carDto) {
		Car car = new Car();
		try {
			car = CarDTO.fromDTO(carDto);
			car.setId(id);
			return CarDTO.fromCar(carService.save(car));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "Remove entity with given id", security = @SecurityRequirement(name = "bearerAuth"))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Removed the entity"),
			@ApiResponse(responseCode = "404", description = "Entity not found") })
	@DeleteMapping("/{id}/delete")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable("id") Long id) {
		Optional<Car> car = carService.findById(id);
		if (car.isPresent()) {
			carService.deleteById(id);
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Get list of cars with parameters")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the Car with parameters", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = CarDTO.class)) }),
			@ApiResponse(responseCode = "404", description = "Entity not found", content = @Content) })
	@GetMapping("/search")
	public List<CarDTO> showCarsByFilter(CarSearchRequest request) {

		request.setMake(makeService.findByName(request.getMakeName()).orElse(null));
		request.setModel(modelService.findByName(request.getModelName()).orElse(null));

		request.setCategories(request.getCategoryNames()
				.stream()
				.map(e -> categoryService.findByName(e).orElse(null))
				.collect(Collectors.toSet()));

		SearchRequest searchRequest = request.asSearchRequest();

		return carService.findAllFilteredAsListDTO(searchRequest);
	}

}

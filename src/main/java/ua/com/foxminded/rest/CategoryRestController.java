package ua.com.foxminded.rest;

import java.util.List;
import java.util.Optional;

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
import ua.com.foxminded.dto.CategoryDTO;
import ua.com.foxminded.model.Category;
import ua.com.foxminded.model.search.request.PageOptions;
import ua.com.foxminded.service.CategoryService;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryRestController {

	private CategoryService categoryService;

	@Operation(summary = "Get all entities")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found all entities", content = {
					@Content(mediaType = "application/json", 
							array = @ArraySchema(schema = @Schema(implementation = CategoryDTO.class))) }),
			@ApiResponse(responseCode = "400", description = "Entity not found") })
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<CategoryDTO> showCategories(PageOptions pageOptions) {
		Pageable pageable;
		if (pageOptions.isDescent()) {
			pageable = PageRequest.of(pageOptions.getPage(), pageOptions.getSize(),
					Sort.by(pageOptions.getSort()).descending());
		} else {
			pageable = PageRequest.of(pageOptions.getPage(), pageOptions.getSize(), Sort.by(pageOptions.getSort()));
		}
		return CategoryDTO.fromPage(categoryService.findAll(pageable));
	}

	@Operation(summary = "Get a category by its id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the entity", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDTO.class))}),
			@ApiResponse(responseCode = "404", description = "Entity not found")})
	@GetMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public CategoryDTO showById(@PathVariable("id") Long id) {
		Optional<Category> category = categoryService.findById(id);
		if (category.isPresent()) {
			return CategoryDTO.fromCategory(category.get());
		} else {
			throw new ResponseStatusException(
			HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Create an entity", security = @SecurityRequirement(name = "bearerAuth"))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Create the entity", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDTO.class))}),
			@ApiResponse(responseCode = "401", description = "Unauthorized"),
			@ApiResponse(responseCode = "400", description = "BAD_REQUEST")})
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CategoryDTO create(@Valid @RequestBody CategoryDTO categoryDto) {
		Category category = new Category();
		try {
			category = CategoryDTO.fromDTO(categoryDto);
			category.setId(null);
			return CategoryDTO.fromCategory(categoryService.save(category));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "Update existing entity with given id", 
			security = @SecurityRequirement(name = "bearerAuth"))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Updated the entity", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = CategoryDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "BAD_REQUEST"),
			@ApiResponse(responseCode = "401", description = "Unauthorized")})
	@PutMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public CategoryDTO update(@PathVariable("id") Long id, @Valid @RequestBody CategoryDTO categoryDto) {
		Category category = new Category();
		try {
			category = CategoryDTO.fromDTO(categoryDto);
			category.setId(id);
			return CategoryDTO.fromCategory(categoryService.save(category));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST);
		}
	}

	@Operation(summary = "Remove entity with given id", 
			security = @SecurityRequirement(name = "bearerAuth"))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Removed the entity"),
			@ApiResponse(responseCode = "404", description = "Entity not found"),
			@ApiResponse(responseCode = "401", description = "Unauthorized")})
	@DeleteMapping(value = "/{id}/delete")
	@ResponseStatus(HttpStatus.OK)
	public void delete(@PathVariable("id") Long id) {
		Optional<Category> category = categoryService.findById(id);
		if (category.isPresent()) {
			categoryService.deleteById(id);
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND);
		}
	}
}

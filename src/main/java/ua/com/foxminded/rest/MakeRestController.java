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
import ua.com.foxminded.dto.MakeDTO;
import ua.com.foxminded.model.Make;
import ua.com.foxminded.model.search.request.PageOptions;
import ua.com.foxminded.service.MakeService;

@AllArgsConstructor
@RestController
@RequestMapping(path = "/api/v1/makes", produces = MediaType.APPLICATION_JSON_VALUE)
public class MakeRestController {

	private MakeService makeService;

	@Operation(summary = "Get all makes")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found all entities", content = {
					@Content(mediaType = "application/json", 
							array = @ArraySchema(schema = @Schema(implementation = MakeDTO.class)))}),
			@ApiResponse(responseCode = "400", description = "Entity not found")})
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public List<MakeDTO> showMakes(PageOptions pageOptions) {
		Pageable pageable;
		if (pageOptions.isDescent()) {
			pageable = PageRequest.of(pageOptions.getPage(), pageOptions.getSize(),
					Sort.by(pageOptions.getSort()).descending());
		} else {
			pageable = PageRequest.of(pageOptions.getPage(), pageOptions.getSize(), Sort.by(pageOptions.getSort()));
		}
		return MakeDTO.fromPage(makeService.findAll(pageable));
	}

	@Operation(summary = "Get an entity by its id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found the entity", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = MakeDTO.class))}),
			@ApiResponse(responseCode = "404", description = "Entity not found")})
	@GetMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public MakeDTO showById(@PathVariable("id") Long id) {
		Optional<Make> make = makeService.findById(id);
		if (make.isPresent()) {
			return MakeDTO.fromMake(make.get());
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND);
		}
	}

	@Operation(summary = "Create an entity", security = @SecurityRequirement(name = "bearerAuth"))
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Create the entity", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = MakeDTO.class))}),
			@ApiResponse(responseCode = "401", description = "Unauthorized"),
			@ApiResponse(responseCode = "400", description = "BAD_REQUEST")})
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public MakeDTO create(@Valid @RequestBody MakeDTO makeDto) {
		Make make = new Make();
		try {
			make = MakeDTO.fromDTO(makeDto);
			make.setId(null);
			return MakeDTO.fromMake(makeService.save(make));	
			 
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
					@Content(mediaType = "application/json", schema = @Schema(implementation = MakeDTO.class)) }),
			@ApiResponse(responseCode = "400", description = "BAD_REQUEST"),
			@ApiResponse(responseCode = "401", description = "Unauthorized")})
	@PutMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.OK)
	public MakeDTO update(@PathVariable("id") Long id, @Valid @RequestBody MakeDTO makeDto) {
		Make make = new Make();
		try {
			make = MakeDTO.fromDTO(makeDto);
			make.setId(id);
			return MakeDTO.fromMake(makeService.save(make));
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
		Optional<Make> make = makeService.findById(id);
		if (make.isPresent()) {
			makeService.deleteById(id);
		} else {
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND);
		}
	}
}

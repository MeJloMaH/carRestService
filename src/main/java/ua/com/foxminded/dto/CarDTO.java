package ua.com.foxminded.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ua.com.foxminded.model.Car;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CarDTO {

	private Long id;

	@NotBlank
	private String objectId;

	@Min(1800)
	@Max(2500)
	private Integer modelYear;

	@NotNull
	private ModelDTO modelDto;

	@NotEmpty
	private List<CategoryDTO> categoriesDto = new ArrayList<>();

	public static CarDTO fromCar(Car car) {

		if (car == null || (car.getId() == null && car.getObjectId() == null)) {
			return new CarDTO();
		}

		CarDTO dto = new CarDTO();
		dto.setId(car.getId());
		dto.setObjectId(car.getObjectId());
		dto.setModelYear(car.getModelYear());
		dto.setModelDto(ModelDTO.fromModel(car.getModel()));
		dto.setCategoriesDto(new ArrayList<>());

		car.getCategories().forEach(c -> dto.getCategoriesDto().add(CategoryDTO.fromCategory(c)));
		return dto;
	}

	public static List<CarDTO> fromPage(Page<Car> page) {

		if (page == null || page.isEmpty()) {
			return new ArrayList<>();
		}

		List<CarDTO> cars = new ArrayList<>();

		for (Car car : page.getContent()) {

			CarDTO carDTO = fromCar(car);
			cars.add(carDTO);

		}
		return cars;
	}

	public static Car fromDTO(CarDTO dto){

		Car car = new Car();

		car.setId(dto.getId());
		car.setObjectId(dto.getObjectId());
		car.setModelYear(dto.getModelYear());
		car.setModel(ModelDTO.fromDTO(dto.getModelDto()));
		car.setMake(MakeDTO.fromDTO(dto.getModelDto().getMakeDto()));
		car.setCategories(new HashSet<>());
		List<CategoryDTO> categoriesDto = dto.getCategoriesDto();
		for (CategoryDTO c : categoriesDto) {
			car.getCategories().add(CategoryDTO.fromDTO(c));
		}
		return car;
	}
	
}

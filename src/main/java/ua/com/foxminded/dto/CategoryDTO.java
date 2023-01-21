package ua.com.foxminded.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ua.com.foxminded.model.Category;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {

	private Long id;

	@NotBlank
	private String name;

	public static CategoryDTO fromCategory(Category category) {

		if (category == null || (category.getId() == null && category.getName() == null)) {
			return new CategoryDTO();
		}

		return new CategoryDTO(category.getId(), category.getName());
	}

	public static List<CategoryDTO> fromPage(Page<Category> page) {

		if (page == null || page.isEmpty()) {
			return new ArrayList<>();
		}

		List<CategoryDTO> categories = new ArrayList<>();

		for (Category category : page.getContent()) {
			categories.add(new CategoryDTO(category.getId(), category.getName()));
		}
		return categories;
	}

	public static Category fromDTO(CategoryDTO dto){

		Category category = new Category();

		category.setId(dto.getId());
		category.setName(dto.getName());
		return category;

	}

}

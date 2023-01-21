package ua.com.foxminded.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ua.com.foxminded.model.Model;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ModelDTO {

	private Long id;

	@NotBlank
	private String name;

	@NotNull
	private MakeDTO makeDto;

	public static ModelDTO fromModel(Model model) {

		if (model == null || (model.getId() == null && model.getName() == null)) {
			return new ModelDTO();
		}

		ModelDTO dto = new ModelDTO();
		dto.setId(model.getId());
		dto.setName(model.getName());
		dto.setMakeDto(MakeDTO.fromMake(model.getMake()));

		return dto;
	}

	public static List<ModelDTO> fromPage(Page<Model> page) {

		if (page == null || page.isEmpty()) {
			return new ArrayList<>();
		}
		List<ModelDTO> models = new ArrayList<>();

		for (Model model : page.getContent()) {
			models.add(new ModelDTO(model.getId(), model.getName(),
					new MakeDTO(model.getMake().getId(), model.getMake().getName())));
		}
		return models;
	}

	public static Model fromDTO(ModelDTO dto){

		Model model = new Model();

		model.setId(dto.getId());
		model.setName(dto.getName());
		model.setMake(MakeDTO.fromDTO(dto.getMakeDto()));
		return model;

	}

}
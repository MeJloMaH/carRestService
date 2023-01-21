package ua.com.foxminded.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ua.com.foxminded.model.Make;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class MakeDTO {

	private Long id;

	@NotBlank
	private String name;

	public static MakeDTO fromMake(Make make) {
		if (make == null || (make.getId() == null && make.getName() == null)) {
			return new MakeDTO();
		}
		return new MakeDTO(make.getId(), make.getName());
	}

	public static List<MakeDTO> fromPage(Page<Make> page) {

		if (page == null || page.isEmpty()) {
			return new ArrayList<>();
		}

		List<MakeDTO> makes = new ArrayList<>();

		for (Make make : page.getContent()) {
			makes.add(new MakeDTO(make.getId(), make.getName()));
		}
		return makes;
	}

	public static Make fromDTO(MakeDTO dto) {

		Make make = new Make();
		make.setId(dto.getId());
		make.setName(dto.getName());
		return make;
	}

}
package ua.com.foxminded.model.search.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarsFilterParameter {	
	private String make;
	private String model;
	private String category;
	private Integer minYear;
	private Integer maxYear;
}

package ua.com.foxminded.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor 
@Entity
@Table(name = "car")
public class Car {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
	@Column(name = "id")
	private Long id;

	@Column(name = "object_id", nullable = false, unique = true)
	@NotBlank
	private String objectId;

	@Column(name = "year", nullable = false)
	private Integer modelYear;
	
	@ManyToOne
	@JoinColumn(name = "make_ref")
	@EqualsAndHashCode.Exclude
	private Make make;
	
	@ManyToOne
	@JoinColumn(name = "model_ref")
	@EqualsAndHashCode.Exclude
	private Model model;
		
	@ManyToMany
	@JoinTable(name = "car_category", 
		joinColumns = { @JoinColumn(name = "car_id") }, 
		inverseJoinColumns = { @JoinColumn(name = "category_id") })
	@EqualsAndHashCode.Exclude
	private Set<Category> categories;
	
	public Car(Long id) {
		this.id = id;
	}
	
	public Car(Long id, String objectId, Integer modelYear, Make make, Model model, Set<Category> categories) {
		this.id = id;
		this.objectId = objectId;
		this.modelYear = modelYear;
		this.make = make;
		this.model = model;
		this.categories = categories;
		
		
	}

	@Override
	public String toString() {
		return "Car [id=" + id + ", objectId=" + objectId + ", modelYear=" + modelYear + ", "
				+ "make=" + make 
				+ ", categories=" + categories + ", model=" + model + "]";
	}

}

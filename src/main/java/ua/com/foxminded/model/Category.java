package ua.com.foxminded.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor 
@Entity
@Table(name = "category")
public class Category extends LongEntity{

	@Column(name = "name", nullable = false, unique = true)
	@NotBlank
	private String name;
	
	@EqualsAndHashCode.Exclude
	@ManyToMany	(mappedBy = "categories")
	private Set<Car> cars;

	public Category(String name) {
		this(null, name);
	}

	public Category(Long id, String name) {
		super(id);
		this.name = name;
	}

	@Override
	public String toString() {
		return "Category [id= " + id + " name=" + name + "]";
	}
	
}

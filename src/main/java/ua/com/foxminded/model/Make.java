package ua.com.foxminded.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=true)
@NoArgsConstructor 
@Entity
@Table(name = "make")
public class Make extends LongEntity{

	public Make(Long id, String name) {
		super(id);
		this.name = name;
	}

	@Column(name = "name", nullable = false, unique = true)
	@NotBlank
	private String name;
	
	@OneToMany(mappedBy = "make", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	private Set<Car> cars;
	
	@OneToMany(mappedBy = "make", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	private Set<Model> models;

	public Make(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Make [id = " + id + "name= " + name + "]";
	}
	
}

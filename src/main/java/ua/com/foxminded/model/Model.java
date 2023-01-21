package ua.com.foxminded.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor 
@EqualsAndHashCode(callSuper=true)
@Entity
@Table(name = "model")
public class Model extends LongEntity{

	@Column(name = "name", nullable = false, unique = true)
	@NotBlank
	private String name;
	
	@OneToMany(mappedBy = "model", cascade = CascadeType.ALL)
	@EqualsAndHashCode.Exclude
	private List<Car> cars;
	
	@ManyToOne
	@JoinColumn(name = "make_ref")
	private Make make;

	public Model(Long id, String name, Make make) {
		super(id);
		this.name = name;
		this.make = make;
	}
	
	public Model(Long id, String name) {
		super(id);
		this.name = name;
	}

	public Model(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Model [id = " + id + "name= " + name + ", make= " + make + "]";
	}
	
	
}

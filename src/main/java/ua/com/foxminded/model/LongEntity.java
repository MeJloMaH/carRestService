package ua.com.foxminded.model;

import java.util.Objects;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class LongEntity implements Entity<Long> {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    
	protected LongEntity() {   	
    }

	protected LongEntity(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LongEntity other = (LongEntity) obj;
		return Objects.equals(id, other.id);
	}

    
}

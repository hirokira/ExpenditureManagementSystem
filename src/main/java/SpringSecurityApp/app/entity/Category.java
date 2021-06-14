package SpringSecurityApp.app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Category {

	public Category() {}

	public Category(String category_Name) {
		setId(0L);
		this.category_Name=category_Name;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(nullable=false,unique=true)
	private Long id;

	private String category_Name;


}

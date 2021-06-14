package SpringSecurityApp.app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
public class Account  {

	public Account() {}

	public Account(String username,String password,boolean isAdmin) {
		setId(0L);
		setUsername(username);
		setPassword(password);
		setEnabled(true);
		setAdmin(isAdmin);
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(nullable=false,unique=true)
	private Long id;

	@NotNull
	@NotEmpty
	@Column(nullable=false,unique=true)
	private String username;


	@NotNull
	@NotEmpty
	@Column(nullable=false)
	private String password;

	@Column(nullable=false)
	private boolean enabled;

	@Column(nullable=false)
	private boolean admin;

//	@OneToMany(mappedBy="account")
//	private List<Item> itemList;

}

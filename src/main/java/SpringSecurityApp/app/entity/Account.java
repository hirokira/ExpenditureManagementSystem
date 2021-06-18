package SpringSecurityApp.app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.validator.constraints.Length;

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

//	@NotNull
//	@NotEmpty
	@Length(min=4,max=10,message="ユーザーIDは4文字以上10文字未満です。")
	@Column(nullable=false,unique=true)
	private String username;


//	@NotNull
//	@NotEmpty
	@Length(min=1,message="パスワードは必須項目です。")
	@Column(nullable=false)
	private String password;

	@Column(nullable=false)
	private boolean enabled;

	@Column(nullable=false)
	private boolean admin;

//	@OneToMany(mappedBy="account")
//	private List<Item> itemList;

}

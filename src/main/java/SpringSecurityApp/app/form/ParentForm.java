package SpringSecurityApp.app.form;

import java.sql.Date;
import java.util.List;

import javax.validation.Valid;

import SpringSecurityApp.app.entity.Item;
import lombok.Data;

@Data
public class ParentForm {


	@Valid
	private Date insert_date;

	@Valid
	private List<Item> items;

}

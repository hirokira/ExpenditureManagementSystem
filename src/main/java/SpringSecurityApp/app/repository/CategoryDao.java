package SpringSecurityApp.app.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;

import SpringSecurityApp.app.entity.Category;

public interface CategoryDao {

	public List<Category> categoryAll() throws DataAccessException;

	public void insert(Category category) throws DataAccessException;

}

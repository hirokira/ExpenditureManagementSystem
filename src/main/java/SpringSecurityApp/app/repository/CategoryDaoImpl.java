package SpringSecurityApp.app.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import SpringSecurityApp.app.entity.Category;

@Repository
public class CategoryDaoImpl implements CategoryDao{

	//---DB接続のため、jdbcTemplateをDI
	@Autowired
	private JdbcTemplate jdbc;

	@Override
	public List<Category> categoryAll() throws DataAccessException {
		// TODO 自動生成されたメソッド・スタブ

		//sqlの作成
		String sql = "SELECT * FROM Category";

		//BeanPropertyRowMapperインスタンスを生成。この時、型<Account>と生成するインスタンスのクラス(Account.class)を指定する。
		RowMapper<Category> rowMapper =new BeanPropertyRowMapper<Category>(Category.class);
		//SQL実行
		List<Category> list = jdbc.query(sql, rowMapper);

		return list;
	}

	@Override
	public void insert(Category category) throws DataAccessException {
		// TODO 自動生成されたメソッド・スタブ
		String sql = "INSERT INTO category(category_Name) Values(?)";

		jdbc.update(sql, category.getCategory_Name());
	}




}

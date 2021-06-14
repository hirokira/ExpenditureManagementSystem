package SpringSecurityApp.app.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import SpringSecurityApp.app.entity.Account;

@Repository
public class AccountDaoImpl implements AccountDao{


	//---DB接続のため、jdbcTemplateをDI
	@Autowired
	private JdbcTemplate jdbc;

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Account> selectAll() throws DataAccessException {
		// TODO 自動生成されたメソッド・スタブ

		String sql = "SELECT * FROM account";

		//BeanPropertyRowMapperインスタンスを生成。この時、型<Account>と生成するインスタンスのクラス(Account.class)を指定する。
		RowMapper<Account> rowmapper = new BeanPropertyRowMapper<Account>(Account.class);


		//SQL実行　JdbcTemplateのqueryメソッドを使ってDBから情報を取得。
		List<Account> list = jdbc.query(sql, rowmapper);

		return list;
	}

	@Override
	public Account findById(int id) throws DataAccessException  {
		// TODO 自動生成されたメソッド・スタブ

		String sql = "SELECT * FROM account where id = ?";

		RowMapper<Account> rowmapper = new BeanPropertyRowMapper<Account>(Account.class);

		//SQL実行　jdbcTemplateのqueryメソッドを使ってDBから情報を取得。
		Account account = jdbc.queryForObject(sql, rowmapper, id);
		return account;
	}

	@Override
	public Account findByUsername(String username) throws DataAccessException {
		// TODO 自動生成されたメソッド・スタブ

//		String sql = "SELECT * from account where username = ?";
//		RowMapper<Account> rowmapper = new BeanPropertyRowMapper<Account>(Account.class);
//		//SQL実行　jdbcTemplateのqueryメソッドを使ってDBから情報を取得。
//		Account account = jdbc.queryForObject(sql, rowmapper, username);
//
		Query query = em.createQuery("from Account where username = :fstr").setParameter("fstr", username);
		Account account = (Account) query.getSingleResult();
		return account;
	}

	@Override
	public Account findById(Long id) throws DataAccessException {
		// TODO 自動生成されたメソッド・スタブ

		String sql = "SELECT * FROM account where id = ?";

		RowMapper<Account> rowmapper = new BeanPropertyRowMapper<Account>(Account.class);

		//SQL実行　jdbcTemplateのqueryメソッドを使ってDBから情報を取得。
		Account account = jdbc.queryForObject(sql, rowmapper, id);
		return account;
	}

	@Override
	public void insert(Account account)  throws DataAccessException {
		// TODO 自動生成されたメソッド・スタブ
		String sql = "INSERT INTO account(username,password,enabled,admin) Values(?,?,?,?)";

		jdbc.update(sql, account.getUsername(),account.getPassword(),account.isEnabled(),account.isAdmin());
//		jdbc.update("INSERT INTO account(username,password,enabled,admin) Values(?,?,?,?)"
//		,account.getUsername(),account.getPassword(),account.isEnabled(),account.isAdmin());
	}

	@Override
	public void update(Account account)  throws DataAccessException {
		// TODO 自動生成されたメソッド・スタブ
//		Query query = em.createQuery("UPDATE account set username = :name,password = :password,enabled=:enabled,admin=:admin where id = :id")
//									.setParameter("name", account.getUsername())
//									.setParameter("password", account.getPassword())
//									.setParameter("enabled", account.isEnabled())
//									.setParameter("admin", account.isAdmin())
//									.setParameter("id", account.getId());
//		query.executeUpdate();
		String sql = "UPDATE account SET username = ? ,password = ?,enabled = ?,admin=? WHERE id = ?;";

		jdbc.update(sql, account.getUsername(),account.getPassword(),account.isEnabled(),account.isAdmin(),account.getId());

	}

	@Override
	public void delete(Long id) throws DataAccessException {

		String sql = "DELETE FROM account WHERE id = ?;";

		jdbc.update(sql, id);
	}



}

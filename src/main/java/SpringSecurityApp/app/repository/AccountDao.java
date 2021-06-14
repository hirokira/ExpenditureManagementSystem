package SpringSecurityApp.app.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;

import SpringSecurityApp.app.entity.Account;

public interface AccountDao {

	public List<Account> selectAll() throws DataAccessException;

	public Account findById(int id) throws DataAccessException ;

	public Account findById(Long id) throws DataAccessException ;

	public Account findByUsername(String username) throws DataAccessException;

	public void insert(Account account) throws DataAccessException;

	public void update(Account account) throws DataAccessException;

	public void delete(Long id) throws DataAccessException;
}

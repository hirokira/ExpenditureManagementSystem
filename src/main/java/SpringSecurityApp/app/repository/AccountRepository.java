package SpringSecurityApp.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SpringSecurityApp.app.entity.Account;


@Repository
public interface AccountRepository extends JpaRepository<Account,Integer>{

	public Account findByUsername(String username);

}

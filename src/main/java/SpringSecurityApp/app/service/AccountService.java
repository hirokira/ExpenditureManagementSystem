package SpringSecurityApp.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import SpringSecurityApp.app.entity.Account;
import SpringSecurityApp.app.repository.AccountDaoImpl;
import SpringSecurityApp.app.repository.AccountRepository;

@Transactional
@Service
public class AccountService {


	@Autowired
	private AccountDaoImpl dao;

	@Autowired
	private AccountRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;



	//---Accountテーブルの一覧を返す
	public List<Account> selectAll(){
		return dao.selectAll();
	}

	//---Accountテーブルから指定された行のObjectを取得する。(int型)
	public Account findById(int id) {
		return dao.findById(id);
	}

	//---Accountテーブルから指定された行のObjectを取得する。(Long型)
	public Account findById(Long id) {
		return dao.findById(id);
	}

	//---Accountテーブルから指定されたUserNameのObjectを取得する。
	public Account findByUsername(String username) {
		return dao.findByUsername(username);
	}


	//---AccountをDBに登録。(adminかUserでそれぞれ分ける)
	public void accountSave(Account account) {
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		if(account.isAdmin()) {
			svAccountAdmin(account);
		}else {
			svAccountUser(account);
		}
	}


	@Transactional
	public void svAccountAdmin(Account account) {
		account.setAdmin(true);
		dao.insert(account);	//---jdbcTemplateでINSERT処理
		//repository.save(account);
	}

	@Transactional
	public void svAccountUser(Account account) {
		account.setAdmin(false);
		dao.insert(account);
		//repository.save(account);
	}

	//---アカウント情報更新
	@Transactional
	public void updateAccount(Account account) {
		account.setPassword(passwordEncoder.encode(account.getPassword()));
		dao.update(account);
	}


	//---Edit画面の前パスワード比較ロジック
	public boolean passMatches(Account account,String password) {
		if(passwordEncoder.matches(password, findById(account.getId()).getPassword())) {
			return true;
		}else {
			return false;
		}
	}

	//----アカウント情報削除
	/*
	 * テスト用に作ったが、実際には使っていないデスコード
	 */
	@Transactional
	public void deleteAccount(Account account) {
		dao.delete(account.getId());
	}

}

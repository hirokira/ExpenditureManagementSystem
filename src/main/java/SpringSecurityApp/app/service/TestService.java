package SpringSecurityApp.app.service;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import SpringSecurityApp.app.entity.Account;
import SpringSecurityApp.app.entity.Item;
import SpringSecurityApp.app.repository.AccountDaoImpl;
import SpringSecurityApp.app.repository.AccountRepository;
import SpringSecurityApp.app.repository.ItemRepository;

@Service

public class TestService {

	@Autowired
	private AccountRepository repository;

	@Autowired
	private AccountDaoImpl impl;

	@Autowired
	private ItemRepository itemRepository;

//	@Autowired
//	private PasswordEncoder passwordEncoder;

	@Transactional
	public void svAdmin(String username,String password) {
		Account user = new Account(username,password,true);
		repository.save(user);
	}

	@Transactional
	public void svUser(String username,String password) {
		Account user = new Account(username,password,false);
		repository.save(user);
	}

	@Transactional
	public void svItem(String itemName,int price) {

		Account user = impl.findById(2);

		Date currentTime = new Date(System.currentTimeMillis());
		Item item = new Item(itemName,price,user,currentTime,0,2);
		itemRepository.save(item);
	}

}

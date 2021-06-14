package SpringSecurityApp.app.repository;

import java.sql.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import SpringSecurityApp.app.entity.Account;
import SpringSecurityApp.app.entity.Item;

@Repository
public class ItemDaoImpl implements ItemDao{

	private static org.slf4j.Logger logger = LoggerFactory.getLogger(ItemDaoImpl.class);

	//DB接続のため、jdbcTemplateをインジェクション
	@Autowired
	private JdbcTemplate jdbc;

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Item> itemAll() throws DataAccessException {
		// TODO 自動生成されたメソッド・スタブ
		String sql = "SELECT * from item";

		//BeanPropertyRowMapperインスタンスを生成。この時、型<Account>と生成するインスタンスのクラス(Account.class)を指定する。
		RowMapper<Item> rowMapper = new BeanPropertyRowMapper<Item>(Item.class);

		//SQL実行　JdbcTemplateのqueryメソッドを使ってDBから情報を取得。
		List<Item> itemList = jdbc.query(sql, rowMapper);

		return itemList;
	}

	@Override
	public void insert(Item item) throws DataAccessException {
			// TODO 自動生成されたメソッド・スタブ
		String sql = "insert into item (account_id, bunrui1, bunrui2, insert_date, item_name, price) values (?, ?, ?, ?, ?, ?)";

		jdbc.update(sql, item.getAccount().getId(),item.getBunrui1(),item.getBunrui2(),item.getInsert_date(),item.getItemName(),item.getPrice());

	}

	@Override
	@Modifying
	public void itemUpdate(Item item) throws DataAccessException {
		// TODO 自動生成されたメソッド・スタブ
//		Query query = em.createQuery("update item set item_name = ?1,price = ?2,bunrui1=?3,bunrui2=?4 where id = ?5;")
//				.setParameter(1, item.getItemName())
//				.setParameter(2, item.getPrice())
//				.setParameter(3, item.getBunrui1())
//				.setParameter(4, item.getBunrui2())
//				.setParameter(5, item.getId());
//		query.executeUpdate();
//		,price = ?,bunrui1 = ?,bunrui2 = ?
		String sql = "UPDATE item set item_name = ?,price = ?,bunrui1 = ?,bunrui2 = ? WHERE id = ?;";
		jdbc.update(sql, item.getItemName(),item.getPrice(),item.getBunrui1(),item.getBunrui2(),item.getId());

	}

	@Override
	public List<Item> itemAllByAccount(Account account) throws DataAccessException {
		// TODO 自動生成されたメソッド・スタブ

		//SQL作成 account_idが一致するレコードのみ抽出
		String sql = "SELECT * from item where account_id = ?";
		RowMapper<Item> rowMapper = new BeanPropertyRowMapper<Item>(Item.class);

		//SQL実行　引数は、sql,rowMapper,抽出条件
		List<Item> itemList = jdbc.query(sql, rowMapper, account.getId());

		return itemList;
	}

	@Override
	public List<Item> itemAllByDay(String insert_date) throws DataAccessException {
		// TODO 自動生成されたメソッド・スタブ

		//SQL作成　dateが一致するレコードのみ抽出
		String sql = "SELECT * from item where insert_date = ?";
		RowMapper<Item> rowMapper = new BeanPropertyRowMapper<Item>(Item.class);

		//SQL実行 引数は、sql文,rowMapper,抽出条件
		List<Item> list = jdbc.query(sql, rowMapper, insert_date);
		return list;
	}


	//---年月日が同じデータのみリストで取得
	@Override
	public List<Item> itemAllByDate(Date insert_date) throws DataAccessException {
		//SQL作成　dateが一致するレコードのみ抽出
		String sql = "SELECT * from item where insert_date = ?";
		RowMapper<Item> rowMapper = new BeanPropertyRowMapper<Item>(Item.class);

		//SQL実行 引数は、sql文,rowMapper,抽出条件
		List<Item> list = jdbc.query(sql, rowMapper, insert_date);
		return list;
	}

	//---年月日が同じデータのみリストで取得
	@Override
	public List<Item> itemByIdAndDate(Date insert_date,Account account) throws DataAccessException {
		//SQL作成　dateが一致するレコードのみ抽出
		String sql = "SELECT * from item where insert_date = ? and account_id = ?";
		RowMapper<Item> rowMapper = new BeanPropertyRowMapper<Item>(Item.class);

		//SQL実行 引数は、sql文,rowMapper,抽出条件
		List<Item> list = jdbc.query(sql, rowMapper, insert_date,account.getId());
		return list;
	}

	//---年月日が同じデータのみリストで取得（ページングあり)
	@Override
	public Page<Item> itemAllByDate(Pageable pageable, Date insert_date,Account account) throws DataAccessException {
		// TODO 自動生成されたメソッド・スタブ
		List<Item> itemAll = itemByIdAndDate(insert_date,account);
		int total = itemAll.size();

		Query query = em.createQuery("from Item where insert_date = :insert_date and account_id = :account_id")
				.setParameter("insert_date", insert_date)
				.setParameter("account_id", account.getId());
		List<Item> list = query.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();

		System.out.println(total);
		Page<Item> pageList = new PageImpl<Item>(list,pageable,total);
		return pageList;
	}


	@Override
	public List<Item> itemAllByMonth(String insert_date) throws DataAccessException {
		// TODO 自動生成されたメソッド・スタブ

		//SQL作成 insert_dateにおいて、年月が一致しているレコードのみ抽出
		String sql = "SELECT * FROM item where insert_date like ?";
		RowMapper<Item> rowMapper = new BeanPropertyRowMapper<Item>(Item.class);

		//SQL実行　 引数は、SQL文、ROWMAPPER,抽出条件
		List<Item> list = jdbc.query(sql, rowMapper, insert_date+"%");
		return list;
	}

	/*
	 * 学習の過程で作成したメソッド。使わない。
	@Override
	public List<Item> itemByIdAndMonth(String insert_date, Account account) throws DataAccessException {
		// TODO 自動生成されたメソッド・スタブ
//		String sql = "SELECT * from item where insert_date like ? and account_id = ?";
		String sql = "SELECT * from item where DATE_FORMAT(insert_date,'%Y-%m') = ? and account_id = ?";
		RowMapper<Item> rowMapper = new BeanPropertyRowMapper<Item>(Item.class);

		//SQL実行 引数は、sql文,rowMapper,抽出条件
		List<Item> list = jdbc.query(sql, rowMapper, insert_date,account.getId());
		return list;
	}
	*/

	public List<Item> itemByIdAndMonth(Date insert_date, Account account) throws DataAccessException {
		// TODO 自動生成されたメソッド・スタブ
		String sql = "SELECT * from item where DATE_FORMAT(insert_date,'%Y-%m') = DATE_FORMAT(?,'%Y-%m') and account_id = ?";
		RowMapper<Item> rowMapper = new BeanPropertyRowMapper<Item>(Item.class);

		//SQL実行 引数は、sql文,rowMapper,抽出条件
		List<Item> list = jdbc.query(sql, rowMapper, insert_date,account.getId());
		return list;
	}

	/*
	 * 学習の過程で作成したメソッド。使わない。
	@Override
	public Page<Item> itemAllByMonth(Pageable pageable, String insert_date, Account account) throws DataAccessException {
		// TODO 自動生成されたメソッド・スタブ
		List<Item> itemAll = itemByIdAndMonth(insert_date,account);
		int total = itemAll.size();

		Query query = em.createQuery("from Item where DATE_FORMAT(insert_date,'%Y-%m') = :insert_date and account_id = :account_id")
				.setParameter("insert_date", insert_date)
				.setParameter("account_id", account.getId());
		@SuppressWarnings("unchecked")
		List<Item> list = query.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();

		Page<Item> pageList= new PageImpl<Item>(list,pageable,total);
		return pageList;
	}
	*/

	@Override
	public Page<Item> itemAllByMonth(Pageable pageable, Date insert_date, Account account) throws DataAccessException {
		// TODO 自動生成されたメソッド・スタブ
		List<Item> itemAll = itemByIdAndMonth(insert_date,account);
		int total = itemAll.size();

		Query query = em.createQuery("from Item where DATE_FORMAT(insert_date,'%Y-%m') = DATE_FORMAT(:insert_date,'%Y-%m') and account_id = :account_id")
				.setParameter("insert_date", insert_date)
				.setParameter("account_id", account.getId());
		@SuppressWarnings("unchecked")
		List<Item> list = query.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();

		Page<Item> pageList= new PageImpl<Item>(list,pageable,total);
		return pageList;
	}

	@Override
	public Page<Item> findItemByAccount(Pageable pageable, Account account) {

		//---ページング機能なしのListのカウント数を算出
		//---ToDO:正直イケてないロジックなので修正必要---  20210320  -------------------
		List<Item> itemAll = findItemByAccount2(account);
		int total = itemAll.size();

		//---ページング機能ありの実装
		Query query = em.createQuery("from Item where account_id = :id").setParameter("id", account.getId());
		List<Item> list = query.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();

		System.out.println(total);
		//List<Item> list = query.setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
		Page<Item> pageList = new PageImpl<Item>(list,pageable,total);
		return pageList;
	}

	//---ページング確認用DapImpl
	@Override
	public List<Item> findItemByAccount2(Account account) {
		// TODO 自動生成されたメソッド・スタブ
		Query query = em.createQuery("from Item where account_id = :id").setParameter("id", account.getId());
		List<Item> list = query.getResultList();
		int total = query.getResultList().size();
		System.out.println(total);
		return list;
	}

	@Override
	public List<Item> itemAllByYear(Date insert_date,Account account) throws DataAccessException {
		// TODO 自動生成されたメソッド・スタブ
//		String sql = "SELECT * from item where insert_date like ? and account_id = ?";
		String sql = "SELECT * from item where DATE_FORMAT(insert_date,'%Y') = DATE_FORMAT(?,'%Y') and account_id = ?";
		RowMapper<Item> rowMapper = new BeanPropertyRowMapper<Item>(Item.class);

		//SQL実行 引数は、sql文,rowMapper,抽出条件
		List<Item> list = jdbc.query(sql, rowMapper, insert_date,account.getId());
		return list;
	}


}

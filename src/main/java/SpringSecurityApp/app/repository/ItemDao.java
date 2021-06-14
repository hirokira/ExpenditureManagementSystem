package SpringSecurityApp.app.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import SpringSecurityApp.app.entity.Account;
import SpringSecurityApp.app.entity.Item;

public interface ItemDao {

	public List<Item> itemAll() throws DataAccessException;

	public List<Item> itemAllByAccount(Account account) throws DataAccessException;


	public void insert(Item item) throws DataAccessException;

	public void itemUpdate(Item item) throws DataAccessException;

	public List<Item> itemAllByDay(String insert_date) throws DataAccessException;

	//---年月日が同じデータのみリストで取得
	public List<Item> itemAllByDate(Date insert_date) throws DataAccessException;

	public List<Item> itemByIdAndDate(Date insert_date,Account account)throws DataAccessException;

	//---ページングありで年月日が同じデータのみリストで取得
	public Page<Item> itemAllByDate(Pageable pageable,Date insert_date,Account account) throws DataAccessException;

	public List<Item> itemAllByMonth(String insert_date) throws DataAccessException;

	//---年月が同じ、かつ、登録アカウントがログインアカウントと一致したもののみ抽出
//	public List<Item> itemByIdAndMonth(String insert_date,Account account)throws DataAccessException;

	//---年月が同じ、かつ、登録アカウントがログインアカウントと一致したもののみ抽出
	public List<Item> itemByIdAndMonth(Date insert_date,Account account)throws DataAccessException;

	//---年月が同じ、かつ、登録アカウントがログインアカウントと一致したもののみ抽出(ページング)引数：String型
//	public Page<Item> itemAllByMonth(Pageable pageable,String insert_date,Account account)throws DataAccessException;

	//---年月が同じ、かつ、登録アカウントがログインアカウントと一致したもののみ抽出(ページング)引数：Date型
	public Page<Item> itemAllByMonth(Pageable pageable,Date insert_date,Account account)throws DataAccessException;

	//---年が同じ、かつ登録アカウントがログインアカウントと一致したものを取得
	public List<Item> itemAllByYear(Date insert_date,Account account) throws DataAccessException;

	public Page<Item> findItemByAccount(Pageable pageable,Account account);

	//---ページングテスト用DAO
	public List<Item> findItemByAccount2(Account account);



}

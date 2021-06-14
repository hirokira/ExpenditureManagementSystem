package SpringSecurityApp.app.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import SpringSecurityApp.app.entity.Account;
import SpringSecurityApp.app.entity.Item;
import SpringSecurityApp.app.repository.ItemDaoImpl;
import SpringSecurityApp.app.repository.ItemRepository;

@Service
public class ItemService {


	@Autowired
	private ItemDaoImpl dao;

	@Autowired
	private ItemRepository repository;

	//Item一覧を取得
	public List<Item> itemAll(){
		return dao.itemAll();
	}

	//指定したアカウントのItem一覧を取得
	public List<Item> itemAllByAccount(Account account){
		return dao.itemAllByAccount(account);
	}

	//Itemのインサート処理
	public void itemInsert(Item item) {
		dao.insert(item);
	}

	//---Itemのアップデート処理
	public void itemUpdate(Item item) {
		dao.itemUpdate(item);
	}

	//指定した年月日のItem一覧を取得(引数：String型）
	public List<Item> itemAllByDay(String insert_date){
		return dao.itemAllByDay(insert_date);
	}

	//指定した年月日のItem一覧を取得（引数：Date型）
	public List<Item> itemAllByDate(Date insert_date){
		return dao.itemAllByDate(insert_date);
	}

	//指定したUserNameの年月日のItem一覧を取得（引数：Date型）
	public List<Item> itemByIdAndDate(Date insert_date,Account account){
		return dao.itemByIdAndDate(insert_date,account);
	}

	//指定した年月日のItem一覧を取得（引数：Date型）(ページング)
	public Page<Item> itemAllByDate(Pageable pageable,Date insert_date,Account account){
		return dao.itemAllByDate(pageable,insert_date,account);
	}

	//指定した年月のItem一覧を取得(引数：String型)
	public List<Item> itemAllByMonth(String insert_date){
		return dao.itemAllByMonth(insert_date);
	}

	//指定したアカウントと、年月が一致するItem一覧を取得(引数：String型、Account)
//	public List<Item> itemByIdAndMonth(String insert_date,Account account){
//		return dao.itemByIdAndMonth(insert_date, account);
//	}

	//指定したアカウントと、年月が一致するItem一覧を取得(引数：Date型、Account)
	public List<Item> itemByIdAndMonth(Date insert_date,Account account){
		return dao.itemByIdAndMonth(insert_date, account);
	}

	//指定したアカウントと、年月が一致するItem一覧を取得(ページングあり　引数：String型、Account)
//	public Page<Item> itemAllByMonth(Pageable pageable,String insert_date,Account account){
//		return dao.itemAllByMonth(pageable,insert_date, account);
//	}

	//指定したアカウントと、年月が一致するItem一覧を取得(ページングあり　引数：Date型、Account)
	public Page<Item> itemAllByMonth(Pageable pageable,Date insert_date,Account account){
		return dao.itemAllByMonth(pageable,insert_date, account);
	}


	public Page<Item> getItems(Pageable pageable){
		return repository.findAll(pageable);
	}

	public Page<Item> findItemByAccount(Pageable pageable,Account account){
		return dao.findItemByAccount(pageable, account);
	}

	public List<Item> findItemByAccount2(Account account){
		return dao.findItemByAccount2(account);
	}

	public List<Item> itemAllByYear(Date insert_date,Account account){
		return dao.itemAllByYear(insert_date, account);
	}

	public Item findById(Long id) {
		return repository.findById(id);
	}

	//---引数リストの分類１毎のトータルを算出(SQLでも出来る気がする。SQL勉強：2021/04/03 Add
	public Long bunrui1Syukei(List<Item> list,int bunrui1) {
		Long total = 0L;
		for(int i =0;i < list.size();i++) {
			if(list.get(i).getBunrui1()==bunrui1) {
				total+=list.get(i).getPrice();
			}
		}
		return total;
	}

	public List<Integer> bunrui1List(){
		List<Integer> list = new ArrayList<Integer>();
		list.add(0);
		list.add(1);
		return list;
	}

	public Map<String,Long> bunrui1Syukei(List<Item> list,List<Integer> bunrui1List){
		Map<String,Long> map = new HashMap<String,Long>();
		Long total = 0L;
		for(int j = 0;j < bunrui1List.size();j++) {
			for(int i = 0; i < list.size();i++) {
				if(list.get(i).getBunrui1()==bunrui1List.get(j)) {  //----list[i]の分類１がbunrui1List[j]が同じであればトータルに加算
					total += list.get(i).getPrice();
				}
			}
			if(j==0) {
				map.put("固定費", total);//---mapに追加<キー：bunrui1+j,値：total>
			}else {
				map.put("変動費", total);
			}
			total = 0L;
		}
		map.put("総額", map.get("固定費")+map.get("変動費"));

		return map;
	}




}

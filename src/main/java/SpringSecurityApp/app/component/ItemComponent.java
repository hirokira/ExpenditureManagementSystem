package SpringSecurityApp.app.component;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import SpringSecurityApp.app.entity.Item;
import SpringSecurityApp.app.form.ParentForm;

@Component
public class ItemComponent {

	//分類2から分類1を決定するロジック(Itemクラス引数)
	public Item bunrui_logic(Item item) {
		Item item_r = item;
		if(item_r.getBunrui2()<=3) {
			item_r.setBunrui1(0);
		}else {
			item_r.setBunrui1(1);
		}
		return item;
	}

	//分類2から分類1を決定するロジック(ParentFormクラス引数)
	public ParentForm bunrui_logic2(ParentForm form) {
		//Item item = new Item();
		ParentForm parentForm = form;
		for(int i = 0; i<form.getItems().size() ; i++) {
			if(form.getItems().get(i).getBunrui2()<=3) {
				form.getItems().get(i).setBunrui1(0);

				System.out.println(form);
			}else {
				form.getItems().get(i).setBunrui1(1);

				System.out.println(form);
			}
		}

		System.out.println(form);
		return parentForm;
	}


	//Item名とPriceが入力されたかチェックするロジック(引数：Item.class)
	public boolean nyuryokuCheck(Item item) {
		if(item.getItemName().equals("")) {
			return false;
		}else {
			return true;
		}
	}

	//---Item名がどれか一件でも入力されたかチェックするロジック(引数：ParentForm.class)
	public boolean nyuryokuCheckParent(ParentForm parentForm) {
		boolean checkFlag = false;
		for(int i = 0;i < parentForm.getItems().size(); i++) {
			if(!parentForm.getItems().get(i).getItemName().equals("")) {
				checkFlag=true;
			}
		}
		return checkFlag;
	}


	//dayIndex用に、年月日のListを作成。
	//年
	public List<String> yearList(){
		List<String> yearlist=new ArrayList<String>();
		for(int i = 2021; i < 2031 ; i++) {
			yearlist.add(String.valueOf(i));
		}
		return yearlist;
	}

	//---月
	public List<String> monthList(){
		List<String> monthlist=new ArrayList<String>();
		for(int i = 1; i <= 12 ; i++) {
			monthlist.add(String.valueOf(i));
		}
		return monthlist;
	}

	//---日
	public List<String> dayList(String year,String month){
		List<String> daylist=new ArrayList<String>();
		if(month.equals("04")||month.equals("06")||month.equals("09")||month.equals("11")) {
			for(int i=1 ; i<30;i++) {
				daylist.add(String.valueOf(i));
			}
		}else if(month.equals("02")) {
			int yearInt = Integer.parseInt(year);

			//---うるう年チェック：4の倍数。しかし、100の倍数で400で割り切れない年は平年
			if(yearInt%4!=0 || (yearInt%100==0 && yearInt%400!=0)) {
				for(int i=1 ; i<28;i++) {
					daylist.add(String.valueOf(i));
				}
			}else {
				for(int i=1 ; i<29;i++) {
					daylist.add(String.valueOf(i));
				}
			}
		}else {
			for(int i=1 ; i<31;i++) {
				daylist.add(String.valueOf(i));
			}
		}
		return daylist;
	}


	//---分類2一覧を配列に入れて返す
	public List<String> categoryList(){
		List<String> list = new ArrayList<String>();
		list.add("住居費");
		list.add("水道光熱費");
		list.add("通信費");
		list.add("保険料");
		list.add("食費");
		list.add("日用品費");
		list.add("被服費");
		list.add("美容費");
		list.add("交際費");
		list.add("交通費");
		list.add("教育費");
		list.add("医療費");
		list.add("特別費");
		list.add("雑費");
		return list;
	}

}

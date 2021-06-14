package SpringSecurityApp.app.component;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import SpringSecurityApp.app.entity.Category;
import SpringSecurityApp.app.entity.Item;
import SpringSecurityApp.app.service.CategoryService;

/*
 * 日付の集計管理をするクラス
 */
@Component
public class DaySyuukeiComponent {

	@Autowired
	private CategoryService categoryService;

	private int sum=0;

	//---分類②毎に合計値を算出するメソッド 2021/03/02追加
	public List<Integer> bunruiSyukei(List<Item> itemList){
		List<Integer> list = new ArrayList<Integer>();	//---分類２毎に合計値をADDする用のList
		List<Category> categoryList =categoryService.categoryList();//---カテゴリーをDBから読み込んで、Listに格納。


		for(int i = 0;i<categoryList.size();i++) {//---カテゴリーの数だけ回す
			for(int j = 0; j < itemList.size();j++) {//---ItemListの数だけ回す
				if(itemList.get(j).getBunrui2()==categoryList.get(i).getId()) {	//---カテゴリーリストのIDと、ItemList[j]の分類２が同じであれば合計値に加算する。
					sum+=itemList.get(j).getPrice();//---合計値を加算
				}
			}
			list.add(sum);//---listに分類2(i)の合計値を追加
			sum = 0;//----合計値の初期化を行って次のカテゴリーへ
		}
		return list;
	}

}

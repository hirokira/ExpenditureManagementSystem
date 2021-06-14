package SpringSecurityApp.app.component;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import SpringSecurityApp.app.entity.Item;


@Component
public class MonthTotalInYear {



	public List<Integer> monthTotalInYear(List<Item> list){
		int total = 0;
		List<Integer> monthTotalList = new ArrayList<Integer>();

		for(int j = 1; j <= 12 ;j++) {
			for(int i = 0; i < list.size(); i++) {
				if(((list.get(i).getInsert_date().getMonth()+1)==j)) {	//---getMonthでは0～11で取得するため、条件式に+1をする必要がある。
					total+=list.get(i).getPrice();
				}
			}
			monthTotalList.add(total);
			total = 0;
		}
		return monthTotalList;

	}

}

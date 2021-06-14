package SpringSecurityApp.app.controller;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import SpringSecurityApp.app.component.DaySyuukeiComponent;
import SpringSecurityApp.app.component.ItemComponent;
import SpringSecurityApp.app.component.MonthTotalInYear;
import SpringSecurityApp.app.entity.Account;
import SpringSecurityApp.app.entity.Category;
import SpringSecurityApp.app.entity.Item;
import SpringSecurityApp.app.service.CategoryService;
import SpringSecurityApp.app.service.ItemService;

/*
 * Date:2021/03/14
 *
 * Comment:年毎の画面コントローラー（/yearIndex/*)
 *
 */
@Controller
public class YearIndexController {

	@Autowired
	private ItemService service;

	@Autowired
	private ItemComponent itemComponent;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private DaySyuukeiComponent dayComponent;

	@Autowired
	private HttpSession session;

	@Autowired
	private MonthTotalInYear monthTotalInYear;

	@RequestMapping(value="/item/yearIndex",method=RequestMethod.GET)
	public ModelAndView dayIndex(ModelAndView mav) {
		mav.setViewName("/item/yearIndex");

		// 現在日時情報で初期化されたインスタンスの取得
		LocalDateTime nowDateTime = LocalDateTime.now();
		String year = String.valueOf(nowDateTime.getYear());//---現在の日時の"年"情報を取得

		mav.addObject("year", year);
		mav.addObject("yearList", itemComponent.yearList());//----selectの要素を作成し格納
		return mav;
	}

	@RequestMapping(value="/item/yearIndex/circle",method=RequestMethod.POST)
	public ModelAndView circle(@RequestParam(value="year",required=true)String year,ModelAndView mav) {

		//---Sessionからログインアカウント情報を取得
		Account account = (Account) session.getAttribute("sessionAccount");

		String strDate = year+"-"+"01-01";//---選択した年月をString型の変数に格納
        Date insert_date = java.sql.Date.valueOf(strDate);//---String型の年月日をsql.Date型に変換

        /*　以下のロジックは不要の為コメントアウト
         * 　　理由としては、daoでDATE_FORMATを使って年月までの比較が行えるため。　2021/04/03 Add
         *
		//----yyyy-MM-ddの年まで取得するため、subStringを使用する。その為のbeginIndex,endIndexを指定する。
//		final int  BEGININDEX = 0;
//		final int ENDINDEX = 4;
        //---insert_dateをString型に変換し、日付を除外する。もしくは、strDate作成時にmonthを編集する。
        //---insert_dateをString型に変換し、substringで年月まで抽出。
//        strDate = insert_date.toString();
//        strDate = strDate.substring(BEGININDEX, ENDINDEX);

*/
		/*
		 * 月毎のトータル支出を算出して表示
		 * ToDo:monthTotalInYearメソッドをコンポーネントに作成する。：完了
		 *作成還流尾に伴い、コメントアウト
		 */
//		Long total = (long) 0;
//		for(int j = 1; j <= 12 ;j++) {
//			for(int i = 0; i < list.size(); i++) {
//				if(((list.get(i).getInsert_date().getMonth()+1)==j)) {	//---getMonthでは0～11で取得するため、条件式に+1をする必要がある。
//					total+=list.get(i).getPrice();
//				}
//			}
//			monthTotalList.add(total);
//			total = 0L;
//		}

		List<Item> list = service.itemAllByYear(insert_date, account);
		List<Integer> monthTotalList = new ArrayList<Integer>();
		monthTotalList = monthTotalInYear.monthTotalInYear(list);

		//---棒グラフのdate用に、monthTotalListを配列化
		Integer[] monthTotalArray = monthTotalList.toArray(new Integer[monthTotalList.size()]);
		System.out.println(Arrays.toString(monthTotalArray));

		mav.addObject("monthTotalDate", monthTotalArray);

		//--ToDo:MAPを使って取得できない？Map<String,Long>で分類１の固定費、変動費それぞれのトータルを取得する。：2021/04/04 完了
//		Long bunrui1Total = service.bunrui1Syukei(list, 1);

		//---Mapを使って、分類１の固定費/変動費/総額を取得するコード追加　2021/04/04
		Map<String,Long> map = new HashMap<String,Long>();
		map = service.bunrui1Syukei(list, service.bunrui1List());
		mav.addObject("bunrui1Syukei", map);

        mav.addObject("list", service.itemAllByYear(insert_date, account));

		mav.addObject("year", year);
		mav.addObject("totalList", monthTotalList);
		mav.addObject("url", "/item/monthIndex/Details");
		mav.setViewName("/item/yearCircle");

	//-----円グラフ用の情報取得--------------------
		List<Category> categoryList = categoryService.categoryList();//-----カテゴリーテーブルからカテゴリ一覧を取得。

		//---CategoriListを配列化(for文で回して順番に配列へ格納する。
		List<String> array = new ArrayList<String>();
		for(int i = 0;i<categoryList.size();i++) {
			array.add(categoryList.get(i).getCategory_Name());
		}
		System.out.println("Listで表示："+array);//----Listで格納したList変数arrayを出力
		String[] strArray = array.toArray(new String[array.size()]);//----カテゴリが格納されたListを配列に変換

		mav.addObject("label", strArray);

		//---Category毎の合計をListに格納する。
		List<Integer> categorySumList = new ArrayList<Integer>();//---Listの箱作成
		categorySumList =dayComponent.bunruiSyukei(service.itemAllByYear(insert_date,account));//---カテゴリ毎の合計値をListに格納
		System.out.println(categorySumList);

		//---categorySumListを配列化
		Integer[] sumArray = categorySumList.toArray(new Integer[categorySumList.size()]);
		System.out.println(Arrays.toString(sumArray));

		mav.addObject("date", sumArray);
		mav.addObject("yearText", year+"年の支出");		//---Circleのtext用文字列格納
		return mav;
	}


	/*
	 * 以下メソッドの内容は"/item/yearIndex/circle"に表示するようにしたため、
	 * 以下コードはデッドコード。
	 *   2021/04/03 Add
	 */
	@RequestMapping(value="/item/yearIndex/Details",method=RequestMethod.GET)
	public ModelAndView yearIndexDetails(@RequestParam(name="year")String year,
										Pageable pageable,ModelAndView mav) {

		String strDate = year+"-"+"01-01";//---選択した年月をString型の変数に格納
        Date insert_date = java.sql.Date.valueOf(strDate);//---String型の年月日をsql.Date型に変換

		//---ログインアカウント情報を取得
		Account account = (Account) session.getAttribute("sessionAccount");

		/*
		 * 月毎のトータル支出を算出して表示
		 * monthTotalInYearメソッドをコンポーネントに作成する。
		 *
		 */
		List<Item> list = service.itemAllByYear(insert_date, account);
		List<Integer> monthTotalList = new ArrayList<Integer>();
		//---指定した年の、月毎の支出額を算出してリスト化するメソッド追加　2021/04/03Add
		monthTotalList = monthTotalInYear.monthTotalInYear(list);

		System.out.println(monthTotalList);
		mav.addObject("totalList", monthTotalList);
		mav.addObject("year", year);
		mav.addObject("url", "/item/monthIndex/Details");

		mav.setViewName("/item/hello");
		return mav;
	}
}

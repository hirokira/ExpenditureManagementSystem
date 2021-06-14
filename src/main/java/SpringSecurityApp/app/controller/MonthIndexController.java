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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import SpringSecurityApp.app.component.DaySyuukeiComponent;
import SpringSecurityApp.app.component.ItemComponent;
import SpringSecurityApp.app.entity.Account;
import SpringSecurityApp.app.entity.Category;
import SpringSecurityApp.app.entity.Item;
import SpringSecurityApp.app.repository.ItemDaoImpl;
import SpringSecurityApp.app.service.CategoryService;
import SpringSecurityApp.app.service.ItemService;

/*
 * Date:2021/03/14
 *
 * Comment:月毎の画面コントローラー（/monthIndex/*)
 *
 */
@Controller
public class MonthIndexController {

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
	private ItemDaoImpl dao;


	@RequestMapping(value="/item/monthIndex",method=RequestMethod.GET)
	public ModelAndView dayIndex(ModelAndView mav) {
		mav.setViewName("/item/monthIndex");

		// 現在日時情報で初期化されたインスタンスの取得
		LocalDateTime nowDateTime = LocalDateTime.now();
		String year = String.valueOf(nowDateTime.getYear());//---現在の日時の"年"情報を取得
		String month = String.valueOf(nowDateTime.getMonthValue());//---現在の日時の"月"情報を取得

		mav.addObject("year", year);
		mav.addObject("month", month);

		mav.addObject("yearList", itemComponent.yearList());//----selectの要素を作成し格納
		mav.addObject("monthList", itemComponent.monthList());//----selectの要素を作成し格納
		return mav;
	}

	@RequestMapping(value="/item/monthIndex/circle",method=RequestMethod.POST)
	public ModelAndView circle(@RequestParam(value="year",required=true)String year,
								@RequestParam(value="month",required=true)String month,ModelAndView mav) {

		//---Loginしているアカウントユーザー情報取得--Add 2021/03/29
		Account account = (Account) session.getAttribute("sessionAccount");

		String strDate = year+"-"+month+"-01";//---選択した年月をString型の変数に格納
        Date insert_date = java.sql.Date.valueOf(strDate);//---String型の年月日をsql.Date型に変換

/*　以下のロジックは不要の為コメント
 * 　　理由としては、daoでDATE_FORMATを使って年月までの比較が行えるため。　2021/03/31 Add
 *
 *		//----yyyy-MM-ddの年月まで取得するため、subStringを使用する。その為のbeginIndex,endIndexを指定する。
		final int  BEGININDEX = 0;
		final int ENDINDEX = 7;
        //---2021/03/15 ToDo:insert_dateをString型に変換し、日付を除外する。もしくは、strDate作成時にmonthを編集する。
        //---2021/03/15 insert_dateをString型に変換し、substringで年月まで抽出。
//        strDate = insert_date.toString();
//        strDate = strDate.substring(BEGININDEX, ENDINDEX);
//
//        mav.addObject("list", service.itemByIdAndMonth(strDate,account));

*/
		mav.addObject("year", year);
		mav.addObject("month", month);
		mav.setViewName("/item/monthCircle");

		/*
		 *　分類１毎の総額/固定費/変動費をmap型で取得　2021/04/04 Add
		 */
		List<Item> list = service.itemByIdAndMonth(insert_date, account);//---アカウントと指定した月が同じItemをリストで取得
		List<Integer> bunrui1_list = service.bunrui1List();//---分類１のリストを取得
		Map<String,Long> map = new HashMap<String,Long>();
		map=service.bunrui1Syukei(list, bunrui1_list);

		mav.addObject("bunrui1_Syukei", map);

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
		categorySumList =dayComponent.bunruiSyukei(service.itemByIdAndMonth(insert_date,account));//---カテゴリ毎の合計値をListに格納
		System.out.println(categorySumList);

		//---categorySumListを配列化
		Integer[] sumArray = categorySumList.toArray(new Integer[categorySumList.size()]);
		System.out.println(Arrays.toString(sumArray));

		mav.addObject("date", sumArray);
		mav.addObject("monthText", month+"月の支出");		//---Circleのtext用文字列格納
		return mav;
	}


	@RequestMapping(value="/item/monthIndex/Details",method=RequestMethod.GET)
	public ModelAndView monthDetails(@RequestParam(name="year")String year,
									@RequestParam(name="month")String month,
									@PageableDefault(page = 0,size = 15)Pageable pageable,ModelAndView mav) {

		//---ログインアカウント情報をセッションから取得
		Account account = (Account)session.getAttribute("sessionAccount");

		mav.addObject("msg", year+"年"+month+"月の支出一覧");//---View表示用のmsg作成

		String strDate = year+"-"+month+"-01";//---選択した年月をString型の変数に格納
        Date insert_date = java.sql.Date.valueOf(strDate);//---String型の年月日をsql.Date型に変換

  /*　以下のロジックは不要の為コメント　2021/03/31 Add
  * 　　理由としては、daoでDATE_FORMATを使って年月までの比較が行えるため。
  *
  * 	//----yyyy-MMのみ取得するため、subStringを使用する。その為のbeginIndex,endIndexを指定する。
		final int  BEGININDEX = 0;
		final int ENDINDEX = 7;
        //---insert_dateをString型に変換し、日付を除外する。もしくは、strDate作成時にmonthを編集する。
        //---insert_dateをString型に変換し、substringで年月まで抽出。
        strDate = insert_date.toString();
        strDate = strDate.substring(BEGININDEX, ENDINDEX);
  */
        //----年月が同じ、かつ、登録アカウントがログインアカウント情報と一致しているもののみ取得。
        Page<Item> list = service.itemAllByMonth(pageable, insert_date, account);
        mav.addObject("page", list);
        mav.addObject("list", list.getContent());
		String urlRequest = "?year="+year+"&month="+month;//---ページネーション時にRequestParam受け取り用のクエリパラメータ作成
		mav.addObject("url", "/item/monthIndex/Details"+urlRequest);//---ページネーション時に遷移する用のURL作成

		mav.setViewName("/item/dayList");

		return mav;
	}
}

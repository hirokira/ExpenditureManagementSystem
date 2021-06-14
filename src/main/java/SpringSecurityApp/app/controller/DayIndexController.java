package SpringSecurityApp.app.controller;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import SpringSecurityApp.app.service.AccountService;
import SpringSecurityApp.app.service.CategoryService;
import SpringSecurityApp.app.service.ItemService;

/*
 * Date:2021/03/10
 *
 * Comment:日付毎の画面コントローラー（/day/*)
 *
 */
@Controller
public class DayIndexController {

	@Autowired
	private ItemService service;

	@Autowired
	private ItemComponent itemComponent;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private DaySyuukeiComponent dayComponent;

	@Autowired
	private AccountService as;

	@Autowired
	private HttpSession session;

	@RequestMapping(value="/item/dayIndex",method=RequestMethod.GET)
	public ModelAndView dayIndex(ModelAndView mav) {
		mav.setViewName("/item/dayIndex");

		// 現在日時情報で初期化されたインスタンスの取得
		LocalDateTime nowDateTime = LocalDateTime.now();
		String year = String.valueOf(nowDateTime.getYear());//---現在の日時の"年"情報を取得
		String month = String.valueOf(nowDateTime.getMonthValue());//---現在の日時の"月"情報を取得
		String day = String.valueOf(nowDateTime.getDayOfMonth());//---現在の日時の"日"情報を取得

		mav.addObject("year", year);
		mav.addObject("month", month);
		mav.addObject("day", day);

		mav.addObject("yearList", itemComponent.yearList());//----selectの要素を作成し格納
		mav.addObject("monthList", itemComponent.monthList());//----selectの要素を作成し格納

		//---2021/03/14 ToDO:年月から、Dayの要素を算出。2,4,6,9,11月は31日以外。:2021/03/15完了
		mav.addObject("dayList", itemComponent.dayList(year,month));//----selectの要素を作成し格納
		return mav;
	}

	@RequestMapping(value="/item/dayIndex/circle",method=RequestMethod.POST)
	public ModelAndView circle(@RequestParam(value="year",required=true)String year,
								@RequestParam(value="month",required=true)String month,
								@RequestParam(value="day",required=true)String day,
								@PageableDefault(page = 0, size = 15)Pageable pageable,ModelAndView mav) {

		//---Sessionからログインアカウント情報取得
          Account account = (Account)session.getAttribute("sessionAccount");

		/*
		 * リスト表示させるための情報取得とAdd作業---Start
		 */
		String strDate = year+"-"+month+"-"+day;//---選択した年月日をString型の変数に格納
        System.out.println(strDate);

        Date insert_date = java.sql.Date.valueOf(strDate);//---String型の年月日をsql.Date型に変換
        System.out.println(insert_date);

        //---同じ年月日のリストを取得してAddする。(1)
//        mav.addObject("list", service.itemAllByDate(insert_date));

        //---同じ年月日のリストを取得してAddする。(ページングあり)(2)---20210321追加
        Page<Item> itemList = service.itemAllByDate(pageable, insert_date,account);
		mav.addObject("page", itemList);
		mav.addObject("list", itemList.getContent());
		mav.addObject("url", "/item/dayIndex/circle");

		mav.addObject("year", year);
		mav.addObject("month", month);
		mav.addObject("day", day);

		mav.setViewName("/item/circle");
		/*
		 * リスト表示させるための情報取得とAdd作業---End
		 */

		/*
		 * --- 円グラフに必要な情報取得--Start
		 */
		List<Category> categoryList = categoryService.categoryList();//-----カテゴリーテーブルからカテゴリ一覧を取得。

		//---CategoriListを配列化(for文で回して順番に配列へ格納する。
		List<String> array = new ArrayList<String>();
		for(int i = 0;i<categoryList.size();i++) {
			array.add(categoryList.get(i).getCategory_Name());
		}
		String[] strArray = array.toArray(new String[array.size()]);//----カテゴリが格納されたListを配列に変換
		System.out.println("配列で表示："+Arrays.toString(strArray));//----配列で格納した配列strArrayを出力

		mav.addObject("label", strArray);

		//---Category毎の合計をListに格納する。
		List<Integer> categorySumList = new ArrayList<Integer>();//---Listの箱作成
		categorySumList =dayComponent.bunruiSyukei(service.itemByIdAndDate(insert_date,account));//---カテゴリ毎の合計値をListに格納
		System.out.println(categorySumList);

		//---2021/03/03 categorySumListを配列化
		Integer[] sumArray = categorySumList.toArray(new Integer[categorySumList.size()]);
		System.out.println(Arrays.toString(sumArray));

		mav.addObject("date", sumArray);
		mav.addObject("dayText", strDate+"の支出");		//---Circleのtext用文字列格納
		/*
		 * --- 円グラフに必要な情報取得--End
		 */
		return mav;
	}

	/*
	 * 指定した日付の支出情報詳細を表示する。(ページネーション)
	 */
	@RequestMapping(value="/item/dayIndex/Details",method=RequestMethod.GET)
	public ModelAndView circleDetailsGet(@RequestParam(name="year") String year,
										@RequestParam(name="month")String month,
										@RequestParam(name="day")String day,
										@PageableDefault(page = 0, size = 15)Pageable pageable,ModelAndView mav) {

		//---Sessionからログインアカウント情報取得
        Account account = (Account)session.getAttribute("sessionAccount");
		/*
		 * リスト表示させるための情報取得とAdd作業---Start
		 */
		String strDate = year+"-"+month+"-"+day;//---選択した年月日をString型の変数に格納

      Date insert_date = java.sql.Date.valueOf(strDate);//---String型の年月日をsql.Date型に変換

      //---同じ年月日のリストを取得してAddする。(ページングあり)(2)---20210321追加
      Page<Item> itemList = service.itemAllByDate(pageable, insert_date,account);
		mav.addObject("page", itemList);
		mav.addObject("list", itemList.getContent());
		String urlRequest = "?year="+year+"&month="+month+"&day="+day;//---ページネーション時にRequestParam受け取り用のクエリパラメータ作成
		mav.addObject("url", "/item/dayIndex/Details"+urlRequest);//---ページネーション時に遷移する用のURL作成
		mav.setViewName("/item/dayList");
		mav.addObject("msg", year+"年"+month+"月"+day+"日の支出一覧");
		/*
		 * リスト表示させるための情報取得とAdd作業---End
		 */

		return mav;

	}
}

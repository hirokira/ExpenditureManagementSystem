package SpringSecurityApp.app.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import SpringSecurityApp.app.component.ItemComponent;
import SpringSecurityApp.app.entity.Account;
import SpringSecurityApp.app.entity.Item;
import SpringSecurityApp.app.entity.UserAccount;
import SpringSecurityApp.app.form.ParentForm;
import SpringSecurityApp.app.repository.ItemDaoImpl;
import SpringSecurityApp.app.repository.ItemRepository;
import SpringSecurityApp.app.service.AccountService;
import SpringSecurityApp.app.service.ItemService;

/*
 * Date:2021/03/10
 *
 * Comment：ItemのCRUDコントローラー
 */
@Controller
public class ItemController {

	@Autowired
	private ItemService service;

	@Autowired
	private ItemDaoImpl dao;

	@Autowired
	private ItemRepository repository;

	@Autowired
	private ItemComponent itemComponent;

	@Autowired
	private AccountService as;

	@Autowired
	private HttpSession session;

	@RequestMapping(value="/item/top")
	public ModelAndView itemTop(ModelAndView mav) {
		mav.setViewName("/item/itemTop");

    	//---現在のリクエストに紐づく Authentication を取得するには
    	//    	SecurityContextHolder.getContext().getAuthentication() とする。
    	//    	SecurityContextHolder.getContext() は、現在のリクエストに紐づく SecurityContext を返している。
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      //Authentication.getAuthorities() で、現在のログインユーザーに付与されている権限（GrantedAuthority のコレクション）を取得できる。
        System.out.println("  " + authentication.getAuthorities());

        //---Authentication.getPrincipal() で、ログインユーザーの UserDetails を取得できる。
        UserDetails principal = (UserDetails)authentication.getPrincipal();
        session.setAttribute("sessionAccount", as.findByUsername(String.valueOf(principal.getUsername())));
        mav.addObject("loginUser", (Account)session.getAttribute("sessionAccount"));

        //---UserAccountがログインユーザーの UserDetailsと同じもしくは子クラスかをチェック。
    	if(authentication.getPrincipal() instanceof UserAccount) {
    		UserAccount user = UserAccount.class.cast(authentication.getPrincipal());
    		mav.addObject("userInfo","LoginUser："+user.getUsername());
    	}else {
    		mav.addObject("userInfo", "");
    	}

    	mav.addObject("msg", session.getAttribute("msg"));

    	if(session.getAttribute("msg")!=null) {
    		session.removeAttribute("msg");
    	}

		return mav;
	}

	//Item一覧を取得して表示する
//	@RequestMapping(value="/item/indexAdmin")
//	public ModelAndView itemIndexAdmin(ModelAndView mav) {
//		mav.setViewName("/item/itemIndex");
//		List<Item> itemList=service.itemAll();
//		mav.addObject("list", itemList);
//		return mav;
//	}

	//Item一覧を取得して表示する(ページネーション)
	@RequestMapping(value="/item/indexAdmin")
	public ModelAndView itemIndexAdmin(ModelAndView mav,@PageableDefault(page = 0, size = 15) Pageable pageable) {
		Page<Item> itemList=service.getItems(pageable);
		mav.setViewName("/item/itemIndex2");
//		List<Item> itemList=service.itemAll();
		mav.addObject("page", itemList);
		mav.addObject("list", itemList.getContent());
		mav.addObject("url", "/item/indexAdmin");
		System.out.println(itemList);
		return mav;
	}

//	//指定したアカウントのItem一覧を取得し表示する
//	@RequestMapping(value="/item/index")
//	public ModelAndView itemIndex(ModelAndView mav) {
//		mav.setViewName("/item/itemIndex");
//		Account account = as.findById(1);
//		List<Item> itemList=service.itemAllByAccount(account);
//		mav.addObject("list", itemList);
//		return mav;
//	}

	//指定したアカウントのItem一覧を取得し表示する(ページネーション)
	@RequestMapping(value="/item/index")
	public ModelAndView itemIndex(@PageableDefault(page = 0, size = 15)Pageable pageable,ModelAndView mav) {
		mav.setViewName("/item/itemIndex2");
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails principal = (UserDetails)authentication.getPrincipal();

        //---principal.getUsername()で取得できるのはStringu型ではないので、String型に変換する。
//		Account account = as.findByUsername(String.valueOf(principal.getUsername()));
        Account account = (Account)session.getAttribute("sessionAccount");

		Page<Item> itemList=service.findItemByAccount(pageable, account);
		mav.addObject("page", itemList);
		mav.addObject("list", itemList.getContent());
		mav.addObject("url", "/item/index");
		return mav;
	}

//	@RequestMapping(value="/item/index")
//	public ModelAndView itemIndex(@PageableDefault(page = 0, size = 15)Pageable pageable,ModelAndView mav) {
//		mav.setViewName("/item/itemIndex");
//		Account account = as.findById(1);
//		List<Item> itemList=service.findItemByAccount2(account);
//		mav.addObject("list", itemList);
//		mav.addObject("url", "/item/index");
//		return mav;
//	}

//	@RequestMapping(value="/item/index")
//	public ModelAndView itemIndex2(ModelAndView mav) {
//		mav.setViewName("/item/itemIndex");
//		Account account = as.findById(2);
//		List<Item> itemList=service.findItemByAccount(account);
////		mav.addObject("page", itemList);
////		mav.addObject("list", itemList.getContent());
////		System.out.println(itemList.getSize());
////		System.out.println(itemList.getContent());
////		mav.addObject("url", "/item/index");
//		mav.addObject("list", itemList);
//		return mav;
//	}

//	@RequestMapping(value="/item/new")
//	public ModelAndView itemNew(ModelAndView mav) {
//		Item item = new Item();
//		//Account account =as.findById(1);
//		//item.setAccount(account);
//		Date ts = new Date(System.currentTimeMillis());
//		item.setInsert_date(ts);
//		mav.addObject("ts", ts);
//		mav.addObject("formModel", item);
//		mav.setViewName("/item/itemNew");
//		return mav;
//	}

//	@RequestMapping(value="/item/create",method=RequestMethod.POST)
//	public ModelAndView itemCreate(@ModelAttribute("formModel")@Validated Item item,
//									BindingResult result,ModelAndView mav) {
//		//---2021/02/24 ToDo：複数のオブジェクト受け取りに対応する。：未完了
//        /*-------*/
//		//--2021/02/24 ToDo：Repositoryを使わない保存をする。：完了
//		ModelAndView res = null;
//		if(!result.hasErrors()&&itemComponent.nyuryokuCheck(item)) {
//			Account account =as.findById(1);
//			item.setAccount(account);
//			item = itemComponent.bunrui_logic(item);//---分類２を元に、分類１を決定するロジック
//			System.out.println(item);
//			//repository.save(item); ----repositoryを使ったインサート
//			service.itemInsert(item);
//			res = new ModelAndView("redirect:/item/top");
//		}else {
//			//---2021/02/24 ToDo:エラーがあった場合の処理
//			mav.addObject("msg", "入力内容に不備があります。");
//			mav.setViewName("/item/itemNew");
//			mav.addObject("formModel", item);
//			res = mav;
//		}
//		return res;
//	}


	/*
	 * 2021/03/11 Add
	 * フォームに複数のItemを入力して登録する
	 *
	 */
	@RequestMapping(value="/item/new")
	public ModelAndView itemNew(ModelAndView mav) {
		final int COUNT=9;

		ParentForm form = new ParentForm();//---親クラス
		List<Item> itemList=new ArrayList<Item>();//---List<Item>のList変数作成
		Item item = new Item();//---Itemのインスタンス作成
		Date ts = new Date(System.currentTimeMillis());
		System.out.println(ts);
		//item.setInsert_date(ts);
		form.setInsert_date(ts);
		itemList.add(item);//---ListにItem追加
		form.setItems(itemList);//---ParentFormのItemsにItenList追加。

		mav.addObject("COUNT", COUNT);
		mav.addObject("ts", ts);
		mav.addObject("formModel", form);
		mav.setViewName("/item/itemNew2");
		System.out.println(form);
		return mav;
	}

	/*
	 * 2021/03/11
	 * フォームに複数のItemを入力してDBに登録する。
	 */
	@RequestMapping(value="/item/create2",method=RequestMethod.POST)
	public ModelAndView itemCreateTest(@ModelAttribute("formModel")@Validated ParentForm form,
									BindingResult result,ModelAndView mav) {
		//---2021/02/24 ToDo：複数のオブジェクト受け取りに対応する。：2021/03/13完了
        /*-------*/
		//--2021/02/24 ToDo：Repositoryを使わない保存をする。：完了
		ModelAndView res = null;

		//---ToDO:直下のIF文を、resultにエラーがない　かつ　フォームのどのItemNameの入力欄が空だった場合に修正する。---2021/03/11---2021/03/14完了

		if(!result.hasErrors()&&itemComponent.nyuryokuCheckParent(form)) {		//---Sessionからログインアカウント情報取得
	        Account account2 = (Account)session.getAttribute("sessionAccount");
			Account account =as.findById(account2.getId());

			for(int i = 0; i< form.getItems().size(); i++) {
				form.getItems().get(i).setAccount(account);//---ログインユーザーを登録者情報にセット
				form.getItems().get(i).setInsert_date(form.getInsert_date());//---ParentFormフィールドのInsert_dateをItemフィールドのInsert_dateに代入
				//item = itemComponent.bunrui_logic(form.getItems().get(0));//---分類２を元に、分類１を決定するロジック(Itemクラス引数）
				form = itemComponent.bunrui_logic2(form);//---分類２を元に、分類１を決定するロジック(ParentFormクラス引数)

				if(form.getItems().get(i).getItemName()!=null && !form.getItems().get(i).getItemName().equals("")) {
					service.itemInsert(form.getItems().get(i));//---Itemオブジェクトのインサート処理
				}
			}
			session.setAttribute("msg", "商品の登録が完了しました。");
			res = new ModelAndView("redirect:/item/top");
		}else {
			//---2021/02/24 ToDo:エラーがあった場合の処理---2021/03/14完了
			mav.addObject("msg", "入力内容に不備があります。");//---Formに漏れがある事を知らせるメッセージ格納
			//mav.addObject("formModel", form);					//---入力されたフォーム内容を再度格納
			res = itemNew(mav);									//---メソッドitemNewを呼び出し。
		}
		return res;
	}

	@RequestMapping(value="/item/edit/{id}",method=RequestMethod.GET)
	public ModelAndView itemShow(@PathVariable("id")Long id,ModelAndView mav) {
		Item item = service.findById(id);
		mav.addObject("formModel", item);
		mav.setViewName("/item/itemEdit");
		return mav;
	}

	@RequestMapping(value="/item/update",method=RequestMethod.POST)
	public ModelAndView itemUpdate(@ModelAttribute("formModel")@Validated Item item,
									BindingResult result,ModelAndView mav) {
		ModelAndView res = null;
		dao.itemUpdate(item);
		session.setAttribute("msg", "商品の編集が完了しました。");
		res = new ModelAndView("redirect:/item/top");
//		if(!result.hasErrors()) {
//			service.itemUpdate(item);
//			res = new ModelAndView("redirect:/item/top");
//		}else {
//			mav.setViewName("/item/itemEdit");
//			mav.addObject("formModel", item);
//			mav.addObject("result", result);
//			res = mav;
//		}
		return res;
	}


}

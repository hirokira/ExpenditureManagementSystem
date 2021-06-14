package SpringSecurityApp.app.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import SpringSecurityApp.app.entity.Account;
import SpringSecurityApp.app.repository.AccountRepository;
import SpringSecurityApp.app.service.AccountService;

@Controller
public class AccountController {


	@Autowired
	private AccountService as;

	@Autowired
	private AccountRepository repository;

	@Autowired
	private HttpSession session;

	@RequestMapping(value="/user/index",method=RequestMethod.GET)
	public ModelAndView userIndex(ModelAndView mav) {

		List<Account> listAccount = as.selectAll();
		mav.addObject("list", listAccount);
		mav.setViewName("/user/accountList");
		return mav;
	}


	@RequestMapping(value="/user/new",method=RequestMethod.GET)
	public ModelAndView userNew(ModelAndView mav) {

		Account account = new Account();
		account.setEnabled(true);

		mav.addObject("formModel", account);

		mav.setViewName("/user/accountNew");

		return mav;
	}


	@RequestMapping(value="/user/create",method=RequestMethod.POST)
	public ModelAndView userCreate(@ModelAttribute("formModel") @Validated Account account,
									BindingResult result,ModelAndView mav) {

		ModelAndView res =null;
		if(!result.hasErrors()/*&&!"".equals(account.getUsername())&&!"".equals(account.getPassword())*/) {
			as.accountSave(account);
			mav.addObject("msg", "登録が完了しました。");
			session.setAttribute("msg","ユーザー登録が完了しました。");
			res=new ModelAndView("redirect:/login");
		}else {
			mav.setViewName("/user/accountNew");
			mav.addObject("result", result);
			mav.addObject("formModel", account);
			mav.addObject("msg", "内容に不備があります。");
			res = mav;
		}
		return res;
	}

	@RequestMapping(value="/user/show/{id}",method=RequestMethod.GET)
	public ModelAndView userShow(@PathVariable("id")int id,ModelAndView mav) {
		Account account = as.findById(id);
		mav.addObject("formModel", account);
		mav.setViewName("/user/accountShow");
		return mav;
	}

	@RequestMapping(value="/user/show/{id}",method=RequestMethod.POST)
	public ModelAndView userEdit(@RequestParam("id") int id,ModelAndView mav) {
		Account account = as.findById(id);
		mav.addObject("formModel", account);
		mav.setViewName("/user/accountEdit");
		return mav;
	}

	@RequestMapping(value="/user/update",method=RequestMethod.POST)
	public ModelAndView userUpdate(@ModelAttribute("formModel")@Validated Account account,
									BindingResult result,
									@RequestParam("password_before")String password_before,
									ModelAndView mav) {
		ModelAndView res = null;

		if(!result.hasErrors()&&as.passMatches(account, password_before)) {
			as.updateAccount(account);
			res = new ModelAndView("redirect:/item/top");
			session.setAttribute("msg","更新が完了しました。");
		}else {
			mav.setViewName("/user/accountEdit");
			mav.addObject("formModel", account);
			mav.addObject("result", result);
			if(!as.passMatches(account, password_before)) {
				mav.addObject("msg", "変更前のパスワードが違います。");
			}
			res = mav;
		}
		return res;
	}


	/*
	 *
	 * 家計簿アプリでは使用していないが、お試しで作成した。
	 *ToDO：課題として、Itemテーブルに登録されているユーザーIDは削除できない。
	 *　　2021/03/23
	 */
	@RequestMapping(value="/user/delete/{id}",method=RequestMethod.POST)
	public ModelAndView userDelete(@RequestParam("id") long id,ModelAndView mav) {
		Account account = as.findById(id);
		ModelAndView res =null;
		if(account != null) {
			mav.setViewName("/user/accountList");
			mav.addObject("msg", "削除が完了しました。");
			as.deleteAccount(account);
			List<Account> accountList = as.selectAll();
			mav.addObject("list", accountList);
			res = mav;
		}else {
			res = new ModelAndView("/redirect:/user/index");
			res.addObject("msg", "該当するユーザーは存在しません。");
		}
		return res;
	}
}

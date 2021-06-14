package SpringSecurityApp.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import SpringSecurityApp.app.entity.Category;
import SpringSecurityApp.app.repository.CategoryDaoImpl;
import SpringSecurityApp.app.service.TestService;

@Controller
public class TestController {


	@Autowired
	private TestService sv;

	@Autowired
	private CategoryDaoImpl categoryDao;


	@RequestMapping(value="/test",method = RequestMethod.GET)
	public String test(Model model) {
		//sv.svAdmin("admin","admin");
//		sv.svUser("hiro", "1234");
		sv.svItem("ピーマン", 98);
		return "/user/hello";


	}

	@RequestMapping(value="/test2",method = RequestMethod.GET)
	public String category_create(Model model) {
		//sv.svAdmin("admin","admin");
//		sv.svUser("hiro", "1234");
		List<Category> list = new ArrayList<Category>();
		list.add(new Category("住居費"));
		list.add(new Category("水道光熱費"));
		list.add(new Category("通信費"));
		list.add(new Category("保険料"));
		list.add(new Category("食費"));
		list.add(new Category("日用品費"));
		list.add(new Category("被服費"));
		list.add(new Category("美容費"));
		list.add(new Category("交際費"));
		list.add(new Category("交通費"));
		list.add(new Category("教育費"));
		list.add(new Category("医療費"));
		list.add(new Category("特別費"));
		list.add(new Category("雑費"));

//		for(int i = 0;i<list.size();i++) {
//			categoryDao.insert(list.get(i));
//		}
		return "/user/hello";


	}
}

package SpringSecurityApp.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import SpringSecurityApp.app.entity.Category;
import SpringSecurityApp.app.repository.CategoryDaoImpl;

@Service
public class CategoryService {

	@Autowired
	private CategoryDaoImpl dao;



	//カテゴリ一覧を取得するメソッド
	public List<Category> categoryList(){
		List<Category> list = dao.categoryAll();
		return list;

	}

}

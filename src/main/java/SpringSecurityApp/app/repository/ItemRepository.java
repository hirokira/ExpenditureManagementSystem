package SpringSecurityApp.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import SpringSecurityApp.app.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item,Integer>{
	//---ページネーション
	public Page<Item> findAll(Pageable pageable);

	public Item findById(Long id);
//
//	public List<Item> getList(long index,int end);

}

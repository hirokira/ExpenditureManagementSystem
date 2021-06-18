package SpringSecurityApp.app.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class Item {

	public Item() {}

	public Item(String itemName,int price,Account account,Date insert_date,int bunrui1,int bunrui2) {
		setId(0L);
		setItemName(itemName);
		setPrice(price);
		setAccount(account);
		setInsert_date(insert_date);
		setBunrui1(bunrui1);
		setBunrui2(bunrui2);
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(nullable=false,unique=true)
	private Long id;


	@Column(nullable=false)
//	@NotNull
//	@Length(min=1,message="商品名は必須項目です。")
	private String itemName;

	@Column(nullable=false)
//	@NotNull
//	@Length(min=1,message="値段は必須項目です。")
	private int price;

	@ManyToOne
	private Account account;


	@Column(nullable=false)
	private Date insert_date;

	/*
	 * 0.固定費
	 * 1.変動費
	 */
	@Column(nullable=false)
	private int bunrui1;


	/*
	 * ----以下固定費
	 * 1.住居費
	 * 2.水道光熱費
	 * 3.通信費
	 * 4.保険料
	 *
	 * ----以下変動費
	 * 5.食費	外食費や食材購入費、酒代など飲食に関わる支出項目
	 * 6.日用品費	掃除道具やシャンプー、ティッシュペーパーなど日用品の支出項目
	 * 7.被服費	洋服や靴、アクセサリーなどの支出項目
	 * 8.美容費	化粧品やエステなどの支出項目
	 * 9.交際費	デートや飲み会、友人・知人へのプレゼントなどの支出項目
	 * 10.趣味費	書籍代や映画チケット代など趣味に関する支出項目
	 * 11.交通費	電車やバスの運賃、ガソリン代などの支出項目
	 * 12.教育費	子どもの学費や教材の購入費などの支出項目
	 * 13.医療費	通院費や入院費、医薬品の購入費などの支出項目
	 * 14.特別費	イベントなど、毎月発生しない支出の項目
	 * 15.雑費	上記以外、用途不明の支出項目
	 * 15.
	 */
	@Column(nullable=false)
	private int bunrui2;

}

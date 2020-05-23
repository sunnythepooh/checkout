package hk.sunnychan.checkout.model;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class Basket {

	private int id;
	private Map<BasketItem, Integer> items;

	public Basket(int id, Map<BasketItem, Integer> items) {
		this.id = id;
		this.items=items;
	}
	
	public int getId() {
		return id;
	}
	
	public Map<BasketItem,Integer> getItems() {
		return Collections.unmodifiableMap(items);
	}
	
	public void visitBasket(BasketVisitor visitor) {
		Objects.requireNonNull(visitor);
		items.forEach((item, count) -> item.accept(visitor, count));
	}
}

package hk.sunnychan.checkout.manager;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import hk.sunnychan.checkout.model.Basket;
import hk.sunnychan.checkout.model.BasketItem;
import hk.sunnychan.checkout.model.Product;

public class BasketManager {
	private static class BasketData extends Basket {
		HashMap<BasketItem, Integer> basketData;

		public BasketData(int id, HashMap<BasketItem, Integer> basket) {
			super(id, basket);
			this.basketData = basket;
		}
	}

	private AtomicInteger idGenerator = new AtomicInteger(0);
	HashMap<Integer, BasketData> baskets = new HashMap<>();

	/**
	 * Create a new empty basket
	 * 
	 * @return id for new basket
	 */
	public Basket createBasket() {
		int id = idGenerator.incrementAndGet();
		BasketData basket = new BasketData(id, new HashMap<>());
		baskets.put(id, basket);
		return basket;
	}

	/**
	 * update item to basket
	 * 
	 * @param basket   to update
	 * @param product
	 * @param quantity
	 * @return if update successful, an updated basket will return, otherwise return
	 *         null
	 */
	public boolean updateBasket(Basket basket, Product product, int quantity) {
		Objects.requireNonNull(product);
		Objects.requireNonNull(basket);
		if (quantity < 0) {
			throw new IllegalArgumentException("quantity cannot be < 0");
		}
		BasketData b = baskets.get(basket.getId());
		if (b == null) {
			return false;
		}
		b.basketData.put(product, quantity);
		return true;
	}

	/**
	 * Remove product form basket
	 * 
	 * @param basket
	 * @param product
	 * @return updated instance of basket if successful, null otherwise
	 */
	public boolean removeFromBasket(Basket basket, Product product) {
		Objects.requireNonNull(basket);
		Objects.requireNonNull(product);

		BasketData b = baskets.get(basket.getId());
		if (b == null) {
			return false;
		}
		Integer result = b.basketData.remove(product);
		if (result == null) {
			return false;
		}
		return true;
	}

	public boolean removeBasket(Basket basket) {
		Objects.requireNonNull(basket);
		BasketData b = baskets.remove(basket.getId());
		return b != null;
	}

	public List<Basket> getAllBasket() {
		return Collections
				.unmodifiableList(baskets.values().stream().map(Basket.class::cast).collect(Collectors.toList()));
	}

}

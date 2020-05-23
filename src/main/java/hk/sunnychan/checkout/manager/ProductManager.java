package hk.sunnychan.checkout.manager;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import hk.sunnychan.checkout.model.Product;

public class ProductManager {
	AtomicInteger idGenerator = new AtomicInteger(0);

	HashMap<Integer, Product> productStore = new HashMap<>();

	/**
	 * add a new product and returns product id
	 * 
	 * @param description
	 * @param price
	 * @return unique product id
	 */
	public Product addProduct(String description, Double price) {
		Objects.requireNonNull(description);
		if (price < 0) {
			throw new IllegalArgumentException("price is less than 0");
		}
		int id = idGenerator.incrementAndGet();
		Product p = new Product(id, description, price);
		productStore.put(id, p);
		return p;
	}

	public Optional<Product> getProduct(int id) {
		if (id <= 0) {
			throw new IllegalArgumentException("id cannot be < 0");
		}
		return Optional.ofNullable(productStore.get(id));
	}

	/**
	 * Update product 
	 * @param p product to update
	 * @param description
	 * @param price
	 * @return if update is successful, return new Product instance, otherwise null
	 */
	public Product updateProduct(Product p, String description, double price) {
		Objects.requireNonNull(description);
		if (price < 0) {
			throw new IllegalArgumentException("price is less than 0");
		}
		if(productStore.get(p.getId()) == null) {
			return null;
		} else {
			Product update = new Product(p.getId(), description, price);
			productStore.put(p.getId(), update);
			return update;
		}
	}

	/**
	 * Remove product from store
	 * @param p
	 * @return true if product is removed successfully
	 */
	public boolean removeProduct(Product p) {
		return productStore.remove(p.getId()) != null;
	}
	
	/**
	 * Get all products
	 * @return unmodifiable view of all products
	 */
	public Collection<Product> getAllProducts() {
		return Collections.unmodifiableCollection(productStore.values());
	}

}

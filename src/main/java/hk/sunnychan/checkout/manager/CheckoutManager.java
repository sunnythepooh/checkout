package hk.sunnychan.checkout.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import hk.sunnychan.checkout.model.Basket;
import hk.sunnychan.checkout.model.BasketItem;
import hk.sunnychan.checkout.model.Bundle;
import hk.sunnychan.checkout.model.Deal;
import hk.sunnychan.checkout.model.Product;

public class CheckoutManager {
	private AtomicInteger idGenerator = new AtomicInteger(0);
	private Map<BasketItem, List<Bundle>> productBundles = new HashMap<>();
	private Map<BasketItem, List<Deal>> productDeals = new HashMap<>();
	private Map<Integer, Bundle> bundles = new HashMap<>();
	private Map<Integer, Deal> deals = new HashMap<>();
	private final ProductManager productManager;
	private final BasketManager basketManager;

	public CheckoutManager(ProductManager productManager, BasketManager basketManager) {
		this.productManager = productManager;
		this.basketManager = basketManager;
	}

	/**
	 * 
	 * @param bundleName
	 * @param products
	 * @param price
	 * @return
	 */
	public boolean createBundle(String bundleName, List<Product> products, double price) {
		Objects.requireNonNull(products);
		int id = idGenerator.incrementAndGet();
		Bundle bundle = new Bundle(id, bundleName, products, price);
		if (products.stream().map(product -> productManager.getProduct(product.getId())).anyMatch(Optional::isEmpty)) {
			return false;
		}
		products.forEach(product -> {
			productBundles.compute(product, (key, value) -> {
				if (value == null) {
					List<Bundle> newValue = new ArrayList<>();
					newValue.add(bundle);
					return newValue;
				} else {
					value.add(bundle);
					return value;
				}
			});
		});
		bundles.put(bundle.getId(), bundle);
		return true;
	}

	public boolean createDeal(String dealName, Product product, int numberOfItems, double price) {
		Objects.requireNonNull(product);
		int id = idGenerator.incrementAndGet();
		Deal deal = new Deal(id, dealName, product, numberOfItems, price);
		if (productManager.getProduct(product.getId()).isEmpty()) {
			return false;
		}

		productDeals.compute(product, (key, value) -> {
			if (value == null) {
				List<Deal> newValue = new ArrayList<>();
				newValue.add(deal);
				return newValue;
			} else {
				value.add(deal);
				return value;
			}
		});
		deals.put(deal.getId(), deal);
		return true;
	}

	public List<Bundle> getAllBundles() {
		return bundles.values().stream().collect(Collectors.toList());
	}

	public List<Deal> getAllDeals() {
		return deals.values().stream().collect(Collectors.toList());
	}

	private boolean matched(Basket basket, Bundle bundle) {
		return bundle.getBundledProduct().stream().allMatch(product -> basket.getItems().containsKey(product));
	}

	private boolean matched(Basket basket, Deal deal) {
		return basket.getItems().containsKey(deal.getProduct())
				&& basket.getItems().get(deal.getProduct()) >= deal.getNumberOfItems();
	}

	private double sum(Basket b) {
		return b.getItems().entrySet().stream()
				.collect(Collectors.summingDouble(entry -> entry.getKey().getPrice() * entry.getValue()));
	}

	/**
	 * 
	 * @param basket
	 * @return
	 */
	public double checkoutPrice(Basket basket) {
		return sum(checkoutBasket(basket));
	}

	/**
	 * Return a basket with deal applied
	 * 
	 * @param basket
	 * @return
	 */
	public Basket checkoutBasket(Basket basket) {
		Map<BasketItem, Integer> itemsInBasket = basket.getItems();
		Set<Bundle> allAppliedBundles = itemsInBasket.keySet().stream().map(product -> productBundles.get(product))
				.filter(Objects::nonNull).flatMap(List::stream).filter(bundle -> matched(basket, bundle))
				.collect(Collectors.toSet());
		Set<Deal> allAppliedDeals = itemsInBasket.keySet().stream().map(product -> productDeals.get(product))
				.filter(Objects::nonNull).flatMap(List::stream).filter(deal -> matched(basket, deal))
				.collect(Collectors.toSet());
		ApplyBundlesAndDeals processor = new ApplyBundlesAndDeals(basket);
		allAppliedBundles.stream().forEach(processor::apply);
		allAppliedDeals.stream().forEach(processor::apply);
		return processor.getFinalBasket();
	}

	private static class ApplyBundlesAndDeals {
		HashMap<BasketItem, Integer> afterBundlesAndDeals = new HashMap<>();
		
		public ApplyBundlesAndDeals(Basket b) {
			afterBundlesAndDeals=new HashMap<>(b.getItems());
		}
		
		public void apply(Bundle bundle) {
			for (Product product : bundle.getBundledProduct()) {
				Integer count = afterBundlesAndDeals.get(product);
				if (count == null || count < 1) {
					return;
				}
				afterBundlesAndDeals.put(product, count - 1);
			}
			afterBundlesAndDeals.put(bundle, 1);
		}

		public void apply(Deal deal) {
			Integer count = afterBundlesAndDeals.get(deal.getProduct());
			if (count == null || count < deal.getNumberOfItems()) {
				return;
			}
			int nDeals = count / deal.getNumberOfItems();
			if (count % deal.getNumberOfItems() == 0) {
				afterBundlesAndDeals.remove(deal.getProduct());
			} else {
				afterBundlesAndDeals.put(deal.getProduct(), count % deal.getNumberOfItems());
			}
			afterBundlesAndDeals.put(deal, nDeals);
		}
		
		public Basket getFinalBasket() {
			return new Basket(-1, afterBundlesAndDeals);
		}
	}

}

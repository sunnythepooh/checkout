package hk.sunnychan.checkout.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import hk.sunnychan.checkout.form.BasketItemForm;
import hk.sunnychan.checkout.manager.BasketManager;
import hk.sunnychan.checkout.manager.CheckoutManager;
import hk.sunnychan.checkout.manager.ProductManager;
import hk.sunnychan.checkout.model.Basket;
import hk.sunnychan.checkout.model.BasketItem;
import hk.sunnychan.checkout.model.BasketVisitor;
import hk.sunnychan.checkout.model.Bundle;
import hk.sunnychan.checkout.model.Deal;
import hk.sunnychan.checkout.model.Product;

@Controller
@Configurable
@Scope("session")
public class CheckoutController {

	@Autowired
	ProductManager productManager;

	@Autowired
	CheckoutManager checkoutManager;

	@Autowired
	BasketManager basketManager;

	@Bean
	@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public Basket basket() {
		return basketManager.createBasket();
	}

	@Autowired
	private Basket basket;

	@ModelAttribute
	public void setStandardAttribute(Model model) {
		model.addAttribute("basketItemForm", new BasketItemForm());
		model.addAttribute("basket", basket);
		HashMap<Product, Integer> productInBasket = new HashMap<>();
		BasketVisitor visitor = new BasketVisitorAdapter() {
			Map<BasketItem, Integer> allItems = basket.getItems();

			@Override
			public void visit(Product p, int count) {
				productInBasket.put(p, allItems.get(p));
			}	
		};
		basket.visitBasket(visitor);
		model.addAttribute("productInBasket", productInBasket);
		model.addAttribute("totalPrice", checkoutManager.checkoutPrice(basket));
		
		Basket afterCheckout = checkoutManager.checkoutBasket(basket);
		List<Bundle> bundlesApplied = new ArrayList<>();
		List<Deal> dealsApplied = new ArrayList<>();
		BasketVisitor dealsAndBundles = new BasketVisitorAdapter() {
			@Override
			public void visit(Bundle b, int count) {
				bundlesApplied.add(b);
			}
			@Override
			public void visit(Deal d, int count) {
				dealsApplied.add(d);
			}
		};
		afterCheckout.visitBasket(dealsAndBundles);
		model.addAttribute("bundlesApplied", bundlesApplied);
		model.addAttribute("dealsApplied", dealsApplied);
	}

	@GetMapping("/checkout")
	public String getCheckout(Model model) {
		return "checkout";
	}

	@PostMapping("/checkout/addItem")
	public String addItemToBasket(@ModelAttribute BasketItemForm newProduct, Model model) {
		Optional<Product> product = productManager.getProduct(newProduct.getProductId());
		if (product.isEmpty()) {
			return "error";
		}
		basketManager.updateBasket(basket, product.get(), newProduct.getQuantity());
		return "redirect:/checkout";
	}

	@RequestMapping(value = "/checkout/modifyItem")
	public String removeItem(@ModelAttribute BasketItemForm basketItem, Model mode,
			@RequestParam(value = "action") String action) {
		Optional<Product> product = productManager.getProduct(basketItem.getProductId());
		if (product.isEmpty()) {
			return "error";
		}
		if ("remove".equals(action)) {
			if (!basketManager.removeFromBasket(basket, product.get())) {
				return "error";
			}
		}
		if ("change".equals(action)) {
			if (!basketManager.updateBasket(basket, product.get(), basketItem.getQuantity())) {
				return "error";
			}
		}
		return "redirect:/checkout";

	}
}

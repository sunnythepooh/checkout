package hk.sunnychan.checkout.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import hk.sunnychan.checkout.form.BundleForm;
import hk.sunnychan.checkout.form.DealForm;
import hk.sunnychan.checkout.form.ProductForm;
import hk.sunnychan.checkout.manager.CheckoutManager;
import hk.sunnychan.checkout.manager.ProductManager;
import hk.sunnychan.checkout.model.Product;

@Controller
@Configurable
public class OwnerController {

	@Autowired
	ProductManager productManager;

	@Autowired
	CheckoutManager checkoutManager;
	
	@ModelAttribute
	public void setStandardAttributes(Model model) {
		model.addAttribute("productForm", new ProductForm());
		model.addAttribute("dealForm", new DealForm());
		model.addAttribute("bundleForm", new BundleForm());
	}

	@GetMapping("/owner")
	public String getOwner(Model model) {
		return "owner";
	}

	@PostMapping("/owner/create")
	@GetMapping("/owner/create")
	public String createProduct(Model model, @ModelAttribute ProductForm form) {

		productManager.addProduct(form.getDescription(), form.getPrice().doubleValue());
		return "owner";
	}

	@PostMapping("/owner/amend")
	@GetMapping("/owner/amend")
	public String amendProduct(Model model, @ModelAttribute ProductForm form) {
		Optional<Product> p = productManager.getProduct(form.getId());
		if (p.isPresent()) {
			productManager.updateProduct(p.get(), form.getDescription(), form.getPrice().doubleValue());
		}
		return "owner";
	}

	@PostMapping("/owner/remove")
	@GetMapping("/owner/remove")
	public String removeProduct(Model model, @ModelAttribute ProductForm form) {
		Optional<Product> p = productManager.getProduct(form.getId());
		if (p.isPresent()) {
			productManager.removeProduct(p.get());
		}
		return "owner";
	}

	@PostMapping("/owner/applyDeal")
	@GetMapping("/owner/applyDeal")
	public String addDeal(Model model, @ModelAttribute DealForm form) {
		Optional<Product> product = productManager.getProduct(form.getProductId());
		if (product.isPresent()) {
			checkoutManager.createDeal(form.getDescription(), product.get(), form.getCount(),
					form.getPrice().doubleValue());
		}
		return "owner";
	}

	@PostMapping("/owner/applyBundle")
	@GetMapping("/owner/applyBundle")
	public String addBundle(Model model, @ModelAttribute BundleForm form) {

		List<Product> products = form.getProductIds().stream().filter(id -> id > 0).map(productManager::getProduct).filter(Optional::isPresent)
				.map(Optional::get).collect(Collectors.toList());
		checkoutManager.createBundle(form.getDescription(), products, form.getPrice().doubleValue());
		return "owner";
	}

}

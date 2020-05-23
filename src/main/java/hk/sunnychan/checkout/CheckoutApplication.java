package hk.sunnychan.checkout;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import hk.sunnychan.checkout.manager.BasketManager;
import hk.sunnychan.checkout.manager.CheckoutManager;
import hk.sunnychan.checkout.manager.ProductManager;
import hk.sunnychan.checkout.model.Product;

@SpringBootApplication
public class CheckoutApplication {

	@Bean
	ProductManager productManager() {
		ProductManager manager =  new ProductManager();
		manager.addProduct("Mouse", 100.0d);
		manager.addProduct("Laptop", 10000.0d);
		manager.addProduct("Removable HD", 200.0d);
		return manager;
	}
	
	@Bean
	BasketManager basketManager() {
		return new BasketManager();
	}
	
	@Autowired
	ProductManager productManager;
	@Autowired
	BasketManager basketManager;
	
	@Bean
	CheckoutManager checkoutManager() {
		return new CheckoutManager(productManager, basketManager);
	}
	
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(CheckoutApplication.class, args);
		ProductManager manager = context.getBean(ProductManager.class);
		System.out.println(manager.getAllProducts().stream().map(Product::toString).collect(Collectors.joining("\n")));
	}

}

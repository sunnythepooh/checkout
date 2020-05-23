package hk.sunnychan.checkout.manager;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;

import hk.sunnychan.checkout.model.Basket;
import hk.sunnychan.checkout.model.Product;

public class CheckoutManagerTest {
	@Test
	public void createBundleTest() {
		ProductManager productManager = new ProductManager();
		BasketManager basketManager = new BasketManager();
		CheckoutManager checkoutManager = new CheckoutManager(productManager, basketManager);

		Product id1 = productManager.addProduct("test1", 100.0d);
		Product id2 = productManager.addProduct("test2", 5.0d);
		ArrayList<Product> bundle = new ArrayList<>();
		bundle.add(id1);
		bundle.add(id2);
		assertTrue(checkoutManager.createBundle("testbundle", bundle, 102.0d));

		bundle.add(new Product(100, "bad", 10d));
		assertFalse(checkoutManager.createBundle("testbundle1", bundle, 100));

		assertThatExceptionOfType(NullPointerException.class)
				.isThrownBy(() -> checkoutManager.createBundle("test", null, 100.0d));
	}
	
	@Test
	public void createDealTest() {
		ProductManager productManager = new ProductManager();
		BasketManager basketManager = new BasketManager();
		CheckoutManager checkoutManager = new CheckoutManager(productManager,basketManager);

		Product id1 = productManager.addProduct("test1", 100.0d);		
		assertTrue(checkoutManager.createDeal("testdeal", id1, 3, 200.0d));

		assertFalse(checkoutManager.createDeal("testdeal1", new Product(1000, "test4", 1.0d), 3, 100));

		assertThatExceptionOfType(NullPointerException.class)
				.isThrownBy(() -> checkoutManager.createDeal("test", null, 4, 100.0d));
	}
	
	@Test
	public void matchDealTest() {
		ProductManager productManager = new ProductManager();
		BasketManager basketManager = new BasketManager();
		CheckoutManager checkoutManager = new CheckoutManager(productManager,basketManager);

		Product id1 = productManager.addProduct("test1", 100.0d);
		Product id2 = productManager.addProduct("test2", 5.0d);		
		assertTrue(checkoutManager.createDeal("testdeal", id1, 3, 200.0d));

		Basket basket = basketManager.createBasket();
		assertTrue(basketManager.updateBasket(basket, id1, 3));
		assertTrue(basketManager.updateBasket(basket, id2, 1));
		assertEquals(205.0d, checkoutManager.checkoutPrice(basket), 0.01d);
		
		basket = basketManager.createBasket();
		assertTrue(basketManager.updateBasket(basket, id1, 4));
		assertTrue(basketManager.updateBasket(basket, id2, 1));
		assertEquals(305.0d, checkoutManager.checkoutPrice(basket), 0.01d);
		
		basket = basketManager.createBasket();
		assertTrue(basketManager.updateBasket(basket, id1, 1));
		assertTrue(basketManager.updateBasket(basket, id2, 1));
		assertEquals(105.0d, checkoutManager.checkoutPrice(basket), 0.01d);

		basket = basketManager.createBasket();
		assertTrue(basketManager.updateBasket(basket, id1, 6));
		assertEquals(400.0d, checkoutManager.checkoutPrice(basket), 0.01d);

		basket = basketManager.createBasket();
		assertTrue(basketManager.updateBasket(basket, id1, 7));
		assertEquals(500.0d, checkoutManager.checkoutPrice(basket), 0.01d);
	}
	
	@Test
	public void matchBundleTest() {
		ProductManager productManager = new ProductManager();
		BasketManager basketManager = new BasketManager();
		CheckoutManager checkoutManager = new CheckoutManager(productManager, basketManager);

		Product id1 = productManager.addProduct("test1", 100.0d);
		Product id2 = productManager.addProduct("test2", 5.0d);
		ArrayList<Product> bundle = new ArrayList<>();
		bundle.add(id1);
		bundle.add(id2);
		assertTrue(checkoutManager.createBundle("testbundle", bundle, 102.0d));
		
		Basket basket = basketManager.createBasket();
		assertTrue(basketManager.updateBasket(basket, id1, 1));
		assertTrue(basketManager.updateBasket(basket, id2, 1));
		
		assertEquals(102.0d, checkoutManager.checkoutPrice(basket), 0.01);
		
		assertTrue(basketManager.removeFromBasket(basket, id2));
		assertEquals(100.0d, checkoutManager.checkoutPrice(basket), 0.01);
	}
	
	@Test
	public void checkoutTest() {
		ProductManager productManager = new ProductManager();
		BasketManager basketManager = new BasketManager();
		CheckoutManager checkoutManager = new CheckoutManager(productManager, basketManager);

		Product id1 = productManager.addProduct("test1", 100.0d);
		Product id2 = productManager.addProduct("test2", 5.0d);
		
		Basket basket = basketManager.createBasket();
		assertTrue(basketManager.updateBasket(basket, id1, 2));
		assertTrue(basketManager.updateBasket(basket, id2, 2));
		
		assertEquals(2*100+2*5, checkoutManager.checkoutPrice(basket), 0.01);
		
		ArrayList<Product> bundle = new ArrayList<>();
		bundle.add(id1);
		bundle.add(id2);
		assertTrue(checkoutManager.createBundle("testbundle", bundle, 102.0d));
		
		assertEquals(102.0+100+5, checkoutManager.checkoutPrice(basket), 0.01);
	
	}
}

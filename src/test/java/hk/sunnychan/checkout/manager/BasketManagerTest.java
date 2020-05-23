package hk.sunnychan.checkout.manager;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import hk.sunnychan.checkout.model.Basket;
import hk.sunnychan.checkout.model.Product;

public class BasketManagerTest {
	@Test
	void testCreateBasket() {
		BasketManager basketManager = new BasketManager();

		Basket b = basketManager.createBasket();
		assertTrue(b.getItems().isEmpty());
		assertTrue(basketManager.getAllBasket().contains(b));

	}

	@Test
	public void testUpdateBasket() {
		ProductManager productManager = new ProductManager();
		BasketManager basketManager = new BasketManager();

		Product id1 = productManager.addProduct("test1", 100.0d);
		Product id2 = productManager.addProduct("test2", 5.0d);

		Basket b = basketManager.createBasket();
		basketManager.updateBasket(b, id1, 2);
		assertEquals(2, b.getItems().get(id1).intValue());
		assertNull(b.getItems().get(id2));

		assertThatExceptionOfType(IllegalArgumentException.class)
				.isThrownBy(() -> basketManager.updateBasket(b, id1, -1));
		assertThatExceptionOfType(NullPointerException.class)
				.isThrownBy(() -> basketManager.updateBasket(null, id1, 10));
		assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> basketManager.updateBasket(b, null, 10));

		assertFalse(basketManager.updateBasket(new Basket(1000, null), id1, 10));
	}
	
	@Test
	public void removeBasket() {
		ProductManager productManager = new ProductManager();
		BasketManager basketManager = new BasketManager();

		Product id1 = productManager.addProduct("test1", 100.0d);

		Basket b1 = basketManager.createBasket();
		basketManager.updateBasket(b1, id1, 2);
		
		Basket b2 = basketManager.createBasket();	
		basketManager.removeBasket(b2);
		
		assertTrue(basketManager.getAllBasket().contains(b1));
		assertFalse(basketManager.getAllBasket().contains(b2));
	}
	
	@Test
	public void testRemoveItemFromBasket() {
		ProductManager productManager = new ProductManager();
		BasketManager basketManager = new BasketManager();

		Product id1 = productManager.addProduct("test1", 100.0d);
		Product id2 = productManager.addProduct("test2", 5.0d);

		Basket b1 = basketManager.createBasket();
		basketManager.updateBasket(b1, id1, 2);
		basketManager.updateBasket(b1, id2, 30);
		
		basketManager.removeFromBasket(b1, id1);
		assertFalse(b1.getItems().containsKey(id1));
		assertTrue(b1.getItems().containsKey(id2));
		
		assertThatExceptionOfType(NullPointerException.class).isThrownBy(()->basketManager.removeFromBasket(null, id1));
		assertThatExceptionOfType(NullPointerException.class).isThrownBy(()->basketManager.removeFromBasket(b1, null));
		
		}
}

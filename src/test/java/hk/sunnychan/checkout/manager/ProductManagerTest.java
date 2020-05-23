package hk.sunnychan.checkout.manager;

import org.junit.jupiter.api.Test;

import hk.sunnychan.checkout.model.Product;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

public class ProductManagerTest {
	@Test
	public void ifProductwithNegativePriceThenThrowsException() {
		ProductManager manager = new ProductManager();
		assertThrows(IllegalArgumentException.class, () -> manager.addProduct("test1", -1.0d));
	}

	@Test
	public void testProductRetrieval() {
		ProductManager manager = new ProductManager();
		Product id1 = manager.addProduct("test1", 100.0d);
		Product id2 = manager.addProduct("test2", 5.0d);

		assertTrue(id2 != id1);
		Optional<Product> p1 = manager.getProduct(id1.getId());
		assertTrue(p1.isPresent());
		assertEquals("test1", p1.get().getDescription());
		Optional<Product> p2 = manager.getProduct(id2.getId());
		assertTrue(p2.isPresent());
		assertEquals("test2", p2.get().getDescription());

		Optional<Product> p3 = manager.getProduct(1000);
		assertFalse(p3.isPresent());
	}

	@Test
	public void testProductRemoval() {
		ProductManager manager = new ProductManager();
		Product id1 = manager.addProduct("test1", 100.0d);
		Product id2 = manager.addProduct("test2", 5.0d);

		assertTrue(manager.removeProduct(id1));
		assertFalse(manager.getProduct(id1.getId()).isPresent());
		assertTrue(manager.getProduct(id2.getId()).isPresent());
		assertFalse(manager.removeProduct(new Product(1000, null, 0.1d)));
	}

	@Test
	public void testProductUpdate() {
		ProductManager manager = new ProductManager();
		Product id1 = manager.addProduct("test1", 100.0d);

		Product result = manager.updateProduct(new Product(12345, "abcd", 1000.0d), "test1", 100.0d);
		assertNull(result);

		result = manager.updateProduct(id1, "testu1", 10.0);
		assertEquals(id1.getId(), result.getId());
		assertEquals(result.getDescription(), "testu1");
		assertEquals(result.getPrice(), 10.0, 0.01);

		assertThatExceptionOfType(IllegalArgumentException.class)
				.isThrownBy(() -> manager.updateProduct(id1, "ok", -0.1));
		assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> manager.updateProduct(id1, null, 0.1));
		assertThatExceptionOfType(NullPointerException.class).isThrownBy(() -> manager.updateProduct(null, "ok", 0.1));

	}

	@Test
	public void testAllProductRetrieval() {
		ProductManager manager = new ProductManager();
		assertTrue(manager.getAllProducts().isEmpty());

		Product id1 = manager.addProduct("test1", 100.0d);
		Product id2 = manager.addProduct("test2", 5.0d);

		assertTrue(manager.getAllProducts().contains(id1));
		assertTrue(manager.getAllProducts().contains(id2));

	}

}

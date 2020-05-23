package hk.sunnychan.checkout.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class BasketItemTest {

	private static class VisitorChecker implements BasketVisitor {

		boolean bundleCalled = false;
		int bundleCount = 0;
		boolean dealCalled = false;
		int dealCount = 0;
		boolean productCalled = false;
		int productCount=0;
		
		Bundle bundle;
		Product product;
		Deal deal;
		
		public VisitorChecker(Bundle b, Deal d, Product p) {
			bundle=b;
			product=p;
			deal=d;
		}

		@Override
		public void visit(Bundle b, int count) {
			bundleCalled = true;
			bundleCount = count;
			assertEquals(bundle, b);
		}

		@Override
		public void visit(Deal d, int count) {
			dealCalled = true;
			dealCount = count;
			assertEquals(deal, d);
		}

		@Override
		public void visit(Product p, int count) {
			productCalled = true;
			productCount = count;
			assertEquals(product, p);
		}
	}

	@Test
	public void testVisitor() {
		Product product = new Product(1, "test", 12.3d);
		Deal deal = new Deal(2, "testDeal", product, 3, 12.3d);
		Bundle bundle = new Bundle(3, "testBundle", Collections.singletonList(product), 10.0d);

		Map<BasketItem, Integer> items = new HashMap<>();
		items.put(product, 1);
		items.put(deal, 1);
		items.put(bundle, 1);
		Basket b = new Basket(4, items);

		VisitorChecker visitor = new VisitorChecker(bundle, deal, product);
		b.visitBasket(visitor);
		assertTrue(visitor.bundleCalled);
		assertTrue(visitor.dealCalled);
		assertTrue(visitor.productCalled);
		
		assertEquals(1, visitor.bundleCount);
		assertEquals(1, visitor.dealCount);
		assertEquals(1, visitor.productCount);
	}
}

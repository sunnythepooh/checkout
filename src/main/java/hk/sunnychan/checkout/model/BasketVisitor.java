package hk.sunnychan.checkout.model;

public interface BasketVisitor {
	void visit(Bundle b, int count);
	void visit(Deal d, int count);
	void visit(Product p, int count);
}

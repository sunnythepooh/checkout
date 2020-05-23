package hk.sunnychan.checkout.model;

public interface BasketItem {
	void accept(BasketVisitor visitor, int count);
	double getPrice();
	String getDescription();
}

package hk.sunnychan.checkout.model;

public class Product implements BasketItem {
	private int id;
	private String description;
	private double price;
	
	public Product(int id, String description, double price) {
		this.id=id;
		this.description=description;
		this.price=price;
	}
	
	public int getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public double getPrice() {
		return price;
	}
	
	@Override
	public String toString() {
		return String.format("product id=%d, description=%s, price=%.2f", id, description, price);
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(obj instanceof Product) {
			return id == ((Product) obj).getId();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return id;
	}
	
	@Override
	public void accept(BasketVisitor visitor, int count) {
		visitor.visit(this, count);
	}
}

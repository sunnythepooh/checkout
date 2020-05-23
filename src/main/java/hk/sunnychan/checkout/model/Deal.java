package hk.sunnychan.checkout.model;

public class Deal implements BasketItem {
	private int id;
	private Product product;

	private int numberOfItems;
	private double price;
	private String description;

	public Deal(int id, String description, Product product, int numberOfItems, double price) {
		this.id = id;
		this.product = product;
		this.numberOfItems = numberOfItems;
		this.price = price;
		this.description = description;
	}

	@Override
	public double getPrice() {
		return price;
	}

	public int getId() {
		return id;
	}

	public Product getProduct() {
		return product;
	}

	public int getNumberOfItems() {
		return numberOfItems;
	}

	public String getDescription() {
		return description;
	}
	
	@Override
	public void accept(BasketVisitor visitor, int count) {
		visitor.visit(this, count);
	}
}

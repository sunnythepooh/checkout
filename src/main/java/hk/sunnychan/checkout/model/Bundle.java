package hk.sunnychan.checkout.model;

import java.util.List;

public class Bundle implements BasketItem {
	private int id;
	private List<Product> bundledProduct;
	private double price;
	private String description;
	
	public Bundle(int id, String descrpition, List<Product> bundledProduct, double price) {
		this.id = id;
		this.bundledProduct = bundledProduct;
		this.price=price;
		this.description=descrpition;
	}
	
	public int getId() {
		return id;
	}
	public List<Product> getBundledProduct() {
		return bundledProduct;
	}

	public double getPrice() {
		return price;
	}
	
	public String getDescription() {
		return description;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(obj == this) return true;
		if(obj instanceof Bundle) {
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

package hk.sunnychan.checkout.form;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import hk.sunnychan.checkout.model.Basket;

public class BasketItemForm {

	String userAction;

	@NotBlank
	Basket basket;

	@NotBlank
	int productId;

	@NotBlank
	@Min(value = 0)
	int quantity;

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Basket getBasket() {
		return basket;
	}

	public void setBasket(Basket basket) {
		this.basket = basket;
	}

	public String getUserAction() {
		return userAction;
	}

	public void setUserAction(String userAction) {
		this.userAction = userAction;
	}

}

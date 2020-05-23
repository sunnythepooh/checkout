package hk.sunnychan.checkout.form;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;

public class BundleForm {
	@NotBlank
	private String description;

	@NotBlank
	private List<Integer> productIds;
	@NotBlank
	@DecimalMin("0.0")
	private BigDecimal price;

	public List<Integer> getProductIds() {
		return productIds;
	}

	public void setProductIds(List<Integer> productIds) {
		this.productIds = productIds;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void addProduct(Integer id) {
		productIds.add(id);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}

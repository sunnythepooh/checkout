package hk.sunnychan.checkout.controller;

import hk.sunnychan.checkout.model.BasketVisitor;
import hk.sunnychan.checkout.model.Bundle;
import hk.sunnychan.checkout.model.Deal;
import hk.sunnychan.checkout.model.Product;

public class BasketVisitorAdapter implements BasketVisitor {
	@Override
	public void visit(Bundle b, int count) {

	}

	@Override
	public void visit(Deal d, int count) {

	}

	@Override
	public void visit(Product p, int count) {

	}
}

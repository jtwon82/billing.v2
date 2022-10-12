package org.mobon.billing.report;

import java.math.BigDecimal;
import java.util.LinkedList;

public class StreamTest2 {

	public static void main(String[] args) {

		LinkedList<Invoice> invoices = new LinkedList<>();
		invoices.add(new Invoice("C1", "I-001", BigDecimal.valueOf(.1), BigDecimal.valueOf(10)));
		invoices.add(new Invoice("C2", "I-002", BigDecimal.valueOf(.7), BigDecimal.valueOf(13)));
		invoices.add(new Invoice("C3", "I-003", BigDecimal.valueOf(2.3), BigDecimal.valueOf(8)));
		invoices.add(new Invoice("C4", "I-004", BigDecimal.valueOf(1.2), BigDecimal.valueOf(7)));

		// Classical Java approach
		BigDecimal sum = BigDecimal.ZERO;
		for (Invoice invoice : invoices) {
			BigDecimal total = invoice.unit_price.multiply(invoice.quantity);
			System.out.println(total);
			sum = sum.add(total);
		}
		System.out.println("Sum = " + sum);

		// Java 8 approach
		invoices.forEach((invoice) -> System.out.println(invoice.total()));
		System.out.println("Sum = " + invoices.stream().map((x) -> x.total()).reduce((x, y) -> x.add(y)).get());
	}

	static class Invoice {
	    String company;
	    String invoice_number;
	    BigDecimal unit_price;
	    BigDecimal quantity;

	    public Invoice() {
	        unit_price = BigDecimal.ZERO;
	        quantity = BigDecimal.ZERO;
	    }

	    public Invoice(String company, String invoice_number, BigDecimal unit_price, BigDecimal quantity) {
	        this.company = company;
	        this.invoice_number = invoice_number;
	        this.unit_price = unit_price;
	        this.quantity = quantity;
	    }

	    public BigDecimal total() {
	        return unit_price.multiply(quantity);
	    }

	    public void setUnit_price(BigDecimal unit_price) {
	        this.unit_price = unit_price;
	    }

	    public void setQuantity(BigDecimal quantity) {
	        this.quantity = quantity;
	    }

	    public void setInvoice_number(String invoice_number) {
	        this.invoice_number = invoice_number;
	    }

	    public void setCompany(String company) {
	        this.company = company;
	    }

	    public BigDecimal getUnit_price() {
	        return unit_price;
	    }

	    public BigDecimal getQuantity() {
	        return quantity;
	    }

	    public String getInvoice_number() {
	        return invoice_number;
	    }

	    public String getCompany() {
	        return company;
	    }
	}
}
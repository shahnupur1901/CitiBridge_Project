package com.project.citi.dto;
public class Transaction {
	private String transactionRefNo;
	private String valueDate;
	private String payerName;
	private String payerAccountNumber;
	private String payeeName;
	private String payeeAccountNumber;
	private double amount;
	private int validationStatus;
	private int sanctioningStatus;
	
	
	public Transaction() {}
	public String getTransactionRefNo() {
		return transactionRefNo;
	}
	public void setTransactionRefNo(String transactionRefNo) {
		this.transactionRefNo = transactionRefNo;
	}
	public String getValueDate() {
		return valueDate;
	}
	public void setValueDate(String valueDate) {
		this.valueDate = valueDate;
	}
	public String getPayerName() {
		return payerName;
	}
	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}
	public String getPayerAccountNumber() {
		return payerAccountNumber;
	}
	public void setPayerAccountNumber(String payerAccountNumber) {
		this.payerAccountNumber = payerAccountNumber;
	}
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	public String getPayeeAccountNumber() {
		return payeeAccountNumber;
	}
	public void setPayeeAccountNumber(String payeeAccountNumber) {
		this.payeeAccountNumber = payeeAccountNumber;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public int getValidationStatus() {
		return validationStatus;
	}
	public void setValidationStatus(int validationStatus) {
		this.validationStatus = validationStatus;
	}
	public int getSanctioningStatus() {
		return sanctioningStatus;
	}
	public void setSanctioningStatus(int sanctioningStatus) {
		this.sanctioningStatus = sanctioningStatus;
	}
	
}

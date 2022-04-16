package com.project.citi.dto;

import java.sql.Timestamp;

public class TransactionFile {
	@Override
	public String toString() {
		return "TransactionFile [filename=" + filename + ", numTransactions=" + numTransactions + ", timestamp="
				+ timestamp + ", numValidationFailed=" + numValidationFailed + ", numSanctionFailed="
				+ numSanctionFailed + "]";
	}
	public TransactionFile(String filename, int numTransactions, Timestamp timestamp, int numValidationFailed,
			int numSanctionFailed) {
		super();
		this.filename = filename;
		this.numTransactions = numTransactions;
		this.timestamp = timestamp;
		this.numValidationFailed = numValidationFailed;
		this.numSanctionFailed = numSanctionFailed;
	}
	public TransactionFile() {
		super();
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public int getNumTransactions() {
		return numTransactions;
	}
	public void setNumTransactions(int numTransactions) {
		this.numTransactions = numTransactions;
	}
	public Timestamp getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}
	public int getNumValidationFailed() {
		return numValidationFailed;
	}
	public void setNumValidationFailed(int numValidationFailed) {
		this.numValidationFailed = numValidationFailed;
	}
	public int getNumSanctionFailed() {
		return numSanctionFailed;
	}
	public void setNumSanctionFailed(int numSanctionFailed) {
		this.numSanctionFailed = numSanctionFailed;
	}
	private String filename;
	private int numTransactions;
	private Timestamp timestamp;
	private int numValidationFailed;
	private int numSanctionFailed;
}

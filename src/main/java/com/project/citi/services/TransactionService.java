package com.project.citi.services;

import java.util.List;

import com.project.citi.dto.Transaction;

public interface TransactionService {
	public String addTransactionFile(List<Transaction> list);
	public String sanctionTransactions();
	public List<Transaction> filter(String field, String status);
	public List<Transaction> retrieveAll();
	public String returnMessage(String message, String transactionRefNo);
	public String truncateToArchive();
}

package com.project.citi.repository;
import java.util.List;

import com.project.citi.dto.Transaction;
public interface TransactionRepository {
	public String addTransactionFile(List<Transaction> list);
	public String validateTransactions(Transaction t);
	public String sanctionTransactions();
	public List<Transaction> filter(String field, String status);
	public List<Transaction> retrieveAll();
}

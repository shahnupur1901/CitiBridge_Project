package com.project.citi.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.citi.dto.Transaction;
import com.project.citi.repository.TransactionRepository;

@Service("transactionService")
public class TransactionServiceImpl implements TransactionService {
	private TransactionServiceImpl() {}
	
	private static TransactionService transactionService;
	
	public static TransactionService getInstance() {
		if(transactionService == null) 
		{
			transactionService = new TransactionServiceImpl();
		}
		return transactionService;
	}
	
	
	@Autowired
	private TransactionRepository transactionRepository; 

	@Override
	public String addTransactionFile(List<Transaction> list) {
		// TODO Auto-generated method stub
		return transactionRepository.addTransactionFile(list);
	}
	
	public String truncateToArchive() {
		transactionRepository.truncateToArchive();
		return "Done";
	}

	@Override
	public String sanctionTransactions() {
		// TODO Auto-generated method stub
		return transactionRepository.sanctionTransactions();
	}

	@Override
	public List<Transaction> filter(String field, String status) {
		// TODO Auto-generated method stub
		return transactionRepository.filter(field, status);
	}
	
	public List<Transaction> retrieveAll(){
		return transactionRepository.retrieveAll();
	}

	@Override
	public String returnMessage(String message, String transactionRefNo) {
		// TODO Auto-generated method stub
		return transactionRepository.returnMessage(message, transactionRefNo);
	}

	
}

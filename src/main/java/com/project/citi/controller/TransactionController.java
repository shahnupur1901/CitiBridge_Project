package com.project.citi.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.citi.dto.Transaction;
import com.project.citi.services.TransactionService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/transaction")
public class TransactionController {

	@Autowired
	TransactionService transactionService;
	
	@GetMapping("/sanction")
	public List<Transaction> sanctionTransactions() {
		transactionService.sanctionTransactions();
		List<Transaction> list = transactionService.retrieveAll();
		return list;
	}
	
	@GetMapping("/{field}/{status}")
	public List<Transaction> filter(@PathVariable("field")String field, @PathVariable("status")String status) 
	{
		System.out.println(field+" "+status);
		transactionService.sanctionTransactions();
		List<Transaction> list = transactionService.filter(field, status);
		return list;
	}
	
	@GetMapping("/{message}/num/{transactionrefno}")
	public String returnMessage(@PathVariable("message")String msg, @PathVariable("transactionrefno")String tno) 
	{
		return transactionService.returnMessage(msg, tno);
	}
	
	@PostMapping("/add")
	public String addTransactionFile(@RequestBody ArrayList<Transaction> list) {
		return transactionService.addTransactionFile(list);
	}
	
	
	
}

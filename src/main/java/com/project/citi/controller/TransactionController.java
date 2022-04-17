package com.project.citi.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.citi.dto.Transaction;
import com.project.citi.dto.TransactionFile;
import com.project.citi.services.TransactionService;
import com.project.citi.services.TransactionFileService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/transaction")
public class TransactionController {

	@Autowired
	TransactionService transactionService;
	
	@Autowired
	TransactionFileService transactionFileService;
	

	@PostMapping("/getFile")
	public String transactionlist(@RequestParam("file") MultipartFile file, @RequestParam("filename") String filename ){
		return transactionService.addTransactionFile(this.transactionFileService.transactionlist(file));
	}
	
	@GetMapping("/sanction")
	public List<Transaction> sanctionTransactions() {
		transactionService.sanctionTransactions();
		List<Transaction> list = transactionService.retrieveAll();
		return list;
	}
	
	@GetMapping("/truncate")
	public String truncateCT() {
		transactionService.truncateToArchive();
		return "done";
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
	
	@GetMapping("/validate")
	public List<Transaction> validateTransactions() {
		List<Transaction> list = transactionService.retrieveAll();
		return list;
	}
	
	
	
}

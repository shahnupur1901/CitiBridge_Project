package com.project.citi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.citi.dto.Transaction;
import com.project.citi.dto.TransactionFile;
import com.project.citi.services.TransactionFileService;

@RestController
@CrossOrigin("*")
@RequestMapping("/home")
public class FileController {
	
	@Autowired
	TransactionFileService transactionFileService;
	
	@GetMapping("")
	public List<TransactionFile> sanctionTransactions() {
		List<TransactionFile> list = transactionFileService.getFileInformation();
		return list;
	}

}

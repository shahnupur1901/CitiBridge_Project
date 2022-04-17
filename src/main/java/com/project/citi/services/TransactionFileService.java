package com.project.citi.services;

import java.io.File;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.citi.dto.Transaction;
import com.project.citi.dto.TransactionFile;

public interface TransactionFileService {
	public List<TransactionFile> getFileInformation();
	public List<Transaction> transactionlist(MultipartFile file);
	
	
}

package com.project.citi.repository;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.project.citi.dto.Transaction;
import com.project.citi.dto.TransactionFile;

public interface TransactionFileRepository {
	
	public List<TransactionFile> getFileInformation(); 
	public List<Transaction> transactionlist(MultipartFile file) throws IOException;
}

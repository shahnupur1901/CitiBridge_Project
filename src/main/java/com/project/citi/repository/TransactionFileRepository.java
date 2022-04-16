package com.project.citi.repository;

import java.util.List;

import com.project.citi.dto.TransactionFile;

public interface TransactionFileRepository {
	
	public List<TransactionFile> getFileInformation(); 
}

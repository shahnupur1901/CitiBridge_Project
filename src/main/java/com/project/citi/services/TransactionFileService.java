package com.project.citi.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.citi.dto.TransactionFile;

public interface TransactionFileService {
	public List<TransactionFile> getFileInformation();
	
	
}

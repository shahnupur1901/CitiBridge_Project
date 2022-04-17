package com.project.citi.services;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.citi.dto.Transaction;
import com.project.citi.dto.TransactionFile;
import com.project.citi.repository.TransactionFileRepository;

@Service("transactionFileService")
public class TransactionFileServiceImpl implements TransactionFileService {
	
private TransactionFileServiceImpl() {}
	
	private static TransactionFileService transactionFileService;
	
	public static TransactionFileService getInstance() {
		if(transactionFileService == null) 
		{
			transactionFileService = new TransactionFileServiceImpl();
		}
		return transactionFileService;
	}
	
	@Autowired
	private TransactionFileRepository transactionFileRepository; 

	@Override
	public List<TransactionFile> getFileInformation() {
		// TODO Auto-generated method stub
		return this.transactionFileRepository.getFileInformation();
	}
	
	public List<Transaction> transactionlist(MultipartFile file){
		try {
			return this.transactionFileRepository.transactionlist(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	

}

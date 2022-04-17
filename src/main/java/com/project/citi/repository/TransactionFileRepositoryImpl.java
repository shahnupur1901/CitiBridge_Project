package com.project.citi.repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.project.citi.dto.Transaction;
import com.project.citi.dto.TransactionFile;
import com.project.citi.utils.DBUtils;

@Repository
public class TransactionFileRepositoryImpl implements TransactionFileRepository{

	@Autowired
	DataSource dataSource;
	@Override
	public List<TransactionFile> getFileInformation() {
		Connection conn = null;
		List<TransactionFile> transactionFiles = new ArrayList();
		try
		{
			conn=dataSource.getConnection();
			PreparedStatement prepsmt=null;
			ResultSet rs=null;// resultset hold whole row in a db
			String query="select * from FileSystem order by time desc;";
			prepsmt=conn.prepareStatement(query);
			rs=prepsmt.executeQuery();
			int count = 0;
			while(rs.next())
			{
				count++;
				if(count > 10) break;
				TransactionFile tf = new TransactionFile();
				tf.setFilename(rs.getString(1));
				tf.setNumTransactions(rs.getInt(2));
				tf.setTimestamp(rs.getTimestamp(3));
				tf.setNumValidationFailed(rs.getInt(4));
				tf.setNumSanctionFailed(rs.getInt(5));
				transactionFiles.add(tf);
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	    finally
		{
			DBUtils.closeConnection(conn);
		}
		return transactionFiles;		
		
	}
	



public List<Transaction> transactionlist(MultipartFile  file) throws IOException {
// TODO Auto-generated method stub
List<Transaction> transaction_list = new ArrayList();

BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream())); 
String line;  
while((line=br.readLine())!=null)  
{  
String arr[]=line.split(" ");
Transaction transaction_obj=new Transaction();
transaction_obj.setTransactionRefNo(arr[0]);
transaction_obj.setValueDate(arr[1]);
transaction_obj.setPayerName(arr[2]);
transaction_obj.setPayerAccountNumber(arr[3]);
transaction_obj.setPayeeName(arr[4]);
transaction_obj.setPayeeAccountNumber(arr[5]);
transaction_obj.setAmount(Double.parseDouble(arr[6]));
transaction_obj.setFilename(file.getOriginalFilename());
transaction_list.add(transaction_obj);
}  
br.close();    
return transaction_list;
}

}

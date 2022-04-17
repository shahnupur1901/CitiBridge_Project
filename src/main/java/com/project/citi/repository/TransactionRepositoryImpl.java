package com.project.citi.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.project.citi.dto.Transaction;
import com.project.citi.dto.TransactionFile;
import com.project.citi.utils.DBUtils;

@Repository	
public class TransactionRepositoryImpl implements TransactionRepository{
	@Autowired
	DataSource dataSource;
	
	int currentCount = 0;
	List<String> transactionRefNumberList = new ArrayList();;
	public TransactionRepositoryImpl()
	{	
		
	}
	private static TransactionRepository transactionRepository;
	public String addTransactionFile(List<Transaction> list) {
		Connection conn = null;
		PreparedStatement prepsmt = null;
		currentCount = 0;
		String query = "insert into CurrentTransaction values(?,?,?,?,?,?,?,?,?,?,?,?);";
			try
			{
				for(Transaction t : list) {
				conn = dataSource.getConnection();
				prepsmt=conn.prepareStatement(query);
				String vstatus = this.validateTransactions(t);
				if(vstatus.equals("Pass"))  prepsmt.setString(9, "Pass");
				else prepsmt.setString(9, "Fail");
				prepsmt.setString(8,"Pending");
				prepsmt.setString(10,null);
				prepsmt.setString(11,vstatus);
				prepsmt.setString(12,t.getFilename());
				prepsmt.setString(1,t.getTransactionRefNo());
				prepsmt.setString(2,this.formatDate(t.getValueDate()));
				prepsmt.setString(3,t.getPayerName());
				prepsmt.setString(4,t.getPayerAccountNumber());
				prepsmt.setString(5,t.getPayeeName());
				prepsmt.setString(6,t.getPayeeAccountNumber());
				prepsmt.setDouble(7,t.getAmount());
				int res=prepsmt.executeUpdate();
				currentCount++;
				transactionRefNumberList.add(t.getTransactionRefNo());
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
		
		return "finished";
		}
	
	public String formatDate(String date) {
		if(date.length() != 8 ) return null;
		return date.substring(4) +"-"+date.substring(0,2)+"-"+date.substring(2,4);
	}
	
	public String validateTransactions(Transaction t) {
		if(t.getTransactionRefNo().length() > 12) {
			t.setTransactionRefNo(t.getTransactionRefNo().substring(0,12));
			return "Transaction Reference Number exceeds 12 characters.";
		}
		if(t.getValueDate().length() > 8) {
			return "Wrong date.";
		}
		for(Character c : t.getValueDate().toCharArray()) {
			if(!Character.isDigit(c)) return "Date should not contain non-digit characters.";
		}
		if(t.getPayerName().length() > 35) {
			t.setPayerName(t.getPayerName().substring(0,35));
			return "Payer name exceeds 35 characters.";
		}
		if(t.getPayerAccountNumber().length() > 12){
			t.setPayerAccountNumber(t.getPayerAccountNumber().substring(0,12));
			return "Payer account number exceeds 12 characters.";
		}
		if(t.getPayeeName().length() > 35) {
			t.setPayeeName(t.getPayeeName().substring(0,35));
			return "Payee name exceeds 35 characters.";
		}
		if(t.getPayeeAccountNumber().length() > 12) {
			t.setPayeeAccountNumber(t.getPayeeAccountNumber().substring(0,12));
			return "Payee account number exceeds 12 characters.";
		}
			return "Pass";		
	}
	
	public List<String> getKeywords() {
		List<String> keywords = new ArrayList();
		Connection conn = null;
		try
		{
			conn=dataSource.getConnection();
			PreparedStatement prepsmt=null;
			ResultSet rs=null;// resultset hold whole row in a db
			String query="select keyword from keywords";
			boolean flag=false;
			prepsmt=conn.prepareStatement(query);
			rs=prepsmt.executeQuery();
			while(rs.next())
			{
				keywords.add(rs.getString("keyword"));
			}
			return keywords;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	    finally
		{
			DBUtils.closeConnection(conn);
		}
		return keywords;
	}
	public String sanctionTransactions() {
		Connection conn = null;
		List<String> keywords = this.getKeywords();
		System.out.println(keywords);
		try
		{
			conn=dataSource.getConnection();
			PreparedStatement prepsmt=null;
			ResultSet rs=null;// resultset hold whole row in a db
			String query="select transactionRefNo, payerName, payeeName from CurrentTransaction;";
			boolean flag=false;
			prepsmt=conn.prepareStatement(query);
			rs=prepsmt.executeQuery();
			while(rs.next())
			{
				String transactionRefNumber = rs.getString("transactionRefNo");
				String payerName = rs.getString("payerName"); 
				String payeeName = rs.getString("payeeName"); 
				query = "update CurrentTransaction set sanctioningStatus = ?, sanctionFailMessage = ? where transactionRefNo = ?";
				if(keywords.contains(payeeName) ) {
					prepsmt=conn.prepareStatement(query);
					prepsmt.setString(1, "Fail");
					prepsmt.setString(3, transactionRefNumber);
					prepsmt.setString(2, "Payee name in keyword list.");
			    }
				else if(keywords.contains(payerName)) {
					prepsmt=conn.prepareStatement(query);
					prepsmt.setString(1, "Fail");
					prepsmt.setString(3, transactionRefNumber);
					prepsmt.setString(2, "Payer name in keyword list.");
				}
				else {
					prepsmt=conn.prepareStatement(query);
					prepsmt.setString(1, "Pass");
					prepsmt.setString(3, transactionRefNumber);
					prepsmt.setString(2, "Pass");
				}
				prepsmt.executeUpdate();
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
		//this.truncateToArchive();
		return "finished";
	}
	
	public String truncateToArchive() {
		Connection conn = null;
		PreparedStatement selectPrepsmt = null;
		PreparedStatement insertPrepsmt = null;
		PreparedStatement truncatePrepsmt = null;
		System.out.println("hi"+currentCount);
		TransactionFile tf = null;
			try
			{
				conn=dataSource.getConnection();
				this.fileStatistics();
				String insertQuery = "insert into archive values(?,?,?,?,?,?,?,?,?,?,?,?);";
				for(int i=0;i<currentCount;i++) {
					String selectQuery = "select * from CurrentTransaction where transactionRefNo = " + "\"" + this.transactionRefNumberList.get(i) + "\"";
					selectPrepsmt=conn.prepareStatement(selectQuery);
					ResultSet rs=selectPrepsmt.executeQuery();
					if(rs.next()) {
						insertPrepsmt=conn.prepareStatement(insertQuery);
						insertPrepsmt.setString(9,rs.getString("validationStatus"));
						insertPrepsmt.setString(8,rs.getString("sanctioningStatus"));
						insertPrepsmt.setString(10,rs.getString("sanctionFailMessage"));
						insertPrepsmt.setString(11,rs.getString("validationFailMessage"));
						insertPrepsmt.setString(12,rs.getString("filename"));
						insertPrepsmt.setString(1,rs.getString("transactionRefNo"));
						insertPrepsmt.setString(2,rs.getString("valueDate"));
						insertPrepsmt.setString(3,rs.getString("payerName"));
						insertPrepsmt.setString(4,rs.getString("payerAccountNumber"));
						insertPrepsmt.setString(5,rs.getString("payeeName"));
						insertPrepsmt.setString(6,rs.getString("payeeAccountNumber"));
						insertPrepsmt.setDouble(7,rs.getDouble("amount"));
						int res1=insertPrepsmt.executeUpdate();
					}
				}
				String truncateQuery = "truncate table CurrentTransaction";
				truncatePrepsmt=conn.prepareStatement(truncateQuery);
				int res2=truncatePrepsmt.executeUpdate();
				this.transactionRefNumberList.clear();
				currentCount = 0;
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			
			}
			finally
			{
				DBUtils.closeConnection(conn);
			}
		
		return "finished";
		
	}
	
	public List<Transaction> retrieveAll(){
		Connection conn = null;
		List<Transaction> transactions = new ArrayList();
		try
		{
			conn=dataSource.getConnection();
			PreparedStatement prepsmt=null;
			ResultSet rs=null;// resultset hold whole row in a db
			String query="select * from CurrentTransaction;";
			prepsmt=conn.prepareStatement(query);
			rs=prepsmt.executeQuery();
			while(rs.next())
			{
				Transaction t = new Transaction();
				t.setTransactionRefNo(rs.getString("transactionRefNo"));
				t.setPayerName(rs.getString("payerName"));
				t.setPayeeName(rs.getString("payeeName"));
				t.setPayeeAccountNumber(rs.getString("payeeAccountNumber"));
				t.setPayerAccountNumber(rs.getString("payerAccountNumber"));
				t.setValueDate(rs.getString("valueDate"));
				t.setAmount(rs.getDouble("amount"));
				t.setSanctioningStatus(rs.getString("sanctioningStatus"));
				t.setValidationStatus(rs.getString("validationStatus"));
				t.setFilename(rs.getString("filename"));
				t.setSanctionFailMessage(rs.getString("sanctionFailMessage"));
				t.setValidationFailMessage(rs.getString("validationFailMessage"));
				transactions.add(t);
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
		return transactions;		
		
	}
	
	String createQuery(String field, String status) {
		if (status.equals("Pass")) return  "select * from CurrentTransaction where "+field+" = \"Pass\" ";
		else return "select * from CurrentTransaction where "+field+" = \"Fail\" ";
	}
	@Override
	public List<Transaction> filter(String field, String status) {
		Connection conn = null;
		int option;
		if(status.equals("Fail")) option = 0;
		else option = 1;
		List<Transaction> transactions = new ArrayList();
		try
		{
			conn=dataSource.getConnection();
			PreparedStatement prepsmt=null;
			ResultSet rs=null;// resultset hold whole row in a db
			String query= createQuery(field,status);
			prepsmt=conn.prepareStatement(query);
			System.out.println(query);
			rs=prepsmt.executeQuery();
			while(rs.next())
			{
				Transaction t = new Transaction();
				t.setTransactionRefNo(rs.getString("transactionRefNo"));
				t.setPayerName(rs.getString("payerName"));
				t.setPayeeName(rs.getString("payeeName"));
				t.setPayeeAccountNumber(rs.getString("payeeAccountNumber"));
				t.setPayerAccountNumber(rs.getString("payerAccountNumber"));
				t.setValueDate(rs.getString("valueDate"));
				t.setAmount(rs.getDouble("amount"));
				t.setSanctioningStatus(rs.getString("sanctioningStatus"));
				t.setValidationStatus(rs.getString("validationStatus"));
				t.setFilename(rs.getString("filename"));
				t.setSanctionFailMessage(rs.getString("sanctionFailMessage"));
				t.setValidationFailMessage(rs.getString("validationFailMessage"));
				transactions.add(t);
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
		return transactions;
		
	}
	
	public String returnMessage(String message, String transactionRefNo) {
		
		Connection conn = null;
		try
		{
			conn=dataSource.getConnection();
			PreparedStatement prepsmt=null;
			ResultSet rs=null;// resultset hold whole row in a db
			String query="select "+message+" from CurrentTransaction where transactionRefNo = "+ "\"" + transactionRefNo + "\"";
			prepsmt=conn.prepareStatement(query);
			rs=prepsmt.executeQuery();
			if(rs.next())
			{
				String rmsg = rs.getString(1);
				return rmsg;
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
		
		return "No such transactionRefNo";
		
	}
	
	public TransactionFile fileStatistics() {
		Connection conn = null;
		TransactionFile tf = null;
		try
		{
			conn=dataSource.getConnection();
			System.out.println("xoxo");
			PreparedStatement prepsmt=null;
			ResultSet rs=null;// resultset hold whole row in a db
			String query="select sanctioningStatus, validationStatus,filename from CurrentTransaction;";
			prepsmt=conn.prepareStatement(query);
			rs=prepsmt.executeQuery();
			int numValidationFailed = 0, numSanctionFailed=0, numTransaction = 0;
			String filename = "";
			while(rs.next())
			{
				numTransaction++;
				String ss = rs.getString("sanctioningStatus");
				if(ss.equals("Fail")) numSanctionFailed++;
				String vs = rs.getString("validationStatus");
				if(vs.equals("Fail")) numValidationFailed++;
				filename = rs.getString("filename");
				
				
	
			}
			Timestamp ts = Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Kolkata")));
			tf = new TransactionFile(filename, numTransaction, ts, numValidationFailed, numSanctionFailed);
			System.out.println(tf.toString());
			String insertquery = "insert into FileSystem values(?,?,?,?,?);";
			PreparedStatement insertprepsmt=conn.prepareStatement(insertquery);
			insertprepsmt.setString(1,tf.getFilename());
			insertprepsmt.setInt(2,tf.getNumTransactions());
			insertprepsmt.setTimestamp(3,tf.getTimestamp());
			insertprepsmt.setInt(4,tf.getNumValidationFailed());
			insertprepsmt.setInt(5,tf.getNumSanctionFailed());
			int res=insertprepsmt.executeUpdate();
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	    finally
		{
			DBUtils.closeConnection(conn);
		}
		
		return tf;
	}

}

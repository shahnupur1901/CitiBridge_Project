package com.project.citi.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.project.citi.dto.Transaction;
import com.project.citi.utils.DBUtils;

@Repository	
public class TransactionRepositoryImpl implements TransactionRepository{
	@Autowired
	DataSource dataSource;
	public TransactionRepositoryImpl()
	{	
		
	}
	private static TransactionRepository transactionRepository;
	public String addTransactionFile(List<Transaction> list) {
		Connection conn = null;
		PreparedStatement prepsmt = null;
		String query = "insert into CurrentTransaction values(?,?,?,?,?,?,?,?,?);";
			try
			{
				for(Transaction t : list) {
				conn = dataSource.getConnection();
				prepsmt=conn.prepareStatement(query);
				prepsmt.setString(9, this.validateTransactions(t));
				prepsmt.setString(8,"In process.");
				prepsmt.setString(1,t.getTransactionRefNo());
				prepsmt.setString(2,this.formatDate(t.getValueDate()));
				prepsmt.setString(3,t.getPayerName());
				prepsmt.setString(4,t.getPayerAccountNumber());
				prepsmt.setString(5,t.getPayeeName());
				prepsmt.setString(6,t.getPayeeAccountNumber());
				prepsmt.setDouble(7,t.getAmount());
				int res=prepsmt.executeUpdate();
				}
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			
		}
		
		return "finished";
		}
	
	public String formatDate( String date) {
		return "";
	}
	
	public String validateTransactions(Transaction t) {
		if(t.getTransactionRefNo().length() > 12) {
			t.setTransactionRefNo(t.getTransactionRefNo().substring(0,12));
			return "Fail";
		}
		if(t.getValueDate().length() > 8) {
			return "Fail";
		}
		if(t.getPayerName().length() > 35) {
			t.setPayerName(t.getPayerName().substring(0,35));
			return "Fail";
		}
		if(t.getPayerAccountNumber().length() > 12){
			t.setPayerAccountNumber(t.getPayerAccountNumber().substring(0,12));
			return "Fail";
		}
		if(t.getPayeeName().length() > 35) {
			t.setPayeeName(t.getPayeeName().substring(0,35));
			return "Fail";
		}
		if(t.getPayeeAccountNumber().length() > 12) {
			t.setPayeeAccountNumber(t.getPayeeAccountNumber().substring(0,12));
			return "Fail";
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
				query = "update CurrentTransaction set sanctioningStatus = ? where transactionRefNo = ?";
				if(keywords.contains(payeeName) || keywords.contains(payerName)) {
					prepsmt=conn.prepareStatement(query);
					prepsmt.setString(1, "Fail");
					prepsmt.setString(2, transactionRefNumber);
			    }
				else {
					prepsmt=conn.prepareStatement(query);
					prepsmt.setString(1, "Pass");
					prepsmt.setString(2, transactionRefNumber);
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
		

}

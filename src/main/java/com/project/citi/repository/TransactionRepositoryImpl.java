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
				prepsmt.setInt(9, this.validateTransactions(t));
				prepsmt.setInt(8,1);
				prepsmt.setString(1,t.getTransactionRefNo());
				prepsmt.setString(2,t.getValueDate());
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
	public int validateTransactions(Transaction t) {
		if(t.getTransactionRefNo().length() > 12) {
			t.setTransactionRefNo(t.getTransactionRefNo().substring(0,12));
			return 0;
		}
		if(t.getValueDate().length() > 8) return 0;
		if(t.getPayerName().length() > 35) return 0;
		if(t.getPayerAccountNumber().length() > 12) return 0;
		if(t.getPayeeName().length() > 35) return 0;
		if(t.getPayeeAccountNumber().length() > 12) return 0;
		return 1;
			
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
					prepsmt.setInt(1, 0);
					prepsmt.setString(2, transactionRefNumber);
			    }
				else {
					prepsmt=conn.prepareStatement(query);
					prepsmt.setInt(1, 1);
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
				t.setSanctioningStatus(rs.getInt("sanctioningStatus"));
				t.setValidationStatus(rs.getInt("validationStatus"));
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
			String query= " select * from CurrentTransaction where sanctioningStatus = 1;";
			prepsmt=conn.prepareStatement(query);
			//prepsmt.setString(1, field);
			System.out.println(field);System.out.println(option);
			//prepsmt.setInt(2, option);
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
				t.setSanctioningStatus(rs.getInt("sanctioningStatus"));
				t.setValidationStatus(rs.getInt("validationStatus"));
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

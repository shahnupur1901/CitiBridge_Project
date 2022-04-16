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
			String query="select * from FileSystem;";
			prepsmt=conn.prepareStatement(query);
			rs=prepsmt.executeQuery();
			int count = 0;
			while(rs.next())
			{
				count++;
				if(count > 10) break;
				TransactionFile tf = new TransactionFile();
				tf.setFilename(rs.getString(0));
				tf.setNumTransactions(rs.getInt(1));
				tf.setTimestamp(rs.getTimestamp(2));
				tf.setNumValidationFailed(rs.getInt(3));
				tf.setNumSanctionFailed(rs.getInt(3));
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

}

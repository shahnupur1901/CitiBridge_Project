package com.project.citi.utils;

//
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtils 
{
	public static void main(String[] args) throws SQLException {
		System.out.println(getConnection()!=null);
	}
	
	public static Connection getConnection() //to create connection with JDBC using the details read from the file
	{
		Connection conn=null;
		
		Properties properties=readProperties();
		try
		{
		conn=DriverManager.getConnection(properties.getProperty("db.url"),
				properties.getProperty("db.username"),
				properties.getProperty("db.password"));}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return conn;
	}
	
	public static void closeConnection(Connection connection)
	{
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private static Properties readProperties()
	//this is to read the jdbc connecting details like password and path i.e. given in the application properties file
	
	{
		InputStream ips=DBUtils.class.getResourceAsStream("/applicationproperties");
		Properties properties=new Properties();
		try
		{
			properties.load(ips);
			System.out.println(properties);
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		return properties;
	}

}
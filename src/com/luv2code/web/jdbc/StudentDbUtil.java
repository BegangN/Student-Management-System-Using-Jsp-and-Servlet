package com.luv2code.web.jdbc;

import java.util.List;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

public class StudentDbUtil {


	private static DataSource dataSource;
	
	public StudentDbUtil(DataSource theDataSource) {
		dataSource = theDataSource;
	}
	
	public static List<Student> getStudents() throws Exception{
		
		List<Student> students = new ArrayList<>();
		
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		
		try {
			myConn = dataSource.getConnection();
			
			String sql = "select * from student order by last_name";
			
			myStmt = myConn.createStatement();
			
			myRs = myStmt.executeQuery(sql);
			
			while(myRs.next()) {
				
				int id = myRs.getInt("id");
				String first_name = myRs.getString("first_name");
				String last_name = myRs.getString("last_name");
				String email = myRs.getString("email");
				
				Student tempStudent = new Student(id, first_name, last_name, email);
				
				students.add(tempStudent);

			}
			
			return students;
	}
		finally {
		   
			close(myConn, myStmt, myRs);
			
		}
	}
	
	private static void close(Connection myConn, Statement myStmt, ResultSet myRs) {
		try {
			if (myRs != null) {
				myRs.close();
			}
			
			if (myStmt != null) {
				myStmt.close();
			}
			
			if (myConn != null) {
				myConn.close();
			}
			
		}
		catch(Exception exc) {
			exc.printStackTrace();
		}
		
	}
	
	public void addStudent(Student theStudent) throws Exception {
		
		    Connection myConn =null;
		    PreparedStatement myStmt= null;
		       
		    try {
		    	  myConn=dataSource.getConnection();
		    	  
		    	  
		    	  String sql="insert into student "
		    	  		+ "(first_name, last_name, email) "
		    	  		+ "values(?, ?, ?)";
		    	  
		    	  
		    	  myStmt= myConn.prepareStatement(sql);
		    	  
		    	  myStmt.setString(1,theStudent.getFirst_name());
		    	  myStmt.setString(2,theStudent.getLast_name());
		    	  myStmt.setString(3,theStudent.getEmail());
		    	  
		    	  myStmt.execute();
		    }
		     finally {
		    	 close(myConn,myStmt,null);
		    	
		    }
	
	}
	public  Student getStudent(String theStudentId) throws Exception {
		
		  Student theStudent=null;
		  Connection myConn =null;
		  PreparedStatement myStmt =null;
		  ResultSet myRs=null;
		  int studentId;
		  
		   try {
			    studentId = Integer.parseInt(theStudentId);
			    
			    myConn = dataSource.getConnection();
			    
			    String sql="select * from student where id=?";
			    
			    myStmt =myConn.prepareStatement(sql);
			    
			    
			    myStmt.setInt(1, studentId);
			    
			    
			    myRs = myStmt.executeQuery();
			    
			    if(myRs.next()) {
			    	
			    	String first_name =myRs.getString("first_name");
			    	String last_name =myRs.getString("last_name");
			    	String email =myRs.getString("email");
			    	
			    	theStudent = new Student(studentId,first_name,last_name,email);
			    	
			    	
			    }
			    else {
			    	
			    	throw new Exception("Could not find student id:"+ studentId);
			    	
			    }
			     return theStudent;
		  
		   }
		    finally {
		    	
		    	close(myConn,myStmt,myRs);
		    }
		
	}
	 public static void updateStudent(Student theStudent) throws Exception{
		 
		 Connection myConn =null;
		 PreparedStatement myStmt =null;
		 
		   try {
			     myConn = dataSource.getConnection();
			     
			 String sql="Update student set first_name=?, last_name=? , email=? Where id=?";
			     
			     myStmt = myConn.prepareStatement(sql);
			     
			     myStmt.setString(1, theStudent.getFirst_name());
			     myStmt.setString(2, theStudent.getLast_name());
			     myStmt.setString(3, theStudent.getEmail());
			     myStmt.setInt(4, theStudent.getId());
			     
			     myStmt.execute();
			     
			    		 
		   }
		    finally {
		    	close(myConn,myStmt,null);
		    	
		    }
		 
		 
		 
	 }
}

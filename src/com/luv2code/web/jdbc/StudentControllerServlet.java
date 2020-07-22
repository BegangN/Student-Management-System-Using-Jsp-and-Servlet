package com.luv2code.web.jdbc;


import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet implementation class StudentControllerServlet
 */
@WebServlet("/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private StudentDbUtil studentDbUtil;
	
	@Resource(name = "jdbc/web_student_tracker")
	private DataSource dataSource;
	
	@Override
	public void init() throws ServletException{
		super.init();
		
		try {
			studentDbUtil = new StudentDbUtil(dataSource);
		}
		catch(Exception exc) {
			throw new ServletException(exc);
		}
		
	}
	
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			//listStudents(request, response);
			String theCommand = request.getParameter("command");
			
			if(theCommand==null) {
				theCommand ="LIST";
			}
			 switch(theCommand) {
			 
			 case "LIST":
				 listStudents(request,response);
				 break;
				 
			 case "ADD":
				 addStudent(request,response);
				 break;
				 
				 
			 case "LOAD":
				 loadStudent(request,response);
				 break;
				 
			 case "UPDATE":
				 updateStudent(request,response);
				 break;
			      
				 
				 default:
					 listStudents(request,response);
			 }
			
		}
		
		catch(Exception exc) {
			throw new ServletException(exc);
		}
		
	}
	
	  private void  updateStudent(HttpServletRequest request, HttpServletResponse
			  response) throws Exception{
		  
		  int id=Integer.parseInt(request.getParameter("studentId"));
		  String first_name =request.getParameter("first_name");
		  String last_name =request.getParameter("last_name");
		  String email =request.getParameter("email");
		  
		  Student theStudent = new Student(id,first_name,last_name,email);
		  
		  studentDbUtil.updateStudent(theStudent);
		  
		  listStudents(request,response);
				  
	  }
	  private void  loadStudent(HttpServletRequest request, HttpServletResponse
			  response) throws Exception{
		  
		  String theStudentId = request.getParameter("studentId");
		  
		  Student theStudent = studentDbUtil.getStudent(theStudentId);
		  
		  request.setAttribute("THE_STUDENT",theStudent);
		  
		  
		  RequestDispatcher dispatcher=
				    request.getRequestDispatcher("/updateStudentForm.jsp");
		            dispatcher.forward(request, response);
		  
		  
	  }
	   
	  
	  private  void addStudent(HttpServletRequest request,HttpServletResponse 
			  response ) throws Exception{
		  
		  String first_name= request.getParameter("first_name");
		  String last_name = request.getParameter("last_name");
		  String email =request.getParameter("email");
		  
		  Student theStudent =new Student( 0, first_name,last_name,email);
		  
		  studentDbUtil.addStudent(theStudent);
		  
		  listStudents(request,response);
	  }
	
	  
		private void listStudents(HttpServletRequest request, HttpServletResponse response)
		throws Exception{
			
			List<Student> students = StudentDbUtil.getStudents();
			
			request.setAttribute("STUDENT_LIST", students);
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("/list-students.jsp");
			dispatcher.forward(request, response);
		}
		
}



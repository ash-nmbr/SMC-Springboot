package com.example.StockMarketCharting.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

import org.apache.tomcat.jni.Buffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.example.StockMarketCharting.dao.UserModelRepository;
import com.example.StockMarketCharting.model.UserModel;


import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.*;


@CrossOrigin
@RestController
public class UserModelController {
	
	@Autowired
	UserModelRepository userRep;
	
	//Test purposes
	@RequestMapping(value="/addUser",method=RequestMethod.POST)
	public RedirectView addUser(@RequestBody UserModel userModel)  {

			userRep.save(userModel);
			return new RedirectView("test");
		}
	
	
	@RequestMapping(value="/test",method=RequestMethod.GET)
	public String test()  {

			
			return "working";
		}
	
	@RequestMapping(value="/getUserByEmail/{email}",method=RequestMethod.GET)
	public UserModel getUserByEmail(@PathVariable String email)  {
			
		System.out.println("hello");
			System.out.println(email);
			UserModel u = userRep.findByEmail(email);
			return u;
			
		}
	
	
	//Update User Details
	@RequestMapping(value = "/updateUser",method=RequestMethod.POST)
	public String updateUser(@RequestBody Map<String, String> userCreds){	
	
		boolean admin;
		String name, mobileNumber, role, password, email;
		name = userCreds.get("name");
		mobileNumber = userCreds.get("mobileNumber");
		password = userCreds.get("password");
		role = userCreds.get("role");
		email = userCreds.get("email");
		
		UserModel user = userRep.findByEmail(email);
		
		user.setName(name);
		user.setMobileNumber(mobileNumber);
		user.setPassword(password);
		user.setRole(role);
		
		userRep.save(user);
		return "Your profile has been updated";

	}
	
	

	
	//LOGIN
	@RequestMapping(value = "/login",method=RequestMethod.POST)
	public ResponseEntity<String> Login(@RequestBody Map<String, String> userCreds){	
	
		boolean admin;
		String email, pass, message;
		email = userCreds.get("email");
		pass = userCreds.get("password");
		admin = Boolean.parseBoolean(userCreds.get("admin"));
		System.out.println(admin);
		System.out.println(email);
		
		UserModel user = userRep.findByEmail(email);
		if (admin)
		{
			if (user == null || !user.isAdmin())
				message = "You're not an admin";
			
			else if (!user.getPassword().equals(pass))
				message = "Wrong Password";
			
			else
				message = "admin";
		}
		
		else
		{
			if (user == null || user.isAdmin())
				message = "You're not registered as a user";
			
			else if (!user.getPassword().equals(pass))
				message = "Wrong Password";
			
			else if (!user.isConfirmed())
				message = "Email not yet verified. Go check your mail.";
			
			else
				message = "user";
		}
		
		
		
		return new ResponseEntity<>(message, HttpStatus.OK);

	}
	
	
	//UPDATE PROFILE
	@RequestMapping(value = "/updateProfile",method=RequestMethod.POST)
	public String updateProfile(@RequestBody Map<String, String> newCreds){	
	
		String email, name, pass, mobile, role, message;
		email = newCreds.get("email");
		pass = newCreds.get("password");
		name = newCreds.get("name");
		mobile = newCreds.get("mobile");
		role = newCreds.get("role");
		
		UserModel user = userRep.findByEmail(email);
		if (user == null)
			message = "User not Registered";

		else if (user.getPassword().equals(pass))
		{
			System.out.println(user.isConfirmed());
			if (user.isConfirmed() == false)
				message = "Email Verification Required";
				
			else
			{
				user.setPassword(pass);
				user.setMobileNumber(mobile);
				user.setName(name);
				user.setRole(role);
				userRep.save(user);
				return "Profile Updated";
			}
		}
			
		else
			message = "Wrong password ";
		
		return message;

	}
	
	
	//SIGNUP
	@RequestMapping(value = "/setuserapi",method=RequestMethod.POST)
	public String Stringreactuserapi(@RequestBody UserModel user) throws AddressException, MessagingException, IOException {	
	
		Long id;
		String email, message;
		email = user.getEmail();
		UserModel u = userRep.findByEmail(email);
		
		if (u != null && u.isConfirmed())
			return "Email Id already in use, Go to Sign In Page";
		
		else if (u != null && (!u.isConfirmed()) )
		{
			id = u.getId();
			
			HttpHeaders headers = new HttpHeaders();
			headers.add("Responded", "UserController");
			headers.add("Access-Control-Allow-Origin", "*");
			System.out.println(email);
			System.out.println(id);
			sendemail(id) ;
		}
		
		else
		{
			userRep.save(user);
			id = userRep.findByEmail(email).getId();
			
			HttpHeaders headers = new HttpHeaders();
			headers.add("Responded", "UserController");
			headers.add("Access-Control-Allow-Origin", "*");
			System.out.println(email);
			System.out.println(id);
			sendemail(id) ;
		}
		

		
		u = userRep.findByEmail(email);
		if (u.isConfirmed() )
			message = "user";
		
		else
			message = "Verify your Email.";
			
		return message;

	}

		


	public void sendemail(Long userid) throws AddressException, MessagingException, IOException {
      UserModel user = userRep.getById(userid);	

//		final String username = "ashwin.nambiar47@gmail.com";
//		final String password = "wontforgetitthistime";
//
//		Properties prop = new Properties();
////		prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
////		prop.put("mail.smtp.host", "smtp.gmail.com");
////		prop.put("mail.smtp.port", "587");
////		prop.put("mail.smtp.auth", "true");
////		prop.put("mail.smtp.starttls.enable", "true"); //TLS
//		
//		
//		prop.put("mail.smtp.host", "smtp.gmail.com");
//		prop.put("mail.smtp.port", "465");
//		prop.put("mail.smtp.auth", "true");
//		prop.put("mail.smtp.starttls.enable", "true");
//		prop.put("mail.smtp.starttls.required", "true");
//		prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
//		prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//		
//
//		Session session = Session.getInstance(prop,
//				new javax.mail.Authenticator() {
//			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
//				return new javax.mail.PasswordAuthentication(username, password);
//			}
//		});
////		session.setDebug(true);
//		try {
//
//			Message message = new MimeMessage(session);
//			message.setFrom(new InternetAddress("ashwin.nambiar47@gmail.com"));
//			//message.setRecipients(
//				//	Message.RecipientType.TO,
//				//	InternetAddress.parse("sftrainerram@gmail.com")
//				//	);
//			message.setRecipients(
//					Message.RecipientType.TO,
//					InternetAddress.parse(user.getEmail())
//					);
//			message.setSubject("USer confirmation email");
//			//     message.setText("Dear Mail Crawler,"
//			//           + "\n\n Please do not spam my email!");
//			message.setContent(
//					"<h1><a href =\"http://127.0.0.1:8080/confirmuser/"+userid+"/\"> Click to confirm </a></h1>",
//					"text/html");
//			System.out.println("Message");
//			System.out.println(message);
//			
//			
//			Transport.send(message);
//
//			System.out.println("Done");
//
//		} catch (MessagingException e) {
//			e.printStackTrace();
//		}
      
      
      
      // sendgrid
      Email from = new Email("ashwin.nambiar47@gmail.com");
      Email to = new Email("ashwin.nambiar47@gmail.com"); // use your own email address here

      String subject = "Sending with Twilio SendGrid is Fun";
      Content content = new Content("text/html", "<h1><a href =\"https://smc-springboot.herokuapp.com/confirmuser/"+userid+"/\"> Click to confirm </a></h1>");

      Mail mail = new Mail(from, subject, to, content);

      SendGrid sg = new SendGrid("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");
      Request request = new Request();

      request.setMethod(Method.POST);
      request.setEndpoint("mail/send");
      request.setBody(mail.build());

      Response response = sg.api(request);

      System.out.println(response.getStatusCode());
      System.out.println(response.getHeaders());
      System.out.println(response.getBody());
      
      
      
      
	}
	
	


	@RequestMapping(value="/confirmuser/{userid}", method=RequestMethod.GET)
	public String welcomepage(@PathVariable Long userid) {
		Optional<UserModel> userlist =  userRep.findById(userid);
		//do a null check for home work
		UserModel usr = new UserModel();
		usr = userRep.getById(userid);
		usr.setConfirmed(true);
		userRep.save(usr);
		return "User confirmed" +usr.getName();
	}
	

}

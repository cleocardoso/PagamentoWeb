package com.Teste.Aplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.Teste.Aplication.model.User;

@Controller
public class HomeController {

	@GetMapping("/home")
	public String home() {
	  //User usuarioByEmail = service.getEmail(SecurityContextHolder.getContext().getAuthentication().getName());
	  //serviceSession.criarSession("usuario", usuarioByEmail);
		return "home";
	}  
	
	
	
	

}

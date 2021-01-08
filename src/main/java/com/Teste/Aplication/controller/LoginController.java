package com.Teste.Aplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Teste.Aplication.model.User;
import com.Teste.Aplication.util.RestTemplateUtil;
import com.Teste.Aplication.util.SessionUtil;

@Controller
public class LoginController {
	@Autowired
	private SessionUtil<User> sessionUtil;

	@GetMapping("/")
	public String login() {
		return "login";
	}
	@SuppressWarnings("unchecked")
	@PostMapping("/entrar")
	public ModelAndView entrar(@RequestParam("username") String username, @RequestParam("password") String senha,RedirectAttributes attrs) {
		//String fooResourceUrl = "http://localhost:8081/api/usuarios/login";
		String fooResourceUrl = "https://projeto-pag-api.herokuapp.com/api/usuarios/login";
		try {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("email", username);
		params.add("senha", senha);
		ResponseEntity<User> responseEntity = 
				(ResponseEntity<User>) RestTemplateUtil.sendByParams(HttpMethod.POST, fooResourceUrl, params, User.class);
		
		if(responseEntity.getStatusCode().is2xxSuccessful()) {
			sessionUtil.criarSession("user", responseEntity.getBody());
			return new ModelAndView("home");
		}
		
		}catch(Exception e) {
			attrs.addAttribute("fail", "Usuário Inválido!");	
			return new ModelAndView("login");
			
		}
		
		return new ModelAndView("login");
	}

	@PostMapping("/home")
	public String test() {
		return "home";
	}

	// login invalido
	@GetMapping("/login-error")
	public String loginError(ModelMap model) {
		model.addAttribute("alerta", "erro");
		model.addAttribute("titulo", "Credenciais inválidas!");
		model.addAttribute("texto", "Login ou senha incorretos, tente novamente!");
		return "login";
	}

}

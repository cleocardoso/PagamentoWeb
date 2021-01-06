package com.Teste.Aplication.controller;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Teste.Aplication.model.User;
import com.Teste.Aplication.util.RestTemplateUtil;
import com.Teste.Aplication.util.SessionUtil;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private SessionUtil<User> sessionUtil;
	
	@GetMapping("/cadastrar")
	public String cadastrar(User user) {
		return "user/cadastro";
	}

	@PostMapping("/salvar")
	public ModelAndView salvar(@Valid User user, BindingResult result, RedirectAttributes attr) {
		if (result.hasErrors()) {
			return new ModelAndView("user/cadastro");
		}
		ModelAndView view = new ModelAndView("login");
		String fooResourceUrl = "http://localhost:8081/api/usuarios/save";
		try {
			
			MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
			params.add("nome", user.getNome());
			params.add("email", user.getEmail());
			params.add("senha", user.getSenha());

			
			ResponseEntity<User> responseEntity = (ResponseEntity<User>) RestTemplateUtil.sendByParams(HttpMethod.POST, fooResourceUrl, params, User.class);
			if(responseEntity.getStatusCode().is2xxSuccessful()) {
				view.addObject("success", "Usuário Cadastrado com sucesso!");
			}else if(responseEntity.getStatusCode().is5xxServerError()) {
				result.reject("email", "Ops... Este e-mail já existe!");
				return new ModelAndView(cadastrar(user));
			}
			
			}catch (Exception e) {
				// TODO: handle exception
			}		
		return view;
	}

	@GetMapping("/detalhes")
	public ModelAndView detalhePorId() {
		User user = sessionUtil.getSession("user");
		
		if(user != null) {
			ModelAndView mv = new ModelAndView("user/detalhes");
			mv.addObject("usuario", user);
			return mv;
		}
		return new ModelAndView("login").addObject("fail", "Sessão Expirada!");
	}

	@GetMapping("/editar")
	public String preEditar(ModelMap model) {
		User user = sessionUtil.getSession("user");
		
		if(user != null) {
			model.addAttribute("usuario", user);
			return "user/formeditar";
		}
		model.addAttribute("fail", "Sessão Expirada!");
		return "login";
	}

	@PostMapping("/editar")
	public String editar(User user, RedirectAttributes attr) {
		String fooResourceUrl = "http://localhost:8081/api/usuarios/updateUser/"+user.getId();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.add("nome", user.getNome());
		params.add("email", user.getEmail());
		params.add("senha", user.getSenha());
		
		ResponseEntity<User> responseEntity = (ResponseEntity<User>) RestTemplateUtil.sendByParams(HttpMethod.PUT, fooResourceUrl, params, User.class);
		
		if(responseEntity.getStatusCode().is2xxSuccessful()) {
			attr.addFlashAttribute("success", "Seus dados foram alterados com sucesso!");
			sessionUtil.criarSession("user", responseEntity.getBody());
		}
		
		return "redirect:/user/editar";
	}

	// não vamos excluir
	/*
	 * @GetMapping("/excluir/{id}") public String excluir(@PathVariable("id") Long
	 * id, RedirectAttributes attr) { service.excluir(id);
	 * attr.addFlashAttribute("success", "Funcionário removido com sucesso.");
	 * return "redirect:/usuario/listar"; }
	 */

	@GetMapping("/editarSenha")
	public String editarSenha() {
		return "user/editarSenha";
	}

	@PostMapping("/trocarSenha")
	public ModelAndView trocarSenha(@RequestParam("email") String email) {
		// comentar na video chamada
		/*
		 * User user2 = service.getEmail(email); ModelAndView view = new
		 * ModelAndView("login"); if (user2 == null) {
		 * 
		 * view.addObject("error", "Email não está cadastrado no sistema!"); } else {
		 * Random r = new Random(); String novaSenhaGerada =
		 * String.valueOf(Math.abs(r.nextInt())); System.out.println(novaSenhaGerada);
		 * // user2.setSenha(novaSenhaGerada); user2.setSenha(new
		 * BCryptPasswordEncoder().encode(novaSenhaGerada)); service.salvar(user2);
		 * Email email2 = new Email(); email2.setTo(user2.getEmail());
		 * sendEmail.sendNovaSenhaEmail(email2, novaSenhaGerada);
		 * view.addObject("mensagem", "Nova senha gerada!!!"); }
		 */
		//return view;
		return null;
	}

}

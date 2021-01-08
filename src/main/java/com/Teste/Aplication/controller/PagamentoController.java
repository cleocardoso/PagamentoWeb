package com.Teste.Aplication.controller;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Teste.Aplication.Enuns.TipoPagamento;
import com.Teste.Aplication.model.Boleto;
import com.Teste.Aplication.model.Pagamento;
import com.Teste.Aplication.model.User;
import com.Teste.Aplication.util.RestTemplateUtil;
import com.Teste.Aplication.util.SessionUtil;

@Controller
@RequestMapping("/compras")
public class PagamentoController {
	@Autowired
	private SessionUtil<User> sessionUtil;
	
	@Autowired
	private RestTemplateUtil  restTemplateUtil;

	@GetMapping("/comprar")
	public String comprar(Pagamento compra) {
		return "compra/pagamento";
	}

	@PostMapping("/test")
	public ModelAndView postTest(@RequestParam("id") Long id, @RequestParam("valor") Double valor,
			@RequestParam("data") String data, @RequestHeader(required = true, value = "Origin") String origin) {
		return new ModelAndView("compra/pagamento").addObject("id", id).addObject("valor", valor);
	}

	@SuppressWarnings("unchecked")
	@GetMapping("/comprar/{token}")
	public ModelAndView test(@PathVariable("token") String token) {
		try {
		User user = sessionUtil.getSession("user");
		if (user == null) {
			user = (User) restTemplateUtil.getEntity("/api/usuarios/tokenPagamento/" + token,User.class);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + user.getToken());
		ResponseEntity<Pagamento> responseEntity = (ResponseEntity<Pagamento>) restTemplateUtil
				.get("/api/compras/pagamento/" + token, headers, Pagamento.class);

		Pagamento pagamento = responseEntity.getBody();

		if (pagamento != null) {
			sessionUtil.criarSession("user", pagamento.getUsuario());
			return new ModelAndView("compra/pagamento").addObject("compra", pagamento);
		}
		}catch(Exception e) {
			return new ModelAndView("home").addObject("compra", new Pagamento()).addObject("fail",
					"Token expirado!");
		}
		return new ModelAndView("home").addObject("compra", new Pagamento()).addObject("fail",
				"Token expiradoo!");
	}

	@GetMapping("/detalhes")
	public ModelAndView detalhes() {
		User user = sessionUtil.getSession("user");

		if (user != null) {
			try {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Bearer " + user.getToken());
			
			Pagamento[] pagamentos = (Pagamento[]) restTemplateUtil
					.getEntity("/api/compras/detalhes/" + user.getEmail(), headers, Pagamento[].class);
			
			ModelAndView modelAndView = new ModelAndView("compra/detalhes");
			modelAndView.addObject("compras", pagamentos);
			return modelAndView;
			}catch(Exception e) {
				return new ModelAndView("compra/detalhes").addObject("fail", "Não existe nenhum pagamento! ");
			}
		}
		return new ModelAndView("login").addObject("fail", "Sessão Expirada!");
	}

	@PostMapping("/salvar")
	public String salvar(@Valid Pagamento compra, User user, Boleto boleto, RedirectAttributes attr,
			@RequestParam("tipoPagamento") TipoPagamento tipoPagamento) {

		compra.setDataCompra(new Date());

		if (tipoPagamento.equals(TipoPagamento.CARTAO)) {
			attr.addAttribute("id", compra.getIdCompra()); // envia o id na requisição
			attr.addAttribute("quantidade", compra.getQuantidade()); // envia a quantidade na requisição
			attr.addAttribute("valor", compra.getValor()); // envia o valor na requisição
			return "redirect:/cartao/cartao";
		}

		else if (tipoPagamento.equals(TipoPagamento.BOLETO)) {
			attr.addAttribute("id", compra.getIdCompra()); // envia o id na requisição
			attr.addAttribute("quantidade", compra.getQuantidade()); // envia a quantidade na requisição
			attr.addAttribute("valor", compra.getValor()); // envia o valor na requisição
			return "redirect:/boleto/boleto";
		}

		attr.addFlashAttribute("success", "Operação realizada com sucesso!");
		return "/home";

	}

	
}

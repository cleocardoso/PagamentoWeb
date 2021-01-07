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
			user = (User) RestTemplateUtil.getEntity("http://localhost:8081/api/usuarios/tokenPagamento/" + token,
					User.class);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + user.getToken());
		ResponseEntity<Pagamento> responseEntity = (ResponseEntity<Pagamento>) RestTemplateUtil
				.get("http://localhost:8081/api/compras/pagamento/" + token, headers, Pagamento.class);

		Pagamento pagamento = responseEntity.getBody();

		if (pagamento != null) {
			sessionUtil.criarSession("user", pagamento.getUsuario());
			return new ModelAndView("compra/pagamento").addObject("compra", pagamento);
		}
		}catch(Exception e) {
			return new ModelAndView("compra/pagamento").addObject("compra", new Pagamento()).addObject("fail",
					"Token expirado!");
		}
		return new ModelAndView("compra/pagamento").addObject("compra", new Pagamento()).addObject("fail",
				"Token expiradoo!");
	}

	@GetMapping("/detalhes")
	public ModelAndView detalhes() {
		User user = sessionUtil.getSession("user");

		if (user != null) {
			try {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Bearer " + user.getToken());
			
			Pagamento[] pagamentos = (Pagamento[]) RestTemplateUtil
					.getEntity("http://localhost:8081/api/compras/detalhes/" + user.getEmail(), headers, Pagamento[].class);
			
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

	// parcelamento com juros, compra de R$ 1.000, parcelada em 10x, com juros de 2%
	// ao mês,
	// por exemplo. Pagará em cada parcela, R$ 111,33, resultando no total de R$
	// 1.113,27.
	// O valor de R$ 113,27, foi o total que você pagou de juros só por parcelar a
	// compra.
}

package com.Teste.Aplication.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
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

@Controller
@RequestMapping("/compras")
public class PagamentoController {

	private String origem;

	@GetMapping("/comprar")
	public String comprar(Pagamento compra) {
		return "compra/pagamento";
	}
	
	@PostMapping("/test")
	public ModelAndView postTest(@RequestParam("id") Long id, @RequestParam("valor") Double valor, @RequestParam("data") String data, @RequestHeader(required = true, value = "Origin") String origin) {
		return new ModelAndView("compra/pagamento").addObject("id", id)
				.addObject("valor", valor);
	}

	@GetMapping("/comprar/{token}")
	public ModelAndView test(@PathVariable("token") String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "http://localhost:8082");
		ResponseEntity<Pagamento> responseEntity =(ResponseEntity<Pagamento>)RestTemplateUtil
				.get("http://localhost:8081/api/compras/pagamento/"+token, headers, Pagamento.class);
		//System.out.println(responseEntity.g);
		//Aqui vamos  usar o resttemplate consumindo o metodo que retorna o pagamento pelo token da api/
		Pagamento pagamento = responseEntity.getBody();
		
		if(pagamento != null) {
			return new ModelAndView("compra/pagamento")
					.addObject("compra", pagamento) // quando usar o objeto pagamento descomente
			;
		}
		return new ModelAndView("compra/pagamento").addObject("compra", new Pagamento())
				.addObject("fail", "Token expirado!");
	}

	@GetMapping("/detalhes")
	public ModelAndView detalhes(Principal principal) {
		ModelAndView modelAndView = new ModelAndView("compra/detalhes");
		// modelAndView.addObject("compras", compraService.findAllByIdUser(id_compra));
		// System.out.println(id_compra);
		return modelAndView;
	}

	@PostMapping("/salvar")
	public String salvar(@Valid Pagamento compra, User user, Boleto boleto, RedirectAttributes attr,
			@RequestParam("tipoPagamento") TipoPagamento tipoPagamento) {

		compra.setDataCompra(new Date());

		/*
		 * Date data = new Date(); SimpleDateFormat formato = new
		 * SimpleDateFormat("dd/MM/yyyy"); String dataCompra = formato.format(data);
		 * compra.setDataCompra(data);
		 */

		compra.setValor(compra.getValor() * compra.getQuantidade());

		if (tipoPagamento.equals(TipoPagamento.CARTAO)) {

			// compraService.saveAndFlush(compra); // salva a compra para poder enviar o id
			attr.addAttribute("id", compra.getId()); // envia o id na requisição
			attr.addAttribute("origem", this.origem);
			return "redirect:/cartao/cartao";
		}

		else if (tipoPagamento.equals(TipoPagamento.BOLETO)) {

			/// compraService.saveAndFlush(compra);
			// attr.addAttribute("id", compra.getId());
			attr.addAttribute("origem", this.origem);
			return "redirect:/boleto/boleto";
		}

		attr.addFlashAttribute("success", "Operação realizada com sucesso!");
		return "/home";

	}}

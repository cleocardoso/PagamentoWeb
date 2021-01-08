package com.Teste.Aplication.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/boleto")
public class BoletoController {

	private Long id = null;
	@SuppressWarnings("unused")
	private Double valor = null;
	private Integer quantidade = null;
	@Autowired
	private SessionUtil<User> sessionUtil;

	@SuppressWarnings("unchecked")
	@GetMapping("/boleto")
	public String boleto(@Valid Boleto boleto, @RequestParam("id") String attr, @RequestParam("quantidade") Integer quantidade, @RequestParam("valor") Double valor,
			RedirectAttributes attrs) {
		User user = sessionUtil.getSession("user");
		if(user != null) {
			id = Long.parseLong(attr);  
			this.valor = valor; 
			this.quantidade = quantidade;
			
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Bearer " + user.getToken());

			String url = "https://projeto-pag-api.herokuapp.com/api/compras/saveCompra";

			Pagamento pagamento = (Pagamento) RestTemplateUtil
					.getEntity("https://projeto-pag-api.herokuapp.com/api/compras/detalhesCompra/" + id, headers, Pagamento.class);

			sessionUtil.criarSession("user", pagamento.getUsuario());
			

			pagamento.setBoleto(boleto);
			pagamento.setValor(valor);
			pagamento.setQuantidade(quantidade);
			pagamento.setTipoPagamento(TipoPagamento.BOLETO);

			try {
				ResponseEntity<Pagamento> responseEntity = (ResponseEntity<Pagamento>) RestTemplateUtil.post(url, headers,
						pagamento, Pagamento.class);

				if (responseEntity.getStatusCode().is2xxSuccessful()) {
					attrs.addFlashAttribute("success", "Pagamento Realizado com Sucesso!");
					return "redirect:/boleto/detalhesCompraIdBoleto/" + responseEntity.getBody().getBoleto().getIdBoleto();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		attrs.addFlashAttribute("fail", "Sessão Expirada!");
		return "compra/boleto";
	}

	@GetMapping("/detalhesCompraIdBoleto/{id_boleto}")
	public ModelAndView detalhesBoleto(@PathVariable("id_boleto") Long id_boleto) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + sessionUtil.getSession("user").getToken());
		
		Pagamento[] pagamentos = (Pagamento[]) RestTemplateUtil
				.getEntity("https://projeto-pag-api.herokuapp.com/api/compras/detalhesCompraIdBoleto/" + id_boleto, headers, Pagamento[].class);
		ModelAndView modelAndView = new ModelAndView("compra/boleto");
		modelAndView.addObject("compras", pagamentos);
		modelAndView.addObject("origin", pagamentos[0].getOrigin());
		return modelAndView;
	}

}

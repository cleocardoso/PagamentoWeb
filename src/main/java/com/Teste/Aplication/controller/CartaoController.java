package com.Teste.Aplication.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Teste.Aplication.Enuns.TipoPagamento;
import com.Teste.Aplication.model.Cartao;
import com.Teste.Aplication.model.Pagamento;
import com.Teste.Aplication.util.RestTemplateUtil;

@Controller
@RequestMapping("/cartao")
public class CartaoController {

	private Long id = null;
	private Integer quantidade = null;
	private Double valor;

	@GetMapping("/cartao")
	public String cartao(Cartao cartao, @RequestParam("id") String attr,
			@RequestParam("quantidade") Integer quantidade, @RequestParam("valor") Double valor) {
		id = Long.parseLong(attr); // recebe o id da compra
		this.quantidade = quantidade; // recebe a quantidade do pagamento
		this.valor = valor; // recebe o valor do pagamento
		return "compra/cartao";
	}

	@SuppressWarnings({ "unchecked" })
	@PostMapping("/salvarCartao")
	public String salvarCartao(@Valid Cartao cartao, BindingResult result, RedirectAttributes attr) {
		Pagamento pagamento = (Pagamento) RestTemplateUtil
				.getEntity("http://localhost:8081/api/compras/detalhesCompra/" + id, Pagamento.class);
		String url = "http://localhost:8081/api/compras/saveCompra";
		pagamento.setTipoPagamento(TipoPagamento.CARTAO);
		pagamento.setQuantidade(quantidade);
		pagamento.setValor(valor);
		pagamento.setCartao(cartao);
		
		try {
			ResponseEntity<Pagamento> responseEntity = (ResponseEntity<Pagamento>) RestTemplateUtil.post(url, pagamento,
					Pagamento.class);

			if (responseEntity.getStatusCode().is2xxSuccessful()) {
				attr.addFlashAttribute("success", "Pagamento Realizado com Sucesso!");
				return "redirect:/cartao/detalhesCompraIdCartao/" + responseEntity.getBody().getCartao().getId_cartao();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		attr.addFlashAttribute("fail", "Por favor tente novamente!");
		return "redirect:/cartao/cartao";
	}

	@GetMapping("/detalhesCompraIdCartao/{id_cartao}")
	public ModelAndView detalhesCompraCartao(@PathVariable("id_cartao") Long id_cartao) {
		Pagamento [] pagamentos = (Pagamento[]) RestTemplateUtil.getEntity("http://localhost:8081/api/compras/detalhesCompraIdCartao/"+id_cartao, Pagamento[].class);
		ModelAndView modelAndView = new ModelAndView("compra/detalhesCompraCartao");
		modelAndView.addObject("compras", pagamentos);
		System.out.println(pagamentos[0].getOrigin());
		modelAndView.addObject("origin", pagamentos[0].getOrigin());
		return modelAndView;
	}

}

package com.Teste.Aplication.controller;

import java.math.RoundingMode;
import java.security.Principal;
import java.text.DecimalFormat;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.Teste.Aplication.Enuns.Status;
import com.Teste.Aplication.Enuns.TipoPagamento;
import com.Teste.Aplication.model.Cartao;
import com.Teste.Aplication.model.Pagamento;
import com.Teste.Aplication.model.User;
import com.Teste.Aplication.util.RestTemplateUtil;
import com.Teste.Aplication.util.SessionUtil;

@Controller
@RequestMapping("/cartao")
public class CartaoController {

	private Long id = null;
	
	@Autowired
	private SessionUtil<User> sessionUtil;

	@GetMapping("/cartao")
	public String cartao(Cartao cartao, @RequestParam("id") String attr) {
		id = Long.parseLong(attr); // recebe o id da compra
		//Pagamento compra = compraService.getOne(id);
		// compra.setValor(compra.getValor());
		return "compra/cartao";
	}

	private double calPMT(double pv, int n, String i) {

		// System.out.println( decimalFormat.format(valor) );
		String porcent[] = i.split("%");
		double taxa = Double.parseDouble(porcent[0]) / 100;
		double resultOne = (Math.pow((1 + taxa), n) - 1);
		double resultTwo = ((Math.pow(1 + taxa, n) * taxa));
		double resultThree = resultOne / resultTwo;
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
		decimalFormat.setRoundingMode(RoundingMode.DOWN);

		String valor[] = decimalFormat.format(pv / resultThree).split(",");
		return Double.parseDouble(valor[0] + "." + valor[1]);
	}
	@SuppressWarnings({ "unchecked" })
	@PostMapping("/salvarCartao")
	public String salvarCartao(@Valid Cartao cartao, BindingResult result,
			RedirectAttributes attr) {
		User user = sessionUtil.getSession("user");
		String url = "http://localhost:8081/api/compras/saveCompra";
		
		Pagamento pagamento = new Pagamento();
		pagamento.setId(id);
		pagamento.setTipoPagamento(TipoPagamento.CARTAO);
		pagamento.setCartao(cartao); 
		pagamento.setUsuario(user);
		
		ResponseEntity<Pagamento> responseEntity = (ResponseEntity<Pagamento>) RestTemplateUtil.post(url, pagamento, Pagamento.class); 
		 
		if(responseEntity.getStatusCode().is2xxSuccessful()) {
			Pagamento pagamento2 = responseEntity.getBody();
			attr.addFlashAttribute("sucess", "Pagamento Realizado com Sucesso!");
			return "redirect:/cartao/detalhesCompraIdCartao/" + pagamento2.getCartao().getId_Cartao();
		}
		attr.addFlashAttribute("fail", "Por favor tente novamente!");
		return "redirect:/cartao/cartao";
	}
	
	@GetMapping("/detalhesCompraIdCartao/{id_cartao}")
	public ModelAndView detalhesCompraCartao(@PathVariable("id_cartao") Long id_cartao) {
		ModelAndView modelAndView = new ModelAndView("compra/detalhesCompraCartao");
		//modelAndView.addObject("compras", compraService.findByIdCartao(id_cartao));
		return modelAndView;
	}

}

package com.Teste.Aplication.controller;




import java.security.Principal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.validation.Valid;

import org.apache.tomcat.util.buf.UDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Teste.Aplication.Enuns.Status;
import com.Teste.Aplication.Enuns.TipoPagamento;
import com.Teste.Aplication.model.Boleto;
import com.Teste.Aplication.model.Pagamento;
import com.Teste.Aplication.model.User;
import com.Teste.Aplication.util.RestTemplateUtil;

@Controller
@RequestMapping("/boleto") 
public class BoletoController {

	private Long id = null;
	private Double valor = null;
	
	
	@GetMapping("/boleto")  
	public String boleto(@Valid Boleto boleto, @RequestParam("id") String attr,
			@RequestParam("valor") Double valor, RedirectAttributes attrs) {
		
		
		 id = Long.parseLong(attr); 
		 this.valor  = valor;
		 
		String url = "http://localhost:8081/api/compras/saveCompra";
		Pagamento pagamento = (Pagamento) RestTemplateUtil
				.getEntity("http://localhost:8081/api/compras/detalhesCompra/" + id, Pagamento.class);
		pagamento.setBoleto(boleto);
		pagamento.setValor(valor);
		pagamento.setTipoPagamento(TipoPagamento.BOLETO);
		
		 
		  try {
				ResponseEntity<Pagamento> responseEntity = (ResponseEntity<Pagamento>) RestTemplateUtil.post(url, pagamento,
						Pagamento.class);
				if (responseEntity.getStatusCode().is2xxSuccessful()) {
					attrs.addFlashAttribute("success", "Pagamento Realizado com Sucesso!");
					return "redirect:/boleto/detalhesCompraIdBoleto/"+responseEntity.getBody().getBoleto().getIdBoleto();
				}
		      }catch (Exception e) {
					// TODO: handle exception
		      }   
		 
				
		  //Pagamento compra = compraService.getOne(id);
		 /* if(compra != null) { User user = userService.getEmail(principal.getName());
		 * if(user != null) { System.out.println(user.getEmail());
		 * boleto.setDataCompra(new Date()); //gerar codigo do boleto Random r = new
		 * Random(); String codigo = String.valueOf(Math.abs(r.nextInt()*100000000));
		 * System.out.println(codigo); boleto.setNumeroBoleto(codigo);
		 * 
		 * boleto.setDataVencimento(LocalDate.now().plusDays(5));
		 * boletoService.salvarBoleto(boleto); compra.setUsuario(user);
		 * compra.setBoleto(boleto); compra.setStatus(Status.ANDAMENTO);
		 * compraService.saveAndFlush(compra); attrs.addFlashAttribute("success",
		 * "Compra realizada com sucesso"); attrs.addAttribute("compras",
		 * compraService.findAllByIdUser(user.getId())); } }
		 */

		return "compra/boleto";    
		//return "redirect:/boleto/detalhesCompraIdBoleto/"+boleto.getIdBoleto();
	} 
	
	@GetMapping("/detalhesCompraIdBoleto/{id_boleto}")  
	public ModelAndView detalhesBoleto(@PathVariable("id_boleto") Long id_boleto) {
		Pagamento [] pagamentos = (Pagamento[]) RestTemplateUtil.getEntity("http://localhost:8081/api/compras/detalhesCompraIdBoleto/"+id_boleto, Pagamento[].class);
		ModelAndView modelAndView = new ModelAndView("compra/boleto");
		modelAndView.addObject("compras", pagamentos);
		modelAndView.addObject("origin", pagamentos[0].getOrigin());
		return modelAndView;
	}
	
	
}
